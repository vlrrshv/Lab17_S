

import collection.MyCollection;

import java.io.File;
import java.io.*;
import java.util.*;

public class ServerInput extends  Thread {
    private MyCollection collection;
    private File file;

    public ServerInput(MyCollection collection, File file) {
        this.collection = collection;
        this.file = file;
    }
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try{
                String command = scanner.nextLine();
                if (command.equals("save")) {
                    try {
                        collection.save(file);
                        System.out.println("Collection is saved to " + file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Command \"" + command + "\" doesn't exists");
                }
            }catch (NoSuchElementException e){
                try {
                    System.out.println("You turned off the server");
                    System.out.println("Collection is saved to " + file.getAbsolutePath());
                    collection.save(file);
                    System.exit(0);
                }catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
