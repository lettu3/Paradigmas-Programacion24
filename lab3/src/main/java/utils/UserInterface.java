package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import namedEntities.heuristics.Heuristics;

public class UserInterface {

    private HashMap<String, String> optionDict;

    private List<Option> options;

    public UserInterface() {
        options = new ArrayList<Option>();
        options.add(new Option("-h", "--help", 0));
        options.add(new Option("-f", "--feed", 1));
        options.add(new Option("-ne", "--named-entity", 1));
        options.add(new Option("-pf", "--print-feed", 0));
        options.add(new Option("-sf", "--stats-format", 1));

        optionDict = new HashMap<String, String>();
    }

    public Config handleInput(String[] args) {

        for (Integer i = 0; i < args.length; i++) {
            for (Option option : options) {
                if (option.getName().equals(args[i]) || option.getLongName().equals(args[i])) {
                    if (option.getnumValues() == 0) {
                        optionDict.put(option.getName(), null);
                    } else {
                        if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                            optionDict.put(option.getName(), args[i + 1]);
                            i++;
                        } else {
                            System.out.println("Invalid inputs");
                            System.exit(1);
                        }
                    }
                }
            }
        }
        String feedKey = "all";
        if (optionDict.get("-f") != null) {
            feedKey = optionDict.get("-f");
        }
        Boolean computeNamedEntities = optionDict.containsKey("-ne");

        Boolean printFeed = true;
        if (computeNamedEntities) {
            printFeed = optionDict.containsKey("-pf");
        }
        Boolean printHelp = (optionDict.containsKey("-h") ||
                optionDict.containsKey("--help"));

        String heuristic = optionDict.get("-ne");
        String mode = "cat";
        if (optionDict.containsKey("-sf")) {
            mode = optionDict.get("-sf");
        }

        return new Config(printHelp, printFeed, computeNamedEntities, heuristic, mode, feedKey);
    }

    public static void printHelp() {
        System.out.println("Usage: make run ARGS=\"[OPTION]\"");
        System.out.println("Options:");
        System.out.println("  -h, --help: Show this help message and exit");
        System.out.println("  -f, --feed <feedKey>:                Fetch and process the feed with");
        System.out.println("                                       the specified key");
        System.out.println("                                       Available feed keys are: ");
        for (String feedLabel : FeedsData.getAvailableFeeds()) {
            System.out.println("                                       " + feedLabel);
        }
        System.out.println("  -ne, --named-entity <heuristicName>: Use the specified heuristic to extract");
        System.out.println("                                       named entities");
        System.out.println("                                       Available heuristic names are: ");
        for (String heuristic : Heuristics.getAvailableHeuristics()) {
            System.out.println("                                       " +
             heuristic + ": " + Heuristics.getHeuristicDescription(heuristic));
        }
        System.out.println("  -pf, --print-feed:                   Print the fetched feed");
        System.out.println("  -sf, --stats-format <format>:        Print the stats in the specified format");
        System.out.println("                                       Available formats are: ");
        System.out.println("                                       cat: Category-wise stats");
        System.out.println("                                       topic: Topic-wise stats");
    }
}
