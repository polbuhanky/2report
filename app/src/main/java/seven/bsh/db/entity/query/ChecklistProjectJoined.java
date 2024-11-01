package seven.bsh.db.entity.query;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

import seven.bsh.db.ProjectDatabase;

@QueryModel(database = ProjectDatabase.class)
public class ChecklistProjectJoined {
    @Column
    private int id;

    @Column
    private String name;

    @Column(
        getterName = "isMultipleFilling",
        setterName = "setMultipleFilling"
    )
    private boolean multiple_filling;

    @Column(
        getterName = "getExpiresAt",
        setterName = "setExpiresAt"
    )
    private String expires_at;

    @Column(
        getterName = "getAvailableAt",
        setterName = "setAvailableAt"
    )
    private String available_at;

    @Column(
        getterName = "isGps",
        setterName = "setGps"
    )
    private boolean gps;

    @Column(
        getterName = "getGpsEpsilon",
        setterName = "setGpsEpsilon"
    )
    private int gps_epsilon;

    @Column
    private String fields;

    @Column(
        getterName = "getProjectId",
        setterName = "setProjectId"
    )
    private int project_id;

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

    public boolean isMultipleFilling() {
        return multiple_filling;
    }

    public void setMultipleFilling(boolean multipleFilling) {
        multiple_filling = multipleFilling;
    }

    public boolean isGps() {
        return gps;
    }

    public void setGps(boolean gps) {
        this.gps = gps;
    }

    public int getGpsEpsilon() {
        return gps_epsilon;
    }

    public void setGpsEpsilon(int gpsEpsilon) {
        gps_epsilon = gpsEpsilon;
    }

    public String getExpiresAt() {
        return expires_at;
    }

    public void setExpiresAt(String expiresAt) {
        expires_at = expiresAt;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fieldsData) {
        fields = fieldsData;
    }

    public String getAvailableAt() {
        return available_at;
    }

    public void setAvailableAt(String availableAt) {
        available_at = availableAt;
    }

    public int getProjectId() {
        return project_id;
    }

    public void setProjectId(int projectId) {
        project_id = projectId;
    }
}
