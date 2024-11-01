package seven.bsh.view.widget.button;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.mikepenz.iconics.view.IconicsImageView;

import seven.bsh.R;

public class ExpandableButton extends RelativeLayout implements View.OnClickListener {
    private OnClickListener mListener;
    private AppCompatTextView mLabel;
    private IconicsImageView mIconLabel;
    private boolean mExpand;
    private String mExpandText = "";
    private String mCollapseText = "";

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public ExpandableButton(Context context) {
        super(context);
        init(null);
    }

    public ExpandableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ExpandableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(21)
    public ExpandableButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        toggle();
        if (mListener != null) {
            mListener.onExpandableButtonClick(this, mExpand);
        }
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.partial_button_expandable, this);
        mLabel = findViewById(R.id.label);
        mIconLabel = findViewById(R.id.icon);

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ExpandableButton, 0, 0);
            mCollapseText = a.getString(R.styleable.ExpandableButton_tr_collapseText);
            mExpandText = a.getString(R.styleable.ExpandableButton_tr_expandText);
            a.recycle();
        }

        mLabel.setText(mExpandText);
        setOnClickListener(this);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public void toggle() {
        setExpand(!mExpand);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC ACCESSORS
    //
    //---------------------------------------------------------------------------

    public void setOnExpandableClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public void setExpand(boolean expand) {
        if (mExpand == expand) {
            return;
        }

        mExpand = expand;
        if (expand) {
            mLabel.setText(mCollapseText);
//            mIconLabel.setIcon(GoogleMaterial.Icon.gmd_expand_less);
        } else {
            mLabel.setText(mExpandText);
//            mIconLabel.setIcon(GoogleMaterial.Icon.gmd_expand_more);
        }
    }

    public interface OnClickListener {
        void onExpandableButtonClick(ExpandableButton view, boolean expand);
    }
}
