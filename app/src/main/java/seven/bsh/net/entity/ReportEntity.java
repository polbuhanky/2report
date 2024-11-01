package seven.bsh.net.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class ReportEntity {
    private int mId;
    private ReportAttributesEntity mAttributes;

    public int getId() {
        return mId;
    }

    @JsonProperty("id")
    public void setId(String id) {
        mId = Integer.parseInt(id);
    }

    public ReportAttributesEntity getAttributes() {
        return mAttributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(ReportAttributesEntity attributes) {
        mAttributes = attributes;
    }
}
