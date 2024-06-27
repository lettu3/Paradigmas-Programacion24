---
title: Laboratorio 3 - "Computación Distribuida con Apache Spark"

authors:  Nahuel Fernandez, Ignacio Gomez Barrios, Luciano Rojo
---



# Preparación del entorno
-  Instalar [maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) Es probable que sea un paquete de tu distribución (`$ sudo apt install maven` si estás en Ubuntu, Debian o derivados).
-  Descargar [spark 3.5.1](https://www.apache.org/dyn/closer.lua/spark/spark-3.5.1/spark-3.5.1-bin-hadoop3.tgz) y descomprimirlo en el directorio `DIR`.
-  Definir variable de entorno `export SPARK_HOME=<DIR>` (acá­ `<DIR>` es el directorio donde descomprimieron spark).

# Como compilar y usar el proyecto
1. Clonar el repositorio y entrar a su carpeta raíz
```
$ git clone git@bitbucket.org:paradigmas2024gr24/paradigmas2024gr24pr3.git
$ cd paradigmas2024gr24pr3
```
2. Modificar `routePath` en App.java, linea 25. Aquí pondremos la ruta absoluta desde el home.
Ejemplo:
 `private static String routePath = "/home/facultad/paradigmas/lab3/";`
3. usar maven para compilar el proyecto
```
$ mvn compile package
```

4. Luego, moverse a la carpeta de spark y ejecutar el programa

```bash
$ cd spark-3.5.1-bin-hadoop3/
$ ./bin/spark-submit  --master local[2] ../target/EntitiesClassifier-0.1.jar ARGS 2> /dev/null

```
donde `ARGS` son los argumentos utilizados en el laboratorio 2. La flag 2>/dev/null es opcional, sirve para redireccionar la salida de Spark que contiene información de debug hacía un archivo para no saturar la consola.
Por ejemplo, pasando los argumentos `ARGS = -ne cap -f p12pais ` el programa buscará entidades nombradas con la heurística CapitalizedHeuristic, en el feed de Página12.

Alternativamente, se puede utilizar el ejecutable run.sh ubicado en la carpeta raiz del proyecto para compilar y ejecutar el programa de una sola vez. Ahora, `ARGS` también son los argumentos usados en el laboratorio 2.

```bash
$ chmod +x run.sh
$ ./run.sh ARGS
```

# Cambios realizados
En el laboratorio 2 parseabamos los feeds directamente en la estructura de datos List<String> y aplicabamos las heurísticas a los artículos uno por uno.
Ahora, con Spark no podíamos hacer esto. La solución fue crear el archivo de texto plano `feeds.txt`, allí escribir los títulos y las descripciones de cada artículo, y separar cada componente con punto y coma, y salto de linea. Este archivo se crea durante la obtención y parseo de los feeds en FeedParser, y se agregan al archivo en formato Titulo;Descripción. Notar que Spark no interviene en la creación del archivo de texto.
Una vez completado el proceso de parsear el feed comienza la parte más relevante del proyecto, la lectura del archivo y la aplicación de heurísticas al texto que contiene en paralelo mediante Spark, a través del código de App.java.

El cómputo en paralelo de las entidades nombradas se lleva a cabo principalmente en las líneas: 

```java
if(config.getHeuristic().equals("cap")) {
    JavaRDD<String> entitiesRDD = articles.flatMap(article -> CapitalizedWordHeuristic.extractCandidates(article).iterator());
    listEntity.addAll(entitiesRDD.collect());
} else if(config.getHeuristic().equals("doublecap")) {
    JavaRDD<String> entitiesRDD = articles.flatMap(article -> DoubleCapitalizedWordHeuristic.extractPersonNames(article).iterator());
    listEntity.addAll(entitiesRDD.collect());
} else if(config.getHeuristic().equals("corp")) {
    JavaRDD<String> entitiesRDD = articles.flatMap(article -> CorpHeuristic.extractX(article).iterator());
    listEntity.addAll(entitiesRDD.collect());
} else {
    System.out.println("Heuristic not found");
    System.exit(1);
}
```
En el código anterior se puede ver como manipulamos los anteriormente creados Datasets Distribuidos y Resilientes (RDD), una estructura propia de Spark que consiste, en nuestro caso, en una colección distribuída de Strings que puede ser manipulada por los workers mediante un cómputo en paralelo. Estos strings serán provistos por las heurísticas que extraerán las entidades nombradas de los artículos provenientes del archivo de texto, como se explica a continuación.
Para obtener estos strings a través de la aplicación de las heurísticas utilizamos la función flatMap, una implementación de una función map provista por el framework que nos permite aplicar la heurística a cada elemento presente en el RDD articles y obtener varios resultados a partir de cada aplicación de la función usando un iterador.
Este proceso es idéntico para todas las heurísticas.
Una vez terminado el proceso anterior, independientemente de la heurística utilizada, obtenemos en entitiesRDD una coleccion de entidades extraídas en formato string (un JavaRDD<String>), distribuídas en el cluster en el que fueron procesadas en paralelo por los workers. Ya terminado dicho procesamiento, unimos los resultados parciales de cada worker utilizando la función `collect()` y guardamos el resultado general en la lista `listEntity`, cuyo uso no varía respecto al proyecto 2.

Sin embargo, todavía no tratamos la forma en la que se obtienen los artículos en cuestión a partir del archivo de texto.
La obtención de la lista de artículos, que también es un RDD de tipo String, se hace mediante instrucciones provistas por el framework, también en App.java.

```java
JavaRDD<String> lines = spark.read().textFile(routePath + "spark-3.5.1-bin-hadoop3/feeds.txt").javaRDD();
JavaRDD<String> articles = lines.filter(text -> text.length() > 0)
                                .flatMap(text -> Arrays.asList(Pattern.compile(";")
                                .split(text)).iterator());
```

En las líneas anteriores se puede ver como se realiza la extracción.
Una vez creado el archivo de texto plano que contiene los feeds (feeds.txt), poblamos el RDD "articles" con el resultado de filtrar lo que Spark (una instancia de SparkSession) leyó del archivo mediante la instrucción read().textFile(). Para eso, aplicamos nuevamente la función flatMap con split() para separar las líneas en dos elementos cuando se encuentre un carácter ';', que recordemos es la separación que fijamos entre el título y la descripción de cada archivo, usando tambíen un iterador para concretar un proceso de parseo muy similar al de la obtención de entidades nombradas. El uso del método .JavaRDD() al final nos permite obtener un JavaRDD compuesto por líneas de texto (Strings) que originalmente estaban separadas por un ';', que es la estructura con la que trabajamos.

***
