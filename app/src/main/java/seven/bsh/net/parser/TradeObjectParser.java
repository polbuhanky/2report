package seven.bsh.net.parser;

import seven.bsh.db.entity.TradeObject;
import seven.bsh.net.entity.TradeObjectAttributesEntity;
import seven.bsh.net.entity.TradeObjectEntity;
import seven.bsh.net.entity.TradeObjectRelationshipsEntity;

public class TradeObjectParser extends Parser<TradeObjectEntity, TradeObject> {

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public TradeObject parseOne(TradeObjectEntity entity) {
        TradeObject model = new TradeObject();
        model.setId(entity.getId());

        TradeObjectAttributesEntity attributes = entity.getAttributes();
        model.setName(attributes.getName());
        model.setAddress(attributes.getAddress());

        TradeObjectRelationshipsEntity relationships = entity.getRelationships();
        if (relationships != null) {
            model.setProjectIds(relationships.getProjects());
        }
        return model;
    }
}
