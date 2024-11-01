package seven.bsh.net.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import seven.bsh.db.entity.Checklist;
import seven.bsh.db.entity.Project;
import seven.bsh.db.entity.Report;
import seven.bsh.db.entity.TradeObject;
import seven.bsh.model.Pagination;
import seven.bsh.net.entity.PaginationEntity;
import seven.bsh.net.entity.ReportEntity;
import seven.bsh.net.entity.ReportIncludeEntity;
import seven.bsh.net.parser.ChecklistParser;
import seven.bsh.net.parser.PaginationParser;
import seven.bsh.net.parser.ProjectParser;
import seven.bsh.net.parser.ReportParser;
import seven.bsh.net.parser.TradeObjectParser;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportsGetResponse {
    private Pagination mPagination;
    private List<Report> mReports;
    private List<TradeObject> mTradeObjects;
    private List<Project> mProjects;
    private List<Checklist> mChecklists;

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public Pagination getPagination() {
        return mPagination;
    }

    public List<Report> getReports() {
        return mReports;
    }

    @JsonProperty("data")
    public void setData(List<ReportEntity> data) {
        ReportParser parser = new ReportParser();
        mReports = parser.parseAll(data);
    }

    @JsonProperty("include")
    public void setInclude(ReportIncludeEntity include) {
        TradeObjectParser tradeObjectParser = new TradeObjectParser();
        mTradeObjects = tradeObjectParser.parseAll(include.getTradeObjects());

        ProjectParser projectParser = new ProjectParser();
        mProjects = projectParser.parseAll(include.getProjects());

        ChecklistParser checklistParser = new ChecklistParser();
        mChecklists = checklistParser.parseAll(include.getChecklists());

        if (mReports == null) {
            return;
        }

        for (Report report : mReports) {
            for (TradeObject tradeObject : mTradeObjects) {
                if (tradeObject.getId() == report.getTradeObjectId()) {
                    report.setTradeObjectName(tradeObject.getName());
                    report.setTradeObjectAddress(tradeObject.getAddress());
                }
            }
            for (Checklist checklist : mChecklists) {
                if (checklist.getId() == report.getChecklistId()) {
                    report.setChecklist(checklist);
                }
            }
            for (Project project : mProjects) {
                if (project.getId() == report.getProjectId()) {
                    report.setProjectName(project.getName());
                }
            }
        }
    }

    @JsonProperty("meta")
    public void setMeta(PaginationEntity meta) {
        PaginationParser parser = new PaginationParser();
        mPagination = parser.parse(meta);
    }
}
