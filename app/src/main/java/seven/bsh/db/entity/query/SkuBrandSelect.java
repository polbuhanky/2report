package seven.bsh.db.entity.query;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

import seven.bsh.db.ProjectDatabase;

@QueryModel(database = ProjectDatabase.class)
public class SkuBrandSelect {
    @Column
    private String name;

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
