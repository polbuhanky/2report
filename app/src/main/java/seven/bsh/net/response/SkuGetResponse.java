package seven.bsh.net.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import seven.bsh.db.entity.Sku;
import seven.bsh.model.Pagination;
import seven.bsh.net.entity.PaginationEntity;
import seven.bsh.net.entity.SkuEntity;
import seven.bsh.net.parser.PaginationParser;
import seven.bsh.net.parser.SkuParser;

@SuppressWarnings("unused")
public class SkuGetResponse {
    private Pagination mPagination;
    private List<Sku> mSkus;

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public Pagination getPagination() {
        return mPagination;
    }

    public List<Sku> getSkus() {
        return mSkus;
    }

    @JsonProperty("data")
    public void setData(List<SkuEntity> data) {
        SkuParser parser = new SkuParser();
        mSkus = parser.parseAll(data);
    }

    @JsonProperty("meta")
    public void setMeta(PaginationEntity meta) {
        PaginationParser parser = new PaginationParser();
        mPagination = parser.parse(meta);
    }
}
