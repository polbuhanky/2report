package seven.bsh.net.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@SuppressWarnings("unused")
public class ReportIncludeEntity {
    private List<TradeObjectEntity> mTradeObjects;
    private List<ProjectEntity> mProjects;
    private List<ChecklistEntity> mChecklists;

    public List<TradeObjectEntity> getTradeObjects() {
        return mTradeObjects;
    }

    @JsonProperty("trade-object")
    public void setTradeObjects(List<TradeObjectEntity> tradeObjects) {
        mTradeObjects = tradeObjects;
    }

    public List<ProjectEntity> getProjects() {
        return mProjects;
    }

    @JsonProperty("project")
    public void setProjects(List<ProjectEntity> projects) {
        mProjects = projects;
    }

    public List<ChecklistEntity> getChecklists() {
        return mChecklists;
    }

    @JsonProperty("checklist")
    public void setChecklists(List<ChecklistEntity> checklists) {
        mChecklists = checklists;
    }
}
