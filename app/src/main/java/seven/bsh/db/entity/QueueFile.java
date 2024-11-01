package seven.bsh.db.entity;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.File;

import seven.bsh.db.ProjectDatabase;

@Table(
    database = ProjectDatabase.class,
    name = "queue_file"
)
public class QueueFile extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    private int id;

    @Column(
        getterName = "getQueueId",
        setterName = "setQueueId"
    )
    private int queue_id;

    @Column(
        getterName = "getFieldName",
        setterName = "setFieldName"
    )
    private String field_name;

    @Column
    private String path;

    private File file;

    //---------------------------------------------------------------------------
    //
    // PUBLIC ACCESSORS
    //
    //---------------------------------------------------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQueueId() {
        return queue_id;
    }

    public void setQueueId(int queueId) {
        queue_id = queueId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File getFile() {
        if (file == null) {
            file = new File(path);
        }
        return file;
    }

    public String getFieldName() {
        return field_name;
    }

    public void setFieldName(String fieldName) {
        field_name = fieldName;
    }
}
