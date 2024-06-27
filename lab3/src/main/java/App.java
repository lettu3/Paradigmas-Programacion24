import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.net.MalformedURLException;
import feed.*;
import namedEntities.NamedEntity;
import namedEntities.heuristics.CapitalizedWordHeuristic;
import namedEntities.heuristics.CorpHeuristic;
import namedEntities.heuristics.DoubleCapitalizedWordHeuristic;
import utils.Config;
import utils.DictEntity;
import utils.FeedsData;
import utils.JSONParser;
import utils.Stats;
import utils.UserInterface;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;



public class App {
    // Cambiar hasta la ruta absoluta que dirige a paradigmas2024gr24pr3
    private static String routePath = "/home/..MYPATHTO../paradigmas2024gr24pr3/";

    public static void main(String[] args) throws IOException {

        SparkSession spark = SparkSession
        .builder()
        .appName("EntitiesClassifier")
        .getOrCreate();

        // Configuración del input
        UserInterface ui = new UserInterface();
        Config config = ui.handleInput(args);

        // Parseo de todos los feeds disponibles
        List<FeedsData> feedsDataArray = new ArrayList<>();

        //Acá hay que hacer que se parseen los feeds.
        feedsDataArray = JSONParser.parseJsonFeedsData(routePath + "src/main/java/data/feeds.json");
        
        if (config.getPrintHelp()){
            UserInterface.printHelp();
            System.exit(0);
        }
        
        //parseo del diccionario
        DictEntity dictEntity = new DictEntity();
        dictEntity = JSONParser.parseJsonDictEntity(routePath + "src/main/java/data/dictionary.json");

        try {
            run(config, feedsDataArray, dictEntity);
        }
        catch (Exception e){ // atrapa todas las excepciones
            e.printStackTrace();
        }
    }

    private static void run(Config config, List<FeedsData> feedsDataArray, DictEntity dictEntity) throws MalformedURLException, IOException, Exception {

        List<String> listEntity = new ArrayList<>();
        SparkSession spark = SparkSession.builder().appName("App").getOrCreate();
        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        if (feedsDataArray == null || feedsDataArray.size() == 0) {
            System.out.println("No feeds data found");
            sc.close();
            return;
        }

        List<Article> allArticles = new ArrayList<>();
        try {
            String feedKey = config.getFeedKey();
            try {
                for(FeedsData feed : feedsDataArray){
                    if (feedKey.equals("all") || feed.getLabel().equals(feedKey)){
                        String urlXML = FeedParser.fetchFeed(feed.getUrl());
                        allArticles.addAll(FeedParser.parseXML(urlXML));
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (config.getComputeNamedEntities()) {
                System.out.println("Computing named entities using ");
                System.out.println(config.getHeuristic());
  
                JavaRDD<String> lines = spark.read().textFile(routePath + "spark-3.5.1-bin-hadoop3/feeds.txt").javaRDD();
                
                JavaRDD<String> articles = lines
                                        .filter(text -> text.length() > 0)
                                        .flatMap(text -> Arrays.asList(Pattern.compile(";").split(text)).iterator());
                
                if(config.getHeuristic().equals("cap")) {
                    JavaRDD<String> entitiesRDD = articles.flatMap(article -> CapitalizedWordHeuristic.extractCandidates(article).iterator());
                    listEntity.addAll(entitiesRDD.collect());
                } else if(config.getHeuristic().equals("doublecap")) {
                    JavaRDD<String> entitiesRDD = articles.flatMap(article -> DoubleCapitalizedWordHeuristic.extractPersonNames(article).iterator());
                    listEntity.addAll(entitiesRDD.collect());
                } else if(config.getHeuristic().equals("corp")) {
                    JavaRDD<String> entitiesRDD = articles.flatMap(article -> CorpHeuristic.extractX(article).iterator());
                    listEntity.addAll(entitiesRDD.collect());
                }   
                else {
                    System.out.println("Heuristic not found");
                    System.exit(1);
                } 
            }

            for (String s : listEntity){
                String label = dictEntity.getLabelFromKeyword(s);
                if (label != null){
                    dictEntity.increaseAppearanceCount(label);
                }
            }
            
            if (config.getPrintFeed()){
                System.out.println("Printing feed(s) ");
                for (FeedsData feedData : feedsDataArray) {
                    if (feedKey.equals("all") || feedData.getLabel().equals(feedKey)) {
                        feedData.print();
                    }
                }
                System.out.println(); // whitespace
                for (Article art : allArticles) {
                    art.printArticle();
                }
            }

            if (config.getComputeNamedEntities()){
                dictEntity.print();
                List<NamedEntity> entitiesForClass = Stats.objectCreator(Stats.getAppearedEntities(dictEntity.dictionary));

                System.out.println("-".repeat(80));
                System.out.println(entitiesForClass.size() + " objetos creados.");
                System.out.println("\nStats: ");
                Stats stats = new Stats();
                stats.printStatsByMode(Stats.getAppearedEntities(dictEntity.dictionary), config.getMode());
            }
        
        } finally {
            sc.close();
            spark.stop();
        }
    }
}