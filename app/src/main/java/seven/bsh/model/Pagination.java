package seven.bsh.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Pagination implements Serializable {
    public static final int PER_PAGE_DEFAULT = 20;

    private int mCurrentPage;
    private int mPageCount;
    private int mPerPage;
    private int mTotalCount;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public Pagination() {

    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public void parse(JSONObject data) throws JSONException {
        mCurrentPage = data.getInt("currentPage");
        mPageCount = data.getInt("pageCount");
        mPerPage = data.getInt("perPage");
        mTotalCount = data.getInt("totalCount");
    }

    public static Pagination create(int count, int page) {
        Pagination model = new Pagination();
        model.setTotalCount(count);
        model.setPerPage(Pagination.PER_PAGE_DEFAULT);
        model.setPageCount((int) Math.ceil((double) count / (double) model.getPerPage()));
        model.setCurrentPage(page);
        return model;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public void setPageCount(int pageCount) {
        mPageCount = pageCount;
    }

    public int getPerPage() {
        return mPerPage;
    }

    public void setPerPage(int perPage) {
        mPerPage = perPage;
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
    }

    public boolean hasNextPage() {
        return getCurrentPage() < getPageCount();
    }

    public boolean isFirstPage() {
        return getCurrentPage() == 1;
    }

    public int getNextPage() {
        return getCurrentPage() + 1;
    }
}
