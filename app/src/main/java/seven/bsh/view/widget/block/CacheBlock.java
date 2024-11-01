package seven.bsh.view.widget.block;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import seven.bsh.R;
import seven.bsh.net.service.CacheService;
import seven.bsh.view.widget.progressbar.ProgressBar;

public class CacheBlock extends RelativeLayout implements View.OnClickListener {
    private AppCompatButton mGetBtn;
    private AppCompatButton mClearBtn;
    private ProgressBar mProgressBar;
    private AppCompatTextView mStatusField;
    private OnCacheBlockListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public CacheBlock(Context context) {
        super(context);
        init(null);
    }

    public CacheBlock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CacheBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(21)
    public CacheBlock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                onGetCacheBtn();
                break;

            case R.id.btn_clear:
                onClearCacheBtn();
                break;
        }
    }

    private void onGetCacheBtn() {
        mListener.onGetCacheBtn(this);
    }

    private void onClearCacheBtn() {
        mListener.onClearCacheBtn(this);
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.partial_widget_cache_block, this);

        mGetBtn = findViewById(R.id.btn_get);
        mGetBtn.setOnClickListener(this);

        mClearBtn = findViewById(R.id.btn_clear);
        mClearBtn.setOnClickListener(this);

        mProgressBar = findViewById(R.id.progress_bar);
        mStatusField = findViewById(R.id.status);

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CacheBlock, 0, 0);
            mGetBtn.setText(a.getString(R.styleable.CacheBlock_tr_getCacheText));
            a.recycle();
        }

        setEnabled(false);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public void setLastUpdate(String date) {
        if (date == null) {
            mStatusField.setText(R.string.activity_cache_label_not_updated);
        } else {
            mStatusField.setText(getContext().getString(R.string.activity_cache_label_updated_at, date));
        }
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mGetBtn.setEnabled(enabled);
        mClearBtn.setEnabled(enabled);
    }

    public void setListener(OnCacheBlockListener listener) {
        mListener = listener;
    }

    public void setStatus(int progress, int count) {
        mStatusField.setText(getContext().getString(R.string.activity_cache_label_status, progress, count));
    }

    public void setProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

    public void setError(int errorCode) {
        // TODO i18n
        String errorText;
        switch (errorCode) {
            case CacheService.ERROR_500:
                errorText = "Серверная ошибка";
                break;

            case CacheService.ERROR_PARSER:
                errorText = "Ошибка при разборе ответа от сервера";
                break;

            case CacheService.ERROR_NO_INTERNET:
                errorText = "Отсутствует интернет";
                break;

            default:
                errorText = "Непредвиденная ошибка";
                break;
        }
        mStatusField.setText(errorText);
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnCacheBlockListener {
        void onGetCacheBtn(CacheBlock view);

        void onClearCacheBtn(CacheBlock view);
    }
}
