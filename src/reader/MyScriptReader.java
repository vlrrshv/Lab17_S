package reader;

import Exceptions.FileCycleException;
import Exceptions.TooBigNumberException;
import Exceptions.ZeroCheckException;
import collection.MyCollection;
import data.Coordinates;
import data.FuelType;
import data.Vehicle;
import data.VehicleType;
import Connection.*;

import java.io.*;
import java.net.Inet4Address;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MyScriptReader {
    private final File saveFile;

    public MyScriptReader(File saveFile) {
        this.saveFile = saveFile;
    }

    /**
     * Read and execute script
     *
     * @param scriptFile
     * @param collection - Object of class, which contains Queue
     * @throws IOException
     * @throws IllegalArgumentException
     */
    public List<MessageForClient> readScript(File scriptFile, MyCollection collection) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(scriptFile));
        String command;
        List<MessageForClient> out = new ArrayList<>();
        while ((command = reader.readLine()) != null) {
            MessageForClient message = new MessageForClient();
            String[] commands = command.split(" ");
            if (commands[0].equals("help")) {
                message.setCommandIsDone(true);
                message.setMessage(collection.help());
            } else if (commands[0].equals("info")) {
                message.setCommandIsDone(true);
                message.setMessage(collection.info());
            } else if (commands[0].equals("show")) {
                message.setCommandIsDone(true);
                message.setMessage(collection.show());
            } else if (commands[0].equals("add")) {
                Vehicle vehicle;
                String name = reader.readLine();
                try{
                    Float x = Float.parseFloat(reader.readLine());
                    Double y = Double.parseDouble(reader.readLine());
                    Coordinates coordinates = new Coordinates(x, y);
                    float enginePower = Float.parseFloat(reader.readLine());
                    int capacity = Integer.parseInt(reader.readLine());
                    String vehicleType = reader.readLine();
                    String fuelType = reader.readLine();
                    vehicle = new Vehicle(name, coordinates, enginePower, capacity, VehicleType.valueOf(vehicleType), FuelType.valueOf(fuelType));
                }catch (IllegalArgumentException e){
                    vehicle = null;
                }
                if (vehicle!=null){
                    message.setCommandIsDone(true);
                    message.setMessage(collection.add(vehicle));
                }else{
                    message.setCommandIsDone(false);
                    message.setMessage("Vehicle "+name+"from script wasn't added");
                }
            } else if (commands[0].equals("update")) {
                String nameToUpdate = null;
                Vehicle vehicle = null;
                try{
                    int id = Integer.parseInt(commands[1]);
                    nameToUpdate = reader.readLine();
                    Float x = Float.parseFloat(reader.readLine());
                    Double y = Double.parseDouble(reader.readLine());
                    Coordinates coordinates = new Coordinates(x, y);
                    float enginePower = Float.parseFloat(reader.readLine());
                    int capacity = Integer.parseInt(reader.readLine());
                    String vehicleType = reader.readLine();
                    String fuelType = reader.readLine();
                    vehicle = new Vehicle(id,nameToUpdate, coordinates, enginePower, capacity, VehicleType.valueOf(vehicleType), FuelType.valueOf(fuelType));
                }catch (IllegalArgumentException e){
                }catch (IndexOutOfBoundsException e){
                    nameToUpdate = null;
                }
                if (nameToUpdate == null) {
                    message.setCommandIsDone(false);
                    message.setMessage("There wasn't an id to update in the script");
                }else{
                    if (vehicle!=null){
                        message.setCommandIsDone(true);
                        message.setMessage(collection.update(vehicle));
                    }else{
                        message.setCommandIsDone(false);
                        message.setMessage("Vehicle "+nameToUpdate+"from script wasn't updated");
                    }
                }
            } else if (commands[0].equals("remove_by_id")) {
                try{
                    int id = Integer.parseInt(commands[1]);
                    String s = collection.removeById(id);
                    message.setCommandIsDone(s.equals("Element was deleted"));
                    message.setMessage(s);
                }catch (IndexOutOfBoundsException e){
                    message.setCommandIsDone(false);
                    message.setMessage("There wasn't id of element in script");
                }
            } else if (commands[0].equals("clear")) {
                message.setCommandIsDone(!collection.checkToEmpty());
                message.setMessage(collection.clear());
            } else if (commands[0].equals("execute_script")) {
                File sFile;
                try {
                    sFile = new File(commands[1]);
                    if (scriptFile.equals(sFile)){
                        throw new FileCycleException();
                    }
                    readScript(sFile, collection);
                }catch (IndexOutOfBoundsException e ){
                    message.setCommandIsDone(false);
                    message.setMessage("There wasn't script name in this script");
                }catch (FileCycleException e){
                    message.setCommandIsDone(false);
                    message.setMessage("Cycling in the script");
                }
            } else if (commands[0].equals("remove_first")) {
                message.setCommandIsDone(!collection.checkToEmpty());
                message.setMessage(collection.removeFirst());
            } else if (commands[0].equals("remove_head")) {
                message.setCommandIsDone(!collection.checkToEmpty());
                message.setMessage(collection.showFirst());
            } else if (commands[0].equals("add_if_max")) {
                String name = reader.readLine();
                Vehicle vehicle;
                try{
                    Float x = Float.parseFloat(reader.readLine());
                    Double y = Double.parseDouble(reader.readLine());
                    Coordinates coordinates = new Coordinates(x, y);
                    float enginePower = Float.parseFloat(reader.readLine());
                    int capacity = Integer.parseInt(reader.readLine());
                    String vehicleType = reader.readLine();
                    String fuelType = reader.readLine();
                    vehicle = new Vehicle(name, coordinates, enginePower,
                            capacity, VehicleType.valueOf(vehicleType), FuelType.valueOf(fuelType));
                }catch (IllegalArgumentException e){
                    vehicle = null;
                }
                if (vehicle!=null){
                    String s = collection.addIfMax(vehicle);
                    message.setCommandIsDone(s.equals("Element was added"));
                    message.setMessage(s);
                }else{
                    message.setCommandIsDone(false);
                    message.setMessage("Vehicle with name = "+name+"wasn't added, because there wasn't all data about him");
                }
            } else if (commands[0].equals("remove_any_by_fuel_type")) {
                FuelType fuelType;
                try{
                    fuelType = FuelType.valueOf(commands[1]);
                    String s = collection.removeByFuelType(fuelType);
                    message.setCommandIsDone(!s.equals("There is no such element with this FuelType"));
                    message.setMessage(s);
                }catch (IllegalArgumentException e){
                    message.setCommandIsDone(false);
                    message.setMessage("There is no such element with this FuelType, which was in the script");
                }catch (IndexOutOfBoundsException e){
                    message.setCommandIsDone(false);
                    message.setMessage("There wasn't a FuelType to remove in the script");
                }
            } else if (commands[0].equals("max_by_name")) {
                String max = collection.getMaxName();
                message.setCommandIsDone(!max.equals("Collection is empty. Add element firstly"));
                message.setMessage(max);
            } else if (commands[0].equals("group_counting_by_creation_date")) {
                if (!collection.checkToEmpty()){
                    Map<LocalDate, Integer> LocalDateMap = collection.groupByCreationDate();
                    Set<LocalDate> keys = LocalDateMap.keySet();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (LocalDate key : keys) {
                        stringBuilder.append("Creation date is ").append(key).append(" .The amounts is ").append(LocalDateMap.get(key));
                    }
                    message.setCommandIsDone(true);
                    message.setMessage(stringBuilder.toString());
                }else{
                    message.setCommandIsDone(false);
                    message.setMessage("Collection is empty.Add elements firstly");
                }
            }else if (commands[0].equals("exit")){
                collection.save(saveFile);
                message.setCommandIsDone(true);
                message.setMessage("Data is saved to "+saveFile.toString());
            }
            out.add(message);
        }
        return out;
    }
}
