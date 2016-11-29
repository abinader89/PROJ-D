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
 * This is the Program that is going to get us an A. I am unsure exactly what it does.
 */
public class CS3200FinalProject {
  private String userName;
  private String password;
  private String investor;
  private String nextCommand;
  private Scanner reader = new Scanner(System.in);
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
   * Connect to MySQL and run specified commands.
   */
  public void run() {
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
        e.printStackTrace();
        System.out.println("ERROR: Credentials not verified!\nTry again.\n");
      }
    }
    try {
      this.executeUpdate(conn, "INSERT INTO Traders VALUES ('Rob', 20, 500);");
      this.executeUpdate(conn, "INSERT INTO Traders VALUES ('George', 2, 15);");
      this.executeUpdate(conn, "INSERT INTO Traders VALUES ('Prannoy', 60, 2000);");
    } catch (SQLException e) {
      // DO NOTHING BUT CONTINUE
    }
    while (true) {
      // Interact with the program
      System.out.println("Choose one of the following investors in the database:");
      try {
        ResultSet rs1 = executeQuery(conn, "SELECT Trader_name FROM Traders;");
        // SHOW THE RESULTS
        List<String> legalNames = new ArrayList<>();
        while (rs1.next()) {
          String name = rs1.getString("Trader_name");
          legalNames.add(name);
        }
        System.out.println(legalNames.toString());
        this.investor = reader.next();
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
      this.nextCommand = reader.next();
        // EXECUTE FUNCTIONS WITH NEXT COMMAND
      break;
      }
    try {
      conn.close();
      System.out.println("Connection closed.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
  


