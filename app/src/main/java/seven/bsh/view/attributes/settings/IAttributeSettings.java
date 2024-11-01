package seven.bsh.view.attributes.settings;

import org.json.JSONException;
import org.json.JSONObject;

public interface IAttributeSettings {
    void parse(JSONObject settings) throws JSONException;
}
