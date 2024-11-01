package seven.bsh.db;

import seven.bsh.db.repository.ChecklistRepository;
import seven.bsh.db.repository.ProjectRepository;
import seven.bsh.db.repository.QueueRepository;
import seven.bsh.db.repository.ReportRepository;
import seven.bsh.db.repository.SkuRepository;
import seven.bsh.db.repository.TradeObjectRepository;

public class DatabaseHelper {
    private static DatabaseHelper sInstance;

    private final TradeObjectRepository mTradeObjectRepository;
    private final ChecklistRepository mChecklistRepository;
    private final QueueRepository mQueueRepository;
    private final ReportRepository mReportRepository;
    private final ProjectRepository mProjectRepository;
    private final SkuRepository mSkuRepository;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public DatabaseHelper() {
        mTradeObjectRepository = new TradeObjectRepository();
        mChecklistRepository = new ChecklistRepository();
        mReportRepository = new ReportRepository();
        mQueueRepository = new QueueRepository();
        mProjectRepository = new ProjectRepository();
        mSkuRepository = new SkuRepository();
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public TradeObjectRepository getTradeObjectRepository() {
        return mTradeObjectRepository;
    }

    public ReportRepository getReportRepository() {
        return mReportRepository;
    }

    public ChecklistRepository getChecklistRepository() {
        return mChecklistRepository;
    }

    public QueueRepository getQueueRepository() {
        return mQueueRepository;
    }

    public ProjectRepository getProjectRepository() {
        return mProjectRepository;
    }

    public SkuRepository getSkuRepository() {
        return mSkuRepository;
    }

    public static DatabaseHelper getInstance() {
        if (sInstance == null) {
            sInstance = new DatabaseHelper();
        }
        return sInstance;
    }
}
