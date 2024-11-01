package seven.bsh.net.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import seven.bsh.db.entity.Checklist;
import seven.bsh.db.entity.Project;
import seven.bsh.db.entity.TradeObject;
import seven.bsh.model.Pagination;
import seven.bsh.net.entity.PaginationEntity;
import seven.bsh.net.entity.TradeObjectEntity;
import seven.bsh.net.entity.TradeObjectIncludeEntity;
import seven.bsh.net.parser.ChecklistParser;
import seven.bsh.net.parser.PaginationParser;
import seven.bsh.net.parser.ProjectParser;
import seven.bsh.net.parser.TradeObjectParser;

@SuppressWarnings("unused")
public class TradeObjectsGetResponse {
    private Pagination mPagination;
    private List<TradeObject> mTradeObjects;
    private List<Checklist> mChecklists;
    private List<Project> mProjects;
    private List<ChecklistRelation> mRelations;

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public Pagination getPagination() {
        return mPagination;
    }

    public List<TradeObject> getTradeObjects() {
        return mTradeObjects;
    }

    public List<Checklist> getChecklists() {
        return mChecklists;
    }

    public List<Project> getProjects() {
        return mProjects;
    }

    public List<ChecklistRelation> getRelations() {
        return mRelations;
    }

    @JsonProperty("data")
    public void setData(List<TradeObjectEntity> data) {
        TradeObjectParser parser = new TradeObjectParser();
        mTradeObjects = parser.parseAll(data);
    }

    @JsonProperty("include")
    public void setInclude(TradeObjectIncludeEntity include) {
        ProjectParser projectParser = new ProjectParser();
        mProjects = projectParser.parseAll(include.getProjects());

        ChecklistParser checklistParser = new ChecklistParser();
        mChecklists = checklistParser.parseAll(include.getChecklists());

        mRelations = new ArrayList<>();
        if (mTradeObjects.isEmpty() || mChecklists.isEmpty() || mProjects.isEmpty()) {
            return;
        }

        for (Project project : mProjects) {
            List<Integer> projectChecklistIds = project.getChecklistIds();
            if (projectChecklistIds == null) {
                continue;
            }

            for (TradeObject tradeObject : mTradeObjects) {
                List<Integer> tradeObjectProjectIds = tradeObject.getProjectIds();
                if (tradeObjectProjectIds == null) {
                    continue;
                }

                for (int tradeObjectProjectId : tradeObjectProjectIds) {
                    if (tradeObjectProjectId != project.getId()) {
                        continue;
                    }

                    for (int projectChecklistId : projectChecklistIds) {
                        ChecklistRelation relation = new ChecklistRelation();
                        relation.checklistId = projectChecklistId;
                        relation.tradeObjectId = tradeObject.getId();
                        relation.projectId = tradeObjectProjectId;
                        mRelations.add(relation);
                        tradeObject.setChecklistCount(tradeObject.getChecklistCount() + 1);
                    }
                }
            }
        }
    }

    @JsonProperty("meta")
    public void setMeta(PaginationEntity meta) {
        PaginationParser parser = new PaginationParser();
        mPagination = parser.parse(meta);
    }

    public class ChecklistRelation {
        public int checklistId;
        public int tradeObjectId;
        public int projectId;
    }
}
