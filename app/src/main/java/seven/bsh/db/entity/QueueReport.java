package seven.bsh.db.entity;

import android.util.Log;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import seven.bsh.db.ProjectDatabase;
import seven.bsh.utils.Formatter;

@Table(
    database = ProjectDatabase.class,
    name = "queue_report"
)
public class QueueReport extends BaseModel implements Serializable {
    public static final int STATUS_DRAFT = -1;
    public static final int STATUS_QUEUE = 0;
    public static final int STATUS_SENDING_DATA = 1;
    public static final int STATUS_SENDING_FILES = 2;
    public static final int STATUS_PENDING = 3;
    public static final int STATUS_ERROR_DATA = 4;
    public static final int STATUS_ERROR_FILES = 5;
    public static final int STATUS_SENDING_FILLED = 6;
    public static final int STATUS_ERROR_STATUS = 7;

    @Column
    @PrimaryKey(autoincrement = true)
    private int id;

    @Column(
        name = "project_id",
        getterName = "getProjectId",
        setterName = "setProjectId"
    )
    private int mProjectId;

    @Column(
        name = "checklist_id",
        getterName = "getChecklistId",
        setterName = "setChecklistId"
    )
    private int mChecklistId;

    @Column(
        name = "checklist_name",
        getterName = "getChecklistName",
        setterName = "setChecklistName"
    )
    private String mChecklistName;

    @Column(
        name = "trade_object_address",
        getterName = "getTradeObjectAddress",
        setterName = "setTradeObjectAddress"
    )
    private String mTradeObjectAddress;

    @Column(
        name = "trade_object_name",
        getterName = "getTradeObjectName",
        setterName = "setTradeObjectName"
    )
    private String mTradeObjectName;

    @Column(
        name = "trade_object_id",
        getterName = "getTradeObjectId",
        setterName = "setTradeObjectId"
    )
    private int mTradeObjectId;

    @Column
    private String data;

    @Column(
        name = "created_at",
        getterName = "getCreatedAt",
        setterName = "setCreatedAt"
    )
    private String mCreatedAt;

    @Column
    private String errors;

    @Column
    private int status;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @Column(
        name = "inner_id",
        getterName = "getInnerId",
        setterName = "setInnerId"
    )
    private int mInnerId;

    @Column(getterName = "isUpdated")
    private boolean updated;

    @Column(getterName = "isSent")
    private boolean sent;

    private List<QueueFile> fileList;
    private JSONObject dataJson;
    private boolean checked;

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        return o instanceof QueueReport
            && getId() == ((QueueReport) o).getId();
    }

    @Override
    public int hashCode() {
        return id;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setChecklistName(String checklistName) {
        mChecklistName = checklistName;
    }

    public String getChecklistName() {
        return mChecklistName;
    }

    public void setTradeObjectAddress(String tradeObjectAddress) {
        mTradeObjectAddress = tradeObjectAddress;
    }

    public String getTradeObjectAddress() {
        return mTradeObjectAddress;
    }

    public void setTradeObjectName(String tradeObjectName) {
        mTradeObjectName = tradeObjectName;
    }

    public String getTradeObjectName() {
        return mTradeObjectName;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public int getChecklistId() {
        return mChecklistId;
    }

    public void setChecklistId(int checklistId) {
        mChecklistId = checklistId;
    }

    public int getProjectId() {
        return mProjectId;
    }

    public void setProjectId(int projectId) {
        mProjectId = projectId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<QueueFile> getFileList() {
        return fileList;
    }

    public void setFileList(List<QueueFile> fileList) {
        this.fileList = fileList;
    }

    public void setInnerId(int innerId) {
        mInnerId = innerId;
    }

    public int getInnerId() {
        return mInnerId;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public String getFormattedAddedAt() {
        return Formatter.convertDateString(mCreatedAt, "yyyy-MM-dd HH:mm:ss", "dd.MM.yyyy HH:mm:ss");
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getErrors() {
        return errors;
    }

    public JSONObject getDataJson() {
        if (dataJson == null) {
            try {
                dataJson = new JSONObject(data);
            } catch (JSONException ex) {
                Log.e("QueueModel", ex.getMessage(), ex);
            }
        }
        return dataJson;
    }

    public void setDataJson(JSONObject dataJson) {
        this.dataJson = dataJson;
    }

    public int getTradeObjectId() {
        return mTradeObjectId;
    }

    public void setTradeObjectId(int tradeObjectId) {
        mTradeObjectId = tradeObjectId;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public boolean isUpdated() {
        return updated;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public boolean hasFiles() {
        return !fileList.isEmpty();
    }

    public boolean hasLoadedFiles() {
        return fileList != null;
    }

    /**
     * Проверка на возможность удаления отчета из очереди
     *
     * @return true, если модель можно удалить
     */
    public boolean canRemove() {
        return status == STATUS_ERROR_STATUS
            || status == STATUS_ERROR_DATA
            || status == STATUS_ERROR_FILES
            || status == STATUS_QUEUE;
    }

    public boolean isNewModel() {
        return getId() == 0;
    }
}
