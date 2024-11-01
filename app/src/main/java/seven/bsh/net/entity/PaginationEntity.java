package seven.bsh.net.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class PaginationEntity {
    private int mTotalCount;
    private int mPageCount;
    private int mCurrentPage;
    private int mPerPage;

    public int getTotalCount() {
        return mTotalCount;
    }

    @JsonProperty("totalCount")
    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
    }

    public int getPageCount() {
        return mPageCount;
    }

    @JsonProperty("pageCount")
    public void setPageCount(int pageCount) {
        mPageCount = pageCount;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    @JsonProperty("currentPage")
    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    public int getPerPage() {
        return mPerPage;
    }

    @JsonProperty("perPage")
    public void setPerPage(int perPage) {
        mPerPage = perPage;
    }
}
