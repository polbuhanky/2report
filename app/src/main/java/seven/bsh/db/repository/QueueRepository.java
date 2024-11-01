package seven.bsh.db.repository;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Update;
import com.raizlabs.android.dbflow.sql.language.Where;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import seven.bsh.db.entity.QueueFile;
import seven.bsh.db.entity.QueueFile_Table;
import seven.bsh.db.entity.QueueReport;
import seven.bsh.db.entity.QueueReport_Table;

public class QueueRepository {

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    /**
     * Сохранить отчет в очередь
     *
     * @param model модель ЧЛ
     * @param draft сохранить как черновик
     * @return Идентификатор вставленной записи
     */
    public synchronized int save(QueueReport model, boolean draft) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        model.setStatus(draft ? QueueReport.STATUS_DRAFT : QueueReport.STATUS_QUEUE);
        model.setCreatedAt(dateFormat.format(new Date()));
        if (model.isNewModel()) {
            int id = (int) model.insert();
            model.setId(id);
        } else {
            model.save();
        }

        // save file data
        List<QueueFile> fileDataList = model.getFileList();
        for (QueueFile fileModel : fileDataList) {
            fileModel.setQueueId(model.getId());
            fileModel.save();
        }
        return model.getId();
    }

    /**
     * Сохранить отчет в очередь
     *
     * @param model модель ЧЛ
     */
    public synchronized void save(QueueReport model) {
        model.save();
    }

    /**
     * Получение количества черновиков
     */
    public synchronized int countDraft() {
        return (int) new Select(Method.count())
            .from(QueueReport.class)
            .where(QueueReport_Table.status.eq(QueueReport.STATUS_DRAFT))
            .count();
    }

    /**
     * Получение списка отчетов в очереди отправки
     */
    public synchronized List<QueueReport> getList() {
        return getList(null, false);
    }

    /**
     * Получение списка черновиков
     */
    public List<QueueReport> getDraftList() {
        return getList(null, true);
    }

    /**
     * Получение списка отчетов
     *
     * @param ids ID отчетов
     * @param draft true, если нужны черновики
     */
    public synchronized List<QueueReport> getList(List<Integer> ids, boolean draft) {
        Where<QueueReport> query = new Select()
            .from(QueueReport.class)
            .where();

        if (ids != null) {
            query.and(QueueReport_Table.id.in(ids));
        }

        if (draft) {
            query.and(QueueReport_Table.status.eq(QueueReport.STATUS_DRAFT));
        } else {
            query.and(QueueReport_Table.status.notEq(QueueReport.STATUS_DRAFT));
        }
        return query.queryList();
    }

    /**
     * Удалить все отчеты из очереди
     */
    public synchronized void deleteAll() {
        List<Integer> ids = new ArrayList<>();
        List<QueueReport> reports = new Select(QueueReport_Table.id)
            .from(QueueReport.class)
            .where(QueueReport_Table.status.notEq(QueueReport.STATUS_DRAFT))
            .queryList();

        for (QueueReport report : reports) {
            ids.add(report.getId());
        }

        Delete.table(QueueFile.class, QueueFile_Table.queue_id.in(ids));
        Delete.table(QueueReport.class, QueueReport_Table.status.notEq(QueueReport.STATUS_DRAFT));
    }

    /**
     * Удаление всех черновиков
     */
    public synchronized void deleteAllDrafts() {
        List<Integer> ids = new ArrayList<>();
        List<QueueReport> reports = new Select(QueueReport_Table.id)
            .from(QueueReport.class)
            .where(QueueReport_Table.status.eq(QueueReport.STATUS_DRAFT))
            .queryList();

        for (QueueReport report : reports) {
            ids.add(report.getId());
        }

        Delete.table(QueueFile.class, QueueFile_Table.queue_id.in(ids));
        Delete.table(QueueReport.class, QueueReport_Table.status.eq(QueueReport.STATUS_DRAFT));
    }

    /**
     * Удаление отчетов из очереди
     */
    public synchronized void deleteAll(List<Integer> list) {
        Delete.table(QueueReport.class, QueueReport_Table.id.in(list));
        Delete.table(QueueFile.class, QueueFile_Table.queue_id.in(list));
    }

    /**
     * Удаление отчета из очереди
     *
     * @param id ID отчета в очереди
     */
    public synchronized void delete(int id) {
        Delete.table(QueueFile.class, QueueFile_Table.queue_id.eq(id));
        Delete.table(QueueReport.class, QueueReport_Table.id.eq(id));
    }

    /**
     * Получение файлов отчета
     *
     * @param id ID отчета в очереди
     */
    public synchronized List<QueueFile> getFiles(int id) {
        return new Select()
            .from(QueueFile.class)
            .where(QueueFile_Table.queue_id.eq(id))
            .queryList();
    }

    /**
     * Удаление файла из БД
     *
     * @param fileId ID файла
     */
    public synchronized void deleteFile(int fileId) {
        Delete.table(QueueFile.class, QueueFile_Table.id.eq(fileId));
    }

    /**
     * Получение отчета в очереди
     *
     * @param id ID отета в очереди
     */
    public synchronized QueueReport get(int id) {
        return new Select()
            .from(QueueReport.class)
            .where(QueueReport_Table.id.eq(id))
            .querySingle();
    }

    /**
     * Обновление системного идентификтаора отчета
     *
     * @param id ID отчета
     * @param systemId Системный идентификатор
     */
    public synchronized void updateReportId(int id, int systemId) {
        new Update<>(QueueReport.class)
            .set(
                QueueReport_Table.inner_id.eq(systemId),
                QueueReport_Table.sent.eq(true)
            )
            .where(QueueReport_Table.id.eq(id))
            .query();
    }

    /**
     * Обновление статуса модели
     *
     * @param id ID отчета в очереди
     * @param status Статус
     */
    public synchronized void updateStatus(int id, int status, String errorText) {
        new Update<>(QueueReport.class)
            .set(
                QueueReport_Table.status.eq(status),
                QueueReport_Table.errors.eq(errorText)
            )
            .where(QueueReport_Table.id.eq(id))
            .query();
    }
}
