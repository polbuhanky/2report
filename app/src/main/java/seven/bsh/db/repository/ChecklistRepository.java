package seven.bsh.db.repository;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

import seven.bsh.db.ProjectDatabase;
import seven.bsh.db.entity.Checklist;
import seven.bsh.db.entity.Checklist_Table;
import seven.bsh.db.entity.TradeObjectChecklist;
import seven.bsh.db.entity.TradeObjectChecklist_Table;
import seven.bsh.db.entity.query.ChecklistProjectJoined;
import seven.bsh.model.Pagination;
import seven.bsh.net.response.TradeObjectsGetResponse;

public class ChecklistRepository {

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    /**
     * Проверка на существование чеклиста в БД
     *
     * @param id ID чеклиста
     * @return true, если связь существует
     */
    private boolean checklistExists(int id) {
        return new Select()
            .from(Checklist.class)
            .where(Checklist_Table.id.eq(id))
            .hasData();
    }

    /**
     * Проверка на существование связи между чеклистм и ТО в БД
     *
     * @param relation Связь
     * @return true, если связь существует
     */
    private boolean relationExists(TradeObjectsGetResponse.ChecklistRelation relation) {
        return new Select()
            .from(TradeObjectChecklist.class)
            .where(TradeObjectChecklist_Table.checklist_id.eq(relation.checklistId))
            .and(TradeObjectChecklist_Table.trade_object_id.eq(relation.tradeObjectId))
            .and(TradeObjectChecklist_Table.project_id.eq(relation.projectId))
            .hasData();
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    /**
     * Получение количества чеклистов в БД
     *
     * @param tradeObjectId ID ТО
     * @return Количество чеклистов в БД
     */
    public synchronized int count(int tradeObjectId) {
        return (int) new Select(Method.count())
            .from(TradeObjectChecklist.class)
            .where(TradeObjectChecklist_Table.trade_object_id.eq(tradeObjectId))
            .count();
    }

    /**
     * Сохранение ЧЛ
     *
     * @param model модель ЧЛ
     */
    public synchronized void save(Checklist model) {
        model.save();
    }

    /**
     * Сохранение связи списка чеклистов с ТТ
     *
     * @param relations Список связей
     */
    public synchronized void saveRelations(List<TradeObjectsGetResponse.ChecklistRelation> relations) {
        if (relations == null) {
            return;
        }

        for (TradeObjectsGetResponse.ChecklistRelation relation : relations) {
            if (!relationExists(relation)) {
                TradeObjectChecklist link = new TradeObjectChecklist();
                link.setChecklistId(relation.checklistId);
                link.setTradeObjectId(relation.tradeObjectId);
                link.setProjectId(relation.projectId);
                link.save();
            }
        }
    }

    /**
     * Сохранение чеклиста
     *
     * @param list Список моделей чеклистов
     */
    public synchronized void saveAll(List<Checklist> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        FlowManager.getDatabase(ProjectDatabase.class)
            .executeTransaction(databaseWrapper -> {
                for (Checklist model : list) {
                    model.save();
                }
            });
    }

    /**
     * Удалить все чеклисты
     */
    public synchronized void deleteAll() {
        Delete.table(Checklist.class);
    }

    /**
     * Удалить все связи чеклистов с ТТ
     */
    public synchronized void deleteAllRelations() {
        Delete.table(TradeObjectChecklist.class);
    }

    /**
     * Получить список чеклистов
     *
     * @param tradeObjectId ID ТТ
     * @param page страница
     * @return список моделей чеклистов
     */
    public synchronized List<Checklist> getList(int tradeObjectId, int page) {
        int offset = page * Pagination.PER_PAGE_DEFAULT - Pagination.PER_PAGE_DEFAULT;
        List<ChecklistProjectJoined> temp = new Select()
            .from(Checklist.class)
            .innerJoin(TradeObjectChecklist.class)
            .on(TradeObjectChecklist_Table.checklist_id.eq(Checklist_Table.id))
            .where(TradeObjectChecklist_Table.trade_object_id.eq(tradeObjectId))
            .limit(Pagination.PER_PAGE_DEFAULT)
            .offset(offset)
            .queryCustomList(ChecklistProjectJoined.class);

        List<Checklist> list = new ArrayList<>();
        for (ChecklistProjectJoined tempModel : temp) {
            Checklist model = new Checklist();
            model.setId(tempModel.getId());
            model.setName(tempModel.getName());
            model.setProjectId(tempModel.getProjectId());
            model.setFields(tempModel.getFields());
            model.setAvailableAt(tempModel.getAvailableAt());
            model.setExpiresAt(tempModel.getExpiresAt());
            model.setGps(tempModel.isGps());
            model.setGpsEpsilon(tempModel.getGpsEpsilon());
            model.setMultipleFilling(tempModel.isMultipleFilling());
            list.add(model);
        }
        return list;
    }

    /**
     * Получить чеклист
     *
     * @param id ID чеклиста
     * @return модель чеклиста
     */
    public synchronized Checklist get(int id) {
        return new Select()
            .from(Checklist.class)
            .where(Checklist_Table.id.eq(id))
            .querySingle();
    }
}
