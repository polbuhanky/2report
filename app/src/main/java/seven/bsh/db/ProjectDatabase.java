package seven.bsh.db;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(
    name = ProjectDatabase.NAME,
    version = ProjectDatabase.VERSION
)
public class ProjectDatabase {
    public static final String NAME = "sevenreporting";
    public static final int VERSION = 5;
}
