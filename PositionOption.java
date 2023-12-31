import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionOption {
    public static void main(String[] args) {
        TradeOption tradeOption =new TradeOption();
        List<Trade> tradeList=tradeOption.readTradeFile("new\\src\\trade.csv");
        System.out.println(averageBuyPrice(tradeList).get("4373"));
        System.out.println(calculateHoldingQuantity(tradeList).get("4373"));
    }
public static  Map<String,Long> calculateHoldingQuantity(List<Trade> tradeList){
    Map<String,Long> holdingQuantityMap = new HashMap<>();
    for (Trade trade:tradeList){
        String code = trade.getCode();
        Long quantity=trade.getQuantity();
        if (trade.getType()==TradeType.buy){
            holdingQuantityMap.put(code, holdingQuantityMap.getOrDefault(code, 0L)+quantity);
        } else if (trade.getType()==TradeType.sell) {
            holdingQuantityMap.put(code, holdingQuantityMap.getOrDefault(code, 0L)-quantity);
        }
    }
    return holdingQuantityMap;
}
public static Map<String,BigDecimal> averageBuyPrice(List<Trade> tradeList){
    Map<String,Long> buyQuantityMap = new HashMap<>();
    Map<String, BigDecimal> buyTotalPriceMap = new HashMap<>();
    Map<String,BigDecimal> averageBuyPriceMap = new HashMap<>();
    for(Trade trade:tradeList){
        String code= trade.getCode();
        Long quantity= trade.getQuantity();
        BigDecimal price=trade.getPrice();
        if (trade.getType()==TradeType.buy){
            buyTotalPriceMap.put(code,buyTotalPriceMap.getOrDefault(
                code,BigDecimal.ZERO).add(price.multiply(BigDecimal.valueOf(quantity))));
            buyQuantityMap.put(code, buyQuantityMap.getOrDefault(code,0L)+quantity);
        }
    }
    for (Trade trade:tradeList){
        String code= trade.getCode();
        averageBuyPriceMap.put(code,buyTotalPriceMap.get(code).divide(
            BigDecimal.valueOf(buyQuantityMap.get(code)),2,BigDecimal.ROUND_HALF_DOWN));
    }
    return averageBuyPriceMap;
}
}
