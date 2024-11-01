package seven.bsh.view.attributes.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import seven.bsh.R;
import seven.bsh.db.entity.Sku;
import seven.bsh.view.attributes.IInputAttribute;
import seven.bsh.view.attributes.values.SkuInputValue;

public class SkuView extends BaseInputView {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SkuView(Context context, IInputAttribute attribute) {
        super(context, attribute);
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private View createSkuChooser(SkuInputValue.SkuData data) {
        View view = View.inflate(getContext(), R.layout.partial_attribute_sku_item_view, null);
        TextView priceField = view.findViewById(R.id.price_label);
        priceField.setText(String.valueOf(data.getPrice()));

        TextView quantityField = view.findViewById(R.id.quantity_label);
        quantityField.setText(String.valueOf(data.getQuantity()));

        TextView skuLabel = view.findViewById(R.id.sku_label);
        Sku model = data.getModel();
        if (model == null) {
            skuLabel.setText(R.string.attribute_sku_error_no_cache);
        } else {
            skuLabel.setText(model.getSku());
        }
        return view;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView() {
        SkuInputValue value = (SkuInputValue) getValue();
        ViewGroup view = (ViewGroup) View.inflate(getContext(), R.layout.partial_attribute_sku_view, null);
        TextView labelField = view.findViewById(R.id.label);
        labelField.setText(getTitleLabel());

        List<SkuInputValue.SkuData> skuDataList = value.getList();
        for (SkuInputValue.SkuData data : skuDataList) {
            View skuItem = createSkuChooser(data);
            view.addView(skuItem);
        }
        return view;
    }
}
