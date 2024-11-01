package seven.bsh.net.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class UserProfileGetResponse {
    private Data mData;

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public Data getData() {
        return mData;
    }

    @JsonProperty("data")
    public void setData(Data data) {
        mData = data;
    }

    //---------------------------------------------------------------------------
    //
    // CLASSES
    //
    //---------------------------------------------------------------------------

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private String mId;
        private Attributes mAttributes;

        public String getId() {
            return mId;
        }

        @JsonProperty("id")
        public void setId(String id) {
            mId = id;
        }

        public Attributes getAttributes() {
            return mAttributes;
        }

        public void setAttributes(Attributes attributes) {
            mAttributes = attributes;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attributes {
        private String mName;
        private String mRegisteredAt;

        public String getName() {
            return mName;
        }

        @JsonProperty("login")
        public void setName(String name) {
            mName = name;
        }

        public String getRegisteredAt() {
            return mRegisteredAt;
        }

        @JsonProperty("created_at")
        public void setRegisteredAt(String registeredAt) {
            mRegisteredAt = registeredAt;
        }
    }
}
