package seven.bsh.net.parser;

import seven.bsh.model.Pagination;
import seven.bsh.net.entity.PaginationEntity;

public class PaginationParser {

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public Pagination parse(PaginationEntity data) {
        Pagination pagination = new Pagination();
        pagination.setCurrentPage(data.getCurrentPage());
        pagination.setPageCount(data.getPageCount());
        pagination.setPerPage(data.getPerPage());
        pagination.setTotalCount(data.getTotalCount());
        return pagination;
    }
}
