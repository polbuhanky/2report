package seven.bsh.net.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class ChecklistEntity {
    private int mId;
    private ChecklistAttributesEntity mAttributes;

    public int getId() {
        return mId;
    }

    @JsonProperty("id")
    public void setId(String id) {
        mId = Integer.valueOf(id);
    }

    public ChecklistAttributesEntity getAttributes() {
        return mAttributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(ChecklistAttributesEntity attributes) {
        mAttributes = attributes;
    }
}
