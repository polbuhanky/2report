package seven.bsh.db.entity;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import seven.bsh.db.ProjectDatabase;

@Table(database = ProjectDatabase.class)
public class Comment extends BaseModel {
    @Column
    @PrimaryKey
    private int id;

    @Column
    private String text;

    @Column(
        getterName = "getCreatedAt",
        setterName = "setCreatedAt"
    )
    private String created_at;

    @Column
    private String name;

    @Column(
        getterName = "getUserId",
        setterName = "setUserId"
    )
    private int user_id;

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String createdAt) {
        created_at = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return user_id;
    }

    public int getId() {
        return id;
    }

    public void setUserId(int userId) {
        user_id = userId;
    }

    public void setId(int id) {
        this.id = id;
    }
}
