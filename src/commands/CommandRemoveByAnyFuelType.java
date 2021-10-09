package commands;

import java.util.Scanner;
import collection.*;
import data.FuelType;

public class CommandRemoveByAnyFuelType extends AbstractCommand {
    public CommandRemoveByAnyFuelType(MyCollection collection) {
        super(collection);
    }

    @Override
    public String execute() {
        if (!super.getCollection().checkToEmpty()) {
            return super.getCollection().removeByFuelType((FuelType)super.getParameter());
        } else {
            return "Collection is empty. Add element firstly.";
        }
    }
}
