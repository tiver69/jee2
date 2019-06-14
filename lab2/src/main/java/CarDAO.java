import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    private static CarDAO cardao;
    private PreparedStatement preparedStatement;
    private Connection connection = null;

    private String URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&useSSL=true&useJDBCCompliantTimezoneShift=true" +
            "&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String user = "root";
    private String password = ",tkmubz";

    private String sqlInsert = "INSERT INTO test.car (id,brand,model) VALUES (?,?,?);";
    private String sqlUpdate = "UPDATE test.car SET brand=?, model=? WHERE id=?;";
    private String sqlSelectId = "SELECT * FROM test.car WHERE id=?;";
    private String slqSelectBrand = "SELECT * FROM test.car WHERE brand=?;";

    private CarDAO(){
    }

    public static CarDAO getCarDAO(){
        if (cardao == null)
            cardao = new CarDAO();
        return cardao;
    }

    public Connection connect() throws SQLException{
        connection = ConnectionConfiguration.getConnection(URL,user,password);
        return connection;
    }

    public boolean insert(Car car) throws SQLException {
        if (connection == null) this.connect();
        preparedStatement = connection.prepareStatement(sqlInsert);
        preparedStatement.setInt(1, car.getId());
        preparedStatement.setString(2, car.getBrand());
        preparedStatement.setString(3, car.getModel());
        return preparedStatement.execute();
    }

    public int update(int id, Car car) throws SQLException{
        if (connection == null) this.connect();
        preparedStatement = connection.prepareStatement(sqlUpdate);
        preparedStatement.setString(1,car.getBrand());
        preparedStatement.setString(2,car.getModel());
        preparedStatement.setInt(3,id);
        return preparedStatement.executeUpdate();
    }

    public Car selectById(int id) throws SQLException{
        if (connection == null) this.connect();
        preparedStatement = connection.prepareStatement(sqlSelectId);
        preparedStatement.setInt(1,id);
        ResultSet resCar = preparedStatement.executeQuery();
        int resId = 0;
        String resBrand = "", resModel = "";
        while (resCar.next()){
            resId = resCar.getInt("id");
            resBrand = resCar.getString("brand");
            resModel = resCar.getString("model");
        }
        if (resId == 0) return null;
        return new Car(resId,resBrand,resModel);
    }

    public ResultSet selectByBrand(String brand) throws SQLException{
        if (connection == null) this.connect();
        preparedStatement = connection.prepareStatement(slqSelectBrand);
        preparedStatement.setString(1,brand);
        return preparedStatement.executeQuery();
    }

    public int remove(Car car) throws SQLException{
        if (!car.equals(this.selectById(car.getId()))) return 0;
        if (connection == null) this.connect();
        preparedStatement = connection.prepareStatement("DELETE FROM test.car WHERE id=?;");
        preparedStatement.setInt(1,car.getId());
        return preparedStatement.executeUpdate();
    }

    public List<Car> getAll() throws SQLException{
        if (connection == null) this.connect();
        List getCar = new ArrayList<Car>();
        preparedStatement = connection.prepareStatement("SELECT * FROM test.car;");
        ResultSet resCar = preparedStatement.executeQuery();
        while (resCar.next()){
            Car nextCar = new Car();
            nextCar.setId(resCar.getInt("id"));
            nextCar.setBrand(resCar.getString("brand"));
            nextCar.setModel(resCar.getString("model"));
            getCar.add(nextCar);
//            System.out.println(resBrand + " " + resModel + " - " + resId);
        }
        return getCar;
    }

    public void dropTable() throws SQLException{
        if (connection == null) this.connect();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM car");
    }

    public void close(){
        try {
            connection.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }
}
