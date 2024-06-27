package feed;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory; //API para producir árboles DOM a partir de documentos XML.
import javax.xml.parsers.DocumentBuilder;//Se usa para el parseo en sí.
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.StringReader;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;

public class FeedParser {

    public FeedParser(){
        // nose, vacío
    }

    public static List<Article> parseXML(String xmlData) {
        List<Article> articles = new ArrayList<>();

        try {
            PrintWriter printWriter =  new PrintWriter(new FileWriter("feeds.txt", true));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(xmlData)));
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item"); //Obtengo los "elementos" y me fijo cuántos hay. Cada elemento es un artículo.
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    //Voy recorriendo los artículos y guardando los datos de cada uno.
                    Node nNode = nList.item(temp); // me da el item i-esimo
                        if (nNode.getNodeType() == org.w3c.dom.Element.ELEMENT_NODE) {
                             Element eElement = (Element) nNode;
                             String title = "";
                             String link = "";
                             String description = "";
                             String pubDate = "";

                             // Chequeamos que los atributos estén
                             Node titleNode = eElement.getElementsByTagName("title").item(0);
                             
                        if (titleNode != null) {
                            title = titleNode.getTextContent();
                            printWriter.println(title + ";");

                        }

                        Node linkNode = eElement.getElementsByTagName("link").item(0);
                        
                        if (linkNode != null) {
                            link = linkNode.getTextContent();
                        }

                        Node descriptionNode = eElement.getElementsByTagName("description").item(0);
                        
                        if (descriptionNode != null) {
                            description = descriptionNode.getTextContent();
                            printWriter.println(description + ";");
                            printWriter.println();
                        }

                        Node pubDateNode = eElement.getElementsByTagName("pubDate").item(0);
                        
                        if (pubDateNode != null) {
                            pubDate = pubDateNode.getTextContent();
                        }
                        
                        articles.add(new Article(title, description, pubDate, link)); //Creo un nuevo artículo y lo agrego a la lista.
                    }
                }
                printWriter.close();
            } catch (Exception e) {
                e.printStackTrace(); // Si hay un error, imprime el stack trace.
            }
        return articles;
    }

    public static String fetchFeed(String feedURL) throws MalformedURLException, IOException, Exception {

        URL url = new URL(feedURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
         
        // Si todos los grupos usan el mismo user-agent, el servidor puede bloquear las solicitudes.
        connection.setRequestProperty("user-agent", "Kernel Space Program");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int status = connection.getResponseCode();
        if (status != 200) {
            throw new Exception("HTTP error code: " + status + "\nAt URL: " + feedURL);
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();
            return content.toString();
        }
    }
}
