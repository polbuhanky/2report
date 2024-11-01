package seven.bsh.view.queue.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import seven.bsh.R;
import seven.bsh.db.entity.QueueReport;
import seven.bsh.view.widget.adapter.BaseAdapter;

public class QueueAdapter extends BaseAdapter<QueueReport> {
    private final String mHexErrorColor;
    private final List<QueueReport> mList;
    private final List<QueueReport> mSelectedItems;
    private boolean mChoiceMode;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public QueueAdapter(Context context, List<QueueReport> list) {
        mList = list;
        mSelectedItems = new ArrayList<>();
        int color = ContextCompat.getColor(context, R.color.list_item_status_error);
        mHexErrorColor = Integer.toString(color & 0xFFFFFF, 16);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_queue, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        QueueReport item = mList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        Context context = viewHolder.context;
        viewHolder.checklistName.setText(item.getChecklistName());
        viewHolder.tradeObjectName.setText(item.getTradeObjectName() + ", " + item.getTradeObjectAddress());
        viewHolder.date.setText(item.getFormattedAddedAt());

        if (mChoiceMode) {
            viewHolder.checkbox.setVisibility(View.VISIBLE);
            viewHolder.checkbox.setChecked(item.isChecked());
        } else {
            viewHolder.checkbox.setVisibility(View.GONE);
        }

        String statusText = getStatus(context, item.getStatus(), item.getErrors());
        if (statusText.isEmpty()) {
            viewHolder.status.setVisibility(View.GONE);
        } else {
            viewHolder.status.setText(Html.fromHtml(statusText));
            viewHolder.status.setVisibility(View.VISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChoiceMode) {
                    item.setChecked(!item.isChecked());
                    updateCheckedState(viewHolder, item);
                    actionListener.onActionItem(viewHolder.getAdapterPosition());
                } else {
                    listener.onItemClick(viewHolder.getAdapterPosition());
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!mChoiceMode && actionListener != null) {
                    mChoiceMode = true;
                    actionListener.onActionItem(viewHolder.getAdapterPosition());
                    viewHolder.itemView.performClick();
                }
                return true;
            }
        });
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private String getStatus(Context context, int status, String errors) {
        if (errors != null && !errors.isEmpty()) {
            try {
                JSONArray errorsData = new JSONArray(errors);
                int length = errorsData.length();
                List<String> errorText = new ArrayList<>();

                for (int i = 0; i < length; i++) {
                    JSONObject errorData = errorsData.getJSONObject(i);
                    errorText.add(errorData.getString("message"));
                }
                errors = TextUtils.join("<br>", errorText);
            } catch (JSONException e) {
                // ignored
            }
        }

        switch (status) {
            case QueueReport.STATUS_QUEUE:
                return context.getString(R.string.activity_queue_label_status_queue);

            case QueueReport.STATUS_PENDING:
                return context.getString(R.string.activity_queue_label_status_pending);

            case QueueReport.STATUS_SENDING_DATA:
                return context.getString(R.string.activity_queue_label_status_sendingData);

            case QueueReport.STATUS_SENDING_FILES:
                return context.getString(R.string.activity_queue_label_status_sendingFiles);

            case QueueReport.STATUS_SENDING_FILLED:
                return context.getString(R.string.activity_queue_label_status_sendingFilled);

            case QueueReport.STATUS_ERROR_FILES:
                return context.getString(R.string.activity_queue_label_status_error_files, mHexErrorColor, errors);

            case QueueReport.STATUS_ERROR_DATA:
                return context.getString(R.string.activity_queue_label_status_error_data, mHexErrorColor, errors);

            case QueueReport.STATUS_ERROR_STATUS:
                return context.getString(R.string.activity_queue_label_status_error_status, mHexErrorColor, errors);
        }
        return "";
    }

    private void updateCheckedState(ViewHolder viewHolder, QueueReport item) {
        boolean checked = item.isChecked();
        if (checked) {
            mSelectedItems.add(item);
        } else {
            mSelectedItems.remove(item);
        }
        viewHolder.checkbox.setChecked(checked);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    public QueueReport getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public List<QueueReport> getSelectedItems() {
        return mSelectedItems;
    }

    public void setChoiceMode(boolean choiceMode) {
        mChoiceMode = choiceMode;
        if (!choiceMode) {
            mSelectedItems.clear();
        }
        notifyDataSetChanged();
    }

    //---------------------------------------------------------------------------
    //
    // CLASSES
    //
    //---------------------------------------------------------------------------

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public final Context context;
        public final RelativeLayout layout;
        public final TextView checklistName;
        public final TextView tradeObjectName;
        public final TextView date;
        public final TextView status;
        public final CheckBox checkbox;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            layout = itemView.findViewById(R.id.layout);
            checklistName = itemView.findViewById(R.id.label_checklist_name);
            tradeObjectName = itemView.findViewById(R.id.label_trade_object);
            date = itemView.findViewById(R.id.label_date);
            status = itemView.findViewById(R.id.status);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }
}
