package utils;

import dto.Stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

/**
 * @author avarghese
 * Util class contains all calculation logic methods.
 */
public class Utils {


    public static Double calculateVaR(List<Double> portfolio, Double alpha) {
        Collections.sort(portfolio);
        int index = (int) Math.ceil((alpha * portfolio.size()) / 100);
        Double dollarValueAtRisk = portfolio.get(index);
        return dollarValueAtRisk;
    }

    public static List<Double> dailyReturnInPercent(List<Double> values) {
        int decimalPlaces = 4;
        DecimalFormat df = new DecimalFormat("#." + "0".repeat(decimalPlaces));

        List<Double> dailyReturnInpercent = new ArrayList<>();
        double currentPercent;

        for (int i = 1; i < values.size(); i++) {
            currentPercent = (values.get(i) - values.get(i - 1)) / values.get(i - 1);
            String roundedValue = df.format(currentPercent);
            double roundedDouble = Double.parseDouble(roundedValue);

            dailyReturnInpercent.add(roundedDouble);
        }
        return dailyReturnInpercent;
    }


    public static List<Double> calcuateDailyReturnValueInDollar(List<Double> dailyReturnValue, double investAmount) {
        //calculate the daily return in dollar amounts
        return dailyReturnValue.stream().map(percentValue -> percentValue * investAmount).collect(Collectors.toList());
//        return null;
    }

    public static List<Double> calculatePortfolioValues(List<Stock> stocks) {
        List<Double> result = new ArrayList<>();

        if(stocks.size() > 0) {
        int size = stocks.get(0).getDailyReturnInDollars().size();


            for (int i = 0; i < size; i++) {
                double sum = 0;
                for (Stock stock : stocks) {
                    sum += stock.getDailyReturnInDollars().get(i);
                }
                result.add(sum);
            }

        }
        return result;
    }

    public static List<Double> strToDouble(List<String> listOfStrings) {
        // Convert the list of strings to a list of doubles
        List<Double> listOfDoubles = new ArrayList<>();
        for (String str : listOfStrings) {
            try {
                if(str != null) {
                    double value = Double.parseDouble(str);
                    listOfDoubles.add(value);
                }
            } catch (NumberFormatException e) {
                // Handle parsing errors if necessary
                System.err.println("Error parsing string as double: " + str);
            }
        }
        return listOfDoubles;
    }

    public static Double getStockInvestment(Map<String, String> configParameters, Double investment, String stockName ) {
        Double stockInvestment = 0.0;
        try {
            stockInvestment = (Double.valueOf(configParameters.get("Investment_" + stockName)) / 100) * investment;
        } catch(Exception e) {
            return stockInvestment;
        }
        return stockInvestment;
    }


}