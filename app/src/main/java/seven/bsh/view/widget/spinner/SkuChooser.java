package seven.bsh.view.widget.spinner;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import seven.bsh.R;
import seven.bsh.db.entity.Sku;
import seven.bsh.view.attributes.values.SkuInputValue;

public class SkuChooser extends LinearLayout implements
    SingleChoiceSpinner.OnSpinnerChooseListener,
    View.OnClickListener {
    private OnSkuChoiceListener mListener;
    private OnSkuChoiceRemoveListener mRemoveListener;
    private List<String> mCategories;
    private List<String> mSubCategories;
    private List<String> mBrands;
    private List<Sku> mSkus;
    private String[] mEmptyList;

    private SingleChoiceSpinner mBrandSpinner;
    private SingleChoiceSpinner mCategorySpinner;
    private SingleChoiceSpinner mSubCategorySpinner;
    private SearchSingleChoiceSpinner mSkuSpinner;
    private TextView mPriceField;
    private TextView mQuantityField;
    private TextView mRemoveBtn;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SkuChooser(Context context) {
        super(context);
        init();
    }

    public SkuChooser(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SkuChooser(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public SkuChooser(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onSpinnerChooseListener(SingleChoiceSpinner target, int index) {
        if (target == mBrandSpinner) {
            onBrandSpinnerChoose(index);
        } else if (target == mCategorySpinner) {
            onCategorySpinnerChoose(index);
        } else if (target == mSubCategorySpinner) {
            onSubCategorySpinnerChoose(index);
        }
    }

    private void onBrandSpinnerChoose(int index) {
        if (index == -1) {
            mListener.onSkuBrandChange(this, null);
            mCategorySpinner.setList(mEmptyList);
            mCategorySpinner.setEnabled(false);
        } else {
            mListener.onSkuBrandChange(this, mBrands.get(index));
            mCategorySpinner.setEnabled(true);
        }

        mSubCategorySpinner.setEnabled(false);
        mSkuSpinner.setEnabled(false);
        mSubCategorySpinner.setList(mEmptyList);
        mSkuSpinner.setList(mEmptyList);
    }

    private void onCategorySpinnerChoose(int index) {
        String brand = mBrands.get(mBrandSpinner.getSelectedIndex());
        if (index == -1) {
            mListener.onSkuCategoryChange(this, brand, null);
            mSubCategorySpinner.setList(mEmptyList);
            mSubCategorySpinner.setEnabled(false);
        } else {
            String category = mCategories.get(index);
            mListener.onSkuCategoryChange(this, brand, category);
            mSubCategorySpinner.setEnabled(true);
        }

        mSkuSpinner.setEnabled(false);
        mSkuSpinner.setList(mEmptyList);
    }

    private void onSubCategorySpinnerChoose(int index) {
        String brand = mBrands.get(mBrandSpinner.getSelectedIndex());
        String category = mCategories.get(mCategorySpinner.getSelectedIndex());
        if (index == -1) {
            mListener.onSkuSubCategoryChange(this, brand, category, null);
            mSkuSpinner.setList(mEmptyList);
            mSkuSpinner.setEnabled(false);
        } else {
            String subCategory = mSubCategories.get(index);
            mListener.onSkuSubCategoryChange(this, brand, category, subCategory);
            mSkuSpinner.setEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (mRemoveListener != null) {
            mRemoveListener.onSkuRemove(this);
        }
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void init() {
        inflate(getContext(), R.layout.partial_widget_sku, this);
        mEmptyList = new String[1];
        mEmptyList[0] = getContext().getString(R.string.attribute_select_empty);
        mPriceField = findViewById(R.id.price_field);
        mQuantityField = findViewById(R.id.quantity_field);

        mRemoveBtn = findViewById(R.id.remove_btn);
        mRemoveBtn.setOnClickListener(this);

        mBrandSpinner = findViewById(R.id.brand_spinner);
        mBrandSpinner.setListener(this);

        mCategorySpinner = findViewById(R.id.category_spinner);
        mCategorySpinner.setListener(this);
        mCategorySpinner.setList(mEmptyList);
        mCategorySpinner.setEnabled(false);

        mSubCategorySpinner = findViewById(R.id.sub_category_spinner);
        mSubCategorySpinner.setEnabled(false);
        mSubCategorySpinner.setList(mEmptyList);
        mSubCategorySpinner.setListener(this);

        mSkuSpinner = findViewById(R.id.sku_spinner);
        mSkuSpinner.setEnabled(false);
        mSkuSpinner.setList(mEmptyList);
        mSkuSpinner.setListener(this);
    }

    private String[] getList(List<String> source) {
        if (source == null) {
            return mEmptyList;
        }

        int length = source.size();
        String[] list = new String[length + 1];
        list[0] = getContext().getString(R.string.attribute_select_empty);
        for (int i = 0; i < length; i++) {
            list[i + 1] = source.get(i);
        }
        return list;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public SkuInputValue.SkuData serialize() {
        String price = mPriceField.getText().toString();
        String quantity = mQuantityField.getText().toString();
        int skuIndex = mSkuSpinner.getSelectedIndex();

        SkuInputValue.SkuData data = new SkuInputValue.SkuData();
        if (!price.isEmpty()) {
            data.setPrice(Float.valueOf(price));
        }
        if (!quantity.isEmpty()) {
            data.setQuantity(Float.valueOf(quantity));
        }

        if (skuIndex > -1) {
            Sku sku = mSkus.get(skuIndex);
            data.setModel(sku);
            data.setId(sku.getId());
        }
        return data;
    }

    public void hideRemoveButton() {
        mRemoveBtn.setVisibility(GONE);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public void setListener(OnSkuChoiceListener listener) {
        mListener = listener;
    }

    public void setCategories(List<String> categories) {
        mCategories = categories;
        mCategorySpinner.setList(getList(categories));
        mSubCategorySpinner.setSelectedIndex(-1);
        mSkuSpinner.setSelectedIndex(-1);
    }

    public void setSubCategories(List<String> subCategories) {
        mSubCategories = subCategories;
        mSubCategorySpinner.setList(getList(subCategories));
        mSkuSpinner.setSelectedIndex(-1);
    }

    public void setBrands(List<String> brands) {
        mBrands = brands;
        mBrandSpinner.setList(getList(brands));
    }

    public void setSkus(List<Sku> skus) {
        mSkus = skus;
        if (skus == null) {
            return;
        }

        int length = skus.size();
        String[] list = new String[length + 1];
        list[0] = getContext().getString(R.string.attribute_select_empty);
        for (int i = 0; i < length; i++) {
            list[i + 1] = skus.get(i).getSku();
        }
        mSkuSpinner.setList(list);
    }

    public void setBrand(String name) {
        for (int i = 0; i < mBrands.size(); i++) {
            String brand = mBrands.get(i);
            if (brand.equals(name)) {
                mBrandSpinner.setSelectedIndex(i);
                break;
            }
        }
        mCategorySpinner.setEnabled(true);
    }

    public void setCategory(String name) {
        for (int i = 0; i < mCategories.size(); i++) {
            String category = mCategories.get(i);
            if (category.equals(name)) {
                mCategorySpinner.setSelectedIndex(i);
                break;
            }
        }
        mSubCategorySpinner.setEnabled(true);
    }

    public void setSubCategory(String name) {
        for (int i = 0; i < mSubCategories.size(); i++) {
            String subCategory = mSubCategories.get(i);
            if (subCategory.equals(name)) {
                mSubCategorySpinner.setSelectedIndex(i);
                break;
            }
        }
        mSkuSpinner.setEnabled(true);
    }

    public void setSku(String name) {
        for (int i = 0; i < mSkus.size(); i++) {
            Sku sku = mSkus.get(i);
            if (sku.getSku().equals(name)) {
                mSkuSpinner.setSelectedIndex(i);
                break;
            }
        }
    }

    public void setPrice(float price) {
        if (price == -1) {
            mPriceField.setText("");
        } else {
            mPriceField.setText(String.valueOf(price));
        }
    }

    public void setQuantity(float quantity) {
        if (quantity == -1) {
            mQuantityField.setText("");
        } else {
            mQuantityField.setText(String.valueOf(quantity));
        }
    }

    public void setRemoveListener(OnSkuChoiceRemoveListener removeListener) {
        mRemoveListener = removeListener;
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnSkuChoiceListener {
        void onSkuBrandChange(SkuChooser target, String brand);

        void onSkuCategoryChange(SkuChooser target, String brand, String category);

        void onSkuSubCategoryChange(SkuChooser target, String brand, String category, String subCategory);
    }

    public interface OnSkuChoiceRemoveListener {
        void onSkuRemove(SkuChooser target);
    }
}
