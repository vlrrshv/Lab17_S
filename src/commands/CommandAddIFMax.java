package commands;

import java.util.Scanner;
import data.*;
import collection.*;
import reader.*;
public class CommandAddIFMax extends AbstractCommand {

    public CommandAddIFMax(MyCollection collection) {
        super(collection);
    }

    @Override
    public String execute() {
        Vehicle vehicleAddIfMax = (Vehicle) super.getParameter();
        return super.getCollection().addIfMax(vehicleAddIfMax);
    }
}
