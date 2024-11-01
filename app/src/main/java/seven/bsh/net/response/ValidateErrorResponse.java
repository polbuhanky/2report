package seven.bsh.net.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import seven.bsh.net.deserializer.RawValueDeserializer;

@SuppressWarnings("unused")
public class ValidateErrorResponse {
    private String mErrors;

    public String getErrors() {
        return mErrors;
    }

    @JsonProperty("errors")
    @JsonDeserialize(using = RawValueDeserializer.class)
    public void setErrors(String errors) {
        mErrors = errors;
    }
}
