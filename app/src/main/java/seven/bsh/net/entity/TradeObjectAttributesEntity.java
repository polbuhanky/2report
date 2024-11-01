package seven.bsh.net.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class TradeObjectAttributesEntity {
    private String mName;
    private String mAddress;

    public String getName() {
        return mName;
    }

    @JsonProperty("name")
    public void setName(String name) {
        mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        mAddress = address;
    }
}
