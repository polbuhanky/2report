package seven.bsh.db.migration;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import seven.bsh.db.ProjectDatabase;

@Migration(version = 1, database = ProjectDatabase.class)
public class M20170630151300Init extends BaseMigration {
    @Override
    public void migrate(@NonNull DatabaseWrapper database) {
        database.execSQL("CREATE TABLE `checklist` ("
            + "`id` INTEGER PRIMARY KEY,"
            + "`name` TEXT,"
            + "`expires_at` DATETIME,"
            + "`available_at DATETIME,"
            + "`multiple_filling` INTEGER,"
            + "`gps` INTEGER,"
            + "`gps_epsilon` INTEGER,"
            + "`fields` TEXT"
            + ")"
        );

        database.execSQL("CREATE TABLE `trade_object_checklist` ("
            + "`checklist_id` INTEGER,"
            + "`trade_object_id` INTEGER,"
            + "`project_id` INTEGER,"
            + "CONSTRAINT id PRIMARY KEY (`checklist_id`,`trade_object_id`,`project_id`)"
            + ")"
        );

        database.execSQL("CREATE TABLE `trade_object` ("
            + "`id` INTEGER PRIMARY KEY,"
            + "`name` TEXT,"
            + "`address` TEXT,"
            + "`search_string` TEXT,"
            + "`checklist_count` INTEGER"
            + ")"
        );

        database.execSQL("CREATE TABLE `queue_report` ("
            + "`id` INTEGER PRIMARY KEY,"
            + "`report_id` INTEGER,"
            + "`checklist_id` INTEGER,"
            + "`trade_object_id` INTEGER,"
            + "`checklist_name` TEXT,"
            + "`trade_object_name` TEXT,"
            + "`trade_object_address` TEXT,"
            + "`data` TEXT,"
            + "`updated` INTEGER,"
            + "`status` INTEGER,"
            + "`sent` INTEGER,"
            + "`errors` TEXT,"
            + "`latitude` FLOAT,"
            + "`longitude` FLOAT,"
            + "`created_at` DATETIME"
            + ")"
        );

        database.execSQL("CREATE TABLE `queue_file` ("
            + "`id` INTEGER PRIMARY KEY,"
            + "`queue_id` INTEGER,"
            + "`field_name` TEXT,"
            + "`file` TEXT"
            + ")"
        );

        database.execSQL("CREATE TABLE `report` ("
            + "`id` INTEGER PRIMARY KEY,"
            + "`checklist_id` INTEGER,"
            + "`trade_object_id` INTEGER,"
            + "`project_id` INTEGER,"
            + "`checklist_name` TEXT,"
            + "`trade_object_name` TEXT,"
            + "`trade_object_address` TEXT,"
            + "`project_name` TEXT,"
            + "`data` TEXT,"
            + "`checklist_data` TEXT,"
            + "`created_at` DATETIME,"
            + "`updated_at` DATETIME,"
            + "`status` INTEGER"
            + ")"
        );

        database.execSQL("CREATE INDEX checklist_index ON `trade_object_checklist` (`checklist_id`)");
        database.execSQL("CREATE INDEX trade_object_index ON `trade_object_checklist` (`trade_object_id`)");
        database.execSQL("CREATE INDEX project_index ON `trade_object_checklist` (`project_id`)");

        database.execSQL("CREATE INDEX queue_index ON `queue_file` (`queue_id`)");
    }
}
