package seven.bsh.db.repository;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

import seven.bsh.db.ProjectDatabase;
import seven.bsh.db.entity.Project;
import seven.bsh.db.entity.Project_Table;

public class ProjectRepository {

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    /**
     * Проверить, существует ли ТТ
     *
     * @param id ID ТТ
     * @return true, если такая ТТ есть в БД
     */
    public synchronized boolean exists(int id) {
        return new Select()
            .from(Project.class)
            .where(Project_Table.id.eq(id))
            .hasData();
    }

    /**
     * Сохранить ТТ
     *
     * @param model модель ТТ
     */
    public synchronized void save(Project model) {
        model.save();
    }

    /**
     * Сохранить ТТ
     *
     * @param list список ТТ
     */
    public synchronized void saveAll(List<Project> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        FlowManager.getDatabase(ProjectDatabase.class)
            .executeTransaction(databaseWrapper -> {
                for (Project model : list) {
                    model.save();
                }
            });
    }

    /**
     * Получить ТТ
     *
     * @param id ID ТТ
     */
    public synchronized Project get(int id) {
        return new Select()
            .from(Project.class)
            .where(Project_Table.id.eq(id))
            .querySingle();
    }

    /**
     * Получить список ТТ
     *
     * @return список моделей ТТ
     */
    public synchronized List<Project> getList() {
        return new Select()
            .from(Project.class)
            .queryList();
    }

    /**
     * Удалить все проекты из БД
     */
    public synchronized void deleteAll() {
        Delete.tables(Project.class);
    }
}
