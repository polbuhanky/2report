package seven.bsh.view.attributes.fields;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import seven.bsh.R;
import seven.bsh.db.entity.Sku;
import seven.bsh.view.attributes.SkuAttribute;
import seven.bsh.view.attributes.values.SkuInputValue;
import seven.bsh.view.widget.spinner.SkuChooser;

public class SkuInputField extends BaseInputField implements
    View.OnClickListener,
    SkuChooser.OnSkuChoiceRemoveListener,
    SkuChooser.OnSkuChoiceListener {
    private final List<String> mBrands;
    private final List<SkuChooser> mSkuChoosers;
    private LinearLayout mContainer;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SkuInputField(Context context, SkuAttribute attribute) {
        super(context, attribute);
        mBrands = getDb().getSkuRepository().getBrands();
        mSkuChoosers = new ArrayList<>();
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        addSkuChooser();
    }

    @Override
    public void onSkuRemove(SkuChooser target) {
        mContainer.removeView(target);
        mSkuChoosers.remove(target);
    }

    @Override
    public void onSkuBrandChange(SkuChooser target, String brand) {
        List<String> categories = getDb().getSkuRepository()
            .getCategories(brand);
        target.setCategories(categories);
    }

    @Override
    public void onSkuCategoryChange(SkuChooser target, String brand, String category) {
        List<String> subCategories = getDb().getSkuRepository()
            .getSubCategories(brand, category);
        target.setSubCategories(subCategories);
    }

    @Override
    public void onSkuSubCategoryChange(SkuChooser target, String brand, String category, String subCategory) {
        List<Sku> skus = getDb().getSkuRepository()
            .getAll(brand, category, subCategory);
        target.setSkus(skus);
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void addSkuChooser() {
        SkuChooser skuChooser = new SkuChooser(getContext());
        skuChooser.setBrands(mBrands);
        skuChooser.setListener(this);
        skuChooser.setRemoveListener(this);
        mSkuChoosers.add(skuChooser);
        mContainer.addView(skuChooser);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView() {
        View view = View.inflate(getContext(), R.layout.partial_attribute_sku, null);
        mContainer = view.findViewById(R.id.sku_container);
        errorField = view.findViewById(R.id.error);

        SkuChooser skuChooser = view.findViewById(R.id.sku_chooser);
        skuChooser.hideRemoveButton();
        skuChooser.setBrands(mBrands);
        skuChooser.setListener(this);
        skuChooser.setRemoveListener(this);
        mSkuChoosers.add(skuChooser);

        TextView labelField = view.findViewById(R.id.label);
        labelField.setText(getTitleLabel());

        TextView addBtn = view.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(this);

        SkuInputValue value = (SkuInputValue) getValue();
        List<SkuInputValue.SkuData> skuDataList = value.getList();
        for (int i = 1; i < skuDataList.size(); i++) {
            addSkuChooser();
        }

        for (int i = 0; i < skuDataList.size(); i++) {
            skuChooser = mSkuChoosers.get(i);
            SkuInputValue.SkuData skuData = skuDataList.get(i);
            Sku model = skuData.getModel();
            if (model != null) {
                String brand = model.getBrand();
                String category = model.getCategory();
                String subCategory = model.getSubCategory();
                String skuName = model.getSku();

                onSkuBrandChange(skuChooser, brand);
                onSkuCategoryChange(skuChooser, brand, category);
                onSkuSubCategoryChange(skuChooser, brand, category, subCategory);
                skuChooser.setBrand(brand);
                skuChooser.setCategory(category);
                skuChooser.setSubCategory(subCategory);
                skuChooser.setSku(skuName);
            }

            skuChooser.setPrice(skuData.getPrice());
            skuChooser.setQuantity(skuData.getQuantity());
        }
        return view;
    }

    public void prepareDataValue() {
        SkuInputValue value = (SkuInputValue) getValue();
        try {
            value.clear();
            for (SkuChooser skuChooser : mSkuChoosers) {
                value.addSku(skuChooser.serialize());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
