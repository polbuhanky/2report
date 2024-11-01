package seven.bsh.view.widget.sideMenu;

public class SideMenuItem {
    private final String mIcon;
    private final String mLabel;
    private final Id mId;
    private boolean mActive;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SideMenuItem(String icon, String label, Id id, boolean active) {
        mIcon = icon;
        mLabel = label;
        mId = id;
        mActive = active;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getIcon() {
        return mIcon;
    }

    public String getLabel() {
        return mLabel;
    }

    public Id getId() {
        return mId;
    }

    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean active) {
        mActive = active;
    }

    public enum Id {
        TRADE_OBJECTS,
        CACHE,
        QUEUE,
        REPORTS_APPROVED,
        REPORTS_REJECTED,
        REPORTS_FOR_IMPROVED,
        REPORTS_DRAFT,
        REPORTS_NEW,
        LOGOUT,
    }
}
