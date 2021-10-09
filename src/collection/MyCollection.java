package collection;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import data.*;
import collection.*;
import Exceptions.*;
/**
 * Class which contain collection of Vehicles
 */
public class MyCollection {
    private Queue<Vehicle> queue = new PriorityQueue<>();

    /**
     * Returns collections. Use only for changing element by some field!!!
     *
     * @return
     */
    public Queue<Vehicle> getQueue() {
        return queue;
    }

    /**
     * Show all elements from collection
     */
    public String show() {
        StringBuilder ss = new StringBuilder();
        queue.forEach(v->ss.append(v.toString()).append("\n"));
        return ss.toString();
    }

    /**
     * Show info about collection(Type and size)
     */
    public String info() {
        return "Type of collection is " + queue.getClass().toString() + ". Size of collection is " + queue.size();
    }

    /**
     * Method adds <i>vehicle</i> to collection
     *
     * @param vehicle
     */
    public String add(Vehicle vehicle) {
        //if (vehicle.getId()!=0){
           // vehicle.setId(findMaxId(queue));
        //}
        if (vehicle.getId() <= 0) {
            vehicle.setId(findMaxId(queue));
        }
        queue.offer(vehicle);
        return "Vehicle is added";
    }

    /**
     * Method changes element with <i>id</i> to <i>vehicle</i>
     *
     * @param vehicle
     */
    public String update(Vehicle vehicle) {
        Queue<Vehicle> prom = new PriorityQueue<>();
        queue.stream().filter(v->v.getId()==vehicle.getId()).forEach(v->prom.add(v.setAll(vehicle)));
        if (!prom.isEmpty()){
            return "Element is updated";
        }else{
            return "There is no such element with this id";
        }
    }

    /**
     * Method removes element with <i>idToRemove</i>
     *
     * @param idToRemove
     */
    public String removeById(int idToRemove) {
        int sizeBefore = queue.size();
        queue.removeIf(v->v.getId()==idToRemove);
        int sizeAfter = queue.size();
        if (sizeAfter!=sizeBefore){
            return "Element was deleted";
        }else{
            return "There is no such element";
        }
    }

    /**
     * Removes all element from collections
     */
    public String clear() {
        if (queue.isEmpty()) {
            return "Collection is already empty";
        } else {
            queue.clear();
            return "Collection is empty";
        }
    }

    /**
     * Removes first element in collection
     */
    public String removeFirst() {
        queue.poll();
        return "First element was deleted";
    }

    /**
     * Removes and return first element in collection
     *
     * @return first element in collection
     */
    public String showFirst() {
        return queue.poll().toString();
    }

    /**
     * Returns max element from collection
     *
     * @return max element
     */
    private Vehicle getMax() {
        Queue<Vehicle> queueVehicle = new PriorityQueue<>(queue.size(), Collections.reverseOrder());
        queueVehicle.addAll(queue);
        return queueVehicle.poll();
    }

    /**
     * Compare <i>vehicleAddIfMax</i> and max element. If <i>vehicleAddIfMax</i> higher, puts it in collection
     *
     * @param vehicleAddIfMax
     */
    public String addIfMax(Vehicle vehicleAddIfMax) {
        if (queue.isEmpty()) {
            queue.offer(vehicleAddIfMax);
            return "Element was added";
        } else {
            if (vehicleAddIfMax.compareTo(this.getMax()) > 0) {
                queue.offer(vehicleAddIfMax);
                return "Element was added";
            }else{
                return "Element wasn't added, because his engine power isn't bigger than max engine power in collection";
            }
        }
    }

    /**
     * Groups collection by field <i>creationDate</i>
     *
     * @return Map which contains amount of elements in each group
     */
    public Map<LocalDate, Integer> groupByCreationDate() {
        Map<LocalDate, List<Vehicle>> prom = queue.stream().collect(Collectors.groupingBy(Vehicle::getCreationDate));
        Map<LocalDate,Integer> map = new TreeMap<>();
        for (LocalDate time : prom.keySet()) {
            map.put(time,prom.get(time).size());
        }
        return map;
    }

