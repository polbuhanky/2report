package seven.bsh.net.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import seven.bsh.db.entity.Report;
import seven.bsh.net.entity.ReportAttributesEntity;
import seven.bsh.net.entity.ReportEntity;

public class ReportParser extends Parser<ReportEntity, Report> {

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public Report parseOne(JSONObject data) throws JSONException {
        JSONObject attributes = data.getJSONObject("attributes");
        Report model = new Report();
        model.setData(attributes.toString());
        model.setId(Integer.parseInt(data.getString("id")));
        model.setChecklistId(attributes.getInt("checklist_id"));
        model.setTradeObjectId(attributes.getInt("trade_object_id"));
        model.setProjectId(attributes.getInt("project_id"));
        model.setStatus(attributes.getInt("status"));
        model.setCreatedAt(attributes.getString("created_at"));
        model.setUpdatedAt(attributes.getString("updated_at"));
        return model;
    }

    @Override
    public Report parseOne(ReportEntity entity) {
        Report model = new Report();
        model.setId(entity.getId());

        ReportAttributesEntity attributes = entity.getAttributes();
        model.setProjectId(attributes.getProjectId());
        model.setChecklistId(attributes.getChecklistId());
        model.setTradeObjectId(attributes.getTradeObjectId());
        model.setCreatedAt(attributes.getCreatedAt());
        model.setUpdatedAt(attributes.getUpdatedAt());

        try {
            ObjectMapper mapper = new ObjectMapper();
            String data = mapper.writeValueAsString(attributes.getAttributes());
            model.setData(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return model;
    }
}
