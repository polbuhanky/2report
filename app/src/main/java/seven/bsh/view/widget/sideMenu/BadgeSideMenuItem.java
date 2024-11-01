package seven.bsh.view.widget.sideMenu;

public class BadgeSideMenuItem extends SideMenuItem {
    private final int mBadgeColor;
    private int mBadge;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public BadgeSideMenuItem(String icon, String label, int badge, int badgeColor, Id id, boolean active) {
        super(icon, label, id, active);
        mBadgeColor = badgeColor;
        mBadge = badge;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public int getBadge() {
        return mBadge;
    }

    public void setBadge(int badge) {
        mBadge = badge;
    }

    public int getBadgeColor() {
        return mBadgeColor;
    }
}
