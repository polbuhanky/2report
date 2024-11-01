package seven.bsh.view.widget.overlay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import seven.bsh.R;

public class ServerErrorView implements View.OnClickListener {
    private final Context mContext;
    private final OnServerErrorListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public ServerErrorView(Context context, OnServerErrorListener listener) {
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
        switch (view.getId()) {
            case R.id.button_retry:
                mListener.onRetryRequestClick();
                break;

            case R.id.button_close:
                mListener.onCloseErrorClick();
                break;
        }
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public View inflate(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.partial_modal_server_error, null);
        parent.addView(view);

        Button closeBtn = view.findViewById(R.id.button_close);
        closeBtn.setOnClickListener(this);

        Button retryBtn = view.findViewById(R.id.button_retry);
        retryBtn.setOnClickListener(this);
        return view;
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnServerErrorListener {
        void onRetryRequestClick();

        void onCloseErrorClick();
    }
}
