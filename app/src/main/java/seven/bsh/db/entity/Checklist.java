package seven.bsh.db.entity;

import android.util.Log;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import seven.bsh.db.ProjectDatabase;
import seven.bsh.model.ChecklistAttribute;

@Table(database = ProjectDatabase.class)
public class Checklist extends BaseModel implements Serializable {
    private static final String TAG = "ChecklistModel";

    private final List<ChecklistAttribute> attributeList;

    @Column
    @PrimaryKey
    private int id;

    @Column
    private String name;

    @Column(
        name = "multiple_filling",
        getterName = "isMultipleFilling",
        setterName = "setMultipleFilling"
    )
    private boolean mMultipleFilling;

    @Column(
        name = "expires_at",
        getterName = "getExpiresAt",
        setterName = "setExpiresAt"
    )
    private String mExpiresAt;

    @Column(
        name = "available_at",
        getterName = "getAvailableAt",
        setterName = "setAvailableAt"
    )
    private String mAvailableAt;

    @Column(
        getterName = "isGps",
        setterName = "setGps"
    )
    private boolean gps;

    @Column(
        name = "gps_epsilon",
        getterName = "getGpsEpsilon",
        setterName = "setGpsEpsilon"
    )
    private int mGpsEpsilon;

    @Column
    private String fields;

    private int projectId;
    private JSONObject json;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public Checklist() {
        attributeList = new ArrayList<>();
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChecklistAttribute> getAttributeList() {
        if (attributeList.isEmpty() && fields != null && !fields.isEmpty()) {
            try {
                JSONArray data = new JSONArray(fields);
                int length = data.length();
                for (int i = 0; i < length; i++) {
                    ChecklistAttribute attribute = new ChecklistAttribute();
                    attribute.parse((JSONObject) data.get(i));
                    attributeList.add(attribute);
                }
            } catch (JSONException ex) {
                Log.e(TAG, ex.toString(), ex);
            }
        }
        return attributeList;
    }

    public boolean isMultipleFilling() {
        return mMultipleFilling;
    }

    public void setMultipleFilling(boolean multipleFilling) {
        mMultipleFilling = multipleFilling;
    }

    public boolean isGps() {
        return gps;
    }

    public void setGps(boolean gps) {
        this.gps = gps;
    }

    public int getGpsEpsilon() {
        return mGpsEpsilon;
    }

    public void setGpsEpsilon(int gpsEpsilon) {
        mGpsEpsilon = gpsEpsilon;
    }

    public String getExpiresAt() {
        return mExpiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        mExpiresAt = expiresAt;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fieldsData) {
        fields = fieldsData;
    }

    public String getAvailableAt() {
        return mAvailableAt;
    }

    public void setAvailableAt(String availableAt) {
        mAvailableAt = availableAt;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public JSONObject getJson() {
        return json;
    }
}
