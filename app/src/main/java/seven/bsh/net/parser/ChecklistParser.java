package seven.bsh.net.parser;

import seven.bsh.db.entity.Checklist;
import seven.bsh.net.entity.ChecklistAttributesEntity;
import seven.bsh.net.entity.ChecklistEntity;

public class ChecklistParser extends Parser<ChecklistEntity, Checklist> {

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public Checklist parseOne(ChecklistEntity entity) {
        Checklist model = new Checklist();
        model.setId(entity.getId());

        ChecklistAttributesEntity attributes = entity.getAttributes();
        model.setName(attributes.getName());
        model.setExpiresAt(attributes.getExpiresAt());
        model.setAvailableAt(attributes.getAvailableAt());
        model.setMultipleFilling(attributes.isMultipleFilling());
        model.setGps(attributes.isGpsEnabled());
        model.setGpsEpsilon(attributes.getGpsEpsilon());
        model.setFields(attributes.getFields());
        return model;
    }
}
