package commands;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import collection.*;
public class CommandSave extends AbstractCommand {
    public CommandSave(MyCollection collection) {
        super(collection);
    }

    @Override
    public String execute() {
        try {
            super.getCollection().save((File)super.getParameter());
            return "Data is saved to "+super.getParameter();
        } catch (IOException e) {
            return "File doesn't exist. Enter a command";
        }
    }
}
