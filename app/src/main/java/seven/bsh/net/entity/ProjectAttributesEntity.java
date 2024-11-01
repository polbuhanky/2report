package seven.bsh.net.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class ProjectAttributesEntity {
    private String mName;

    public String getName() {
        return mName;
    }

    @JsonProperty("name")
    public void setName(String name) {
        mName = name;
    }
}
