package seven.bsh.view.attributes.values;

import android.content.Context;

import java.io.File;

import seven.bsh.R;
import seven.bsh.view.attributes.SerializableData;
import seven.bsh.view.attributes.settings.FileInputSettings;

public class FileInputValue extends BaseDataValue {
    protected File file;
    protected String path;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public FileInputValue(Context context) {
        super(context);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(Object data) {
        path = String.valueOf(data);
    }

    @Override
    public boolean validate() {
        FileInputSettings settings = (FileInputSettings) this.settings;
        Context context = getContext();
        if (settings.isRequired() && path == null) {
            error = context.getString(R.string.attribute_file_error_empty);
            return false;
        }

        int maxSize = settings.getMaxSize();
        if (maxSize > 0 && file != null && file.length() > maxSize) {
            error = context.getString(R.string.attribute_file_error_too_big);
            return false;
        }

        error = null;
        return true;
    }

    @Override
    public SerializableData serialize() {
        return null;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public boolean isChanged() {
        return file != null;
    }
}
