package namedEntities.heuristics;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import namedEntities.NamedEntity;

public class Heuristics {
    private static String[] availableHeuristics = { "cap", "doublecap", "corp" };
    private static HashMap<String, String> heuristicDescription = new HashMap<>();
    protected Pattern pattern;
    protected List<String> candidates = new ArrayList<>();
    protected List<NamedEntity> listNamedEntities = new ArrayList<>();
    protected String text;

    public void initialize() {
        text = text.replaceAll("[-+.^:,\"]", "");
        text = Normalizer.normalize(text, Normalizer.Form.NFD); // OJO, elimina las tildes.
        text = text.replaceAll("\\p{M}", "");
    }

    static {
        heuristicDescription.put("cap", "Capitalized Word Heuristic");
        heuristicDescription.put("doublecap", "Double Capitalized Word Heuristic");
        heuristicDescription.put("corp", "Corporation Heuristic");
    }

    public static String[] getAvailableHeuristics() {
        return availableHeuristics;
    }

    public static String getHeuristicDescription(String heuristic) {
        return heuristicDescription.get(heuristic);
    }

    public List<String> getCandidates() {
        return new ArrayList<>(candidates); // Devolver una copia de la lista para evitar modificaciones externas
    }

    public void printCandidates() {
        for (String s : candidates) {
            System.out.println(s);
        }
    }

}
