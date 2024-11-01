package seven.bsh.db.entity;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import seven.bsh.db.ProjectDatabase;

@Table(database = ProjectDatabase.class)
public class Sku extends BaseModel {
    @Column
    @PrimaryKey
    private int id;

    @Column
    private String brand;

    @Column
    private String category;

    @Column(
        getterName = "getSubCategory",
        setterName = "setSubCategory"
    )
    private String sub_category;

    @Column
    private String sku;

    @Column
    private int flag;

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return sub_category;
    }

    public void setSubCategory(String subCategory) {
        sub_category = subCategory;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public boolean isDeleted() {
        return (flag & 1) == 1;
    }
}
