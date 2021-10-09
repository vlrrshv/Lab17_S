import Connection.DataToOutput;
import Connection.MessageForClient;
import collection.MyCollection;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import commands.*;
import data.Vehicle;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Exchanger;

public class ServerForReading implements Runnable {
    private SocketChannel clientDialog;
    private DataBaseWorker worker;
    private MyCollection collection;
    private Exchanger<MessageForClient> exchanger;

    public ServerForReading(SocketChannel clientDialog, DataBaseWorker worker, MyCollection collection, Exchanger<MessageForClient> exchanger) {
        this.clientDialog = clientDialog;
        this.worker = worker;
        this.collection = collection;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        while (true) {
            Switcher switcher = new Switcher();
            MessageForClient message = new MessageForClient();
            User user;
            ByteBuffer buffer = ByteBuffer.allocate(65536);
            DataToOutput<?> command = read(buffer);
            try {
                String name = command.getCommandName();
                System.out.println(name);
                synchronized (collection) {
                    switch (name) {
                        case "help":
                            AbstractCommand<String> help = new CommandHelp(collection);
                            switcher.setCommand(help);
                            message.setCommandIsDone(true);
                            message.setMessage(switcher.doCommand());
                            break;
                        case "info":
                            AbstractCommand<String> info = new CommandInfo(collection);
                            switcher.setCommand(info);
                            message.setCommandIsDone(true);
                            message.setMessage(switcher.doCommand());
                            break;
                        case "show":
                            AbstractCommand<String> show = new CommandShow(collection);
                            switcher.setCommand(show);
                            message.setCommandIsDone(true);
                            message.setMessage(switcher.doCommand());
                            break;
                        case "add":
                            AbstractCommand<Vehicle> add = new CommandAdd(collection);
                            add.setParameter((Vehicle) command.getArgument());
                            switcher.setCommand(add);
                            message.setCommandIsDone(true);
                            message.setMessage(switcher.doCommand());

                            worker.add((Vehicle) command.getArgument());
                            break;
                        case "update":
                            int checkUpdate = worker.updateById((Vehicle) command.getArgument());
                            if (checkUpdate > 0) {
                                AbstractCommand<Vehicle> updateById = new CommandUpdateByID(collection);
                                updateById.setParameter((Vehicle) command.getArgument());
                                switcher.setCommand(updateById);
                                String s = switcher.doCommand();
                                message.setCommandIsDone(s.equals("Element is updated"));
                                message.setMessage(s);
                            } else {
                                message.setCommandIsDone(false);
                                message.setMessage("You don't have such element in your possession");
                            }


                            break;
                        case "remove_by_id":
                            boolean checkRemove = false;
                            if (!collection.checkToEmpty()) {
                                checkRemove = worker.removeById((Integer) command.getArgument());
                            }

                            if (checkRemove) {
                                AbstractCommand<Integer> removeById = new CommandRemoveById(collection);
                                removeById.setParameter((Integer) command.getArgument());
                                switcher.setCommand(removeById);
                                String removeId = switcher.doCommand();
                                message.setCommandIsDone(removeId.equals("Element was deleted"));
                                message.setMessage(removeId);
                            } else {
                                message.setCommandIsDone(false);
                                message.setMessage("You don't have such element in your possession");
                            }
                            break;
                        case "clear":
                            AbstractCommand<String> clear = new CommandClear(collection);
                            switcher.setCommand(clear);
                            message.setCommandIsDone(!collection.checkToEmpty());
                            message.setMessage(switcher.doCommand());

                            if (!collection.checkToEmpty()) {
                                worker.clear();
                            }
                            break;
                        case "execute_script":
                            //AbstractCommand<File> execute_script = new CommandExecuteScript(collection, file);
                            //execute_script.setParameter((File) command.getArgument());
                            //switcher.setCommand(execute_script);
                            String sc = switcher.doCommand();
                            message.setCommandIsDone(!sc.equals("This file doesn't exist"));
                            message.setMessage(sc);
                            break;
                        case "remove_first":
                        case "remove_head":
                            int id = 0;
                            if (!collection.checkToEmpty()) {
                                id = worker.removeFirst();
                            }
                            if (id != 0) {
                                AbstractCommand<Integer> removeById2 = new CommandRemoveById(collection);
                                removeById2.setParameter(id);
                                switcher.setCommand(removeById2);
                                String removeId2 = switcher.doCommand();
                                message.setCommandIsDone(removeId2.equals("Element was deleted"));
                                message.setMessage(removeId2);
                            } else {
                                message.setCommandIsDone(false);
                                message.setMessage("You don't have your vehicles");
                            }
                            break;

                        case "add_if_max":
                            AbstractCommand<Vehicle> addIfMax = new CommandAddIFMax(collection);
                            addIfMax.setParameter((Vehicle) command.getArgument());
                            switcher.setCommand(addIfMax);
                            String addMax = switcher.doCommand();
                            message.setCommandIsDone(addMax.equals("Element was added"));
                            message.setMessage(addMax);

                            if (message.isCommandDone()) {
                                worker.add((Vehicle) command.getArgument());
                            }
                            break;
                        case "remove_any_by_fuel_type":
                            int idFuelType = worker.removeAnyByFuelType(command.getArgument().toString());
                            if (idFuelType != 0) {
                                AbstractCommand<Integer> removeById3 = new CommandRemoveById(collection);
                                removeById3.setParameter(idFuelType);
                                switcher.setCommand(removeById3);
                                String removeId3 = switcher.doCommand();
                                message.setCommandIsDone(removeId3.equals("Element was deleted"));
                                message.setMessage(removeId3);
                            } else {
                                message.setCommandIsDone(false);
                                message.setMessage("You don't have this vehicle in your possession");
                            }
                            break;
                        case "max_by_name":
                            AbstractCommand<String> maxByName = new CommandMaxByName(collection);
                            switcher.setCommand(maxByName);
                            message.setCommandIsDone(collection.checkToEmpty());
                            message.setMessage(switcher.doCommand());
                            break;
                        case "group_counting_by_creation_date":
                            AbstractCommand<String> groupCount = new CommandGroupCounting(collection);
                            switcher.setCommand(groupCount);
                            message.setCommandIsDone(!collection.checkToEmpty());
                            message.setMessage(switcher.doCommand());
                            break;
                        case "newUser":
                            user = (User) command.getArgument();
                            String answer = worker.addNewUser(user);
                            if (answer.equals("Something went wrong with authorization")) {
                                System.out.println(answer);
                                System.exit(-1);
                            } else {
                                message.setCommandIsDone(true);
                                message.setMessage(answer);
                            }
                            break;
                        case "exit":
                            AbstractCommand<File> save = new CommandSave(collection);
                            //save.setParameter(file);
                            switcher.setCommand(save);
                            String saved = switcher.doCommand();
                            message.setCommandIsDone(!saved.equals("File doesn't exist. Enter a command"));
                            message.setMessage(saved);
                            break;
                    }
                    exchanger.exchange(message);
                }
            } catch (NullPointerException | InterruptedException e) {
                System.out.println("Client went out");
                break;
            }
        }
    }

    private <T> T deserialize(ByteBuffer byteBuffer) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer.array());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        T data;
        try {
            data = (T) objectInputStream.readObject();
        } catch (IOException e) {
            data = null;
        }
        byteArrayInputStream.close();
        objectInputStream.close();
        byteBuffer.clear();
        return data;
    }

    private DataToOutput<?> read(ByteBuffer buffer) {
        try {
            clientDialog.read(buffer);
            DataToOutput<?> command = deserialize(buffer);
            buffer.clear();
            return command;
        } catch (IOException | ClassNotFoundException e) {
            try {
                clientDialog.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return null;
    }
}
