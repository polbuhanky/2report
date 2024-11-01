package seven.bsh.net.request;

import android.webkit.MimeTypeMap;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import seven.bsh.db.entity.QueueFile;
import seven.bsh.net.response.ReportUploadFileResponse;

public class ReportUploadFileRequest {
    private final String mToken;
    private final int mReportId;
    private final QueueFile mQueueFile;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public ReportUploadFileRequest(String accessToken, int reportId, QueueFile fileModel) {
        mToken = accessToken;
        mReportId = reportId;
        mQueueFile = fileModel;
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    public ReportUploadFileResponse getDemoResponse() {
        ReportUploadFileResponse response = new ReportUploadFileResponse();
        return response;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getToken() {
        return "Bearer " + mToken;
    }

    public int getReportId() {
        return mReportId;
    }

    public MultipartBody.Part getBody() {
        File file = mQueueFile.getFile();
        RequestBody requestFile = RequestBody.create(
            MediaType.parse(getMimeType(mQueueFile.getPath())),
            file
        );
        return MultipartBody.Part.createFormData("ReportForm[" + mQueueFile.getFieldName() + "]", file.getName(), requestFile);
    }

    private String getMimeType(String path) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }

        if (type == null) {
            type = "application/octet-stream";
        }
        return type;
    }
}
