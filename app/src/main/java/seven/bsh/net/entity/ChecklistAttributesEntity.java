package seven.bsh.net.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import seven.bsh.net.deserializer.RawValueDeserializer;

@SuppressWarnings("unused")
public class ChecklistAttributesEntity {
    private String mName;
    private String mExpiresAt;
    private String mAvailableAt;
    private boolean mMultipleFilling;
    private boolean mGpsEnabled;
    private int mGpsEpsilon;
    private String mFields;

    public String getName() {
        return mName;
    }

    @JsonProperty("name")
    public void setName(String name) {
        mName = name;
    }

    public String getExpiresAt() {
        return mExpiresAt;
    }

    @JsonProperty("expires_at")
    public void setExpiresAt(String expiresAt) {
        mExpiresAt = expiresAt;
    }

    public String getAvailableAt() {
        return mAvailableAt;
    }

    @JsonProperty("available_at")
    public void setAvailableAt(String availableAt) {
        mAvailableAt = availableAt;
    }

    public boolean isMultipleFilling() {
        return mMultipleFilling;
    }

    @JsonProperty("multipleFilling")
    public void setMultipleFilling(int value) {
        mMultipleFilling = value == 1;
    }

    public boolean isGpsEnabled() {
        return mGpsEnabled;
    }

    @JsonProperty("gpsEnabled")
    public void setGpsEnabled(int value) {
        mGpsEnabled = value == 1;
    }

    public int getGpsEpsilon() {
        return mGpsEpsilon;
    }

    @JsonProperty("gps_epsilon")
    public void setGpsEpsilon(int gpsEpsilon) {
        if (gpsEpsilon == 0) {
            gpsEpsilon = -1;
        }
        mGpsEpsilon = gpsEpsilon;
    }

    public String getFields() {
        return mFields;
    }

    @JsonDeserialize(using = RawValueDeserializer.class)
    @JsonProperty("fields")
    public void setFields(String fields) {
        mFields = fields;
    }
}
