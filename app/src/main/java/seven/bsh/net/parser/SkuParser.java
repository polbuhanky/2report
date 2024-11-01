package seven.bsh.net.parser;

import seven.bsh.db.entity.Sku;
import seven.bsh.net.entity.SkuAttributesEntity;
import seven.bsh.net.entity.SkuEntity;

public class SkuParser extends Parser<SkuEntity, Sku> {

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public Sku parseOne(SkuEntity entity) {
        SkuAttributesEntity attributes = entity.getAttributes();
        Sku model = new Sku();
        model.setId(entity.getId());
        model.setSku(attributes.getName());
        model.setBrand(attributes.getBrand());
        model.setCategory(attributes.getCategory());
        model.setSubCategory(attributes.getSubCategory());
        model.setFlag(attributes.getFlag());
        return model;
    }
}
