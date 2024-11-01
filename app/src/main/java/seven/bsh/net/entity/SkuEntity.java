package seven.bsh.net.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class SkuEntity {
    private int mId;
    private SkuAttributesEntity mAttributes;

    public int getId() {
        return mId;
    }

    @JsonProperty("id")
    public void setId(String id) {
        mId = Integer.valueOf(id);
    }

    public SkuAttributesEntity getAttributes() {
        return mAttributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(SkuAttributesEntity attributes) {
        mAttributes = attributes;
    }
}
