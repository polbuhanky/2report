package seven.bsh.view.queue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import java.util.ArrayList;
import java.util.List;

import seven.bsh.R;
import seven.bsh.db.DatabaseHelper;
import seven.bsh.db.entity.QueueReport;
import seven.bsh.net.service.QueueService;
import seven.bsh.view.queue.adapter.QueueAdapter;
import seven.bsh.view.report.view.QueueReportActivity;
import seven.bsh.view.widget.adapter.BaseAdapter;
import seven.bsh.view.widget.sideMenu.SideMenuItem;

public class QueueActivity extends BaseQueueActivity {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        initBaseQueueActivity();
        setTitle(R.string.activity_queue_title);
        getSideMenu().setActive(SideMenuItem.Id.QUEUE);

        list = new ArrayList<>();
        adapter = new QueueAdapter(this, list);
        adapter.setActionListener(this);
        listView.setAdapter(adapter);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(QueueService.BROADCAST_ID);
        registerReceiver(receiver, intentFilter);

        List<QueueReport> temp = DatabaseHelper.getInstance()
            .getQueueRepository()
            .getList();

        list.clear();
        list.addAll(temp);
        adapter.notifyDataSetChanged();
        invalidateOptionsMenu();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_queue, menu);
        setMenuItemIcon(menu, R.id.action_delete, GoogleMaterial.Icon.gmd_delete);
        setMenuItemIcon(menu, R.id.action_send, GoogleMaterial.Icon.gmd_send);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean enabled = !list.isEmpty();
        MenuItem deleteItem = menu.findItem(R.id.action_delete);
        MenuItem sendItem = menu.findItem(R.id.action_send);
        deleteItem.setEnabled(enabled);
        sendItem.setEnabled(enabled);

        if (enabled) {
            deleteItem.getIcon().setAlpha(255);
            sendItem.getIcon().setAlpha(255);
        } else {
            deleteItem.getIcon().setAlpha(100);
            sendItem.getIcon().setAlpha(100);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                onDeleteAction();
                return true;

            case R.id.action_send:
                onSendAction();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSendAction() {
        if (list.isEmpty()) {
            return;
        }

        showSimpleDialog(
            R.string.activity_queue_popup_send_all,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(QueueActivity.this, QueueService.class);
                    intent.putExtra(QueueService.KEY_COMMAND, QueueService.COMMAND_ADD);
                    startService(intent);
                }
            }
        );
    }

    private void onDeleteAction() {
        showSimpleDialog(
            R.string.activity_queue_popup_delete_all,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DatabaseHelper.getInstance()
                        .getQueueRepository()
                        .deleteAll();

                    list.clear();
                    adapter.notifyDataSetChanged();
                    invalidateOptionsMenu();
                }
            }
        );
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int result = intent.getIntExtra(QueueService.KEY_RESULT, -1);
            switch (result) {
                case QueueService.RESULT_QUEUE_UPDATED:
                    onResultQueueUpdate(intent);
                    break;

                case QueueService.RESULT_STATUS:
                    onResultStatus(intent);
                    break;
            }
        }

        private void onResultStatus(Intent intent) {
            int id = intent.getIntExtra(QueueService.KEY_MODEL_ID, QueueService.NO_MODEL_ID);
            int status = intent.getIntExtra(QueueService.KEY_MODEL_STATUS, -1);
            String errors = intent.getStringExtra(QueueService.KEY_MODEL_ERRORS);
            for (QueueReport model : list) {
                if (model.getId() != id) {
                    continue;
                }

                model.setStatus(status);
                model.setErrors(errors);
                adapter.notifyDataSetChanged();
                break;
            }
        }

        private void onResultQueueUpdate(Intent intent) {
            int id = intent.getIntExtra(QueueService.KEY_MODEL_ID, QueueService.NO_MODEL_ID);
            if (id == QueueService.NO_MODEL_ID) {
                List<QueueReport> temp = DatabaseHelper.getInstance()
                    .getQueueRepository()
                    .getList();

                list.clear();
                list.addAll(temp);
            } else {
                for (QueueReport model : list) {
                    if (model.getId() == id) {
                        list.remove(model);
                        break;
                    }
                }
            }
            adapter.notifyDataSetChanged();
            invalidateOptionsMenu();
        }
    };

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_action_reports_queue, menu);
        setMenuItemIcon(menu, R.id.action_delete, GoogleMaterial.Icon.gmd_delete);
        setMenuItemIcon(menu, R.id.action_send, GoogleMaterial.Icon.gmd_send);
        return super.onCreateActionMode(mode, menu);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                onDeleteSelectedAction();
                break;

            case R.id.action_send:
                onSendSelectedAction();
                break;
        }
        return false;
    }

    private void onSendSelectedAction() {
        List<QueueReport> selectedItems = adapter.getSelectedItems();
        if (selectedItems.isEmpty()) {
            return;
        }

        showSimpleDialog(
            R.string.activity_queue_popup_send,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int[] ids = new int[selectedItems.size()];
                    int index = 0;
                    for (QueueReport model : selectedItems) {
                        ids[index] = model.getId();
                        index++;
                    }

                    Intent intent = new Intent(QueueActivity.this, QueueService.class);
                    intent.putExtra(QueueService.KEY_COMMAND, QueueService.COMMAND_ADD);
                    intent.putExtra(QueueService.KEY_LIST, ids);
                    startService(intent);
                    mActionMode.finish();
                }
            }
        );
    }

    private void onDeleteSelectedAction() {
        List<QueueReport> selectedItems = adapter.getSelectedItems();
        if (selectedItems.isEmpty()) {
            return;
        }

        showSimpleDialog(
            R.string.activity_queue_popup_delete,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<Integer> ids = new ArrayList<>();
                    for (QueueReport model : selectedItems) {
                        ids.add(model.getId());
                        list.remove(model);
                    }

                    DatabaseHelper.getInstance()
                        .getQueueRepository()
                        .deleteAll(ids);
                    mActionMode.finish();
                }
            }
        );
    }

    @Override
    public void onItemClick(BaseAdapter adapter, int position) {
        if (position < 0) {
            return;
        }

        QueueReport model = (QueueReport) adapter.getItem(position);
        Intent intent = new Intent(this, QueueReportActivity.class);
        intent.putExtra(QueueReportActivity.ARGUMENT_QUEUE_ID, model.getId());
        startActivity(intent);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_queue;
    }
}
