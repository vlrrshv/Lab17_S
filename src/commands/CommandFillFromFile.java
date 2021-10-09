package commands;

import collection.MyCollection;
import data.Vehicle;

import java.util.List;

public class CommandFillFromFile extends AbstractCommand{
    public CommandFillFromFile(MyCollection collection) {
        super(collection);
    }

    @Override
    public String execute() {
        return super.getCollection().fillFromFile((List<Vehicle>) getParameter());
    }
}
