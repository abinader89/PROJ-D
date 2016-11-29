/**
 * Created by robertcarney on 11/12/16.
 */
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * This class demonstrates how to connect to MySQL and run some basic commands.
 *
 * In order to use this, you have to download the Connector/J driver and add
 * its .jar file to your build path.  You can find it here:
 *
 * http://dev.mysql.com/downloads/connector/j/
 *
 * You will see the following exception if it's not in your class path:
 *
 * java.sql.SQLException: No suitable driver found for jdbc:mysql://localhost:3306/
 *
 * To add it to your class path:
 * 1. Right click on your project
 * 2. Go to Build Path -> Add External Archives...
 * 3. Select the file mysql-connector-java-5.1.24-bin.jar
 *    NOTE: If you have a different version of the .jar file, the name may be
 *    a little different.
 *
 * The user name and password are both "root", which should be correct if you followed
 * the advice in the MySQL tutorial. If you want to use different credentials, you can
 * change them below.
 *
 * You will get the following exception if the credentials are wrong:
 *
 * java.sql.SQLException: Access denied for user 'userName'@'localhost' (using password: YES)
 *
 * You will instead get the following exception if MySQL isn't installed, isn't
 * running, or if your serverName or portNumber are wrong:
 *
 * java.net.ConnectException: Connection refused
 */
public class StockMarket {

  /** The name of the MySQL account to use (or empty for anonymous) */
  private String userName;

  /** The password for the MySQL account (or empty for anonymous) */
  private String password;

  /** Name of character, dependent on user input */
  private String characterName;

  /** The name of the computer running MySQL */
  private final String serverName = "localhost";

  /** The port of the MySQL server (default is 3306) */
  private final int portNumber = 3306;

  /** The name of the database we are testing with (this default is installed with MySQL) */
  private final String dbName = "StockMarket";

  private Connection conn;

  public StockMarket()  {
    try  {
      Readable r = new InputStreamReader(System.in);
      Scanner sc = new Scanner(r);
      boolean keepGoing = true;
      System.out.println("What is your username?");
      this.userName = sc.next();
      System.out.println("What is your password?");
      this.password = sc.next();
      this.conn = this.getConnection();
    } catch (Exception e)  {

    }
  }

  /**
   * Get a new database connection
   *
   * @return
   * @throws SQLException
   */
  public Connection getConnection() throws SQLException {
    Connection conn = null;
    Properties connectionProps = new Properties();
    connectionProps.put("user", this.userName);
    connectionProps.put("password", this.password);
    conn = DriverManager.getConnection("jdbc:mysql://"
                    + this.serverName + ":" +
                    this.portNumber + "/" + this.dbName,
            connectionProps);
    return conn;
  }

  /**
   * Run a SQL command which does not return a recordset:
   * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
   *
   * @throws SQLException If something goes wrong
   */
  public boolean executeUpdate(Connection conn, String command) throws SQLException {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      stmt.executeUpdate(command); // This will throw a SQLException if it fails
      return true;
    } finally {

      // This will run whether we throw an exception or not
      if (stmt != null) { stmt.close(); }
    }
  }

  /**
   * Connect to MySQL and run specified commands.
   */
  public void run() {
    Readable r = new InputStreamReader(System.in);
    Scanner sc = new Scanner(r);
    boolean keepGoing = true;
    while (keepGoing) {
      System.out.println("Welcome to StockMarket. User or Admin?");
      switch (sc.next().toLowerCase())  {
        case "user":
          this.userStart();
          keepGoing = false;
          break;
        case "admin":

          keepGoing = false;
          break;
        case "exit":
          return;
        default:
          System.out.println("Please choose either user or admin, or type 'exit' to leave.");
      }
    }
  }

  public void userStart()  {
    Readable r = new InputStreamReader(System.in);
    Scanner sc = new Scanner(r);
    boolean keepGoing = true;
    while (keepGoing)  {
      keepGoing = false;
      System.out.println("Create a new league or use existing?");
      String response = sc.next().toLowerCase();
      switch (response)  {
        case "new":
          this.createLeague();
          break;
        case "existing":

          return;
        case "exit":
          return;
        default:
          keepGoing = true;
      }
      System.out.println("Please choose either an existing or new league, " +
              "or type 'exit' to leave.");
    }

  }

  public void createLeague()  {

  }


  /**
   * Run a SQL Query:
   * SELECT
   *
   * @throws SQLException If something goes wrong
   */
  public ResultSet executeQuery(Connection conn, String command) throws SQLException {
    Statement stmt = conn.createStatement();
    return stmt.executeQuery(command);
  }

  /**
   * Connect to the DB and do some stuff
   */
  public static void main(String[] args) {
    StockMarket app = new StockMarket();
    app.run();
  }
}
