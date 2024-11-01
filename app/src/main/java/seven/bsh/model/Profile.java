package seven.bsh.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Profile implements Serializable {
    private int id;
    private String login;
    private String phone;
    private String email;
    private String registeredAt;
    private String projectId;
    private String projectName;

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public void parse(JSONObject data) throws JSONException {
        id = data.getInt("id");
        login = data.getString("login");
        registeredAt = data.getString("added_at");
        phone = data.getString("phone");
        email = data.getString("email");
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String addedAt) {
        registeredAt = addedAt;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
