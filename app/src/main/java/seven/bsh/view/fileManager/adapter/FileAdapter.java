package seven.bsh.view.fileManager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import seven.bsh.R;

public class FileAdapter extends ArrayAdapter<File> {
    private final DecimalFormat mDecimalFormat;
    private int mNesting;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public FileAdapter(Context context, List<File> list) {
        super(context, 0, list);
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(' ');
        mDecimalFormat = new DecimalFormat("0.##", otherSymbols);
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private String getSize(long bytes) {
        if (bytes < 1024D) {
            return bytes + " B";
        } else if (bytes < 1048576D) {
            return mDecimalFormat.format(bytes / 1024D) + " KB";
        } else if (bytes < 1073741824D) {
            return mDecimalFormat.format(bytes / 1048576D) + " MB";
        }
        return mDecimalFormat.format(bytes / 1073741824D) + " GB";
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        File file = getItem(position);
        String fileName;
        View view;

        if (file == null) {
            view = inflater.inflate(R.layout.item_directory, parent, false);
            fileName = "..";
        } else {
            fileName = file.getName();
            if (file.isDirectory()) {
                view = inflater.inflate(R.layout.item_directory, parent, false);

                if (fileName.isEmpty() && mNesting == 0) {
                    fileName = getContext().getString(R.string.fragment_file_manager_rootDir);
                } else if (position == 0) {
                    fileName = "..";
                }
            } else {
                view = inflater.inflate(R.layout.item_file, parent, false);

                TextView sizeField = view.findViewById(R.id.size);
                sizeField.setText(getSize(file.length()));
            }
        }

        TextView nameField = view.findViewById(R.id.name);
        nameField.setText(fileName);
        return view;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public void setNesting(int nesting) {
        mNesting = nesting;
    }
}
