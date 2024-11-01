package seven.bsh.db.entity;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import seven.bsh.db.ProjectDatabase;

@Table(
    database = ProjectDatabase.class,
    name = "trade_object"
)
public class TradeObject extends BaseModel implements Serializable {
    @Column
    @PrimaryKey
    private int id;

    @Column
    private String name;

    @Column
    private String address;

    @Column(
        getterName = "getChecklistCount",
        setterName = "setChecklistCount"
    )
    private int checklist_count;

    @Column(
        getterName = "getSearchString",
        setterName = "setSearchString"
    )
    private String search_string;

    private List<Integer> projectIds;

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public String toString() {
        return name + ' ' + address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getChecklistCount() {
        return checklist_count;
    }

    public void setChecklistCount(int count) {
        checklist_count = count;
    }

    public void setSearchString(String searchString) {
        search_string = searchString;
    }

    public String getSearchString() {
        if (search_string == null || search_string.isEmpty()) {
            Locale locale = Locale.getDefault();
            search_string = getName().toLowerCase(locale) + " " +
                getAddress().toLowerCase(locale);
        }
        return search_string;
    }

    public void setProjectIds(List<Integer> projectIds) {
        this.projectIds = projectIds;
    }

    public List<Integer> getProjectIds() {
        return projectIds;
    }
}
