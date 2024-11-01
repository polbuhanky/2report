package seven.bsh.view.reports.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import seven.bsh.R;
import seven.bsh.db.entity.Report;
import seven.bsh.utils.Formatter;
import seven.bsh.view.widget.adapter.BaseAdapter;

public class ReportAdapter extends BaseAdapter<Report> {
    private List<Report> mList;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public ReportAdapter(List<Report> list) {
        mList = list;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        Report item = getItem(position);
        viewHolder.checklistName.setText(item.getChecklist().getName());
        viewHolder.tradeObjectName.setText(item.getTradeObjectName() + ", " + item.getTradeObjectAddress());
        viewHolder.date.setText(Formatter.convertDateString(item.getCreatedAt(), "yyyy-MM-dd HH:mm:ss", "dd.MM.yyyy HH:mm:ss"));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(viewHolder.getAdapterPosition());
                }
            }
        });
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public Report getItem(int position) {
        return mList.get(position);
    }

    //---------------------------------------------------------------------------
    //
    // CLASSES
    //
    //---------------------------------------------------------------------------

    private static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView checklistName;
        public final TextView tradeObjectName;
        public final TextView date;
        public TextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            checklistName = itemView.findViewById(R.id.label_checklist_name);
            tradeObjectName = itemView.findViewById(R.id.label_trade_object);
            date = itemView.findViewById(R.id.label_date);
        }
    }
}
