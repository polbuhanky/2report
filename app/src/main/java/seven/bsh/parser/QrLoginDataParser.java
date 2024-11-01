package seven.bsh.parser;

import seven.bsh.model.QrLoginData;

public class QrLoginDataParser {

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public QrLoginData parse(String data) {
        if (data == null) {
            return null;
        }

        String[] split = data.split("\n");
        if (split.length != 3) {
            return null;
        }
        return new QrLoginData(split[0].trim(), split[1].trim(), split[2].trim());
    }
}
