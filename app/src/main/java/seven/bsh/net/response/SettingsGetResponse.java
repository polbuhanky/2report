package seven.bsh.net.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seven.bsh.net.entity.SettingParamEntity;

@SuppressWarnings("unused")
public class SettingsGetResponse {
    private Map<String, Object> mParams;

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public int getMaxImageSize() {
        return Integer.valueOf((String) mParams.get("max_image_size"));
    }

    @JsonProperty("data")
    public void setParams(List<SettingParamEntity> data) {
        mParams = new HashMap<>();
        for (SettingParamEntity param : data) {
            mParams.put(param.key, param.value);
        }
    }
}
