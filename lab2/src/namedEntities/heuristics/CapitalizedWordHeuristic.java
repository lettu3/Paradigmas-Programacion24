package namedEntities.heuristics;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import feed.Article;

public class CapitalizedWordHeuristic extends Heuristics {

    public CapitalizedWordHeuristic() {
        super();
    }

    private void extractCandidates(String text) {
        Pattern pattern = Pattern.compile("[A-Z][a-z]+(?:\\s[A-Z][a-z]+)*");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            candidates.add(matcher.group());
        }
    }

    public static List<String> parseFromHeuristicCap(Article art) {
        CapitalizedWordHeuristic heuristic = new CapitalizedWordHeuristic();
        heuristic.extractCandidates(art.getTitle());
        heuristic.extractCandidates(art.getDescription());
        return heuristic.getCandidates();
    }
}
