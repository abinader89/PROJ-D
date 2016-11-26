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
  private String userName;
  private String password;
  private Scanner reader = new Scanner(System.in);
  private final String serverName = "localhost";
  private final int portNumber = 3306;
  private final String dbName = "StockMarket";
  
  /**
   * Get a new database connection
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
      // EXAMPLE UPDATE
      if (executeUpdate(conn, "INSERT INTO Company VALUES ('Google', 'John " +
              "Buschman'," +
              "600)")) {
        System.out.println("Update successful");
      }
      
      // EXAMPLE QUERY
      ResultSet rs1 = executeQuery(conn, "SELECT * FROM Company;");
      
      // RESET THE DATABASE
      if (executeUpdate(conn, "DELETE FROM Company WHERE Company_name = 'Google';")) {
        System.out.println("Update successful");
      }
      
      // SHOW THE RESULTS
      ResultSetMetaData rsmd = rs1.getMetaData();
      int columnsNumber = rsmd.getColumnCount();
      while (rs1.next()) {
        for (int i = 2; i <= columnsNumber; i++) {
          if (i > 2) System.out.print(",  ");
          String columnValue = rs1.getString(i);
          System.out.print(rsmd.getColumnName(i) + " " + columnValue);
        }
        System.out.println("");
      }
    } catch (SQLException e){
      System.out.println("ERROR: Could not execute the command");
      e.printStackTrace();
    }
    try {
      conn.close();
      System.out.println("Connection closed.");
    } catch (SQLException e1) {
      e1.printStackTrace();
    }
  }
}

