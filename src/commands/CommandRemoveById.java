package commands;

import java.util.Scanner;

import data.*;
import collection.*;

public class CommandRemoveById extends AbstractCommand {
    public CommandRemoveById(MyCollection collection) {
        super(collection);
    }

    @Override
    public String execute() {
        if (!super.getCollection().checkToEmpty()) {
            return super.getCollection().removeById((Integer) getParameter());
        } else {
            return "Collection is empty";
        }
    }
}
