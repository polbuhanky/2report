package seven.bsh.db.entity;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import seven.bsh.db.ProjectDatabase;

@Table(database = ProjectDatabase.class)
public class Report extends BaseModel implements Serializable {
    public static final int STATUS_NEW = 1;
    public static final int STATUS_APPROVED = 2;
    public static final int STATUS_REJECTED = 3;
    public static final int STATUS_FOR_IMPROVED = 4;

    @Column
    @PrimaryKey
    private int id;

    @Column(
        name = "created_at",
        getterName = "getCreatedAt",
        setterName = "setCreatedAt"
    )
    private String mCreatedAt;

    @Column(
        name = "updated_at",
        getterName = "getUpdatedAt",
        setterName = "setUpdatedAt"
    )
    private String mUpdatedAt;

    @Column(
        name = "trade_object_name",
        getterName = "getTradeObjectName",
        setterName = "setTradeObjectName"
    )
    private String mTradeObjectName;

    @Column(
        name = "trade_object_address",
        getterName = "getTradeObjectAddress",
        setterName = "setTradeObjectAddress"
    )
    private String mTradeObjectAddress;

    @Column(
        name = "project_name",
        getterName = "getProjectName",
        setterName = "setProjectName"
    )
    private String mProjectName;

    @Column
    private String data;

    @Column
    private int status;

    @Column(
        name = "checklist_id",
        getterName = "getChecklistId",
        setterName = "setChecklistId"
    )
    private int mChecklistId;

    @Column(
        name = "trade_object_id",
        getterName = "getTradeObjectId",
        setterName = "setTradeObjectId"
    )
    private int mTradeObjectId;

    @Column(
        name = "project_id",
        getterName = "getProjectId",
        setterName = "setProjectId"
    )
    private int mProjectId;

    private Checklist mChecklist;
    private JSONObject dataJson;
    private boolean checked;

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTradeObjectName(String TradeObjectName) {
        mTradeObjectName = TradeObjectName;
    }

    public String getTradeObjectName() {
        return mTradeObjectName;
    }

    public void setTradeObjectAddress(String tradeObjectAddress) {
        mTradeObjectAddress = tradeObjectAddress;
    }

    public String getTradeObjectAddress() {
        return mTradeObjectAddress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getChecklistId() {
        return mChecklistId;
    }

    public void setChecklistId(int checklistId) {
        mChecklistId = checklistId;
    }

    public int getTradeObjectId() {
        return mTradeObjectId;
    }

    public void setTradeObjectId(int tradeObjectId) {
        mTradeObjectId = tradeObjectId;
    }

    public JSONObject getDataJson() {
        if (dataJson == null && data != null) {
            try {
                dataJson = new JSONObject(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataJson;
    }

    public int getProjectId() {
        return mProjectId;
    }

    public void setProjectId(int projectId) {
        mProjectId = projectId;
    }

    public void setProjectName(String projectName) {
        mProjectName = projectName;
    }

    public String getProjectName() {
        return mProjectName;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public Checklist getChecklist() {
        return mChecklist;
    }

    public void setChecklist(Checklist checklist) {
        mChecklist = checklist;
    }

    public double getLaitude() {
        JSONObject json = getDataJson();
        if (json == null) {
            return 0;
        }
        return json.optDouble("latitude", 0);
    }

    public double getLongitude() {
        JSONObject json = getDataJson();
        if (json == null) {
            return 0;
        }
        return json.optDouble("longitude", 0);
    }
}
