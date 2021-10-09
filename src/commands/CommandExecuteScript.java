package commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import collection.*;
import Exceptions.*;
import Connection.*;
import reader.*;
public class CommandExecuteScript extends AbstractCommand {
    private final File saveFile;
    public CommandExecuteScript(MyCollection collection,File saveFile) {
        super(collection);
        this.saveFile = saveFile;
    }

    @Override
    public String execute() {
        StringBuilder s = new StringBuilder();
        MyScriptReader reader = new MyScriptReader(saveFile);
        List<MessageForClient> list = null;
        try {
            list = reader.readScript((File) super.getParameter(),super.getCollection());
        } catch (IOException e) {
            s.append("This file doesn't exist");
        }
        if (!s.toString().equals("This file doesn't exist")){
            for (MessageForClient m:list){
                s.append("Command is done: ").append(m.isCommandDone()).append("\n").append(m.getMessage()).append("\n");
            }
        }
        return s.toString();
    }
}
