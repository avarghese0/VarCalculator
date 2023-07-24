package main;

import dto.ExcelDataDto;
import dto.Stock;
import utils.ExcelUtils;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //Reading the data file
        String filePath = "src/main/resources/VARCalculator.xlsx";
        ExcelDataDto data = ExcelUtils.excelReader(filePath);

        //Total Investment Amount
        double totalInvestmentAmount = Double.valueOf(data.getConfigParams().get("Investment"));

        //Load confidence level and calculate alpha
        double confidenceLevel = Double.valueOf(data.getConfigParams().get("Confidence"));
        double alpha = 100 - confidenceLevel;

        List<Stock> portfolioStocks = new ArrayList<>();

        //Build stock dto objects and calculate daily return investment in percent and in dollars
        for (String stockName : data.getStockData().keySet()) {

            List<Double> stockValue = Utils.strToDouble((data.getStockData().get(stockName)));

            Double stockInvestment = Utils.getStockInvestment(data.getConfigParams(), totalInvestmentAmount, stockName);

            Stock stock = new Stock.StockBuilder()
                    .stockName(stockName)
                    .stockValues(stockValue)
                    .investAmount(stockInvestment)
                    .build();

            portfolioStocks.add(stock);
        }

        //Add stock values across indices based on date to generate portfolio
        List<Double> portfolios =  Utils.calculatePortfolioValues(portfolioStocks);

        //Calculate the Value at risk using alpha, index and portfolios
        double valueAtRisk = Utils.calculateVaR(portfolios, alpha);

        System.out.println("VaR value " + valueAtRisk);

        //Write results back to file in the results tab
        ExcelUtils.stockExcelWriter(filePath, portfolioStocks, portfolios, alpha, valueAtRisk);
    }


}