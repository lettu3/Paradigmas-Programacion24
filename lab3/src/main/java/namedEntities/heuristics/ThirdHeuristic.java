/*
Esta heuristica busca filtrar el nombre de instituciones u organizaciones
Muchas veces se utilizan los conectores "de/del" para referirse al lugar de operaciones de dicha organizacion 
Ej: Universidad Nacional de Córdoba
*/
package namedEntities.heuristics;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
instituciones, empiezan con "Instituto", "Centro", "Fundacion", "Banco", "Corporacion", "Asociacion", "Central", "Confederación", ""
les puede seguir un conector en minuscula "de" o "del" y habria que tomar todas las palabras en mayusculas hasta que no se encuentre
otro conector, es decir cualquier otra palabra en minsucula
nota: despues de que se uso un conector "de"/"del", tambien se podria usar un ultimo conector "y"
 */
public class ThirdHeuristic extends Heuristics {

    public ThirdHeuristic() {
        super();
    }
    
    public List<String> extractX (String text) {
        Pattern pattern = Pattern.compile("(Instituto|Centro|Fundacion|Banco|Corporacion|Asociacion|Central|Confederación)\\s(de|la|de\\sla|del)\\s([A-Z][a-z]+(?:\\s[A-Z][a-z]+)*)(?:\\sy\\s[A-Z][a-z]+)?");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            candidates.add(matcher.group());
        }
        return candidates;
    }
}
