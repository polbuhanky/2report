package seven.bsh.view.profile;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import seven.bsh.LocalData;
import seven.bsh.R;
import seven.bsh.utils.Formatter;
import seven.bsh.view.SideMenuActivity;

public class ProfileActivity extends SideMenuActivity {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.activity_profile_title));
        setHomeAsBackIcon();
        getSideMenu().setActive(null);

        LocalData localData = getLocalData();
        String userId = localData.getUserId();
        String userName = localData.getUserName();
        String registeredAt = localData.getUserRegisteredAt();
        String projectId = localData.getProjectId();
        String projectName = localData.getProjectName();
        registeredAt = Formatter.convertDateString(registeredAt, "yyyy-MM-dd HH:mm:ss", "dd.MM.yyyy");

        TextView idField = findViewById(R.id.id);
        idField.setText(getString(R.string.activity_profile_label_id, userId));

        TextView nameField = findViewById(R.id.name);
        nameField.setText(getString(R.string.activity_profile_label_name, userName));

        TextView registeredAtField = findViewById(R.id.registered_at);
        registeredAtField.setText(getString(R.string.activity_profile_label_registered_at, registeredAt));

        TextView projectIdField = findViewById(R.id.project_id);
        projectIdField.setText(getString(R.string.activity_profile_label_project_id, projectId));

        TextView projectNameField = findViewById(R.id.project_name);
        projectNameField.setText(getString(R.string.activity_profile_label_project_name, projectName));
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onHeaderSideMenuClick() {
        // ignored
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_profile;
    }
}
