package utils;
import java.util.ArrayList;
import java.util.List;

public class FeedsData {
    private String label;
    private String url;
    private String type;
    private static List<String> availableFeedsData = new ArrayList<>();

    public FeedsData(String label, String url, String type) {
        this.label = label;
        this.url = url;
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public void print() {
        System.out.println("Feed: " + label);
        System.out.println("URL: " + url);
        System.out.println("Type: " + type);
    }

    protected static void addAvailableFeed(String label){
        availableFeedsData.add(label);
    }
    
    public static List<String> getAvailableFeeds() {
        return availableFeedsData;
    }
}