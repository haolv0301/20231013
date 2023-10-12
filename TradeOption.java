import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TradeOption {
    public boolean isWorkDay(LocalDateTime userDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DayOfWeek dayOfWeek = userDateTime.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    //===============================================================================
    public boolean isInTimeZone(LocalDateTime userDateTime) {
        LocalTime startTime = LocalTime.of(8, 59);
        LocalTime endTime = LocalTime.of(15, 1);
        LocalTime userTime = userDateTime.toLocalTime();
        return userTime.isAfter(startTime) && userTime.isBefore(endTime);
    }

    //===========================================================================================
    public boolean isPassedTime(LocalDateTime useDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return useDateTime.isBefore(currentDateTime);
    }

    //===============================================================================================
    public void addNewTrade() {
        Scanner scr = new Scanner(System.in);
        Trade addTrade = new Trade();
        StockOption stockOption = new StockOption();
        System.out.print("""
                取引日時を12桁の半角数字で入力してください。
                例:2023年9月22日10時30分 → 202309221030
                """);
        while (true) {
            String inputDateTime = scr.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            try {
                LocalDateTime userDateTime = LocalDateTime.parse(inputDateTime, formatter);
                if (isPassedTime(userDateTime)) {
                    if (isWorkDay(userDateTime)) {
                        if (isInTimeZone(userDateTime)) {
                            addTrade.setTradeDateTime(userDateTime);
                            break;
                        } else {
                            System.out.println("時間帯:9:00~15:00の取引日時を入力してください。");
                            continue;
                        }
                    } else {
                        System.out.println("平日である取引日時を入力してください。");
                        continue;
                    }
                } else {
                    System.out.println("過去の取引日時を入力してください。");
                    continue;
                }
            } catch (Exception exception) {
                System.out.print("""
                        無効日時です。
                        正しい取引日時を12桁の半角数字で入力してください。
                        例:2023年9月22日10時30分 → 202309221030
                        """);
            }

        }
        System.out.println("銘柄コードを入力してください。");
        while (true) {
            String inputCode = scr.nextLine();
            if (!inputCode.isBlank()) {
                if (inputCode.matches("[1-9][0-9]{3}")) {
                    if (stockOption.isExist(inputCode)) {
                        addTrade.setCode(inputCode);
                        break;
                    } else {
                        System.out.print("""
                                この銘柄は存在していません。
                                他の銘柄コードを入力してください。
                                """);
                        continue;

                    }
                } else {
                    System.out.println("4桁の半角数字で入力してください。");
                    continue;
                }
            } else {
                System.out.println("入力してください。");
                continue;
            }
        }
        System.out.print("""
                売買区分を選択してください。
                1.売
                2.買
                番号を選択して、1桁の半角数字で番号を入力してください。
                """);
        while (true) {
            String inputTradeType = scr.nextLine();
            if (!inputTradeType.isBlank()) {
                if (inputTradeType.matches("[1-2]")) {
                    if (inputTradeType.equals("1")) {
                        addTrade.setType(TradeType.sell);
                        break;
                    } else {
                        addTrade.setType(TradeType.buy);
                        break;
                    }
                } else {
                    System.out.print("""
                            1.売
                            2.買
                              番号を選択して、1桁の半角数字で番号を入力してください。
                              """);
                    continue;
                }
            } else {
                System.out.println("入力してください。");
                continue;
            }
        }
        System.out.println("株取引数量を半角数字で入力してください。(100単位)");
        while (true) {
            String inputQuantity = scr.nextLine();
            if (!inputQuantity.isBlank()) {
                if (inputQuantity.matches("[0-9]*")) {
                    if (inputQuantity.matches("[1-9][0-9]*")) {
                        try {
                            if (Long.parseLong(inputQuantity) % 100 == 0) {
                                addTrade.setQuantity(Long.parseLong(inputQuantity));
                                break;
                            } else {
                                System.out.println("最小単位は100で取引数量を入力してください。");
                                continue;
                            }
                        } catch (NumberFormatException numberFormatException) {
                            System.out.println("範囲が正しい取引数量を入力してください。");
                            continue;
                        }
                    } else {
                        System.out.println("最初の数字に0を入れないでください。");
                        continue;
                    }
                } else {
                    System.out.println("半角数字だけで入力してください。");
                    continue;
                }
            } else {
                System.out.println("入力してください。");
                continue;
            }
        }
        System.out.println("価格を半角数字で入力してください。");
        while (true) {
            String price = scr.nextLine();
            if (!price.isBlank()) {
                if (price.matches("^[0-9\\.]+$") && !price.matches("^[0\\.]+$")) {

                    try {
                        BigDecimal bigDecimalPrice = new BigDecimal(price);
                        addTrade.setPrice(bigDecimalPrice);
                        System.out.println(addTrade.getPrice());
                        break;
                    } catch (NumberFormatException numberFormatException) {
                        System.out.println("正しい価格を半角数字で入力してください。");
                        continue;
                    }
                } else {
                    System.out.println("正しい価格を半角数字で入力してください。");
                    continue;
                }
            } else {
                System.out.println("入力してください。");
                continue;
            }
        }

        Path path = Paths.get("new\\src\\trade.csv");
        try {
            File file = new File("new\\src\\trade.csv");
            try (FileWriter fr = new FileWriter(file, true)) {
                fr.write(addTrade.getTradeDateTime() + "\t");
                fr.write(addTrade.getCode() + "\t");
                fr.write(addTrade.getType() + "\t");
                fr.write(addTrade.getQuantity() + "\t");
                fr.write(addTrade.getPrice() + "\t");
                fr.write("\n");
            }

        } catch (IOException ex) {
            System.out.println("loading err");
        }
    }

    public List<Trade> readTradeFile(String path) {
        List<Trade> tradeList = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            for (String line; (line = bufferedReader.readLine()) != null; ) {
                Trade trade = new Trade();
                String[] arrayLine = line.split("\t");
                LocalDateTime tradeDateTime = LocalDateTime.parse(arrayLine[0]);
                trade.setTradeDateTime(tradeDateTime);
                trade.setCode(arrayLine[1]);
                trade.setType(TradeType.valueOf(arrayLine[2]));
                trade.setQuantity(Long.parseLong(arrayLine[3]));
                trade.setPrice(new BigDecimal(arrayLine[4]));
                tradeList.add(trade);
            }
        } catch (IOException ioException) {
            System.out.println("err");
        }
        return tradeList;
    }
    public void displayTradeList(String path){
        List<Trade> tradeList = readTradeFile(path);
        Collections.sort(tradeList, new Comparator<Trade>() {
            @Override
            public int compare(Trade o1, Trade o2) {
                return o2.getTradeDateTime().compareTo(o1.getTradeDateTime());
            }
        });
        for(Trade tradeInfo:tradeList){
            System.out.printf("| %-18s | %-5s | %-4s | %,12d | %10.2f |%n",
                 tradeInfo.getTradeDateTime(),tradeInfo.getCode(),tradeInfo.getType(),
                    tradeInfo.getQuantity(),tradeInfo.getPrice());
        }
    }
}
