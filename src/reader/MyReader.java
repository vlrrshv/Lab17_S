package reader;
import data.Coordinates;
import Exceptions.FileCycleException;
import Exceptions.TooBigNumberException;
import Exceptions.ZeroCheckException;
import data.*;
import collection.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for reading
 */
public class MyReader {
    /**
     * Check file on big amounts of zero
     * @param line - line where could be some problem with a lot of zeros
     * @return
     */
    private static boolean chekByZero(String line) {
        boolean check = false;
        char[] chars = line.toCharArray();
        if (chars.length > 1 && !line.equals("-0")) {
            if (chars[0] == '0' && chars[1] != '.' || chars[0] == '-' && chars[1] == '0' && chars[2] != '.'){
                check = false;
            } else {
                check = true;
            }
        } else check = true;
        return check;
    }

    /**
     * Method gets new <i>Vehicle</i> from User
     * @param scanner
     * @return Vehicle
     */

    public static Vehicle getElementFromConsole(Scanner scanner) {
        String name;
        while (true) {
            System.out.println("Enter the name:");
            name = scanner.nextLine();
            if (!name.equals("")) {
                break;
            }
        }
        System.out.println("Enter X coordinate (X is float and > -898):");
        String xCheck;
        float x;
        while (true) {
            xCheck = scanner.nextLine();
            try {
                if (chekByZero(xCheck)) {
                    x = Float.parseFloat(xCheck);
                    if (x > -898) {
                        if (x<=Float.MAX_VALUE){
                            break;
                        }else throw new TooBigNumberException();
                    } else throw new NumberFormatException();
                } else throw new ZeroCheckException();
            } catch (TooBigNumberException e){
                System.out.println("You tried to enter too big number");
            } catch (ZeroCheckException e) {
                System.out.println("You wrote '000000' or '-000000' or something same. If you want fill field like 0(1/etc),please write just 0(1/etc)");
            } catch (NumberFormatException e) {
                System.out.println("X has to be float type and  > -898.Try again");
            }
        }
        System.out.println("Enter Y coordinate (Y is double):");
        String yCheck;
        double y;
        while (true) {
            yCheck = scanner.nextLine();
            try {
                if (chekByZero(yCheck)) {
                    y = Double.parseDouble(yCheck);
                    if (y<=Double.MAX_VALUE && y>=Double.MIN_VALUE){
                        break;
                    }else throw new TooBigNumberException();
                } else throw new ZeroCheckException();
            }catch (TooBigNumberException e){
                System.out.println("You tried to enter too big or too small number");
            } catch (ZeroCheckException e) {
                System.out.println("You wrote '000000' or '-000000' or something same. If you want fill field like 0(1/etc),please write just 0(1/etc)");
            } catch (NumberFormatException e) {
                System.out.println("Y has to be double type.Try again");
            }
        }
        Coordinates coordinates = new Coordinates(x, y);
        System.out.println("Enter Engine power (EnginePower is float and >0):");
        String enginePowerCheck;
        float enginePower;
        while (true) {
            enginePowerCheck = scanner.nextLine();
            try {
                enginePower = Float.parseFloat(enginePowerCheck);
                if (chekByZero(enginePowerCheck)) {
                    if (enginePower > 0) {
                        if (enginePower<=Float.MAX_VALUE){
                            break;
                        }else throw new TooBigNumberException();
                    } else throw new NumberFormatException();
                } else throw new ZeroCheckException();
            } catch (TooBigNumberException e){
                System.out.println("You tried to enter too big number");
            } catch (ZeroCheckException e) {
                System.out.println("You wrote '000000' or '-000000' or something same. If you want fill field like 0(1/etc),please write just 0(1/etc)");
            } catch (NumberFormatException e) {
                System.out.println("Engine power has to be float and > 0. Try again");
            }
        }
        System.out.println("Enter capacity (Capacity is int and >0):");
        String capacityCheck;
        int capacity;
        while (true) {
            capacityCheck = scanner.nextLine();
            try {
                if (chekByZero(capacityCheck)) {
                    capacity = Integer.parseInt(capacityCheck);
                    if (capacity > 0) {
                        break;
                    } else throw new NumberFormatException();
                } else throw new ZeroCheckException();
            } catch (ZeroCheckException e) {
                System.out.println("You wrote '000000' or '-000000' or something same. If you want fill field like 0(1/etc),please write just 0(1/etc)");
            } catch (NumberFormatException e) {
                System.out.println("Capacity has to be int and > 0. Try again");
            }
        }
        System.out.println("Choose type of vehicle: " + VehicleType.showAllValues());
        String vehicleTypeCheck = scanner.nextLine();
        VehicleType vehicleType;
        try {
            vehicleType = VehicleType.valueOf(vehicleTypeCheck);
        } catch (IllegalArgumentException e) {
            System.out.println("You tried to enter incorrect data. The field will be null");
            vehicleType = null;
        }
        System.out.println("Choose type of fuel: " + FuelType.showAllValues());
        String fuelTypeCheck = scanner.nextLine();
        FuelType fuelType;
        try {
            fuelType = FuelType.valueOf(fuelTypeCheck);
        } catch (IllegalArgumentException e) {
            System.out.println("You tried to enter incorrect data. The field will be null");
            fuelType = null;
        }
        Vehicle vehicle = new Vehicle(name, coordinates, enginePower, capacity, vehicleType, fuelType);
        return vehicle;
    }

