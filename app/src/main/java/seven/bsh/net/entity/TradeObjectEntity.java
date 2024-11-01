package seven.bsh.net.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class TradeObjectEntity {
    private int mId;
    private TradeObjectAttributesEntity mAttributes;
    private TradeObjectRelationshipsEntity mRelationships;

    public int getId() {
        return mId;
    }

    @JsonProperty("id")
    public void setId(String id) {
        mId = Integer.valueOf(id);
    }

    public TradeObjectAttributesEntity getAttributes() {
        return mAttributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(TradeObjectAttributesEntity attributes) {
        mAttributes = attributes;
    }

    public TradeObjectRelationshipsEntity getRelationships() {
        return mRelationships;
    }

    @JsonProperty("relationships")
    public void setRelationships(TradeObjectRelationshipsEntity relationships) {
        mRelationships = relationships;
    }
}
