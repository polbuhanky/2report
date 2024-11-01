package seven.bsh.net.entity;

@SuppressWarnings("unused")
public class SettingParamEntity {
    public String key;
    public Object value;

    public SettingParamEntity() {
        // default
    }

    public SettingParamEntity(String key, Object value) {
        this.key = key;
        this.value = value;
    }
}
