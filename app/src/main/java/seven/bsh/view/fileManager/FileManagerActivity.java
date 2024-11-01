package seven.bsh.view.fileManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import seven.bsh.Application;
import seven.bsh.LocalData;
import seven.bsh.R;
import seven.bsh.view.fileManager.adapter.FileAdapter;

public class FileManagerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private FileAdapter mAdapter;
    private List<File> mList;
    private int mNesting;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        setTitle(getString(R.string.fragment_file_manager_title));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        LocalData localData = getLocalData();
        mList = new ArrayList<>();
        mNesting = localData.getLastDirNesting();
        String dirPath = localData.getLastDirPath();
        if (dirPath == null) {
            createStartList();
        } else {
            addFileList(new File(dirPath));
        }

        mAdapter = new FileAdapter(this, mList);
        mAdapter.setNesting(mNesting);

        mListView = findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File currentFile = mList.get(position);
        if (mNesting > 0 && position == 0) {
            setPreviousDir(currentFile);
            mAdapter.notifyDataSetChanged();
            return;
        }

        if (!currentFile.canRead()) {
            new AlertDialog.Builder(this)
                .setPositiveButton(getString(R.string.dialog_button_ok), null)
                .setMessage(getString(R.string.fragment_file_manager_error_access))
                .show();
            return;
        }

        if (currentFile.isDirectory()) {
            addNesting(1);
            addFileList(currentFile);
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
        } else {
            Intent data = new Intent();
            data.putExtra("file", currentFile.getAbsolutePath());
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (mNesting > 0) {
            setPreviousDir(mList.get(0));
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
        } else {
            super.onBackPressed();
        }
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private final Comparator comparator = new Comparator() {
        public int compare(Object o1, Object o2) {
            File f1 = (File) o1;
            File f2 = (File) o2;

            if (f1.isDirectory() && !f2.isDirectory()) {
                return -1;
            } else if (!f1.isDirectory() && f2.isDirectory()) {
                return 1;
            }
            return (f1.compareTo(f2));
        }
    };

    private void addFileList(File file) {
        getLocalData().setLastDirPath(file.getAbsolutePath());
        mList.clear();
        mList.add(file.getParentFile());
        File[] fileList = file.listFiles();
        if (fileList != null) {
            Arrays.sort(fileList, comparator);
            Collections.addAll(mList, fileList);
        }
    }

    private void createStartList() {
        mList.clear();
        mList.add(new File("/"));

        File externalStorage = Environment.getExternalStorageDirectory();
        if (externalStorage.exists()) {
            mList.add(externalStorage);
        }
    }

    private void setPreviousDir(File file) {
        addNesting(-1);
        if (mNesting == 0) {
            getLocalData().clearLastDir();
            createStartList();
        } else {
            addFileList(file);
        }
    }

    private LocalData getLocalData() {
        return Application.instance().getLocalData();
    }

    private void addNesting(int value) {
        mNesting += value;
        mAdapter.setNesting(mNesting);
        getLocalData().setLastDirNesting(mNesting);
    }
}
