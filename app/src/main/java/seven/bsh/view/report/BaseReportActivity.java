package seven.bsh.view.report;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import seven.bsh.R;
import seven.bsh.db.entity.Checklist;
import seven.bsh.db.entity.TradeObject;
import seven.bsh.utils.Converter;
import seven.bsh.view.BaseActivity;
import seven.bsh.view.attributes.AttributeItem;
import seven.bsh.view.attributes.BigTextAttribute;
import seven.bsh.view.attributes.IInputAttribute;
import seven.bsh.view.attributes.MediumTextAttribute;

public abstract class BaseReportActivity extends BaseActivity {
    private static final String TAG = "BaseReportActivity";

    protected LinearLayout containerLayout;
    protected CardView sendReportCV;

    protected Checklist checklist;
    protected TradeObject tradeObject;
    protected List<IInputAttribute> attributeItems;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setupToolbar();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        );
        attributeItems = new ArrayList<>();
        containerLayout = findViewById(R.id.layout_container);
        sendReportCV = findViewById(R.id.sendReportCV);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    @SuppressLint("SetTextI18n")
    protected View createHeader() {
        View view = View.inflate(this, R.layout.partial_report_header, null);
        TextView nameField = view.findViewById(R.id.checklist_name);
        nameField.setText(checklist.getName());

        TextView toNameField = view.findViewById(R.id.trade_object_name);
        if (tradeObject == null) {
            toNameField.setVisibility(View.GONE);
        } else {
            toNameField.setText(tradeObject.getName() + ", " + tradeObject.getAddress());
        }

        if (!checklist.isMultipleFilling()) {
            TextView multipleFilling = view.findViewById(R.id.multiple_filling);
            multipleFilling.setVisibility(View.GONE);
        }
        if (!checklist.isGps()) {
            TextView gps = view.findViewById(R.id.gps);
            gps.setVisibility(View.GONE);
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(
            layoutParams.leftMargin,
            layoutParams.topMargin,
            layoutParams.rightMargin,
            Converter.dip2Px(this, 8)
        );
        view.setLayoutParams(layoutParams);
        return view;
    }

    protected void prepareItemLayout(AttributeItem prevItem, AttributeItem item) {
        if ((item instanceof BigTextAttribute || item instanceof MediumTextAttribute) &&
            ((prevItem instanceof BigTextAttribute || prevItem instanceof MediumTextAttribute))) {
            prevItem.getLayout().setPadding(0, 0, 0, 0);
        } else if (item instanceof IInputAttribute) {
            attributeItems.add((IInputAttribute) item);
        }
    }

    protected void parseDataJson(IInputAttribute item, JSONObject data) {
        try {
            String key = item.getName();
            if (key != null && data.has(key)) {
                item.setData(data.get(key));
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString(), ex);
        }
    }
}
