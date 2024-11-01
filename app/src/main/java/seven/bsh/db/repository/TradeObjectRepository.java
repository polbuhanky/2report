package seven.bsh.db.repository;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Where;

import java.util.List;

import seven.bsh.db.ProjectDatabase;
import seven.bsh.db.entity.TradeObject;
import seven.bsh.db.entity.TradeObject_Table;
import seven.bsh.model.Pagination;

public class TradeObjectRepository {

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    /**
     * Получение количества ТТ в БД
     */
    public synchronized int count(String q) {
        Where<TradeObject> query = new Select(Method.count())
            .from(TradeObject.class)
            .where();

        if (q != null && !q.isEmpty()) {
            query.and(TradeObject_Table.search_string.like(q));
        }
        return (int) query.count();
    }

    /**
     * Проверить, существует ли ТТ
     *
     * @param id ID ТТ
     * @return true, если такая ТТ есть в БД
     */
    public synchronized boolean exists(int id) {
        return new Select()
            .from(TradeObject.class)
            .where(TradeObject_Table.id.eq(id))
            .hasData();
    }

    /**
     * Сохранить ТТ
     *
     * @param model модель ТТ
     */
    public synchronized void save(TradeObject model) {
        model.save();
    }

    /**
     * Сохранить ТТ
     *
     * @param list список ТТ
     */
    public synchronized void saveAll(List<TradeObject> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        FlowManager.getDatabase(ProjectDatabase.class)
            .executeTransaction(databaseWrapper -> {
                for (TradeObject model : list) {
                    model.save();
                }
            });
    }

    /**
     * Получить ТТ
     *
     * @param id ID ТТ
     */
    public synchronized TradeObject get(int id) {
        return new Select()
            .from(TradeObject.class)
            .where(TradeObject_Table.id.eq(id))
            .querySingle();
    }

    /**
     * Получить список ТТ
     *
     * @param page страница пагинации
     * @param filter поисковый запрос
     */
    public synchronized List<TradeObject> getList(int page, Filter filter) {
        int offset = page * Pagination.PER_PAGE_DEFAULT - Pagination.PER_PAGE_DEFAULT;
        Where<TradeObject> query = new Select()
            .from(TradeObject.class)
            .where();

        if (filter.query != null) {
            query.and(TradeObject_Table.search_string.like(filter.query));
        }

        return query
            .limit(Pagination.PER_PAGE_DEFAULT)
            .offset(offset)
            .queryList();
    }

    /**
     * Удалить все ТТ из БД
     */
    public synchronized void deleteAll() {
        Delete.table(TradeObject.class);
    }

    //---------------------------------------------------------------------------
    //
    // ENUMS
    //
    //---------------------------------------------------------------------------

    public static class Filter {
        private String query;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }
    }
}
