package seven.bsh.views.attributes;

import android.content.Context;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import seven.bsh.view.attributes.settings.NumberInputSettings;
import seven.bsh.view.attributes.values.NumberInputValue;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(DataProviderRunner.class)
public class NumberInputAttributeUnitTest {
    @Test
    @UseDataProvider("providerCorrectSettings")
    public void parseCorrectSettings(String jsonText) throws JSONException {
        NumberInputSettings settings = new NumberInputSettings();
        JSONObject json = new JSONObject(jsonText);
        settings.parse(json);

        assertEquals(settings.getName(), json.getString("name"));
        assertEquals(settings.getLabel(), json.getString("label"));
        assertEquals(settings.isRequired(), json.optInt("required") == 1);
        assertEquals(settings.isFloat(), json.optInt("float") == 1);
    }

    @Test(expected = JSONException.class)
    @UseDataProvider("providerIncorrectSettings")
    public void parseIncorrectSettings(String jsonText) throws JSONException {
        NumberInputSettings settings = new NumberInputSettings();
        settings.parse(new JSONObject(jsonText));
    }

    @Test
    @UseDataProvider("providerCorrectData")
    public void parseCorrectData(Object data, Object expected) throws JSONException {
        String settingsData = getSettingsData(false, true);
        NumberInputValue attribute = getValue(settingsData);
        attribute.parse(data);
        assertEquals(expected, attribute.getValue());
    }

    @Test(expected = ClassCastException.class)
    @UseDataProvider("providerIncorrectData")
    public void parseIncorrectData(Object data) throws JSONException {
        String settingsData = getSettingsData(false, true);
        NumberInputValue attribute = getValue(settingsData);
        attribute.parse(data);
        assertNotSame(data, attribute.getValue());
    }

    @Test
    @UseDataProvider("providerValidateCorrectData")
    public void validateCorrectData(String settingsData, Object data) throws JSONException {
        NumberInputValue attribute = getValue(settingsData);
        attribute.parse(data);
        assertTrue(attribute.validate());
    }

    @Test
    @UseDataProvider("providerValidateIncorrectData")
    public void validateIncorrectData(String settingsData, Object data) throws JSONException {
        NumberInputValue attribute = getValue(settingsData);
        attribute.parse(data);
        assertFalse(attribute.validate());
    }

    @Test
    @UseDataProvider("providerSerializeData")
    public void serializeData(String settingsData, Object data, Object expected) throws JSONException {
        NumberInputValue attribute = getValue(settingsData);
        attribute.parse(data);
        assertTrue(attribute.validate());
        assertEquals(expected, attribute.serialize());
    }

    @DataProvider
    public static Object[][] providerCorrectSettings() {
        return new Object[][]{
            { getSettingsData("input1", "Test attribute 1", false, false) },
            { getSettingsData("input2", "Test attribute 2", true, false) },
            { getSettingsData("input3", "Test attribute 3", true, true) },
            { getSettingsData("input4", "Test attribute 4", false, true) },
        };
    }

    @DataProvider
    public static Object[][] providerIncorrectSettings() {
        return new Object[][]{
            { "{\"required\":0,\"label\":\"Test attribute\",\"float\":1}" },
            { "{\"name\":\"input3\",\"required\":0,\"float\":1}" },
            { "{\"name\":\"input4\",\"required\":1,\"label\":\"Test attribute\"}" },
            { "{}" },
            { "[]" },
        };
    }

    @DataProvider
    public static Object[][] providerCorrectData() {
        return new Object[][]{
            { 123, 123 },
            { 123.5, 123.5 },
            { "test\ntest", "test\ntest" },
            { "test", "test" },
        };
    }

    @DataProvider
    public static Object[][] providerIncorrectData() throws JSONException {
        return new Object[][]{
            { null, },
            { "" },
            { new JSONArray("[]") },
            { new JSONObject("{}") },
        };
    }

    @DataProvider
    public static Object[][] providerValidateCorrectData() {
        return new Object[][]{
            {
                getSettingsData(false, false),
                null,
            },
            {
                getSettingsData(false, false),
                "",
            },
            {
                getSettingsData(false, false),
                "test\ntest",
            },
            {
                getSettingsData(false, false),
                "test",
            },
        };
    }

    @DataProvider
    public static Object[][] providerValidateIncorrectData() {
        return new Object[][]{
            {
                getSettingsData(true, false),
                null,
            },
            {
                getSettingsData(true, false),
                "",
            },
        };
    }

    @DataProvider
    public static Object[][] providerSerializeData() {
        return new Object[][]{
            {
                getSettingsData(false, false),
                null,
                "",
            },
            {
                getSettingsData(false, false),
                "",
                "",
            },
            {
                getSettingsData(false, false),
                "test\ntest",
                "test\ntest",
            },
            {
                getSettingsData(false, false),
                "test",
                "test",
            },
        };
    }

    private NumberInputValue getValue(String settingsData) throws JSONException {
        Context context = mock(Context.class);
        NumberInputSettings settings = getSettings(settingsData);
        NumberInputValue attribute = new NumberInputValue(context);
        attribute.setSettings(settings);
        return attribute;
    }

    private NumberInputSettings getSettings(String data) throws JSONException {
        NumberInputSettings settings = new NumberInputSettings();
        JSONObject json = new JSONObject(data);
        settings.parse(json);
        return settings;
    }

    private static String getSettingsData(String field, String label, boolean required, boolean isFloat) {
        return "{\"name\":\"" + field + "\""
            + ",\"label\":\"" + label + "\""
            + ",\"required\":" + (required ? 1 : 0)
            + ",\"float\":" + (isFloat ? 1 : 0)
            + "}\"";
    }

    private static String getSettingsData(boolean required, boolean isFloat) {
        return getSettingsData("field", "Поле", required, isFloat);
    }
}
