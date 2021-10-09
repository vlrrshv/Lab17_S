package commands;
import collection.MyCollection;
import data.Vehicle;
import reader.MyReader;

import java.util.Scanner;

public class CommandAdd extends AbstractCommand {
    public CommandAdd(MyCollection myCollection) {
        super(myCollection);
    }

    @Override
    public String execute() {
        return getCollection().add((Vehicle) super.getParameter());
    }
}
