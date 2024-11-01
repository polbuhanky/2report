package seven.bsh.view.attributes.fields.filters;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

public class PriceInputFilter implements InputFilter {

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String checkedText = dest.subSequence(0, dstart).toString() +
            source.subSequence(start, end) +
            dest.subSequence(dend, dest.length()).toString();

        if (!Pattern.matches("[0-9]+([.]|[.][0-9]{1,2})?", checkedText)) {
            return "";
        }
        return null;
    }
}
