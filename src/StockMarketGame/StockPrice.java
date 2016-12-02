package StockMarketGame;

/**
 * Created by robertcarney on 12/1/16.
 */
public class StockPrice  {

  public String company;
  public double price;

  public StockPrice(String company, String price) {
    this.company = company;
    this.price = Double.parseDouble(price);
  }

  public String toString()  {
    return  "Company : " + this.company + ", Price : " + this.price;
  }

}
