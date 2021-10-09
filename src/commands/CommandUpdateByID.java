package commands;

import java.util.Scanner;
import data.*;
import collection.*;
import reader.*;
public class CommandUpdateByID extends AbstractCommand {
    public CommandUpdateByID(MyCollection myCollection) {
        super(myCollection);
    }
    @Override
    public String execute() {
        return super.getCollection().update((Vehicle)getParameter());
    }
}
