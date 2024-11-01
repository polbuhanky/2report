package seven.bsh.net.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TradeObjectRelationshipsEntity {
    private List<Integer> mProjects;

    public List<Integer> getProjects() {
        return mProjects;
    }

    @JsonProperty("projects")
    public void setProjects(List<String> ids) {
        mProjects = new ArrayList<>();
        for (String id : ids) {
            mProjects.add(Integer.valueOf(id));
        }
    }
}
