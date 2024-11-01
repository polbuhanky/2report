package seven.bsh.view.attributes.values;

import android.content.Context;

import seven.bsh.view.attributes.settings.IInputAttributeSettings;

public interface IDataValue {
    void parse(Object data);

    boolean isChanged();

    boolean validate();

    Object serialize();

    String getError();

    void setSettings(IInputAttributeSettings settings);

    Context getContext();

    void updateFromSettings();
}
