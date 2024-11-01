package seven.bsh.db.migration;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import seven.bsh.db.ProjectDatabase;

@Migration(version = 2, database = ProjectDatabase.class)
public class M20170729101900Sku extends BaseMigration {
    @Override
    public void migrate(@NonNull DatabaseWrapper database) {
        database.execSQL("CREATE TABLE `sku` (" +
            "`id` INTEGER PRIMARY KEY," +
            "`brand` TEXT," +
            "`category` TEXT," +
            "`sub_category` TEXT," +
            "`sku` TEXT," +
            "`flag` INTEGER" +
            ")"
        );

        database.execSQL("CREATE INDEX sku_idx ON `sku` (`brand`, `category`, `sub_category`)");
    }
}
