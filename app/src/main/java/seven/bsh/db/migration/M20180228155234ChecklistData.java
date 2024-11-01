package seven.bsh.db.migration;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import seven.bsh.db.ProjectDatabase;

@Migration(version = 3, database = ProjectDatabase.class)
public class M20180228155234ChecklistData extends BaseMigration {
    @Override
    public void migrate(@NonNull DatabaseWrapper database) {
        database.execSQL("DROP TABLE `report`");
        database.execSQL("CREATE TABLE `report` ("
            + "`id` INTEGER PRIMARY KEY,"
            + "`checklist_id` INTEGER,"
            + "`trade_object_id` INTEGER,"
            + "`project_id` INTEGER,"
            + "`trade_object_name` TEXT,"
            + "`trade_object_address` TEXT,"
            + "`project_name` TEXT,"
            + "`data` TEXT,"
            + "`created_at` DATETIME,"
            + "`updated_at` DATETIME,"
            + "`status` INTEGER"
            + ")"
        );
    }
}
