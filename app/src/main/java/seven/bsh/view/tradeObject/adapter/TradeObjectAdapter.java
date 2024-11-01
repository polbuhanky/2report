package seven.bsh.view.tradeObject.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import seven.bsh.R;
import seven.bsh.db.entity.TradeObject;
import seven.bsh.view.widget.adapter.BaseAdapter;

public class TradeObjectAdapter extends BaseAdapter<TradeObject> {
    private final List<TradeObject> mList;
    private HeaderHolder mHeaderHolder;
    private int mTotalCount;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public TradeObjectAdapter(List<TradeObject> list) {
        mList = list;
        mTotalCount = list.size();
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == TYPE_HEADER) {
            view = inflater.inflate(R.layout.item_trade_objects_header, parent, false);
            return new HeaderHolder(view);
        }

        view = inflater.inflate(R.layout.item_trade_object, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
            onBindHeader((HeaderHolder) holder);
        } else {
            onBindItem((ViewHolder) holder);
        }
    }

    private void onBindHeader(HeaderHolder holder) {
        mHeaderHolder = holder;
        updateTradeObjectCount(holder, mTotalCount);
    }

    private void onBindItem(ViewHolder holder) {
        final int position = holder.getAdapterPosition() - 1;
        TradeObject item = mList.get(position);
        Context context = holder.context;
        String postfix = context.getResources().getQuantityString(R.plurals.checklists, item.getChecklistCount());

        holder.name.setText(item.getName());
        holder.address.setText(item.getAddress());
        //holder.distance.setText(String.valueOf(item.getDistance()));
        //holder.distanceMetric.setText(Formatter.getDistanceMetric(context, item.getDistance()));
        holder.checklists.setText(String.valueOf(item.getChecklistCount()));
        holder.checklistsPostfix.setText(postfix);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
    }

    private void updateTradeObjectCount(HeaderHolder holder, int count) {
        Context context = holder.context;
        String postfix = context.getResources().getQuantityString(R.plurals.tradeObjects, count);
        holder.count.setText(String.valueOf(count));
        holder.prefix.setText(postfix);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public TradeObject getItem(int position) {
        return mList.get(position);
    }

    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
        if (mHeaderHolder != null) {
            updateTradeObjectCount(mHeaderHolder, totalCount);
        }
    }

    //---------------------------------------------------------------------------
    //
    // CLASSES
    //
    //---------------------------------------------------------------------------

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final Context context;
        public final TextView name;
        public final TextView address;
        //public final TextView distance;
        //public final TextView distanceMetric;
        public final TextView checklists;
        public final TextView checklistsPostfix;

        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
            //distance = view.findViewById(R.id.distance);
            //distanceMetric = view.findViewById(R.id.distance_metric);
            checklists = view.findViewById(R.id.checklists);
            checklistsPostfix = view.findViewById(R.id.checklist_postfix);
        }
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {
        public final Context context;
        public final TextView count;
        public final TextView prefix;

        public HeaderHolder(View view) {
            super(view);
            context = view.getContext();
            count = view.findViewById(R.id.text_count);
            prefix = view.findViewById(R.id.text_prefix);
        }
    }
}
