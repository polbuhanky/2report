package seven.bsh.view.start;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.multidex.BuildConfig;

import seven.bsh.view.BaseActivity;
import seven.bsh.view.tradeObject.TradeObjectActivity;
import seven.bsh.R;

public class StartActivity extends BaseActivity {
    public static final int REQUEST_QR_SCANNER = 0;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);
        TextView textView = findViewById(R.id.version_label);
        textView.setText(getString(R.string.fragment_start_label_version, BuildConfig.VERSION_NAME) + "\n(с) Еланд.рф");

        if (getLocalData().isLogin()) {
            Intent intent = new Intent(this, TradeObjectActivity.class);
            startActivity(intent);
            finish();
        } else {
            showFragment(LoginFragment.class, false);
        }
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_main;
    }
}
