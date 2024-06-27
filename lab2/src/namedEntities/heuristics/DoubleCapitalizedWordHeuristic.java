/*
Esta heurística busca filtrar exclusivamente nombres de personas, ya que estos casi siempre aparecen en formato
nombre-apellido. Para eso, se busca en el texto palabras que comiencen con mayúscula y tengan una o más palabras
seguidas comenzadas con mayúscula. Notar que esto debe dejar afuera nombres de lugares como "La Paz" o "Puerto Stanley".
*/

package namedEntities.heuristics;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import feed.Article;

public class DoubleCapitalizedWordHeuristic extends Heuristics {

    public DoubleCapitalizedWordHeuristic() {
        super();
    }

    public void extractPersonNames(String text) {
        // Se agregan a los candidatos solo palabras que comiencen con mayúscula y
        // tengan una o más palabras seguidas comenzadas con mayúscula.
        // Se ignora todo lo que comience con alguna de las palabras del conjunto de
        // negación.
        Pattern pattern = Pattern.compile(
                "\\b(?!La\\b|El\\b|The\\b|San\\b|Lago\\b|Puerto\\b|Rio\\b|Monte\\b|República\\b)[A-Z][a-z]+\\s[A-Z][a-z]+");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            candidates.add(matcher.group());
        }
    }

    public static List<String> parseFromDoubleHeuristicCap(Article art) {
        DoubleCapitalizedWordHeuristic heuristic = new DoubleCapitalizedWordHeuristic();
        heuristic.extractPersonNames(art.getTitle());
        heuristic.extractPersonNames(art.getDescription());
        return heuristic.getCandidates();
    }
}