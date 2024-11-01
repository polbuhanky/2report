package seven.bsh.view.checklists.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import seven.bsh.R;
import seven.bsh.db.entity.Checklist;
import seven.bsh.db.entity.TradeObject;
import seven.bsh.utils.Formatter;
import seven.bsh.view.widget.adapter.BaseAdapter;

public class ChecklistAdapter extends BaseAdapter<Checklist> {
    private final String mHexCrimsonColor;
    private final String mHexGreenColor;
    private final List<Checklist> mList;
    private final Context mContext;
    private TradeObject mTradeObject;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public ChecklistAdapter(Context context, List<Checklist> list) {
        mContext = context;
        mList = list;
        mHexCrimsonColor = Formatter.int2Hex(context, R.color.crimson);
        mHexGreenColor = Formatter.int2Hex(context, R.color.green);
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private boolean isExpired(String value) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            Date date = format.parse(value);
            Date now = new Date();
            return now.before(date);
        } catch (Exception ignored) {
            return false;
        }
    }

    private boolean hasExpiresAt(String expiresAt) {
        return expiresAt != null
            && !expiresAt.equals("0000-00-00 00:00:00");
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
            view = inflater.inflate(R.layout.item_checklists_header, parent, false);
            return new HeaderHolder(view);
        }

        view = inflater.inflate(R.layout.item_checklist, parent, false);
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
        holder.nameField.setText(mTradeObject.getName());
        holder.addressField.setText(mTradeObject.getAddress());
    }

    private void onBindItem(ViewHolder holder) {
        final int position = holder.getAdapterPosition() - 1;
        Checklist item = mList.get(position);
        holder.name.setText(item.getName());

        if (hasExpiresAt(item.getExpiresAt())) {
            String expiresAt;
            String expiresAtValue = Formatter.convertDateString(item.getExpiresAt(), "yyyy-MM-dd HH:mm:ss", "dd.MM.yyyy");
            if (isExpired(item.getExpiresAt())) {
                expiresAt = mContext.getString(R.string.activity_checklists_label_expired, expiresAtValue, mHexCrimsonColor);
            } else {
                expiresAt = mContext.getString(R.string.activity_checklists_label_not_expired, expiresAtValue, mHexGreenColor);
            }

            holder.expiresAt.setText(Html.fromHtml(expiresAt));
            holder.expiresAt.setVisibility(View.VISIBLE);
        } else {
            holder.expiresAt.setVisibility(View.GONE);
        }

        if (!item.isMultipleFilling()) {
            holder.multipleFilling.setVisibility(View.GONE);
        } else {
            holder.multipleFilling.setVisibility(View.VISIBLE);
        }

        if (!item.isGps()) {
            holder.gps.setVisibility(View.GONE);
        } else {
            holder.gps.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(position);
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
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public Checklist getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    public void setTradeObject(TradeObject tradeObject) {
        mTradeObject = tradeObject;
    }

    //---------------------------------------------------------------------------
    //
    // CLASSES
    //
    //---------------------------------------------------------------------------

    private static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView expiresAt;
        public final TextView multipleFilling;
        public final TextView gps;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            expiresAt = itemView.findViewById(R.id.expires_at);
            multipleFilling = itemView.findViewById(R.id.multiple_filling);
            gps = itemView.findViewById(R.id.gps);
        }
    }

    private static class HeaderHolder extends RecyclerView.ViewHolder {
        public final TextView addressField;
        public final TextView nameField;
        //public TextView metricField;
        //public TextView distanceField;

        public HeaderHolder(View itemView) {
            super(itemView);
            nameField = itemView.findViewById(R.id.name);
            addressField = itemView.findViewById(R.id.address);
        }
    }
}
