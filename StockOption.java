import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StockOption {
    private Stock addNewStock;

    public List<Stock> readStockList(String path) {
        List<Stock> stockList = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            for (String line; (line = bufferedReader.readLine()) != null; ) {
                Stock stock = new Stock();
                String[] arrayLine = line.split("\t");
                /*for (int i = 0; i < arrayLine.length; i++) {
                    if (arrayLine[i].length() > 15) {
                        arrayLine[i] = arrayLine[i].substring(0, 12) + "...";
                    }
                }*/
                stock.setCode(arrayLine[0]);
                stock.setProductName(arrayLine[1]);
                stock.setMarket(MarketType.valueOf(arrayLine[2]));
                stock.setSharesIssued(Long.parseLong(arrayLine[3]));
                stockList.add(stock);
            }
        } catch (IOException ioException) {
            System.out.println("Can't find the file.");
        }
        return stockList;
    }

    public void showStockList(List<Stock> stockList) {
        System.out.printf("%-61s\n", "|===================================================|");
        System.out.printf("| %-5s| %-16s| %-9s|%15s|\n","Code","Product Name","Market","Shares Issued");
        System.out.printf("%-61s\n","|------+-----------------+----------+---------------|");
        for (Stock stockInfo : stockList) {
            String name = stockInfo.getProductName().length()<=16? stockInfo.getProductName() :stockInfo.getProductName().substring(0,12)+"...";
            String sharesIssued = String.format("%,d", stockInfo.getSharesIssued());
            sharesIssued = sharesIssued.length()<=15? sharesIssued : sharesIssued.substring(0,12)+"...";
            System.out.printf("| %-5s| %-16s| %-9s|%15s|", stockInfo.getCode(),
                    name, stockInfo.getMarket(), sharesIssued);
            System.out.println();
        }
        System.out.printf("%-61s\n","|===================================================|");
    }
    public boolean isExist(String newCode) {

        Path path1 = Paths.get("D:\\java project\\Learning\\new\\src\\stock.csv");
        try (BufferedReader reader = Files.newBufferedReader(path1, StandardCharsets.UTF_8)) {
            String oldCode;

            for (String line; (line = reader.readLine()) != null; ) {
                oldCode = line.split("\t")[0];
                if (newCode.equals(oldCode)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            System.out.println("err");
        }
        return false;
    }
    public void addStock(){
        Scanner scr = new Scanner(System.in);
        Stock addNewStock = new Stock();
        System.out.println("please enter the name:");
        while (true) {
            String newProductName = scr.nextLine();
            if (newProductName.isBlank()) {
                System.out.println("plz enter");
                continue;
            } else if (!newProductName.matches("^[a-zA-Z].*")) {
                System.out.println("plz enter english");
                continue;
            } else {
                addNewStock.setProductName(newProductName);
                System.out.println(addNewStock.getProductName());
                break;
            }
        }


        System.out.println("please enter the code:");
        while (true) {
            String newCode = scr.nextLine();
            if (newCode.isBlank()) {
                System.out.println("plz enter");
                continue;
            } else if (newCode.matches("[1-9][0-9]{3}")) {
                if (isExist(newCode)) {
                    System.out.println("code already exists, plz try again");
                    return;
                } else {
                    addNewStock.setCode(newCode);
                    break;
                }
            } else {
                System.out.println("please enter the code with 4 numbers:");
                continue;
            }

        }

        System.out.println("please enter the market type:");
        while (true) {
            String market = scr.nextLine();
            if (!market.isBlank()) {
                if (market.equals("1")) {
                    addNewStock.setMarket(MarketType.PRIME);
                    break;
                } else if (market.equals("2")) {
                    addNewStock.setMarket(MarketType.STANDARD);
                    break;
                } else if (market.equals("3")) {
                    addNewStock.setMarket(MarketType.GROWTH);
                    break;
                } else if (market.equals("PRIME") || market.equals("STANDARD") || market.equals("GROWTH")) {
                    addNewStock.setMarket(MarketType.valueOf(market));
                    break;
                } else {
                    System.out.println("please choose 1P or 2S or 3G");
                    continue;
                }
            } else {
                System.out.println("plz enter");
                continue;
            }
        }
        System.out.println("please enter the Shares Issued: ");
        while (true) {
            String recordShareIssued = scr.nextLine();
            if (recordShareIssued.matches("[1-9][0-9]*")) {
                try {
                    addNewStock.setSharesIssued(Long.parseLong(recordShareIssued));
                    break;
                } catch (NumberFormatException numErr) {
                    System.out.println("plz enter the right number");
                }
            } else if (recordShareIssued.isBlank()) {
                System.out.println("plz enter");
                continue;
            } else {
                System.out.println("please enter with numbers(don't start by 0 or space):");
                continue;
            }
        }
        Path path = Paths.get("new\\src\\stock.csv");
        try {
            File file = new File("new\\src\\stock.csv");
            try (FileWriter fr = new FileWriter(file, true)) {
                fr.write(addNewStock.getCode() + "\t");
                fr.write(addNewStock.getProductName() + "\t");
                fr.write(addNewStock.getMarket() + "\t");
                fr.write(addNewStock.getSharesIssued()+ "\n");

            }
        } catch (IOException ex) {
            System.out.println("loading err");
        }
        System.out.println("finished adding.");
        }
    }
