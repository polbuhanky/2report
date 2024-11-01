package seven.bsh.db.entity;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import seven.bsh.db.ProjectDatabase;

@Table(
    database = ProjectDatabase.class,
    name = "trade_object_checklist"
)
public class TradeObjectChecklist extends BaseModel {
    @PrimaryKey
    @Column(
        getterName = "getTradeObjectId",
        setterName = "setTradeObjectId"
    )
    private int trade_object_id;

    @PrimaryKey
    @Column(
        getterName = "getChecklistId",
        setterName = "setChecklistId"
    )
    private int checklist_id;

    @PrimaryKey
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

    public int getTradeObjectId() {
        return trade_object_id;
    }

    public void setTradeObjectId(int id) {
        trade_object_id = id;
    }

    public int getChecklistId() {
        return checklist_id;
    }

    public void setChecklistId(int id) {
        checklist_id = id;
    }

    public int getProjectId() {
        return project_id;
    }

    public void setProjectId(int id) {
        project_id = id;
    }
}
