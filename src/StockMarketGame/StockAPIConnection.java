package StockMarketGame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by robertcarney on 12/1/16.
 */
public class StockAPIConnection {

  public static String getText(String url) throws Exception {
    URL website = new URL(url);
    URLConnection connection = website.openConnection();
    BufferedReader in = new BufferedReader(
            new InputStreamReader(
                    connection.getInputStream()));

    StringBuilder response = new StringBuilder();
    String inputLine;

    while ((inputLine = in.readLine()) != null)
      response.append(inputLine);

    in.close();

    return response.toString();
  }


  public static String cleanURLJson(String urlText)  {
    return urlText.substring(4, urlText.length() - 1);
  }

  public static ArrayList<StockPrice> stockFromJSON(String jsonInfo)  {
    String company = "";
    String price = "";
    ArrayList<StockPrice> stockPrices = new ArrayList<StockPrice>();
    try  {
      for (int i = 0; i < jsonInfo.length() - 3; i++)  {
        if (jsonInfo.substring(i, i + 3).equals("\"t\""))  {
          for (int j = i + 7; j < i + 20; j++)  {
            if (jsonInfo.substring(j, j+1).equals("\""))  {
              break;
            }
            else  {
              company += jsonInfo.substring(j, j + 1);
            }
          }
        }
        if (jsonInfo.substring(i, i + 3).equals("\"l\""))  {
          for (int j = i + 7; j < i + 20; j++)  {
            if (jsonInfo.substring(j, j+1).equals("\""))  {
              StockPrice current = new StockPrice(company, price);
              stockPrices.add(current);
              company = "";
              price = "";
              break;
            }
            else if (!jsonInfo.substring(j, j + 1).equals(",")) {
              price += jsonInfo.substring(j, j + 1);
            }
            else  {
              continue;
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return stockPrices;
  }

}
