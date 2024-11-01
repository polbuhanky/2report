package seven.bsh.views.attributes;

import android.content.Context;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import seven.bsh.model.ChecklistAttribute;
import seven.bsh.view.attributes.AttributeItem;
import seven.bsh.view.attributes.TextInputAttribute;
import seven.bsh.view.attributes.settings.TextInputSettings;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(DataProviderRunner.class)
public class TextInputAttributeUnitTest {
    private static Context sContext;

    @BeforeClass
    public static void beforeClass() {
        sContext = mock(Context.class);
    }

    @Test
    @UseDataProvider("providerCorrectSettings")
    public void createCorrect(String jsonText) throws JSONException {
        ChecklistAttribute model = getModel(jsonText);
        JSONObject json = model.getSettings();
        TextInputAttribute attribute = (TextInputAttribute) AttributeItem.Builder.create(sContext, model);
        TextInputSettings settings = (TextInputSettings) attribute.getSettings();

        assertEquals(settings.getName(), json.getString("name"));
        assertEquals(settings.getLabel(), json.getString("label"));
        assertEquals(settings.isRequired(), json.optInt("required") == 1);
        assertEquals(settings.isMultiline(), json.getInt("multiline") == 1);
        assertEquals(settings.getMin(), json.getInt("min"));
        assertEquals(settings.getMax(), json.getInt("max"));
    }

    @Test(expected = JSONException.class)
    @UseDataProvider("providerIncorrectSettings")
    public void createIncorrect(String jsonText) throws JSONException {
        ChecklistAttribute model = getModel(jsonText);
        AttributeItem.Builder.create(sContext, model);
    }

    /*@Test
    @UseDataProvider("providerCorrectData")
    public void parseCorrectData(Object data, Object expected) throws JSONException {
        String settingsData = getSettingsData(false, 0, 0, true);
        ChecklistAttribute model = getModel(settingsData);
        TextInputAttribute attribute = (TextInputAttribute) AttributeItem.Builder.create(sContext, model);
        attribute.parse(data);
        assertEquals(expected, attribute.getValue());
    }*/

    /*@Test(expected = ClassCastException.class)
    @UseDataProvider("providerIncorrectData")
    public void parseIncorrectData(Object data) throws JSONException {
        String settingsData = getSettingsData(false, 0, 0, true);
        TextInputValue attribute = getValue(settingsData);
        attribute.parse(data);
        assertNotSame(data, attribute.getValue());
    }

    @Test
    @UseDataProvider("providerValidateCorrectData")
    public void validateCorrectData(String settingsData, Object data) throws JSONException {
        TextInputValue attribute = getValue(settingsData);
        attribute.parse(data);
        assertTrue(attribute.validate());
    }

    @Test
    @UseDataProvider("providerValidateIncorrectData")
    public void validateIncorrectData(String settingsData, Object data) throws JSONException {
        TextInputValue attribute = getValue(settingsData);
        attribute.parse(data);
        assertFalse(attribute.validate());
    }

    @Test
    @UseDataProvider("providerSerializeData")
    public void serializeData(String settingsData, Object data, Object expected) throws JSONException {
        TextInputValue attribute = getValue(settingsData);
        attribute.parse(data);
        assertTrue(attribute.validate());
        assertEquals(expected, attribute.serialize());
    }*/

    @DataProvider
    public static Object[][] providerCorrectSettings() {
        return new Object[][]{
            { getSettingsData("input1", "Test attribute 1", false, 0, 0, false) },
            { getSettingsData("input2", "Test attribute 2", true, 0, 0, false) },
            { getSettingsData("input4", "Test attribute 3", true, 5, 0, false) },
            { getSettingsData("input5", "Test attribute 4", true, 5, 10, false) },
            { getSettingsData("input6", "Test attribute 5", true, 5, 10, true) },
            { getSettingsData("input7", "Test attribute 6", false, 5, 10, true) },
            { getSettingsData("input8", "Test attribute 7", false, 0, 10, true) },
            { getSettingsData("input9", "Test attribute 8", false, 0, 0, true) },
        };
    }

    @DataProvider
    public static Object[][] providerIncorrectSettings() {
        return new Object[][]{
            { "{\"required\":0,\"label\":\"Test attribute\",\"min\":0,\"max\":0,\"multiline\":0}" },
            { "{\"name\":\"input3\",\"required\":0,\"min\":5,\"max\":10,\"multiline\":0}" },
            { "{\"name\":\"input4\",\"required\":1,\"label\":\"Test attribute\",\"max\":0,\"multiline\":0}" },
            { "{\"name\":\"input5\",\"required\":0,\"label\":\"Test attribute\",\"min\":5,\"multiline\":0}" },
            { "{\"name\":\"input5\",\"required\":0,\"label\":\"Test attribute\",\"min\":5\"max\":0}" },
            { "{}" },
            { "[]" },
        };
    }

    @DataProvider
    public static Object[][] providerCorrectData() {
        return new Object[][]{
            { null, "" },
            { "", "" },
            { "test\ntest", "test\ntest" },
            { "test", "test" },
        };
    }

    @DataProvider
    public static Object[][] providerIncorrectData() throws JSONException {
        return new Object[][]{
            { 123 },
            { new JSONArray("[]") },
            { new JSONObject("{}") },
        };
    }

    @DataProvider
    public static Object[][] providerValidateCorrectData() {
        return new Object[][]{
            {
                getSettingsData(false, 0, 0, false),
                null,
            },
            {
                getSettingsData(false, 0, 0, false),
                "",
            },
            {
                getSettingsData(false, 0, 0, false),
                "test\ntest",
            },
            {
                getSettingsData(false, 0, 0, false),
                "test",
            },
        };
    }

    @DataProvider
    public static Object[][] providerValidateIncorrectData() {
        return new Object[][]{
            {
                getSettingsData(true, 0, 0, false),
                null,
            },
            {
                getSettingsData(true, 0, 0, false),
                "",
            },
        };
    }

    @DataProvider
    public static Object[][] providerSerializeData() {
        return new Object[][]{
            {
                getSettingsData(false, 0, 0, false),
                null,
                "",
            },
            {
                getSettingsData(false, 0, 0, false),
                "",
                "",
            },
            {
                getSettingsData(false, 0, 0, false),
                "test\ntest",
                "test\ntest",
            },
            {
                getSettingsData(false, 0, 0, false),
                "test",
                "test",
            },
        };
    }

    private static String getSettingsData(String field, String label, boolean required, int min, int max, boolean multiline) {
        return "{\"type\":" + ChecklistAttribute.TYPE_STRING
            + ",\"settings\":{"
            + "\"name\":\"" + field + "\""
            + ",\"label\":\"" + label + "\""
            + ",\"required\":" + (required ? 1 : 0)
            + ",\"min\":" + min
            + ",\"max\":" + max
            + ",\"multiline\":" + (multiline ? 1 : 0)
            + "}"
            + "}";
    }

    private static String getSettingsData(boolean required, int min, int max, boolean multiline) {
        return getSettingsData("field", "Поле", required, min, max, multiline);
    }

    private ChecklistAttribute getModel(String data) throws JSONException {
        JSONObject json = new JSONObject(data);
        ChecklistAttribute model = new ChecklistAttribute();
        model.parse(json);
        return model;
    }
}
