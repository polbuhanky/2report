package seven.bsh.view.widget.sideMenu;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mikepenz.iconics.view.IconicsTextView;

import java.util.List;

import seven.bsh.R;

public class SlidingMenuAdapter extends ArrayAdapter<SideMenuItem> {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SlidingMenuAdapter(Context context, List<SideMenuItem> items) {
        super(context, 0, items);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SideMenuItem item = getItem(position);
        View view;

        if (item instanceof SeparatorMenuItem) {
            view = inflater.inflate(R.layout.item_side_menu_divider, parent, false);
        } else if (item instanceof CategoryMenuItem) {
            view = inflater.inflate(R.layout.item_side_menu_category, parent, false);

            TextView label = view.findViewById(R.id.label);
            label.setText(item.getLabel());
        } else {
            if (item instanceof BadgeSideMenuItem) {
                view = inflater.inflate(R.layout.item_side_menu_badge, parent, false);
                BadgeSideMenuItem badgeItem = (BadgeSideMenuItem) item;
                TextView badgeField = view.findViewById(R.id.badge);

                if (badgeItem.getBadge() > 0) {
                    badgeField.setText(String.valueOf(badgeItem.getBadge()));
                    GradientDrawable bg = (GradientDrawable) badgeField.getBackground();
                    bg.setColor(badgeItem.getBadgeColor());
                } else {
                    badgeField.setVisibility(View.GONE);
                }
            } else {
                view = inflater.inflate(R.layout.item_side_menu, parent, false);
            }

            if (item.isActive()) {
                int paddingTop = view.getPaddingTop();
                int paddingBottom = view.getPaddingBottom();
                int paddingLeft = view.getPaddingLeft();
                int paddingRight = view.getPaddingRight();
                view.setBackgroundResource(R.drawable.selector_list_item_sliding_menu_active);
                // hack for padding
                view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            }

            if (item.getIcon() != null) {
                IconicsTextView icon = view.findViewById(R.id.icon);
                icon.setText(item.getIcon());
            }

            if (item.getLabel() != null) {
                TextView label = view.findViewById(R.id.label);
                label.setText(item.getLabel());
            }
        }

        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        SideMenuItem item = getItem(position);
        return !(item instanceof SeparatorMenuItem) &&
            !(item instanceof CategoryMenuItem);
    }
}
