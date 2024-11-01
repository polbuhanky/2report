package seven.bsh.view.start;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import seven.bsh.Application;
import seven.bsh.LocalData;
import seven.bsh.R;
import seven.bsh.db.DatabaseHelper;
import seven.bsh.model.QrLoginData;
import seven.bsh.net.ApiConnector;
import seven.bsh.net.listener.OnApiHostGetRequestListener;
import seven.bsh.net.listener.OnAuthRequestListener;
import seven.bsh.net.listener.OnSettingsGetRequestListener;
import seven.bsh.net.listener.OnUserProfileGetRequestListener;
import seven.bsh.net.request.ApiHostGetRequest;
import seven.bsh.net.request.AuthRequest;
import seven.bsh.net.request.SettingsGetRequest;
import seven.bsh.net.request.UserProfileGetRequest;
import seven.bsh.net.response.ApiHostGetResponse;
import seven.bsh.net.response.AuthResponse;
import seven.bsh.net.response.SettingsGetResponse;
import seven.bsh.net.response.UserProfileGetResponse;
import seven.bsh.parser.QrLoginDataParser;
import seven.bsh.view.BaseActivity;
import seven.bsh.view.BaseFragment;
import seven.bsh.view.start.form.LoginFormView;
import seven.bsh.view.tradeObject.TradeObjectActivity;
import seven.bsh.view.widget.button.LoginButton;

