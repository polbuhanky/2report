package seven.bsh.db.repository;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.Arrays;
import java.util.List;

import seven.bsh.db.ProjectDatabase;
import seven.bsh.db.entity.Report;
import seven.bsh.db.entity.Report_Table;
import seven.bsh.model.Pagination;

public class ReportRepository {

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    /**
     * Получение количества отчетов в БД
     *
     * @param status статус отчета
     * @return количество отчетов в БД
     */
    public synchronized int count(int status) {
        return (int) new Select(Method.count())
            .from(Report.class)
            .where(Report_Table.status.eq(status))
            .count();
    }

    /**
     * Проверка на существование отчета в БД
     *
     * @param id ID отчета
     * @return true, если существует
     */
    public synchronized boolean exists(int id) {
        return new Select()
            .from(Report.class)
            .where(Report_Table.id.eq(id))
            .hasData();
    }

    /**
     * Сохранение отчета
     *
     * @param model Модель отчета
     */
    public synchronized void save(Report model) {
        model.save();
    }

    /**
     * Сохранение списка отчетов
     *
     * @param list Список моделей отчетов
     */
    public synchronized void saveAll(List<Report> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        FlowManager.getDatabase(ProjectDatabase.class)
            .executeTransaction(databaseWrapper -> {
                for (Report model : list) {
                    model.save();
                }
            });
    }

    /**
     * Получение списка отчетов
     *
     * @param status Статус отчетов
     * @param page Страница пагинации
     * @return Список моделей отчетов
     */
    public synchronized List<Report> getList(int status, int page) {
        int offset = page * Pagination.PER_PAGE_DEFAULT - Pagination.PER_PAGE_DEFAULT;
        return new Select()
            .from(Report.class)
            .where(Report_Table.status.eq(status))
            .limit(Pagination.PER_PAGE_DEFAULT)
            .offset(offset)
            .queryList();
    }

    /**
     * Удалить все отчеты из БД
     *
     * @param status статус отчетов
     */
    public synchronized void deleteAll(int status) {
        Delete.table(Report.class, Report_Table.status.eq(status));
    }

    /**
     * Удалить все отчеты
     */
    public synchronized void deleteAll() {
        Delete.table(Report.class);
    }

    /**
     * Удалить отчеты
     *
     * @param ids список ID отчетов
     */
    public synchronized void delete(Integer[] ids) {
        List<Integer> list = Arrays.asList(ids);
        Delete.table(Report.class, Report_Table.id.in(list));
    }

    /**
     * Получить отчет
     *
     * @param id ID отчета
     * @return модель отчета
     */
    public Report get(int id) {
        return new Select()
            .from(Report.class)
            .where(Report_Table.id.eq(id))
            .querySingle();
    }
}
