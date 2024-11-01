package seven.bsh.view.attributes;

import android.content.Context;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import seven.bsh.db.DatabaseHelper;
import seven.bsh.model.ChecklistAttribute;
import seven.bsh.view.attributes.settings.IAttributeSettings;
import seven.bsh.view.attributes.views.IStaticView;

public abstract class AttributeItem implements IAttribute {
    protected static final String TAG = "AttributeItem";

    private IAttributeSettings mSettings;
    private IStaticView mView;
    protected View layout;

    private final Context mContext;
    private ChecklistAttribute mModel;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public AttributeItem(Context context, IAttributeSettings settings) {
        mContext = context;
        mSettings = settings;
    }

    public AttributeItem(Context context) {
        this(context, null);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public abstract View createFieldView();

    public abstract View createValueView();

    protected String getString(int resId, Object... formatArgs) {
        return getContext().getString(resId, formatArgs);
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    protected void parseSettings(JSONObject data) throws JSONException {
        mSettings.parse(data);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public View getLayout() {
        return layout;
    }

    public IStaticView getView() {
        return mView;
    }

    public void setView(IStaticView view) {
        mView = view;
    }

    public ChecklistAttribute getModel() {
        return mModel;
    }

    public void setModel(ChecklistAttribute model) throws JSONException {
        mModel = model;
        parseSettings(model.getSettings());
    }

    public Context getContext() {
        return mContext;
    }

    public IAttributeSettings getSettings() {
        return mSettings;
    }

    public DatabaseHelper getDb() {
        return DatabaseHelper.getInstance();
    }

    //---------------------------------------------------------------------------
    //
    // CLASSES
    //
    //---------------------------------------------------------------------------

    public static class Builder {
        public static AttributeItem create(Context context, ChecklistAttribute model) {
            AttributeItem item = null;
            try {
                switch (model.getType()) {
                    case ChecklistAttribute.TYPE_SEPARATOR:
                        item = new SeparatorAttribute(context);
                        break;

                    case ChecklistAttribute.TYPE_TEXT:
                        item = new MediumTextAttribute(context);
                        break;

                    case ChecklistAttribute.TYPE_HEADER:
                        item = new BigTextAttribute(context);
                        break;

                    case ChecklistAttribute.TYPE_COMMENT:
                        item = new SmallTextAttribute(context);
                        break;

                    case ChecklistAttribute.TYPE_STRING:
                        item = new TextInputAttribute(context);
                        break;

                    case ChecklistAttribute.TYPE_NUMBER:
                        item = new NumberInputAttribute(context);
                        break;

                    case ChecklistAttribute.TYPE_CHECKBOX:
                        item = new CheckBoxAttribute(context);
                        break;

                    case ChecklistAttribute.TYPE_DATETIME:
                        item = new DateTimeInputAttribute(context);
                        break;

                    case ChecklistAttribute.TYPE_IMAGE:
                        item = new PhotoInputAttribute(context);
                        break;

                    case ChecklistAttribute.TYPE_MULTI_SELECT:
                        item = new MultiSelectAttribute(context);
                        break;

                    case ChecklistAttribute.TYPE_SELECT:
                        item = new SelectAttribute(context);
                        break;

                    case ChecklistAttribute.TYPE_FILE:
                        item = new FileInputAttribute(context);
                        break;

                    case ChecklistAttribute.TYPE_PRICE:
                        item = new PriceInputAttribute(context);
                        break;

                    case ChecklistAttribute.TYPE_SKU:
                        item = new SkuAttribute(context);
                        break;

                    case ChecklistAttribute.TYPE_WEEK:
                        item = new WeekAttribute(context);
                        break;
                }

                if (item != null) {
                    item.setModel(model);
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString(), ex);
            }
            return item;
        }
    }
}
