package seven.bsh.view.attributes.views;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import seven.bsh.R;
import seven.bsh.view.attributes.AttributeItem;
import seven.bsh.view.attributes.settings.TextSettings;

public class StaticTextView extends BaseStaticView {
    private int viewId;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public StaticTextView(Context context, AttributeItem attribute, int viewId) {
        super(context, attribute);
        this.viewId = viewId;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView() {
        TextSettings settings = (TextSettings) getSettings();
        View view = View.inflate(getContext(), viewId, null);
        TextView labelField = view.findViewById(R.id.label);
        labelField.setText(settings.getText());
        return view;
    }
}
