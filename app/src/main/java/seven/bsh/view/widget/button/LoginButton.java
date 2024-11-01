package seven.bsh.view.widget.button;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import seven.bsh.R;

public class LoginButton extends RelativeLayout implements View.OnClickListener {
    private Button mLoginBtn;
    private ImageButton mLoginQrBtn;
    private OnClickListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public LoginButton(Context context) {
        super(context);
        init();
    }

    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoginButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public LoginButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        if (mListener == null) {
            return;
        }

        switch (view.getId()) {
            case R.id.button_login:
                mListener.onLoginButtonClick();
                break;

            case R.id.button_login_qr:
                mListener.onLoginQrButtonClick();
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams layoutParams = mLoginQrBtn.getLayoutParams();
        layoutParams.height = mLoginBtn.getMeasuredHeight();
        mLoginQrBtn.setLayoutParams(layoutParams);
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void init() {
        inflate(getContext(), R.layout.partial_button_login, this);

        mLoginBtn = findViewById(R.id.button_login);
        mLoginBtn.setOnClickListener(this);

        mLoginQrBtn = findViewById(R.id.button_login_qr);
        ViewGroup.LayoutParams layoutParams = mLoginQrBtn.getLayoutParams();
        layoutParams.height = mLoginBtn.getMeasuredHeight();
        mLoginQrBtn.setLayoutParams(layoutParams);
        mLoginQrBtn.setOnClickListener(this);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC ACCESSORS
    //
    //---------------------------------------------------------------------------

    public void setOnLoginClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public interface OnClickListener {
        void onLoginButtonClick();

        void onLoginQrButtonClick();
    }
}
