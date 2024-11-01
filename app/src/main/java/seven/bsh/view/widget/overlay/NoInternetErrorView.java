package seven.bsh.view.widget.overlay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import seven.bsh.R;

public class NoInternetErrorView implements View.OnClickListener {
    private Context mContext;
    private OnNoInternetErrorListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public NoInternetErrorView(Context context, OnNoInternetErrorListener listener) {
        mContext = context;
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        mListener.onUpdateRequestClick();
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public View inflate(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.partial_modal_no_internet, null);
        parent.addView(view);

        Button updateBtn = view.findViewById(R.id.button_update);
        updateBtn.setOnClickListener(this);
        return view;
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnNoInternetErrorListener {
        void onUpdateRequestClick();
    }
}