public class LoginFragment extends BaseFragment implements
    LoginButton.OnClickListener,
    View.OnClickListener,
    OnAuthRequestListener.OnRequestListener,
    OnSettingsGetRequestListener.OnRequestListener,
    OnApiHostGetRequestListener.OnRequestListener,
    OnUserProfileGetRequestListener.OnRequestListener,
    LoginFormView.OnListener {
    public static final String ARGUMENT_LOGIN = "login";
    public static final String ARGUMENT_PASSWORD = "password";
    public static final String ARGUMENT_ENV_ID = "id";

    private static final int REQUEST_PERMISSION_CAMERA = 0;

    private LoginFormView mForm;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mForm = new LoginFormView(view);
        mForm.setListener(this);

        ImageView backBtn = view.findViewById(R.id.button_back);
        backBtn.setOnClickListener(this);

        Bundle params = getArguments();
        if (params == null) {
            LocalData localData = getLocalData();
            boolean rememberLogin = localData.isRememberLogin();
            mForm.setRememberMe(rememberLogin);

            if (rememberLogin) {
                mForm.setLogin(localData.getLogin());
                mForm.setPassword(localData.getPassword());
                mForm.setEnvironmentId(localData.getEnvironmentId());
            }
        } else {
            mForm.setLogin(params.getString(ARGUMENT_LOGIN));
            mForm.setPassword(params.getString(ARGUMENT_PASSWORD));
            mForm.setEnvironmentId(params.getString(ARGUMENT_ENV_ID));
            onLoginButtonClick();
        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onLoginQrButtonClick();
            }
        }
    }

    @Override
    public void onLoginButtonClick() {
        getBaseActivity().hideKeyboard();
        showLoader();

        LocalData localData = getLocalData();
        String oldLogin = localData.getLogin();
        if (!mForm.getLogin().equals(oldLogin)) {
            DatabaseHelper.getInstance()
                .getTradeObjectRepository()
                .deleteAll();
            DatabaseHelper.getInstance()
                .getChecklistRepository()
                .deleteAll();
            DatabaseHelper.getInstance()
                .getProjectRepository()
                .deleteAll();
            DatabaseHelper.getInstance()
                .getReportRepository()
                .deleteAll();
        }

        localData.saveLoginData(
            mForm.getLogin(),
            mForm.getPassword(),
            mForm.getEnvironmentId(),
            mForm.isRememberMe()
        );
        ApiConnector api = getApi();
        if (api != null) {
            api.init(null);
            ApiHostGetRequest request = new ApiHostGetRequest(mForm.getEnvironmentId());
            api.getHost(request, new OnApiHostGetRequestListener(this));
        }
    }

    @Override
    public void onLoginQrButtonClick() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        if (checkPermission(Manifest.permission.CAMERA)) {
            startActivityForResult(new Intent(getContext(), QrScannerActivity.class), StartActivity.REQUEST_QR_SCANNER);
        } else {
            requestPermissions(
                new String[]{
                    Manifest.permission.CAMERA
                },
                REQUEST_PERMISSION_CAMERA
            );
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != QrScannerActivity.RESULT_OK) {
            return;
        }

        String qrData = data.getStringExtra(QrScannerActivity.EXTRA_DATA);
        QrLoginDataParser parser = new QrLoginDataParser();
        QrLoginData model = parser.parse(qrData);
        if (model == null) {
            showSimpleDialog(getString(R.string.fragment_login_error_wrong_qr));
            return;
        }

        mForm.setLogin(model.getLogin());
        mForm.setPassword(model.getPassword());
        mForm.setEnvironmentId(model.getEnvironmentId());
        onLoginButtonClick();
    }

    @Override
    public void onAuthFailedError() {
        mForm.setLoginError();
        mForm.setPasswordError();
        showSimpleDialog(getString(R.string.fragment_login_error_user_not_found));
        hideLoader();
    }

    @Override
    public void onAuth422Error(String error) {
        // TODO сделать вменяемый вывод JSON-ошибок
        showSimpleDialog(error);
        hideLoader();
    }

    @Override
    public void onTokenError() {
        onAuthFailedError();
    }

    @Override
    public void onApiHostGet404Error() {
        hideLoader();
        mForm.setEnvironmentIdError();
        if (isAdded()) {
            showSimpleDialog(getString(R.string.fragment_login_error_id_not_found));
        }
    }

    @Override
    public void onApiHostGetSuccess(ApiHostGetResponse response) {
        String host = response.getHost();
        if (!host.endsWith("/")) {
            host += "/";
        }

        getLocalData().saveEnvironmentData(
            host,
            response.getProjectId(),
            response.getProjectName()
        );

        BaseActivity activity = getBaseActivity();
        if (activity == null) {
            return;
        }

        ApiConnector api = getApi();
        api.init(host);
        AuthRequest request = new AuthRequest(mForm.getLogin(), mForm.getPassword());
        api.auth(request, new OnAuthRequestListener(LoginFragment.this));
    }

    @Override
    public void onAuthSuccess(AuthResponse response) {
        ApiConnector api = getApi();
        if (api == null) {
            return;
        }

        String token = response.getToken();
        Application.instance().getLocalData().saveToken(token, response.getRefreshToken());
        SettingsGetRequest request = new SettingsGetRequest(token);
        api.getSettings(request, new OnSettingsGetRequestListener(this));
    }

    @Override
    public void onSettingsGetSuccess(SettingsGetResponse response) {
        hideLoader();
        getLocalData().setImageMaxSize(response.getMaxImageSize());
        ApiConnector api = getApi();
        if (api == null) {
            return;
        }

        String token = getLocalData().getAccessToken();
        UserProfileGetRequest request = new UserProfileGetRequest(token);
        api.getUserProfile(request, new OnUserProfileGetRequestListener(this));
    }

    @Override
    public void onUserProfileGetSuccess(UserProfileGetResponse response) {
        Context context = getContext();
        if (context == null) {
            return;
        }

        UserProfileGetResponse.Data userData = response.getData();
        UserProfileGetResponse.Attributes userAttributes = userData.getAttributes();
        getLocalData().saveUser(
            userAttributes.getName(),
            userData.getId(),
            userAttributes.getRegisteredAt()
        );

        Intent intent = new Intent(getContext(), TradeObjectActivity.class);
        startActivity(intent);
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }
}
