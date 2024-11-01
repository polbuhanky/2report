package seven.bsh.db.repository;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.From;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;

import java.util.ArrayList;
import java.util.List;

import seven.bsh.db.ProjectDatabase;
import seven.bsh.db.entity.Sku;
import seven.bsh.db.entity.Sku_Table;
import seven.bsh.db.entity.query.SkuBrandSelect;
import seven.bsh.db.entity.query.SkuCategorySelect;
import seven.bsh.db.entity.query.SkuSubCategorySelect;

public class SkuRepository {

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private From<Sku> find(IProperty... properties) {
        return new Select(properties).from(Sku.class);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    /**
     * Проверка на существование SKU
     *
     * @param id ID SKU
     * @return true, если такая SKU есть в БД
     */
    public synchronized boolean exists(int id) {
        return find()
            .where(Sku_Table.id.eq(id))
            .hasData();
    }

    /**
     * Сохранение SKU
     *
     * @param model модель SKU
     */
    public synchronized void save(Sku model) {
        if (model.isDeleted()) {
            delete(model.getId());
            return;
        }
        model.save();
    }

    /**
     * Сохранение SKU
     *
     * @param list список SKU
     */
    public synchronized void saveAll(List<Sku> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        FlowManager.getDatabase(ProjectDatabase.class)
            .executeTransaction(databaseWrapper -> {
                for (Sku model : list) {
                    model.save();
                }
            });
    }

    /**
     * Получение список категорий
     *
     * @return Массив названий категорий
     */
    public synchronized List<String> getBrands() {
        List<SkuBrandSelect> temp = find(Sku_Table.brand.as("name"))
            .groupBy(Sku_Table.brand)
            .queryCustomList(SkuBrandSelect.class);

        List<String> list = new ArrayList<>();
        for (SkuBrandSelect tempModel : temp) {
            list.add(tempModel.getName());
        }
        return list;
    }

    /**
     * Получение список категорий
     *
     * @param brand Родительский бренд
     * @return Массив названий подкатегорий
     */
    public synchronized List<String> getCategories(String brand) {
        List<SkuCategorySelect> temp = find(Sku_Table.category.as("name"))
            .where(Sku_Table.brand.eq(brand))
            .groupBy(Sku_Table.category)
            .queryCustomList(SkuCategorySelect.class);

        List<String> list = new ArrayList<>();
        for (SkuCategorySelect tempModel : temp) {
            list.add(tempModel.getName());
        }
        return list;
    }

    /**
     * Получение список подкатегорий
     *
     * @param brand Родительский бренд
     * @param category Родительская категория
     * @return Массив названий брендов
     */
    public synchronized List<String> getSubCategories(String brand, String category) {
        List<SkuSubCategorySelect> temp = find(Sku_Table.sub_category.as("name"))
            .where(Sku_Table.brand.eq(brand))
            .and(Sku_Table.category.eq(category))
            .groupBy(Sku_Table.sub_category)
            .queryCustomList(SkuSubCategorySelect.class);

        List<String> list = new ArrayList<>();
        for (SkuSubCategorySelect tempModel : temp) {
            list.add(tempModel.getName());
        }
        return list;
    }

    /**
     * Получение список SKU
     *
     * @param category Категория
     * @param subCategory Подкатегория
     * @return Массив моделей SKU
     */
    public synchronized List<Sku> getAll(String brand, String category, String subCategory) {
        return find()
            .where(Sku_Table.brand.eq(brand))
            .and(Sku_Table.category.eq(category))
            .and(Sku_Table.sub_category.eq(subCategory))
            .queryList();
    }

    /**
     * Получение модели SKU
     *
     * @param id Идентификатор SKU
     * @return модель SKU
     */
    public synchronized Sku get(int id) {
        return find()
            .where(Sku_Table.id.eq(id))
            .querySingle();
    }

    /**
     * Удаление все SKU из БД
     */
    public synchronized void deleteAll() {
        Delete.table(Sku.class);
    }

    /**
     * Удаление SKU из БД
     */
    public synchronized void delete(int id) {
        Delete.table(Sku.class, Sku_Table.id.eq(id));
    }
}
