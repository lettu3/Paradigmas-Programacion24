package namedEntities.heuristics;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import feed.Article;

public class CapitalizedWordHeuristic extends Heuristics {

    public CapitalizedWordHeuristic() {
        super();
    }
    
    public static List<String> extractCandidates(String text) {
        Pattern pattern = Pattern.compile("[A-Z][a-z]+(?:\\s[A-Z][a-z]+)*");
        Matcher matcher = pattern.matcher(text);
        List<String> cand = new ArrayList<>();
        while (matcher.find()) {
            cand.add(matcher.group());
        }
        return cand;
    }

    public List<String> parseFromHeuristicCap (String art){
        CapitalizedWordHeuristic heuristic = new CapitalizedWordHeuristic();
        String[] parts = art.split(";", 2);
        if (parts.length >= 2) {
            CapitalizedWordHeuristic.extractCandidates(parts[0]);
            CapitalizedWordHeuristic.extractCandidates(parts[1]);
        }
        return heuristic.getCandidates();
    }
}
