package seven.bsh.db.entity;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

import seven.bsh.db.ProjectDatabase;

@Table(database = ProjectDatabase.class)
public class Project extends BaseModel {
    @Column
    @PrimaryKey
    private int id;

    @Column
    private String name;

    private List<Integer> checklistIds;

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

    public void setChecklistIds(List<Integer> checklistIds) {
        this.checklistIds = checklistIds;
    }

    public List<Integer> getChecklistIds() {
        return checklistIds;
    }
}
