package seven.bsh.net.service;

import android.content.Intent;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import seven.bsh.R;
import seven.bsh.db.entity.QueueFile;
import seven.bsh.db.entity.QueueReport;
import seven.bsh.net.listener.OnRefreshTokenRequestListener;
import seven.bsh.net.listener.OnReportPostRequestListener;
import seven.bsh.net.listener.OnReportPutRequestListener;
import seven.bsh.net.listener.OnReportStatusFilledRequestListener;
import seven.bsh.net.listener.OnReportUploadFileRequestListener;
import seven.bsh.net.request.RefreshTokenRequest;
import seven.bsh.net.request.ReportPostRequest;
import seven.bsh.net.request.ReportPutRequest;
import seven.bsh.net.request.ReportStatusFilledRequest;
import seven.bsh.net.request.ReportUploadFileRequest;
import seven.bsh.net.response.RefreshTokenResponse;
import seven.bsh.net.response.ReportPostResponse;
import seven.bsh.net.response.ReportStatusFilledResponse;
import seven.bsh.net.response.ReportUploadFileResponse;
import seven.bsh.net.response.ValidateErrorResponse;

public class QueueService extends BaseService implements
    OnReportPostRequestListener.OnRequestListener,
    OnReportUploadFileRequestListener.OnRequestListener,
    OnReportPutRequestListener.OnRequestListener,
    OnReportStatusFilledRequestListener.OnRequestListener,
    OnRefreshTokenRequestListener.OnRequestListener {
    public static final String BROADCAST_ID = "queue";
    public static final String KEY_LIST = "list";
    public static final String KEY_MODEL_ID = "model_id";
    public static final String KEY_MODEL_STATUS = "model_status";
    public static final String KEY_MODEL_ERRORS = "model_errors";

    public static final int COMMAND_ADD = 0;
    public static final int COMMAND_DELETE = 1;

    public static final int RESULT_QUEUE_UPDATED = 0;
    public static final int RESULT_STATUS = 1;

    public static final int NO_MODEL_ID = -1;

    private final List<QueueReport> list;
    private State state = State.PENDING;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public QueueService() {
        list = new ArrayList<>();
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int command = intent.getIntExtra(KEY_COMMAND, COMMAND_NONE);
            Bundle params = intent.getExtras();
            switch (command) {
                case COMMAND_ADD:
                    onCommandAdd(params);
                    break;

                case COMMAND_DELETE:
                    onCommandDelete(params);
                    break;
            }
        }
        return START_STICKY;
    }

    private void onCommandAdd(Bundle params) {
        List<QueueReport> temp;
        int[] listIds = params.getIntArray(KEY_LIST);
        if (listIds == null) {
            temp = getDb().getQueueRepository().getList();
        } else {
            List<Integer> ids = new ArrayList<>();
            for (int id : listIds) {
                ids.add(id);
            }
            temp = getDb().getQueueRepository().getList(ids, false);
        }
        addListWithCompare(temp);
    }

    private synchronized void onCommandDelete(Bundle params) {
        synchronized (list) {
            int[] listIds = params.getIntArray(KEY_LIST);
            removeListWithCompare(listIds);
            sendBroadcastQueueUpdate(NO_MODEL_ID);
        }
    }

    @Override
    public void onAuthFailed() {
        synchronized (list) {
            state = State.PENDING;
            markAsError(getString(R.string.activity_queue_error_auth));
            list.clear();
        }
    }

    @Override
    public void onServerError() {
        onHttpUnknownError(500, null);
    }

    @Override
    public void onTokenError() {
        sendAuthRequest();
    }

    @Override
    public void onHttpUnknownError(int status, String errorBody) {
        if (list.isEmpty()) {
            return;
        }

        markAsError(getString(R.string.activity_queue_error_unknown));
        synchronized (list) {
            QueueReport model = list.remove(0);
            sendBroadcastModelStatus(model);
            execute(true);
        }
    }

    @Override
    public void onNoInternetError() {
        markAsError(getString(R.string.activity_queue_error_no_internet));
        list.clear();
    }

    @Override
    public void onParserError() {
        synchronized (list) {
            markAsError(getString(R.string.activity_queue_error_parse));
            QueueReport model = list.remove(0);
            sendBroadcastModelStatus(model);
            execute(true);
        }
    }

    @Override
    public void onAuthSuccess(RefreshTokenResponse response) {
        getLocalData().saveToken(response.getToken(), response.getRefreshToken());
        execute(false);
    }

    @Override
    public void onReportPostSuccess(ReportPostResponse response) {
        if (response == null) {
            return;
        }

        synchronized (list) {
            if (list.isEmpty()) {
                stopSelf();
                return;
            }

            QueueReport model = list.get(0);
            model.setInnerId(response.getId());
            model.setSent(true);
            model.setErrors(null);
            getDb().getQueueRepository().updateReportId(model.getId(), response.getId());
        }
        execute(false);
    }

    @Override
    public void onReportPostNotFound() {
        if (list.isEmpty()) {
            return;
        }

        markAsError(getString(R.string.activity_queue_error_no_checklist));
        list.remove(0);
        execute(true);
    }

    @Override
    public void onReportPostFailedValidation(ValidateErrorResponse response) {
        if (list.isEmpty()) {
            return;
        }

        markAsError(response.getErrors());
        list.remove(0);
        execute(true);
    }

    @Override
    public void onReportUploadFileSuccess(ReportUploadFileResponse response) {
        synchronized (list) {
            if (list.isEmpty()) {
                stopSelf();
                return;
            }

            QueueReport model = list.get(0);
            List<QueueFile> files = model.getFileList();
            if (files == null) {
                files = getDb().getQueueRepository().getFiles(model.getId());
                model.setFileList(files);
            }

            if (!model.getFileList().isEmpty()) {
                QueueFile fileModel = model.getFileList().remove(0);
                getDb().getQueueRepository().deleteFile(fileModel.getId());
            }
        }
        execute(false);
    }

    @Override
    public void onReportPutSuccess() {
        synchronized (list) {
            if (list.isEmpty()) {
                stopSelf();
                return;
            }

            QueueReport model = list.get(0);
            model.setErrors(null);
            model.setSent(true);
            getDb().getQueueRepository().save(model);
        }
        execute(false);
    }

    @Override
    public void onReportPutNotFound() {
        onReportPostNotFound();
    }

    @Override
    public void onReportPutFailedValidation(ValidateErrorResponse response) {
        onReportPostFailedValidation(response);
    }

    @Override
    public void onReportStatusFilledSuccess(ReportStatusFilledResponse response) {
        synchronized (list) {
            if (list.isEmpty()) {
                stopSelf();
                return;
            }
            QueueReport model = list.remove(0);
            getDb().getQueueRepository().delete(model.getId());
            sendBroadcastQueueUpdate(model.getId());
        }
        execute(false);
    }

    @Override
    public void onReportStatusFilledNotFound() {
        onReportPostNotFound();
    }

    @Override
    public void onReportStatusFilledFailedValidation(ValidateErrorResponse response) {
        onReportPostFailedValidation(response);
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    /**
     * Добавление моделей в очередь с проверками на уникальность
     *
     * @param tempList Список моделей, добавляемых в очередь
     */
    private void addListWithCompare(List<QueueReport> tempList) {
        for (QueueReport tempModel : tempList) {
            if (!list.contains(tempModel)) {
                list.add(tempModel);
            }
        }
        execute(true);
    }

    /**
     * Удаление моделей из очереди
     *
     * @param listIds Список идентификаторов моделей
     */
    private void removeListWithCompare(int[] listIds) {
        for (int tempId : listIds) {
            for (QueueReport model : list) {
                if (model.getId() == tempId && model.canRemove()) {
                    list.remove(model);
                    break;
                }
            }
        }
    }

    /**
     * Отметка отчета в очереди ошибкой
     *
     * @param errorText Текст ошибки
     */
    private void markAsError(String errorText) {
        synchronized (list) {
            if (list.isEmpty()) {
                return;
            }

            QueueReport model = list.get(0);
            int status;
            if (model.getInnerId() == 0) {
                status = QueueReport.STATUS_ERROR_DATA;
            } else if (model.getStatus() == QueueReport.STATUS_SENDING_FILES) {
                status = QueueReport.STATUS_ERROR_FILES;
            } else {
                status = QueueReport.STATUS_ERROR_STATUS;
            }

            model.setStatus(status);
            model.setErrors(errorText);
            getDb().getQueueRepository().updateStatus(model.getId(), status, errorText);
            sendBroadcastModelStatus(model);
        }
    }

    /**
     * Отправка уведомления об изменении статуса отчета в очереди
     *
     * @param model Модель отчета в очереди
     */
    private void sendBroadcastModelStatus(QueueReport model) {
        Intent intent = new Intent(BROADCAST_ID);
        intent.putExtra(KEY_RESULT, RESULT_STATUS);
        intent.putExtra(KEY_MODEL_ID, model.getId());
        intent.putExtra(KEY_MODEL_STATUS, model.getStatus());
        intent.putExtra(KEY_MODEL_ERRORS, model.getErrors());
        sendBroadcast(intent);
    }

    /**
     * Отправка уведомления об изменении списка очереди
     */
    private void sendBroadcastQueueUpdate(int id) {
        Intent intent = new Intent(BROADCAST_ID);
        intent.putExtra(KEY_RESULT, RESULT_QUEUE_UPDATED);
        intent.putExtra(KEY_MODEL_ID, id);
        sendBroadcast(intent);
    }

    /**
     * Выполнение команды отправки
     *
     * @param check Проверка на уже запущенный процесс
     */
    private void execute(boolean check) {
        synchronized (list) {
            if (list.isEmpty()) {
                state = State.PENDING;
                return;
            }
            if (check && state == State.PROCESSING) {
                return;
            }

            String token = getLocalData().getAccessToken();
            QueueReport model = list.get(0);
            if (!model.hasLoadedFiles()) {
                List<QueueFile> files = getDb().getQueueRepository().getFiles(model.getId());
                model.setFileList(files);
            }

            if (!model.isSent()) {
                sendReportData(model, token);
                return;
            }

            if (model.hasFiles()) {
                sendReportFile(model, token);
            } else {
                sendReportStatusFilled(model, token);
            }
        }
    }

    /**
     * Отправка данных отчета
     *
     * @param model Модель отчета
     * @param token Токен авторизации
     */
    private void sendReportData(QueueReport model, String token) {
        model.setStatus(QueueReport.STATUS_SENDING_DATA);
        getDb().getQueueRepository().updateStatus(model.getId(), model.getStatus(), null);
        sendBroadcastModelStatus(model);

        if (model.isUpdated()) {
            ReportPutRequest request = new ReportPutRequest(token, model);
            getApi().putReport(request, new OnReportPutRequestListener(this));
        } else {
            ReportPostRequest request = new ReportPostRequest(token, model);
            getApi().postReport(request, new OnReportPostRequestListener(this));
        }
    }

    /**
     * Отправка файла отчета
     *
     * @param model Модель отчета
     * @param token Токен авторизации
     */
    private void sendReportFile(QueueReport model, String token) {
        if (model.getStatus() != QueueReport.STATUS_SENDING_FILES) {
            model.setStatus(QueueReport.STATUS_SENDING_FILES);
            getDb().getQueueRepository().updateStatus(model.getId(), model.getStatus(), null);
            sendBroadcastModelStatus(model);
        }

        QueueFile fileModel = model.getFileList().get(0);
        File file = fileModel.getFile();

        try {
            ReportUploadFileRequest request = new ReportUploadFileRequest(token, model.getInnerId(), fileModel);
            getApi().uploadFile(request, new OnReportUploadFileRequestListener(this));
        }
        catch (Exception e){
            e.printStackTrace();
        }
//        if (file.exists()) {
//            ReportUploadFileRequest request = new ReportUploadFileRequest(token, model.getInnerId(), fileModel);
//            getApi().uploadFile(request, new OnReportUploadFileRequestListener(this));
//        } else {
//            markAsError(getString(R.string.service_queue_file_notFound, fileModel.getPath()));
//        }
    }

    /**
     * Отправка статуса завершения отправки отчета
     *
     * @param model Модель очереди отправки
     * @param token Токен авторизации
     */
    private void sendReportStatusFilled(QueueReport model, String token) {
        model.setStatus(QueueReport.STATUS_SENDING_FILLED);
        getDb().getQueueRepository().updateStatus(model.getId(), model.getStatus(), null);
        sendBroadcastModelStatus(model);

        ReportStatusFilledRequest request = new ReportStatusFilledRequest(token, model.getInnerId());
        getApi().putReportStatusFilled(request, new OnReportStatusFilledRequestListener(this));
    }

    /**
     * Отправка запроса авторизации
     */
    private void sendAuthRequest() {
        String refreshToken = getLocalData().getRefreshToken();
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        getApi().refreshToken(request, new OnRefreshTokenRequestListener(this));
    }
}
