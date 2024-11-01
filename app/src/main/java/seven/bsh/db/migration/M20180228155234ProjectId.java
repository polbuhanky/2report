package seven.bsh.db.migration;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import seven.bsh.db.ProjectDatabase;

@Migration(version = 5, database = ProjectDatabase.class)
public class M20180228155234ProjectId extends BaseMigration {
    @Override
    public void migrate(@NonNull DatabaseWrapper database) {
        database.execSQL("ALTER TABLE `queue_report` ADD COLUMN `project_id` INTEGER");
    }
}
