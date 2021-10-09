import collection.MyCollection;
import data.Coordinates;
import data.FuelType;
import data.Vehicle;
import data.VehicleType;
import org.postgresql.util.PSQLException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DataBaseWorker {
    private String URL;
    private String username;
    private String password;
    private Connection connection;
    private User user;

    public DataBaseWorker(String URL, String username, String password) {
        this.URL = URL;
        this.username = username;
        this.password = password;
    }

    public void connectToDataBase(){
        try{
            connection = DriverManager.getConnection(URL,username,password);
            System.out.println("Connection to database is done");
        }catch (SQLException e){
            System.out.println("Something was wrong with connection to database. Exiting...");
            //e.printStackTrace();
            System.exit(-1);
        }
    }

    public  void add(Vehicle v){
        String request = "insert into \"Vehicles\" VALUES (nextval('id'),?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setString(1,v.getName());
            statement.setDouble(2,v.getCoordinates().getX());
            statement.setDouble(3,v.getCoordinates().getY());
            statement.setFloat(4,v.getEnginePower());
            statement.setInt(5,v.getCapacity());
            statement.setString(6, v.getVehicleType().toString());
            statement.setString(7, v.getFuelType().toString());
            statement.setDate(8, Date.valueOf(v.getCreationDate()));
            statement.setString(9,user.getUsername());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int updateById(Vehicle v) {
        String request  = "update \"Vehicles\" SET name = ?, x = ?, y = ?, enginepower = ?, capacity = ?, vehicletype = ?, fueltype = ? WHERE id = ? and owner=?";
        int out=0;
        try {
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setString(1,v.getName());
            statement.setDouble(2,v.getCoordinates().getX());
            statement.setDouble(3,v.getCoordinates().getY());
            statement.setFloat(4,v.getEnginePower());
            statement.setInt(5,v.getCapacity());
            statement.setString(6, v.getVehicleType().toString());
            statement.setString(7, v.getFuelType().toString());
            statement.setInt(8,v.getId());
            statement.setString(9,user.getUsername());
            out  = statement.executeUpdate();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return out;
    }

    public boolean removeById(Integer id) {
        String request  = "DELETE from \"Vehicles\" WHERE id=? and owner=?";
        try {
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1,id);
            String name = user.getUsername();
            statement.setString(2,name);
            int result = statement.executeUpdate();
            if (result>0){
                return true;
            }
            statement.close();
        } catch (PSQLException e){
            System.out.println("Error");
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void clear() {
        String request  = "DELETE \"Vehicles\" where owner=?";
        try {
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setString(1,user.getUsername());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int removeFirst() {
        String request  = "DELETE from \"Vehicles\" WHERE ctid in(select ctid from \"Vehicles\" where owner=? LIMIT 1)";
        String requestForSelect = "SELECT*from  \"Vehicles\" where owner=? LIMIT 1";
        try {
            PreparedStatement statementForSelect = connection.prepareStatement(requestForSelect);
            statementForSelect.setString(1,user.getUsername());
            ResultSet resultSet = statementForSelect.executeQuery();
            int id=0;
            while (resultSet.next()){
                id = resultSet.getInt(1);
            }
            statementForSelect.close();
            if (id>0){
                PreparedStatement statement = connection.prepareStatement(request);
                statement.setString(1,user.getUsername());
                statement.executeUpdate();
            }
            return id;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public int removeAnyByFuelType(String fueltype) {
        String request  = "DELETE from \"Vehicles\" WHERE ctid in(select ctid from \"Vehicles\" where fueltype=? and owner=? LIMIT 1)";
        try {
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setString(1,fueltype);
            statement.setString(2,user.getUsername());
            ResultSet resultSet = statement.executeQuery();
            int id=0;
            while (resultSet.next()){
                id = resultSet.getInt(1);
            }
            statement.close();
            return id;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public void fillCollection(MyCollection collection) {
        String request  = "SELECT*FROM  \"Vehicles\"";
        try {
            PreparedStatement statement = connection.prepareStatement(request);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                Vehicle v = getVehicle(result);
                collection.add(v);
            }
            statement.close();
            System.out.println("Collection was successfully filled from DB");
        } catch (SQLException throwables) {
            System.out.println("Something went wrong with filling collection from DB");
            System.exit(-1);
        }
    }

    private Vehicle getVehicle(ResultSet result) throws SQLException {
        int id = result.getInt(1);
        String name  = result.getString(2);
        double x = result.getDouble(3);
        double y = result.getDouble(4);
        Coordinates coordinates = new Coordinates((float)x,y);
        float enginePower = result.getFloat(5);
        int capacity  = result.getInt(6);
        VehicleType vehicleType = VehicleType.valueOf(result.getString(7));
        FuelType fuelType = FuelType.valueOf(result.getString(8));
        Date dateTime = result.getDate(9);
        String s = dateTime.toString();
        String[] ss = s.split("-");
        int year = Integer.parseInt(ss[0]);
        int month = Integer.parseInt(ss[1]);
        int day  = Integer.parseInt(ss[2]);
        LocalDate date = LocalDate.of(year,month,day);
        Vehicle v = new Vehicle(id,name,coordinates,date,enginePower,capacity,vehicleType,fuelType);
        return v;
    }

    public String addNewUser(User user) {
        String request  = "SELECT*FROM  \"Users\"";
        this.user = user;
        try {
            PreparedStatement statement = connection.prepareStatement(request);
            ResultSet result = statement.executeQuery();
            ArrayList<String> usernames = new ArrayList<>();
            while (result.next()){
                String name  = result.getString(1);
                String password = result.getString(2);
                usernames.add(name);
            }
            if (usernames.contains(user.getUsername())){
                return "User signed in successfully";
            }else{
                String requestForNewUser = "insert into \"Users\" VALUES (?,?)";
                PreparedStatement statementForNewUser = connection.prepareStatement(requestForNewUser);
                statementForNewUser.setString(1,user.getUsername());
                String passwordMD5 = toMD5(user.getPassword());
                statementForNewUser.setString(2,passwordMD5);
                statementForNewUser.executeUpdate();
                return "User was signed";
            }
        } catch (SQLException throwables) {
            return "Something went wrong with authorization";
        }
    }

    private String toMD5(String password) {
        try{
            MessageDigest md  = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            BigInteger numRepresentation = new BigInteger(1,digest);
            String hashedString  = numRepresentation.toString(16);
            while (hashedString.length()<32){
                hashedString = "0"+hashedString;
            }
            return hashedString;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No such algorithm");
        }
        return null;
    }
}
