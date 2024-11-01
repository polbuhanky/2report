package seven.bsh.net.parser;

import java.util.ArrayList;
import java.util.List;

public abstract class Parser<E, M> {

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public abstract M parseOne(E entity);

    public List<M> parseAll(List<E> entities) {
        List<M> list = new ArrayList<>();
        for (E entity : entities) {
            M model = parseOne(entity);
            list.add(model);
        }
        return list;
    }
}
