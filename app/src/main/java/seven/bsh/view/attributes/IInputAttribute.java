package seven.bsh.view.attributes;

import seven.bsh.model.ChecklistAttribute;
import seven.bsh.view.attributes.values.IDataValue;

public interface IInputAttribute extends IAttribute {
    IDataValue getValue();

    boolean validate();

    boolean isChanged();

    void prepareDataValue();

    SerializableData serialize();

    ChecklistAttribute getModel();

    void setData(Object data);

    String getName();
}
