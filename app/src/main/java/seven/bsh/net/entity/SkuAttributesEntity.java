package seven.bsh.net.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class SkuAttributesEntity {
    private int mFlag;
    private String mBrand;
    private String mCategory;
    private String mSubCategory;
    private String mName;

    public int getFlag() {
        return mFlag;
    }

    @JsonProperty("flag")
    public void setFlag(int flag) {
        mFlag = flag;
    }

    public String getBrand() {
        return mBrand;
    }

    @JsonProperty("brand")
    public void setBrand(String brand) {
        mBrand = brand;
    }

    public String getCategory() {
        return mCategory;
    }

    @JsonProperty("category")
    public void setCategory(String category) {
        mCategory = category;
    }

    public String getSubCategory() {
        return mSubCategory;
    }

    @JsonProperty("sub_category")
    public void setSubCategory(String subCategory) {
        mSubCategory = subCategory;
    }

    public String getName() {
        return mName;
    }

    @JsonProperty("sku")
    public void setName(String name) {
        mName = name;
    }
}
