package seven.bsh.view.report.view;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;

import seven.bsh.R;
import seven.bsh.db.entity.Report;
import seven.bsh.model.ChecklistAttribute;
import seven.bsh.net.ApiConnector;
import seven.bsh.net.listener.OnRefreshTokenRequestListener;
import seven.bsh.net.request.RefreshTokenRequest;
import seven.bsh.net.response.RefreshTokenResponse;
import seven.bsh.view.attributes.AttributeItem;
import seven.bsh.view.attributes.IInputAttribute;
import seven.bsh.view.report.BaseReportActivity;

public abstract class BaseViewReportActivity extends BaseReportActivity implements
    OnRefreshTokenRequestListener.OnRequestListener {
    public static final String ARGUMENT_REPORT_ID = "report_id";

    protected Report report;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_report_title);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------


    @Override
    public void onAuthSuccess(RefreshTokenResponse response) {
        // ignored
    }

    @Override
    public void onAuthFailed() {
        logout();
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    protected void fillContainer() {
        containerLayout.addView(createHeader());
        AttributeItem prevItem = null;
        attributeItems = new ArrayList<>();

        for (ChecklistAttribute attribute : checklist.getAttributeList()) {
            AttributeItem item = AttributeItem.Builder.create(this, attribute);
            if (item == null) {
                continue;
            }

            if (item instanceof IInputAttribute) {
                parseDataJson((IInputAttribute) item, report.getDataJson());
            }

            View itemView = item.createValueView();
            containerLayout.addView(itemView);
            prepareItemLayout(prevItem, item);
            prevItem = item;
        }
    }

    protected void sendAuthRequest() {
        ApiConnector api = getApi();
        if (api == null) {
            return;
        }

        String token = getLocalData().getRefreshToken();
        RefreshTokenRequest request = new RefreshTokenRequest(token);
        api.refreshToken(request, new OnRefreshTokenRequestListener(this));
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_report;
    }
}
