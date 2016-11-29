package StockMarket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by Abinader on 11/22/16.
 */
public class CS3200FinalProject {
  // AT THE END OF A BUY/SELL THIS NUMBER IS INCREMENTED
  private int dayIncrementer = 0;
  private String userName;
  private String password;
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
      // EXAMPLE UPDATE (INSERT)
      this.executeUpdate(conn, "INSERT INTO Traders VALUES ('Rob', 20, 500);");
      this.executeUpdate(conn, "INSERT INTO Traders VALUES ('George', 2, 15);");
      this.executeUpdate(conn, "INSERT INTO Traders VALUES ('Prannoy', 60, 2000);");
    } catch (SQLException e0) {
      // DO NOTHING
    }
    // Interact with the program
    System.out.println("CHOOSE ONE OF THE FOLLOWING: \n ");
    try {
      ResultSet rs1 = executeQuery(conn, "SELECT Trader_Name FROM Traders;");
      // SHOW THE RESULTS
      ResultSetMetaData rsmd = rs1.getMetaData();
      int columnsNumber = rsmd.getColumnCount();
      while (rs1.next()) {
        for (int i = 1; i <= columnsNumber; i++) {
          if (i > 1) System.out.print(", ");
          String columnValue = rs1.getString(i);
          System.out.print(rsmd.getColumnName(i) + " " + columnValue);
        }
        System.out.println("");
      }
    } catch (SQLException e1) {
      System.out.println("ERROR: Could not execute the command");
    }
  
    try {
      conn.close();
      System.out.println("Connection closed.");
    } catch (SQLException e1) {
      e1.printStackTrace();
    }
  }
}
      
    
  


