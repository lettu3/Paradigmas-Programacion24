---
title: Laboratorio de Programación Orientada a Objetos
author: Ignacio Gomez, Nahuel Fernandez, Luciano Rojo
---

El enunciado del laboratorio se encuentra en [este link](https://docs.google.com/document/d/1wLhuEOjhdLwgZ4rlW0AftgKD4QIPPx37Dzs--P1gIU4/edit#heading=h.xe9t6iq9fo58).

# 1. Tareas
Checklist del avance.

## Verificación de que pueden hacer las cosas.
- [x] Java 17 instalado. Deben poder compilar con `make` y correr con `make run` para obtener el mensaje de ayuda del programa.

## 1.1. Interfaz de usuario
- [x] Estructurar opciones
- [x] Construir el objeto de clase `Config`

## 1.2. FeedParser
- [x] `class Article`
    - [x] Atributos
    - [x] Constructor
    - [x] Método `print`
    - [x] _Accessors_
- [x] `parseXML`

## 1.3. Entidades nombradas
- [x] Pensar estructura y validarla con el docente
- [x] Implementarla
- [x] Extracción
    - [x] Implementación de heurísticas
- [x] Clasificación
    - [x] Por tópicos
    - [x] Por categorías
- Estadísticas
    - [x] Por tópicos
    - [x] Por categorías
    - [x] Impresión de estadísticas

## 1.4 Limpieza de código
- [x] Pasar un formateador de código
- [x] Revisar TODOs

# 2. Experiencia

La experiencia general del grupo respecto al laboratorio fue bastante fluída. Nuestro previo manejo del lenguaje C hizo que nos sintieramos familiares con Java y facilitó en gran medida la adopción del paradigma orientado a objetos, que es el enfoque central de todo el trabajo y aquello a lo que le tuvimos que prestar más atención.
En general, creemos que la mayor "dificultad" a superar fue el parseo del diccionario JSON provisto en el kickstart, ya que a primera vista no había una forma lineal y sencilla de hacerlo (probamos algunas librerías de terceros, y distintos enfoques). También otra dificultad fue que una vez parseado, no nos convencía la estructura que la dimos. Para solucionarlo creamos una clase propia del diccionario de entidades y pudimos resolver bien la algoritmia para manejar la eficiencia de sus métodos, ya que en un principio su código era bastante ineficiente.
El paradigma orientado a objetos tratado en el proyecto nos brindó una nueva forma de encapsular estructuras y datos de manera modular, a manipular instancias de las mismas como ocurrencias individuales con características propias y a entender conceptos básicos sobre procesamiento de lenguajes.
Este último punto cobró importancia a la hora de diseñar e implementar las heurísticas. Debíamos dar diferentes definiciones de "importante" con cada heurística implementada, por lo que debíamos pensar en qué palabras tendría sentido extraer, mientras se mantenía una condición relativamente abarcativa sobre qué estructura literal se debía considerar y cuales deberían ser excluidas o no tenidas en cuenta. Respecto al procesamiento del lenguaje tuvimos un aprendizaje en el manejo de expresiones regulares, ya que si bien las habíamos aprendido en la materia de introducción a la lógica, nunca le habíamos dado uso en un lenguaje de programación.

# 3. Preguntas
1. Explicar brevemente la estructura de datos elegida para las entidades nombradas.
"La estructura" como concepto tiene dos partes:
-   NamedEntity: Una NamedEntity o entidad nombrada es una entidad que aparece en el texto del artículo y fué extraída del mismo a través de una heurística, indiferentemente de cual sea. La entidad nombrada puede ser cualquier cosa, a veces llegando a abarcar a todos los sustantivos propios y a aquello que representan (Ej: una persona, una ubicación).
-   DictEntity: Una DictEntity es toda entidad que aparezca en el diccionario JSON con el que contamos en el kickstart.

Estas dos partes se unen al momento de clasificar las entidades nombradas, para lo cual se utilizan las entidades del diccionario.
Una entidad es clasificada si es una ocurrencia de ambos tipos de entidad, es decir, si es una entidad nombrada extraída del artículo y si a su vez existe en el diccionario.
El resultado final de esta separación (y subsecuente unión) es una entidad nombrada clasificada. Por otro lado, la razón de ser de esta estructura es que necesitamos una forma sencilla de clasificar todo aquello que sea extraído por la heurística en uso. Como se dice en la consigna, esto generalmente se hace con un modelo de aprendizaje automático capaz de devorar una cantidad inmensa de datos con los cuales realizar la clasificación, pero como en nuestro caso esta capacidad de clasificación es limitada, la entidad nombrada debe ser una entidad del diccionario para poder acceder, por ejemplo, a su categoria y tags.
Decimos que la capacidad de clasificación es limitada porque, por ejemplo, no poseemos una gran cantidad de nombres de personas o de lugares, así que muchas de las entidades nombradas que sean pertenecientes a alguna de las categorías anteriores no podrán ser clasificadas, porque al no tener su propia entrada en el diccionario, nuestro programa no tiene forma de saber qué son.
A su vez, una vez clasificadas las NamedEntity se subdividen en PersonEntity, LocationEntity, OrganizationEntity y OtherEntity dependiendo de la categoría que estas hayan recibido en el diccionario.
Es evidente tanto en el código como en el concepto, que las subdivisiones anteriores constituyen subclases de la clase NamedEntity. En caso de encontrarse una NamedEntity (mediante la heurística) y ser clasificada (mediante el diccionario), se creará una instancia de una de las subclases dependiendo de la categoría de la entidad en el diccionario y se mostrará en pantalla la cantidad de objetos creados en esa ejecución del programa. Notar que la cantidad de objetos creados siempre se condice con la cantidad de entidades mostradas en las estadísticas, que representan a las entidades clasificadas correctamente.


2. Explicar brevemente cómo se implementaron las heurísticas de extracción.
Las heurísticas de extracción fueron implementadas mediante expresiones regulares que actúan de la siguiente manera:

-   CapitalizedWordHeuristic: La expresión regular utilizada considera a TODA palabra que comience con mayúscula, seguida de 0 o más palabras que comienza con mayúscula.
    **Ejemplos:** ["Casa", "De", "Argentina", "Himno Nacional Argentino, "Lio Messi", "Debido", "Apagón", "Revolución"]
-   DoubleCapitalizedWordHeuristic: La expresión regular utilizada considera a todo conjunto de dos palabras capitalizadas consecutivas, cuenta además con un grupo de exclusión que elimina de la consideración a articulos y prefijos comunes como "El", "La" y "The", entre otros. Esta heurística busca capturar nombres de personas.
    **Ejemplos:** ["River Plate", "Manchester City" "Tom Jobim", "Casa Rosada", "Javier Milei", "Martín Llaryora"]
-   CorpHeuristic: Esta heurística busca encontrar nombres de organizaciones(corporaciones) con una expresión regular que considera patrones que comienzan con palabras comúnmente usadas para nombrar instituciones y organizaciones como "Instituto", "Centro", "Fundación", etc.
    **Ejemplos:** ["Banco de Córdoba", "Fundación Favaloro", "Confederación del Trabajo", "Instituto de Neurología y Neurociencias"]
# 4. Extras
El trabajo fue hecho casi en su totalidad en GitHub, pero fue terminado en el repositorio de BitBucket. Aquí dejamos el link del repositorio GitHub utilizado:

https://github.com/lettu3/lab2-paradigmas-OOP



