package seven.bsh.net.entity;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@JsonIgnoreProperties(value = {
    "id",
    "version",
    "user_id",
    "viewer_id",
    "manager_id",
    "is_archive",
    "additional_data",
    "type",
    "moderate_comment",
})
public class ReportAttributesEntity {
    private int mProjectId;
    private int mChecklistId;
    private int mTradeObjectId;
    private int mStatus;
    private float mLatitude;
    private float mLongitude;
    private String mCreatedAt;
    private String mUpdatedAt;
    private Map<String, Object> mAttributes;

    public ReportAttributesEntity() {
        mAttributes = new HashMap<>();
    }

    public int getProjectId() {
        return mProjectId;
    }

    @JsonProperty("project_id")
    public void setProjectId(int projectId) {
        mProjectId = projectId;
    }

    public int getChecklistId() {
        return mChecklistId;
    }

    @JsonProperty("checklist_id")
    public void setChecklistId(int checklistId) {
        mChecklistId = checklistId;
    }

    public int getTradeObjectId() {
        return mTradeObjectId;
    }

    @JsonProperty("trade_object_id")
    public void setTradeObjectId(int tradeObjectId) {
        mTradeObjectId = tradeObjectId;
    }

    public int getStatus() {
        return mStatus;
    }

    @JsonProperty("status")
    public void setStatus(int status) {
        mStatus = status;
    }

    public float getLatitude() {
        return mLatitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(float latitude) {
        mLatitude = latitude;
    }

    public float getLongitude() {
        return mLongitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(float longitude) {
        mLongitude = longitude;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public Map<String, Object> getAttributes() {
        return mAttributes;
    }

    @JsonAnySetter
    public void setAttribute(String name, Object data) {
        mAttributes.put(name, data);
    }
}
