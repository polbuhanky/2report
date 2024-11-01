package seven.bsh.view.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import seven.bsh.R;

public class SearchListViewDialog extends Dialog implements
    AdapterView.OnItemClickListener,
    TextWatcher {
    private String[] mList;
    private final ArrayAdapter<String> mAdapter;
    private OnItemSelectedListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SearchListViewDialog(Context context) {
        super(context);
        mAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modal_sku_search);

        EditText searchField = findViewById(R.id.search_field);
        searchField.addTextChangedListener(this);

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (mListener != null) {
            String item = mAdapter.getItem(position);
            for (int i = 0; i < mList.length; i++) {
                if (item.equals(mList[i])) {
                    mListener.onItemSelected(i);
                    break;
                }
            }
        }
        dismiss();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // ignored
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // ignored
    }

    @Override
    public void afterTextChanged(Editable editable) {
        mAdapter.getFilter().filter(editable.toString());
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public void setList(String[] list) {
        mList = list;
        mAdapter.clear();
        mAdapter.addAll(list);
    }

    public void setListener(OnItemSelectedListener listener) {
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnItemSelectedListener {
        void onItemSelected(int index);
    }
}
