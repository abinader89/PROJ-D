import java.sql.SQLException;

import StockMarket.CS3200FinalProject;

/**
 * Created by Abinader on 11/24/16.
 */
public class MainRunner {
  public static void main(String[] args) {
    CS3200FinalProject app = new CS3200FinalProject();
    try {
      app.run();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}