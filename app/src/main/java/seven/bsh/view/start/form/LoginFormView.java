package seven.bsh.view.start.form;

import android.view.View;

import seven.bsh.R;
import seven.bsh.view.widget.button.LoginButton;
import seven.bsh.view.widget.input.IconTextEdit;
import seven.bsh.view.widget.input.RememberMeSwitch;

public class LoginFormView implements LoginButton.OnClickListener {
    private OnListener mListener;
    private IconTextEdit mLoginField;
    private IconTextEdit mPasswordField;
    private IconTextEdit mEnvironmentIdField;
    private RememberMeSwitch mRememberMeSwitch;
    private String mLogin;
    private String mPassword;
    private String mEnvironmentId;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public LoginFormView(View view) {
        mLoginField = view.findViewById(R.id.edit_login);
        mPasswordField = view.findViewById(R.id.edit_password);
        mEnvironmentIdField = view.findViewById(R.id.edit_env_id);
        mRememberMeSwitch = view.findViewById(R.id.switch_remember_me);

        LoginButton loginBtn = view.findViewById(R.id.widget_login);
        loginBtn.setOnLoginClickListener(this);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onLoginButtonClick() {
        if (validate() && mListener != null) {
            mListener.onLoginButtonClick();
        }
    }

    @Override
    public void onLoginQrButtonClick() {
        if (mListener != null) {
            mListener.onLoginQrButtonClick();
        }
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public void setLoginError() {
        mLoginField.setHasError(true);
    }

    public void setPasswordError() {
        mPasswordField.setHasError(true);
    }

    public void setEnvironmentIdError() {
        mEnvironmentIdField.setHasError(true);
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private boolean validate() {
        boolean valid = loginValidate()
            & passwordValidate()
            & idValidate();

        if (valid) {
            mLoginField.setHasError(false);
            mPasswordField.setHasError(false);
            mEnvironmentIdField.setHasError(false);
        }
        return valid;
    }

    private boolean loginValidate() {
        mLogin = mLoginField.getText().trim();
        if (mLogin.isEmpty()) {
            setLoginError();
            return false;
        }
        return true;
    }

    private boolean passwordValidate() {
        mPassword = mPasswordField.getText().trim();
        if (mPassword.isEmpty()) {
            mPasswordField.setHasError(true);
            return false;
        }
        return true;
    }

    private boolean idValidate() {
        mEnvironmentId = mEnvironmentIdField.getText().trim();
        if (mEnvironmentId.isEmpty()) {
            mEnvironmentIdField.setHasError(true);
            return false;
        }
        return true;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        mLogin = login;
        if (login != null && !login.isEmpty()) {
            mLoginField.setText(login);
        }
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
        if (password != null && !password.isEmpty()) {
            mPasswordField.setText(password);
        }
    }

    public String getEnvironmentId() {
        return mEnvironmentId;
    }

    public void setEnvironmentId(String environmentId) {
        mEnvironmentId = environmentId;
        if (environmentId != null && !environmentId.isEmpty()) {
            mEnvironmentIdField.setText(environmentId);
        }
    }

    public boolean isRememberMe() {
        return mRememberMeSwitch.isChecked();
    }

    public void setRememberMe(boolean rememberMe) {
        mRememberMeSwitch.setChecked(rememberMe);
    }

    public void setListener(OnListener listener) {
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnListener {
        void onLoginButtonClick();

        void onLoginQrButtonClick();
    }
}
