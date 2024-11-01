package seven.bsh.view.attributes.values;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import seven.bsh.R;
import seven.bsh.view.attributes.settings.FileInputSettings;

public class PhotoInputValue extends FileInputValue {
    private static final String[] EXTENSIONS = {
        "jpg",
        "jpeg",
        "png",
    };

    private String mPreview;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public PhotoInputValue(Context context) {
        super(context);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(Object data) {
        if (data == null || data == JSONObject.NULL) {
            return;
        }

        try {
            JSONObject json = (JSONObject) data;
            path = json.getString("image");
            mPreview = json.getString("preview");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean validate() {
        FileInputSettings settings = (FileInputSettings) this.settings;
        Context context = getContext();
        if (settings.isRequired() && path == null) {
            error = context.getString(R.string.attribute_file_error_empty);
            return false;
        }

        if (file != null) {
            String fileName = file.getName();
            String ext = getExtension(fileName);
            if (ext == null) {
                error = context.getString(R.string.attribute_file_error_incorrect);
                return false;
            }

            ext = ext.toLowerCase(Locale.getDefault());
            List<String> extensions = Arrays.asList(EXTENSIONS);
            if (!extensions.contains(ext)) {
                error = context.getString(R.string.attribute_file_error_incorrect);
                return false;
            }
        }

        error = null;
        return true;
    }

    private String getExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } catch (Exception ex) {
            return null;
        }
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getPreview() {
        return mPreview;
    }
}
