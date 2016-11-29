package StockMarket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by Abinader on 11/22/16.
 * This class represents the program that will simulate a stock market trading game. Optimally,
 * this will update its data on companies from a free stock trader api online.
 */
public class CS3200FinalProject {
  private String userName;
  private String password;
  private String investor;
  private String nextCommand;
  private String commandArgs;
  private Scanner reader = new Scanner(System.in);
  private Scanner commandArgsReader = new Scanner(System.in);
  private final String serverName = "localhost";
  private final int portNumber = 3306;
  private final String dbName = "StockMarket";
  
  /**
   * Get a new database connection
   *
   * @return Connection
   */
  public Connection getConnection() throws SQLException {
    Connection conn;
    Properties connectionProps = new Properties();
    connectionProps.put("user", this.userName);
    connectionProps.put("password", this.password);
    
    conn = DriverManager.getConnection("jdbc:mysql://"
                    + this.serverName
                    + ":"
                    + this.portNumber
                    + "/" + this.dbName,
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
    Statement stmt = conn.createStatement();
    stmt.executeUpdate(command);
    return true;
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
   * Connect to MySQL and run specified commands based on user input.
   *
   * @throws SQLException If something goes wrong
   */
  public void run() throws SQLException {
    // Connect to MySQL
    Connection conn;
    while (true) {
      try {
        System.out.println("Username: ");
        this.userName = reader.next(); // Scans the next token.
        System.out.println("Password: ");
        this.password = reader.next(); // Scans the next token
        conn = this.getConnection();
        System.out.println("Connected to database");
        break;
      } catch (SQLException e) {
        System.out.println("ERROR: Credentials not verified!\nTry again.\n");
      }
    }
    
    // INSERT VALUES INTO TRADERS TABLE
    try {
      this.executeUpdate(conn, "INSERT INTO Traders VALUES ('Rob', 20, 500);");
      this.executeUpdate(conn, "INSERT INTO Traders VALUES ('George', 2, 15);");
      this.executeUpdate(conn, "INSERT INTO Traders VALUES ('Prannoy', 60, 2000);");
    } catch (SQLException e) {
      // IF VALUES ALREADY EXIST, DO NOTHING BUT CONTINUE
    }
    while (true) {
      // CHOOSE YOUR INVESTOR
      System.out.println("Choose one of the following investors in the database:");
      try {
        ResultSet rs1 = executeQuery(conn, "SELECT Trader_name FROM Traders;");
        List<String> legalNames = new ArrayList<>();
        List<String> displayNames = new ArrayList<>();
        while (rs1.next()) {
          String name = rs1.getString("Trader_name");
          legalNames.add(name.toLowerCase());
          displayNames.add(name);
        }
        System.out.println(displayNames.toString());
        this.investor = reader.next().toLowerCase();
        if (legalNames.contains(this.investor)) {
          System.out.println(this.investor + " selected");
          break;
        } else {
          System.out.println("Investor not found!\n");
        }
      } catch (SQLException e) {
        System.out.println("ERROR: Could not execute the command");
        e.printStackTrace();
      }
    }
    // FINALLY EXECUTE SOME FUNCTIONS/PROCEDURES
    while (true) {
      System.out.println("Input Command:\n");
      this.nextCommand = reader.next().toLowerCase();
      if (this.nextCommand.equals("quit")) {
        this.disconnect(conn);
        break;
      }
      System.out.println("Input Arguments:\n");
      this.commandArgs = commandArgsReader.nextLine();
      // EXECUTE FUNCTIONS WITH NEXT COMMAND

    }
  }
  
  /**
   * Disconnects from the server.
   * @param conn Connection
   */
  private void disconnect(Connection conn) {
    // DISCONNECT FROM THE SERVER WHEN FINISHED
    try {
      conn.close();
      System.out.println("Server quit.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
  