    /**
     * Method for updating element by ID
     * @param scanner
     * @param id
     * @return
     */
    public static Vehicle getElementFromConsoleToUpdate(Scanner scanner, int id) {
        Vehicle vehicle = getElementFromConsole(scanner);
        String name = vehicle.getName();
        Coordinates coordinates = vehicle.getCoordinates();
        float enginePower = vehicle.getEnginePower();
        int capacity = vehicle.getCapacity();
        VehicleType vehicleType = vehicle.getVehicleType();
        FuelType fuelType = vehicle.getFuelType();
        Vehicle vehicleToReturn = new Vehicle(id, name, coordinates, enginePower, capacity, vehicleType, fuelType);
        return vehicleToReturn;
    }

    public static int getId(Scanner scanner,String[] task) {
        String idCheck;
        int countCheckToUpdate = 1;
        int id;
        while (true) {
            try {
                if (countCheckToUpdate==1){
                    if (task.length<2) {
                        throw new IndexOutOfBoundsException();
                    }else{
                        id = Integer.parseInt(task[1]);
                        break;
                    }
                }else{
                    System.out.println("Enter an id");
                    idCheck = scanner.nextLine();
                    id = Integer.parseInt(idCheck);
                    break;
                }
            } catch (NumberFormatException e) {
                countCheckToUpdate++;
                System.out.println("Id has to be int and > 0. Try again");
            }catch (IndexOutOfBoundsException e){
                countCheckToUpdate++;
                System.out.println("You forgot to enter an id. Do it");
            }
        }
        return id;
    }

    public static File getFileName(Scanner scanner, String[] task) {
        File filename;
        int countToCheck = 1;
        while(true){
            try {
                if (countToCheck==1){
                    if (task[1].equals("")){
                        throw new IndexOutOfBoundsException();
                    }else{
                        filename = new File(task[1]);
                        break;
                    }
                }else{
                    System.out.println("Enter the filename");
                    String ss = scanner.nextLine();
                    filename = new File(ss);
                    if (ss.isEmpty()) throw new IndexOutOfBoundsException();
                    break;
                }
            }catch (IndexOutOfBoundsException e){
                countToCheck++;
                System.out.println("You didn't enter the filename. Do it");
            }
        }
        return filename;
    }

