package StockMarketGame;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
  
  /**
   * Constructor for a StockMarket.
   */
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
        System.out.println("Invalid credentials!\nTry again.\n");
      }
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
  
  /**
   * This method will update the database.
   */
  void priceUpdate()  {
    try {
      String stockInfo = cleanURLJson(StockAPIConnection.getText(stockURL));
      ArrayList<StockPrice> newPrices = StockAPIConnection.stockFromJSON(stockInfo);
      for (StockPrice s : newPrices)  {
        String command = "CALL update_stock(" + "'" + s.company + "', " + s.price + ")";
        this.executeUpdate(this.conn, command);
      }
      System.out.println("Update successful!");
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
          this.userStart();
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
  
  /**
   * This method will be called when selecting the user option or after finishing adding a firm
   * to the database.
   */
  public void userStart()  {
    boolean keepGoing = true;
    System.out.println("Please Specify Trader:\n");
    while (keepGoing)  {
      String traderSelected = sc.next();
      this.isExit(traderSelected);
      try {
        ResultSet rs = executeQuery(conn, "SELECT Trader_name FROM Traders WHERE Trader_Name = "
                + "'" + traderSelected + "';");
        if (!rs.next()) {
          System.out.println("Investor not found!\nPlease Specify Trader:\n");
        } else {
          this.characterName = traderSelected;
          System.out.println(traderSelected + " selected");
          
        }
      } catch (SQLException e) {
        System.out.println("ERROR: Could not execute the command");
        e.printStackTrace();
      }
      if (this.characterName != null) {
        this.traderCommands();
      }
      keepGoing = true;
    }
  }
  
  /**
   * A trader is selected. Now operate commands.
   */
  private void traderCommands() {
    boolean keepGoing = true;
    while (keepGoing) {
      System.out.println("\nWhat would you like to do?");
      String nextCommand = sc.next();
      this.isExit(nextCommand.toLowerCase());
      switch (nextCommand) {
        case "bu":
          this.buyStock();
          break;
        case "se":
          this.sellStock();
          // CALL PROCEDURE TO SELL STOCK SPECIFIED AMOUNT OF STOCKS
          break;
        case "in":
          this.checkInventory();
          break;
        case "st":
          // CHECK THE STANDINGS
          this.standings();
          // CALL THE PROCEDURE TO CHECK THE STANDINGS
          break;
        case "pr":
          this.displayCurrentStockInformation();
          break;
        case "lo":
          this.characterName = null;
          this.run();
          break;
        case "ab":
          this.checkBalance();
          break;
        case "help":
          System.out.println("Available commands are:\n[bu] - Buy available stocks.\n[se] - Sell" +
                  " stocks.\n[st] - Check the standings of the traders within the league.\n" +
                  "[in] - Check current inventory of stocks.\n[pr] - Check the current rates " +
                  "of stocks available.\n[lo] - Logs you out of program.\n[ab] - Checks the " +
                  "available balance of your trader.\n[help] - Displays this information.");
          break;
        default:
          System.out.println("Invalid input!\nPlease use the [help] command for a list of " +
                  "available commands.\n");
      }
    }
  }
  
  /**
   * Checks the balance of your trader.
   */
  private void checkBalance() {
    // TODO
  }
  
  /**
   * Displays the standings within the current trader's league.
   */
  private void standings() {
    // TODO
  }
  
  /**
   * Sells stock if the trader has enough of it in inventory.
   */
  private void sellStock() {
    // TODO
  }
  
  /**
   * Buys stock if the trader has enough capital.
   */
  private void buyStock() {
    String currentFundsQuery = "SELECT available_funds FROM traders WHERE trader_name = '"
            + this.characterName + "';";
    try {
      ResultSet rs0 = this.executeQuery(this.conn, currentFundsQuery);
      if (rs0.next()) {
        double funds = (Double) rs0.getObject("available_funds");
        System.out.println("Input company ID & Quantity\nCompany?");
        String company = sc.next();
        System.out.println("Quantity?");
        int qty = sc.nextInt();
        String priceQuery = "SELECT PRICE FROM stock_prices where company = '" + company + "';";
        ResultSet rs1 = this.executeQuery(this.conn, priceQuery);
        if (!rs1.next()) {
          System.out.println("No company found!");
        } else {
          double price = (Double) rs1.getObject("price");
          System.out.println("Funds available: $" + funds + " ...total price $" + price * qty);
          if (funds >= (price * qty)) {
            for (int i = 0; i < qty; i++) {
//                executeUpdate(this.conn, BUY THE STOCK)
            }
            System.out.println(qty + " stock(s) purchased from " + company.toUpperCase());
            System.out.println("Operation successful.");
            System.out.println("Remaining balance ...$" + (funds - (price * qty)) );
          } else {
            System.out.println("Insufficient funds for transaction.");
          }
        }
      }
    } catch (Exception e) {
      System.out.println("Something went wrong!");
    }
  }
  
  /**
   * This displays information about current stock prices.
   */
  private void displayCurrentStockInformation() {
    String viewStockInfo = "SELECT * FROM Stock_Prices;";
    ResultSet rs = null;
    try {
      rs = this.executeQuery(this.conn, viewStockInfo);
      ResultSetMetaData metadata = rs.getMetaData();
      System.out.println(metadata.getColumnName(1) + "  |  " + metadata.getColumnName(3));
      System.out.println("---------|---------");
      
      StringBuilder sb = new StringBuilder();
      while (rs.next()) {
        sb.append(String.format("%4s", rs.getString(1)));
        sb.append("     |  $" + rs.getString(3) + "\n");
      }
      System.out.print(sb.toString());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Checks the inventory of this trader.
   */
  private void checkInventory() {
    // TODO
  }
  
  /**
   * Access the admin commands.
   */
  public void adminStart()  {
    boolean keepGoing = true;
    while (keepGoing)  {
      keepGoing = false;
      System.out.println("\nInput command to execute.");
      String traderSelected = sc.next().toLowerCase();
      this.isExit(traderSelected);
      switch (traderSelected)  {
        case "ne":
          this.createLeague();
          break;
        case "re":
          this.resetTraders();
          break;
        case "up":
          this.priceUpdate();
          keepGoing = true;
          break;
        case "de":
          try {
            if (this.deleteTrader()) {
              System.out.println("Trader " + this.characterName + " deleted.");
            }
          } catch (SQLException e) {
            System.out.println("Trader not found!");
          }
          keepGoing = true;
          break;
        case "pl":
          this.userStart();
          break;
        case "vl":
          this.viewLeagues();
          keepGoing = true;
          break;
        case "vt":
          this.viewTraders();
          keepGoing = true;
          break;
        case "pr":
          this.displayCurrentStockInformation();
          keepGoing = true;
          break;
        case "help":
          System.out.println("Available commands are:\n[ne] - Creates a new league, enter done " +
                  "when finished specifying new traders to add to this firm.\n[up] - Updates" +
                  " the database with the most recent StockMarket values.\n[re] - Resets all the " +
                  "information the program has for the traders.\n[pl] - Play the stock market " +
                  "game.\n[de] - Deletes a trader from the database.\n[vl] - Show the " +
                  "leagues in the database.\n[vt] - Shows the traders in the database.\n[pr] - " +
                  "Check the current rates of stocks available.\n[help] - Displays this" +
                  " information.");
          keepGoing = true;
          break;
        default:
          System.out.print("Invalid input!\nPlease use the [help] command for a list of " +
                  "available commands.\n");
          keepGoing = true;
      }
    }
  }
  
  /**
   * Displays all of the traders in the database.
   */
  private void viewTraders() {
    String viewLeaguesQuery = "SELECT trader_name, team FROM TRADERS ORDER BY team;";
    ResultSet rs = null;
    try {
      rs = this.executeQuery(this.conn, viewLeaguesQuery);
      System.out.println("* Current Traders *");
      
      StringBuilder sb = new StringBuilder();
      while (rs.next()) {
        sb.append(String.format("%4s", rs.getString(1)));
        sb.append(" - " + rs.getString(2) + "\n");
      }
      System.out.print(sb.toString());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Displays all of the leagues in the database.
   */
  private void viewLeagues() {
    String viewLeaguesQuery = "SELECT DISTINCT(team) FROM TRADERS;";
    ResultSet rs = null;
    try {
      rs = this.executeQuery(this.conn, viewLeaguesQuery);
      System.out.println("* Current Leagues *");
      
      StringBuilder sb = new StringBuilder();
      while (rs.next()) {
        sb.append(rs.getString(1));
      }
      System.out.print(sb.toString());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Deletes a trader from the database.
   * @return boolean
   * @throws SQLException
   */
  private boolean deleteTrader() throws SQLException {
    System.out.println("Delete which trader?");
    this.characterName = sc.next();
    String delete = "DELETE FROM TRADERS WHERE trader_name = " + "'" + this.characterName + "';";
    return this.executeUpdate(this.conn, delete);
  }
  
  /**
   * This method will reset the traders table.
   */
  private void resetTraders() {
    try {
      String resetTradersTable = "DELETE FROM Traders;";
      this.executeUpdate(this.conn, resetTradersTable);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    System.out.println("Traders data reset!");
    this.adminStart();
  }
  
  /**
   * This method will create a league.
   */
  public void createLeague() {
    boolean keepGoing = true;
    System.out.println("League name?");
    String leagueName = sc.next();
    while (keepGoing) {
      System.out.println("Confirm league name " + leagueName + "?\n[y|n]");
      switch (sc.next().toLowerCase()) {
        case "y":
          keepGoing = false;
          break;
        case "n":
          System.out.println("Input new league name:\n");
          leagueName = sc.next();
          break;
        case "exit":
          this.isExit("exit");
        default:
          System.out.println("Invalid input!");
      }
    }
    List<String> players = new ArrayList<String>();
    keepGoing = true;
    this.addTraders(players);
    try {
      StringBuilder insertTraders = new StringBuilder("INSERT INTO Traders VALUES("
              + "'" + players.get(0) + "', 5000," + "'" + leagueName + "')");
      if (players.size() == 1) {
        insertTraders.append(";");
      } else {
        for (int ii = 1; ii < players.size(); ii++) {
          insertTraders.append(", (" + "'" + players.get(ii) + "', 5000," + "'" + leagueName +
                  "')");
          if (ii == players.size() - 1) {
            insertTraders.append(";");
          }
        }
      }
      System.out.println("Attempting to insert into: " + leagueName + "\nTrader(s):\n");
      for (String p : players) {
        System.out.println(p);
      }
      this.executeUpdate(this.conn, insertTraders.toString());
    } catch (SQLException e) {
      System.out.println("Process failed, aborted!");
    }
    this.adminStart();
  }
  
  /**
   * Adds traders to the param list.
   * @param players List[String]
   */
  private void addTraders(List<String> players) {
    boolean keepGoing = true;
    String nextCommand;
    System.out.println("First trader name?");
    String traderName = sc.next();
    while (keepGoing) {
      keepGoing = false;
      if (traderName != null) {
        System.out.println("Confirm adding " + traderName + "?\n[y|n]");
      }
      nextCommand = sc.next();
      switch (nextCommand.toLowerCase()) {
        case "y":
          keepGoing = true;
          players.add(traderName);
          System.out.println("Next trader or [done]?");
          traderName = sc.next();
          if (traderName.toLowerCase().equals("done")) {
            keepGoing = false;
          }
          break;
        case "n":
          System.out.println("Input new trader name:");
          traderName = sc.next();
          if (traderName.toLowerCase().equals("done")) {
            keepGoing = false;
          } else {
            keepGoing = true;
          }
          break;
        case "exit":
          this.isExit("exit");
          break;
        default:
          System.out.println("Invalid input!\ninput 'done' when finished adding traders.");
      }
    }
  }
  
  /**
   * Disconnects from the server.
   * @param message String
   */
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


