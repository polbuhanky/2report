package seven.bsh.view.attributes.views;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import seven.bsh.R;
import seven.bsh.view.attributes.IInputAttribute;
import seven.bsh.view.attributes.values.FileInputValue;

public class FileInputView extends BaseInputView {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public FileInputView(Context context, IInputAttribute attribute) {
        super(context, attribute);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView() {
        FileInputValue value = (FileInputValue) getValue();
        View view = View.inflate(getContext(), R.layout.partial_attribute_file_view, null);
        TextView labelField = view.findViewById(R.id.label);
        labelField.setText(getTitleLabel());

        String path = value.getPath();
        TextView fileLabel = view.findViewById(R.id.file_label);
        if (path == null || path.isEmpty()) {
            fileLabel.setText(R.string.attribute_file_empty);
        } else {
            fileLabel.setText(R.string.attribute_file_remote);
        }
        return view;
    }
}