    /**
     * Saves collection to <i>fileName</i>
     *
     * @param fileName
     * @throws FileNotFoundException
     */
    public void save(File fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String lineForReading;
        StringBuilder s = new StringBuilder();
        while ((lineForReading = reader.readLine()) != null) {
            if (!lineForReading.trim().equals("</Vehicles>")) {
                s.append(lineForReading).append("\n");
            }
        }
        reader.close();
        FileOutputStream fos = new FileOutputStream(fileName, false);
        Queue<Vehicle> printQueue = new PriorityQueue<>(queue);
        while (!printQueue.isEmpty()) {
            Vehicle vehicle = printQueue.poll();

            if (!vehicle.isInFile()) {
                s.append("  <Vehicle>\n");
                s.append("      <Name>").append(vehicle.getName()).append("</Name>\n");
                s.append("      <Id>").append(vehicle.getId()).append("</Id>\n");
                s.append("      <X>").append(vehicle.getCoordinates().getX()).append("</X>\n");
                s.append("      <Y>").append(vehicle.getCoordinates().getY()).append("</Y>\n");
                if (vehicle.getCreationDate() != null) {
                    s.append("      <CreationDate>").append(vehicle.getCreationDate()).append("</CreationDate>\n");
                } else {
                    s.append("      <CreationDate>").append("null").append("</CreationDate>\n");
                }
                s.append("      <EnginePower>").append(vehicle.getEnginePower()).append("</EnginePower>\n");
                s.append("      <Capacity>").append(vehicle.getCapacity()).append("</Capacity>\n");
                if (vehicle.getVehicleType() != null) {
                    s.append("      <VehicleType>").append(vehicle.getVehicleType().toString()).append("</VehicleType>\n");
                } else {
                    s.append("      <VehicleType>").append("null").append("</VehicleType>\n");
                }
                if (vehicle.getFuelType() != null) {
                    s.append("      <FuelType>").append(vehicle.getFuelType().toString()).append("</FuelType>\n");
                } else {
                    s.append("      <FuelType>").append("null").append("</FuelType>\n");
                }
                s.append("  </Vehicle>\n");
            }
            isFile(vehicle.getId());
        }
        s.append("</Vehicles>");
        byte[] buffer = s.toString().getBytes();
        try {
            fos.write(buffer, 0, buffer.length);
        } catch (
                IOException e) {
            System.out.println("Something went wrong");
        } finally {
            fos.close();
        }

    }

    /**
     * Private method for setting checkInFile as true
     * @param id
     * @return
     */
    private void isFile(int id){
        for (Vehicle v:queue){
            if (v.getId()==id){
                v.setInFile(true);
            }
        }
    }

    /**
     * Removes one element with FuelType = <i>fuelType</i>
     *
     * @param fuelType
     */
    public String removeByFuelType(FuelType fuelType) throws IllegalArgumentException {
        Queue<Vehicle> prom = new PriorityQueue<>();
        queue.stream().filter(v->v.getFuelType()==fuelType).forEach(prom::add);
        if (prom.size()!= queue.size()){
            Vehicle deleted = prom.poll();
            queue.remove(deleted);
            return "Element with id = " + deleted.getId() +" was deleted from collection. ";
        }else{
            return "There is no such element with this FuelType";
        }
    }

    /**
     * Returns max element by field Name
     *
     * @return maxName element
     */
    public String getMaxName() {
        if (!queue.isEmpty()) {
            Optional<Vehicle> s  = queue.stream().max(new NameComparator());
            return "Max element is " + s.get().getName();
        } else {
            return "Collection is empty. Add element firstly";
        }
    }

    /**
     * Fill collection from XML file <i>fileName</i>
     *
     * @param list
     * @throws IOException
     */
    public String fillFromFile(List<Vehicle> list){
        queue.addAll(list);
        return "okay";
    }

    /**
     * Private Method for finding max element by ID in collection <i>queue</i>
     * @param queue
     * @return maxElement
     */
    private int findMaxId(Queue<Vehicle> queue) {
        int max = 0;
        for (Vehicle v : queue) {
            if (v.getId() > max) {
                max = v.getId();
            }
        }
        max++;
        return max;
    }

    /**
     * Check collection by emptiness
     *
     * @return
     */
    public boolean checkToEmpty() {
        return queue.isEmpty();
    }

    public String help() {
        return "All commands: \n"
                + "help - show list of commands \n"
                + "info - show information about collection \n"
                + "show - show all elements of collection \n"
                + "add - add new element \n"
                + "update id - update element with id;you need to have the id in the same line as command\n"
                + "remove_by_id id - remove element from collection; you have to write the id in the same line as command\n"
                + "clear - clear the collection \n"
                + "save - save collection to file \n"
                + "execute_script scriptName - read and execute commands from file_name; you have to write the scriptName in the same line as command \n"
                + "exit - end the program \n"
                + "remove_first - remove first element \n"
                + "remove_head - remove fist element and delete him \n"
                + "add_if_max - add element if it's bigger than max element in collection \n"
                + "remove_any_by_fuel_type fuelType - remove elements which have this fuelType; you have to write the fuelType in the same line as command \n"
                + "max_by_name - show element with max name \n"
                + "group_counting_by_creation_date - group elements by creationDate and show amounts of each group \n";
    }
}
