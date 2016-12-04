package StockMarketGame;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static StockMarketGame.StockAPIConnection.cleanURLJson;

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
  
  Readable r = new InputStreamReader(System.in);
  Scanner sc = new Scanner(r);
  
  // The name of the MySQL account to use (or empty for anonymous)
  private String userName;
  
  // The password for the MySQL account (or empty for anonymous)
  private String password;
  
  // Name of character, dependent on user input
  private String characterName;
  
  // The name of the computer running MySQL
  private final String serverName = "localhost";
  
  // The port of the MySQL server (default is 3306)
  private final int portNumber = 3306;
  
  // The name of the database we are testing with (this default is installed with MySQL)
  private final String dbName = "StockMarket";
  
  private Connection conn;
  
  private String stockURL = "https://www.google.com/finance/info?q=NSE:BABA,CVS,BMY,MRK,XOM,TGT," +
          "DOW,TWX,TSN,CBS,APA,MSFT,V,WMT,PG,CRM,COI,NVS,PRU,AAPL,GOOG,AMZN,C";
  
  public StockMarket()  {
    boolean keepGoing = true;
    while (keepGoing) {
      try {
        System.out.println("What is your username?");
        this.userName = sc.next();
        System.out.println("What is your password?");
        this.password = sc.next();
        this.conn = this.getConnection();
        keepGoing = false;
      } catch (Exception e) {
        // HANDLE
      }
    }
  }
  
  /**
   * Enum representing the permissions granted to the current user.
   */
  private enum Permissions {
    USER("user"), ADMIN("admin");
    
    private String stringRep;
    
    Permissions(String str) {
      this.stringRep = str;
    }
    
    public String toString() {
      return this.stringRep;
    }
  }
  /**
   * Get a new database connection
   *
   * @return Connection
   * @throws SQLException
   */
  public Connection getConnection() throws SQLException {
    Connection conn;
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
   * Run a SQL Query:
   * SELECT
   *
   * @throws SQLException If something goes wrong
   */
  public ResultSet executeQuery(Connection conn, String command) throws SQLException {
    Statement stmt = conn.createStatement();
    return stmt.executeQuery(command);
  }
  
  void priceUpdate()  {
    try {
      String stockInfo = cleanURLJson(StockAPIConnection.getText(stockURL));
      ArrayList<StockPrice> newPrices = StockAPIConnection.stockFromJSON(stockInfo);
      for (StockPrice s : newPrices)  {
        String command = "CALL update_stock(" + "'" + s.company + "', " + s.price + ")";
        this.executeUpdate(this.conn, command);
      }
    }
    catch (Exception e)  {
      System.out.println("Update failed");
    }
  }
  
  
  /**
   * Connect to MySQL and run specified commands.
   */
  public void run() {
    boolean keepGoing = true;
    while (keepGoing) {
      System.out.println("Welcome to StockMarketGame. User or Admin?");
      switch (sc.next().toLowerCase())  {
        case "user":
//          this.userStart();
          keepGoing = false;
          break;
        case "admin":
          this.adminStart();
          keepGoing = false;
          break;
        case "exit":
          return;
        default:
          System.out.println("Please choose either user or admin, or type 'exit' to leave.");
      }
    }
  }
  
//  public void userStart()  {
//    boolean keepGoing = true;
//    while (keepGoing)  {
//      keepGoing = false;
//      System.out.println("Trader?");
//      String traderSelected = sc.next().toLowerCase();
//      this.isExit(traderSelected);
//      switch (traderSelected)  {
//        case "new":
//          this.createLeague();
//          break;
//        case "existing":
//          this.existingLeague();
//          break;
//        default:
//          System.out.println("Trader not in database!");
//          keepGoing = true;
//      }
//    }
//  }
  
  public void adminStart()  {
    boolean keepGoing = true;
    while (keepGoing)  {
      keepGoing = false;
      System.out.println("Command?");
      String traderSelected = sc.next().toLowerCase();
      this.isExit(traderSelected);
      switch (traderSelected)  {
        case "new":
          this.createTeam();
          break;
        case "existing":
//          this.existingLeague();
          break;
        default:
          keepGoing = true;
          System.out.println("\n Invalid input" + "\nPlease choose one of:" + "\nupdate, new, " +
                  "delete.");
      }
    }
  }
  
  public void createTeam() {
    System.out.println("Firm name?");
    String name = sc.next();
    List<String> players = new ArrayList<String>();
    System.out.println("First trader name?");
    players.add(sc.next());
    boolean keepGoing = true;
    while (true) {
      System.out.println("Next trader name?");
      String newName = sc.next();
      if (newName.toLowerCase().equals("done")) {
        break;
      }
      players.add(newName);
    }
    // ADD A LEAGUE TO THE DATABASE
    try {
      StringBuilder insertLeague = new StringBuilder("INSERT INTO Team VALUES("
              + "'" + name + "'" + ", " + "'" + players.get(0) + "'" + ")");
      for (int ii = 1; ii < players.size(); ii++) {
        insertLeague.append(", (" + "'" + name + "'" +  ", " + "'" + players.get(ii) + "'" + ")");
        if (ii == players.size() - 1) {
          insertLeague.append(";");
        }
      }
      System.out.println(insertLeague.toString());
      this.executeUpdate(this.conn, insertLeague.toString());
    } catch (SQLException e) {
      e.printStackTrace();
    }
      System.out.println("Traders added");
      System.out.println("Enter 'return' to select a league or enter any other key to exit.");
      String response = sc.next();
      if (response.equalsIgnoreCase("return")) {
//        this.userStart();
        return;
      } else {
        System.out.println("Thanks for playing!");
        return;
      }
    }
  
//  public void existingLeague() {
//    String traderName;
//    System.out.println("What is your trader name?");
//    traderName = sc.next();
//    isExit(traderName);
//    System.out.println("Get stats or make a trade?");
//    String action = sc.next();
//    boolean keepGoing = true;
//    while (keepGoing) {
//      switch (action) {
//        case "stats":
////          this.checkStats(traderName);
//        case "trade":
////          this.trade(traderName);
//        case "logout":
//          keepGoing = false;
//          this.userStart();
//        default: // REPEAT
//      }
//    }
//  }
  
  static void isExit(String message)  {
    if (message.equalsIgnoreCase("exit"))  {
      System.out.println("Goodbye");
      System.exit(0);
    }
  }
  
  /**
   * Connect to the Database and execute commands.
   */
  public static void main(String[] args) {
    StockMarket app = new StockMarket();
    app.run();
  }
}


