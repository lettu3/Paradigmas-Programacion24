package utils;

public class Config {
    private boolean printHelp;
    private boolean printFeed = false;
    private boolean computeNamedEntities = false;
    private String heuristic;
    private String mode;
    private String feedKey;

    public Config(boolean printHelp, boolean printFeed, boolean computeNamedEntities, String heuristic, String mode,
            String feedKey) {
        this.printHelp = printHelp;
        this.printFeed = printFeed;
        this.computeNamedEntities = computeNamedEntities;
        this.heuristic = heuristic;
        this.mode = mode;
        this.feedKey = feedKey;
    }

    public boolean getPrintHelp() {
        return printHelp;
    }

    public boolean getPrintFeed() {
        return printFeed;
    }

    public boolean getComputeNamedEntities() {
        return computeNamedEntities;
    }

    public String getHeuristic() {
        return heuristic;
    }

    public String getMode() {
        return mode;
    }

    public String getFeedKey() {
        return feedKey;
    }

}
