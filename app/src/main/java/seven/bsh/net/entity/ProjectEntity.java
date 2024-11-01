package seven.bsh.net.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class ProjectEntity {
    private int mId;
    private ProjectAttributesEntity attributes;
    private ProjectRelationshipsEntity relationships;

    public int getId() {
        return mId;
    }

    @JsonProperty("id")
    public void setId(String id) {
        mId = Integer.valueOf(id);
    }

    public ProjectAttributesEntity getAttributes() {
        return attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(ProjectAttributesEntity attributes) {
        this.attributes = attributes;
    }

    public ProjectRelationshipsEntity getRelationships() {
        return relationships;
    }

    @JsonProperty("relationships")
    public void setRelationships(ProjectRelationshipsEntity relationships) {
        this.relationships = relationships;
    }
}
