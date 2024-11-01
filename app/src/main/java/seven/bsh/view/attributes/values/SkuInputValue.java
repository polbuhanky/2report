package seven.bsh.view.attributes.values;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import seven.bsh.R;
import seven.bsh.db.DatabaseHelper;
import seven.bsh.db.entity.Sku;

public class SkuInputValue extends BaseDataValue {
    private static final String TAG = "SkuInputValue";
    private List<SkuData> mList;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SkuInputValue(Context context) {
        super(context);
        mList = new ArrayList<>();
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(Object data) {
        try {
            JSONArray dataList = (JSONArray) data;
            int length = dataList.length();
            for (int i = 0; i < length; i++) {
                JSONObject skuDataJson = dataList.getJSONObject(i);
                int skuId = skuDataJson.optInt("id");
                Double price = skuDataJson.optDouble("price");
                Double quantity = skuDataJson.optDouble("quantity");

                SkuData skuData = new SkuData();
                if (!price.equals(Double.NaN)) {
                    skuData.setPrice(price.floatValue());
                }
                if (!quantity.equals(Double.NaN)) {
                    skuData.setQuantity(quantity.floatValue());
                }

                if (skuId > 0) {
                    skuData.setId(skuId);
                    Sku skuModel = getDb().getSkuRepository().get(skuData.getId());
                    skuData.setModel(skuModel);
                }
                mList.add(skuData);
            }
        } catch (JSONException ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    public boolean validate() {
        Context context = getContext();
        SkuData first = mList.get(0);

        for (SkuData skuData : mList) {
            if (!skuData.validate(settings.isRequired(), first == skuData)) {
                error = context.getString(R.string.attribute_sku_error_required);
                return false;
            }
        }

        error = null;
        return true;
    }

    @Override
    public Object serialize() {
        try {
            JSONArray data = new JSONArray();
            for (SkuInputValue.SkuData sku : mList) {
                JSONObject json = new JSONObject();
                int id = sku.getId();
                float price = sku.getPrice();
                float quantity = sku.getQuantity();

                if (id > 0) {
                    json.put("id", sku.getId());
                }
                if (price != SkuInputValue.SkuData.EMPTY) {
                    json.put("price", sku.getPrice());
                }
                if (quantity != SkuInputValue.SkuData.EMPTY) {
                    json.put("quantity", sku.getQuantity());
                }
                if (json.length() > 0) {
                    data.put(json);
                }
            }

            if (data.length() > 0) {
                return data;
            }
            return null;
        } catch (JSONException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            return null;
        }
    }

    public void addSku(SkuData sku) {
        mList.add(sku);
    }

    public void clear() {
        mList.clear();
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    private DatabaseHelper getDb() {
        return DatabaseHelper.getInstance();
    }

    public List<SkuData> getList() {
        return mList;
    }

    @Override
    public boolean isChanged() {
        if (mList.isEmpty()) {
            return false;
        }

        SkuData sku = mList.get(0);
        return sku.getModel() != null
            || sku.getPrice() != SkuData.EMPTY
            || sku.getQuantity() != SkuData.EMPTY;
    }

    //---------------------------------------------------------------------------
    //
    // CLASSES
    //
    //---------------------------------------------------------------------------

    public static class SkuData {
        public static final int EMPTY = -1;

        private int mId;
        private float mPrice = EMPTY;
        private float mQuantity = EMPTY;
        private Sku mModel;

        public boolean validate(boolean required, boolean first) {
            boolean quantityEmpty = mQuantity == EMPTY;
            boolean priceEmpty = mPrice == EMPTY;
            boolean modelEmpty = mModel == null;

            if (required || !first) {
                return !quantityEmpty && !priceEmpty && !modelEmpty;
            }
            return (quantityEmpty && priceEmpty && modelEmpty) ||
                (!quantityEmpty && !priceEmpty && !modelEmpty);
        }

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            this.mId = id;
        }

        public float getPrice() {
            return mPrice;
        }

        public void setPrice(float price) {
            this.mPrice = price;
        }

        public float getQuantity() {
            return mQuantity;
        }

        public void setQuantity(float quantity) {
            this.mQuantity = quantity;
        }

        public Sku getModel() {
            return mModel;
        }

        public void setModel(Sku model) {
            this.mModel = model;
        }

        @Override
        public String toString() {
            return mModel.getBrand() + ", " +
                mModel.getCategory() + ", " +
                mModel.getSubCategory() + ", " +
                mModel.getSku();
        }
    }
}
