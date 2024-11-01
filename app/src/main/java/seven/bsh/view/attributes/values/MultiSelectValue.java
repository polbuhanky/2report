package seven.bsh.view.attributes.values;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import seven.bsh.R;

public class MultiSelectValue extends BaseDataValue {
    private List<Integer> mIndexes;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public MultiSelectValue(Context context) {
        super(context);
        mIndexes = new ArrayList<>();
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(Object data) {
        mIndexes = new ArrayList<>();
        try {
            JSONArray indexes = (JSONArray) data;
            int count = indexes.length();
            for (int i = 0; i < count; i++) {
                mIndexes.add(indexes.getInt(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validate() {
        if (settings.isRequired() && mIndexes.isEmpty()) {
            error = getContext().getString(R.string.attribute_select_error_empty);
            return false;
        }

        error = null;
        return true;
    }

    @Override
    public Object serialize() {
        if (mIndexes.isEmpty()) {
            return null;
        }
        return new JSONArray(mIndexes);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public List<Integer> getIndexes() {
        return mIndexes;
    }

    public void setIndexes(List<Integer> indexes) {
        mIndexes = indexes;
    }

    @Override
    public boolean isChanged() {
        return !mIndexes.isEmpty();
    }
}