    public static FuelType getFuelType(Scanner scanner,String[] task) {
        String checkFuelType;
        FuelType fuelType;
        int checkCount = 1;
        while(true){
            try{
                if (checkCount==1){
                    if (task[1].equals("")){
                        throw new IndexOutOfBoundsException();
                    }else{
                        fuelType = FuelType.valueOf(task[1]);
                        break;
                    }
                }else{
                    System.out.println("Enter the FuelType");
                    checkFuelType = scanner.nextLine();
                    fuelType = FuelType.valueOf(checkFuelType);
                    break;
                }
            }catch (IndexOutOfBoundsException e){
                checkCount++;
                System.out.println("You didn't enter the FuelType. Do it");
            }catch (IllegalArgumentException e){
                checkCount++;
                System.out.println("There is no such FuelType");
            }
        }
        return fuelType;
    }
    public static List<Vehicle> readVehiclesFromFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        List<Vehicle> list = new ArrayList<>();
        int id = 0;
        String name = null;
        float x = 0;
        double y = 0;
        LocalDate creationDate = null;
        float enginePower = 0;
        int capacity = 0;
        String vehicleType = null;
        String fuelType = null;
        String line1;
        String line;
        VehicleType vehicleType1 = null;
        FuelType fuelType1 = null;
        String regex1 = "\\w*";
        String regex2 = "-?\\d+?(\\.\\d+?)?";
        String regex3 = "\\d{4}-\\d{2}-\\d{2}\\w\\d{2}:\\d{2}:\\d{2}.\\d*";
        int count = 0;
        int countOfBrokenObjects = 0;
        ArrayList<String> namesOfBrokenObjects = new ArrayList<>();
        while ((line1 = reader.readLine()) != null) {
            line = line1.trim();
            if (!line.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                    && !line.equals("<Vehicles>") && !line.equals("<Vehicle>") && !line.equals("</Vehicles>") && !line.equals("</Vehicle>")) {
                if (count == 1) {
                    final Pattern pattern = Pattern.compile("<Id>(" + regex2 + ")</Id>", Pattern.DOTALL);
                    final Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        try {
                            id = Integer.parseInt(matcher.group(1));
                            if (id>0){
                                count++;
                            }else throw new NumberFormatException();
                        } catch (NumberFormatException e) {
                            count = -1;
                            countOfBrokenObjects++;
                            namesOfBrokenObjects.add(name);
                        }
                    } else {
                        count = -1;
                        countOfBrokenObjects++;
                        namesOfBrokenObjects.add(name);
                    }
                } else if (count == 0) {
                    final Pattern pattern = Pattern.compile("<Name>(" + regex1 + ")</Name>", Pattern.DOTALL);
                    final Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        name = matcher.group(1);
                        count++;
                    } else {
                        count = -1;
                        countOfBrokenObjects++;
                        namesOfBrokenObjects.add(name);
                    }
                } else if (count == 2) {
                    final Pattern pattern = Pattern.compile("<X>(" + regex2 + ")</X>", Pattern.DOTALL);
                    final Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        try {
                            x = Float.parseFloat(matcher.group(1));
                            if (x>-898 && x<=Float.MAX_VALUE){
                                count++;
                            }else throw new NumberFormatException();
                        } catch (NumberFormatException e) {
                            count = -1;
                            countOfBrokenObjects++;
                            namesOfBrokenObjects.add(name);
                        }
                    } else {
                        count = -1;
                        countOfBrokenObjects++;
                        namesOfBrokenObjects.add(name);
                    }
                } else if (count == 3) {
                    final Pattern pattern = Pattern.compile("<Y>(" + regex2 + ")</Y>", Pattern.DOTALL);
                    final Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        try {
                            y = Double.parseDouble(matcher.group(1));
                            if (y>=Double.MIN_VALUE && y<=Double.MAX_VALUE){
                                count++;
                            }else throw new NumberFormatException();
                        } catch (NumberFormatException e) {
                            count = -1;
                            countOfBrokenObjects++;
                            namesOfBrokenObjects.add(name);
                        }
                    } else {
                        count = -1;
                        countOfBrokenObjects++;
                        namesOfBrokenObjects.add(name);
                    }
                } else if (count == 4) {
                    final Pattern pattern = Pattern.compile("<CreationDate>(" + regex3 + ")</CreationDate>", Pattern.DOTALL);
                    final Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        try {
                            creationDate = LocalDate.parse(matcher.group(1));
                            count++;
                        } catch (IllegalArgumentException e) {
                            count = -1;
                            countOfBrokenObjects++;
                            namesOfBrokenObjects.add(name);
                        }
                    } else {
                        count = -1;
                        countOfBrokenObjects++;
                        namesOfBrokenObjects.add(name);
                    }
                } else if (count == 5) {
                    final Pattern pattern = Pattern.compile("<EnginePower>(" + regex2 + ")</EnginePower>", Pattern.DOTALL);
                    final Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        try {
                            enginePower = Float.parseFloat(matcher.group(1));
                            if (enginePower>0 && enginePower<=Float.MAX_VALUE){
                                count++;
                            }else throw new NumberFormatException();
                        } catch (NumberFormatException e) {
                            count = -1;
                            countOfBrokenObjects++;
                            namesOfBrokenObjects.add(name);
                        }
                    } else {
                        count = -1;
                        countOfBrokenObjects++;
                        namesOfBrokenObjects.add(name);
                    }
                } else if (count == 6) {
                    final Pattern pattern = Pattern.compile("<Capacity>(" + regex2 + ")</Capacity>", Pattern.DOTALL);
                    final Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        try {
                            capacity = Integer.parseInt(matcher.group(1));
                            if (capacity>0){
                                count++;
                            }else throw new NumberFormatException();
                        } catch (NumberFormatException e) {
                            count = -1;
                            countOfBrokenObjects++;
                            namesOfBrokenObjects.add(name);
                        }
                    } else {
                        count = -1;
                        countOfBrokenObjects++;
                        namesOfBrokenObjects.add(name);
                    }
                } else if (count == 7) {
                    final Pattern pattern = Pattern.compile("<VehicleType>(" + regex1 + ")</VehicleType>", Pattern.DOTALL);
                    final Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        vehicleType = matcher.group(1);
                        count++;
                        try {
                            if (vehicleType.equals("null")) {
                                vehicleType1 = null;
                            } else {
                                vehicleType1 = VehicleType.valueOf(vehicleType);
                            }
                        } catch (IllegalArgumentException e) {
                            count = -1;
                            countOfBrokenObjects++;
                            namesOfBrokenObjects.add(name);
                        }
                    } else {
                        count = -1;
                        countOfBrokenObjects++;
                        namesOfBrokenObjects.add(name);
                    }
                } else if (count == 8) {
                    final Pattern pattern = Pattern.compile("<FuelType>(" + regex1 + ")</FuelType>", Pattern.DOTALL);
                    final Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        fuelType = matcher.group(1);
                        try {
                            if (fuelType.equals("null")) {
                                fuelType1 = null;
                            } else {
                                fuelType1 = FuelType.valueOf(fuelType);
                            }
                        } catch (IllegalArgumentException e) {
                            count = 0;
                            countOfBrokenObjects++;
                            namesOfBrokenObjects.add(name);
                        }
                    } else {
                        count = -1;
                        countOfBrokenObjects++;
                        namesOfBrokenObjects.add(name);
                    }
                    if (name != null && count != 0) {
                        Vehicle vehicle = new Vehicle(id, name, new Coordinates(x, y), creationDate, enginePower,
                                capacity, vehicleType1, fuelType1);
                        list.add(vehicle);
                        count = 0;
                    }
                }
            } else if (line.equals("<Vehicle>")) {
                count = 0;
            }
        }
        if (countOfBrokenObjects == 0) {
            System.out.println("All vehicles were written");
        } else {
            System.out.println("Amount of incorrect objects: " + countOfBrokenObjects + ". Their names: " + namesOfBrokenObjects);
        }
        return list;
    }
}
