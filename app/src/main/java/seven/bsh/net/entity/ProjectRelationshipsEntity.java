package seven.bsh.net.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ProjectRelationshipsEntity {
    private List<Integer> mChecklists;

    public List<Integer> getChecklists() {
        return mChecklists;
    }

    @JsonProperty("checklists")
    public void setChecklists(List<String> ids) {
        mChecklists = new ArrayList<>();
        for (String id : ids) {
            mChecklists.add(Integer.valueOf(id));
        }
    }
}
