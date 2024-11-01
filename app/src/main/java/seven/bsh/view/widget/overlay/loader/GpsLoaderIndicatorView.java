package seven.bsh.view.widget.overlay.loader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import seven.bsh.R;

public class GpsLoaderIndicatorView {
    private final Context mContext;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public GpsLoaderIndicatorView(Context context) {
        mContext = context;
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    @SuppressLint("InflateParams")
    public View inflate(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.partial_gps_loader, null);
        parent.addView(view);
        return view;
    }
}
