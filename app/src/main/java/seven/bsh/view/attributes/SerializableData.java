package seven.bsh.view.attributes;

public class SerializableData {
    private final String key;
    private final Object value;

    public SerializableData(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
