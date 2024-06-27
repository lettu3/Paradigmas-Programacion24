---
title: Laboratorio de Funcional
author: Nahuel Fernandez, Ignacio Gomez Barrios, Luciano Rojo
---
La consigna del laboratorio está en https://tinyurl.com/funcional-2024-famaf

# 1. Tareas
Pueden usar esta checklist para indicar el avance.

## Verificación de que pueden hacer las cosas.
- [x] Haskell instalado y testeos provistos funcionando. (En Install.md están las instrucciones para instalar.)

## 1.1. Lenguaje
- [x] Módulo `Dibujo.hs` con el tipo `Dibujo` y combinadores. Puntos 1 a 3 de la consigna.
- [x] Definición de funciones (esquemas) para la manipulación de dibujos.
- [x] Módulo `Pred.hs`. Punto extra si definen predicados para transformaciones innecesarias (por ejemplo, espejar dos veces es la identidad).

## 1.2. Interpretación geométrica
- [x] Módulo `Interp.hs`.

## 1.3. Expresión artística (Utilizar el lenguaje)
- [x] El dibujo de `Dibujos/Feo.hs` se ve lindo.
- [x] Módulo `Dibujos/Grilla.hs`.
- [x] Módulo `Dibujos/Escher.hs`.
- [x] Listado de dibujos en `Main.hs`.

## 1.4 Tests
- [x] Tests para `Dibujo.hs`.
- [x] Tests para `Pred.hs`.

# 2. Experiencia
La experiencia a lo largo del desarrollo fue relativamente fluída. Algunos tuvimos que volver a acostumbrarnos al paradigma funcional, lo que tomó algo de tiempo pero resultó salir bien.
El proyecto nos desconcertó al principio pero cobró sentido muy rápido a medida que ibamos diagramando e implementando las funciones de Dibujo.hs, algo similar ocurrió con Interp.hs antes de comenzar a hacer los dibujos.
Los dibujos fueron entretenidos, fueron una sucesión de pruebas visuales que a veces hasta daban risa. Sentimos que la mayoria de los problemas fueron de tipado, ya que a veces los tipos y sus ordenes no eran tan obvios y llegaban a formar lineas bastante largas para lo que acostumbrabamos ver en Haskell.
La implementación de los tests fue una lección pedagógica a Copilot, la mayor parte del tiempo fue destinada a que entienda la implementacion de nuestras funciones y jamás llegó a proveer tests del todo robustos.
En general el proyecto fue más entretenido que el resto, seguramente por su apartado gráfico y visual, y porque no pasamos demasiado tiempo trabados en nada.


# 3. Preguntas
Al responder tranformar cada pregunta en una subsección para que sea más fácil de leer.

## 1. ¿Por qué están separadas las funcionalidades en los módulos indicados? Explicar detalladamente la responsabilidad de cada módulo.

Las funcionalidades están separadas en distintos módulos para poder trabajar en distintas capas de abstracción en cada uno de ellos.

`Dibujo.hs`: Definimos la sintaxis de nuestro lenguaje.
`Interp.hs`: Definimos la interpretación geométrica de las operaciones dadas por el lenguaje y su representación en pantalla.
`/Dibujos/{Ejemplo, Grilla, Escher}`: cada uno de los dibujos
`Pred.hs`: predicados que hablan sobre el tipo Dibujo.

## 2. ¿Por qué las figuras básicas no están incluidas en la definición del lenguaje, y en vez de eso, es un parámetro del tipo?

Esto es para poder abstraer y generalizar las figuras bases que usemos según la necesidad. Por ejemplo, en el dibujo Grilla.hs, decidimos usar el tipo Dibujo String ya que el dibujo es sobre Strings, en particular usamos las Strings { (, ), 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }.
Pero en Escher utilizamos Dibujo Bool, donde la interpretación es que True es un triangulo y False es una picture vacía.

## 3. ¿Qué ventaja tiene utilizar una función de `fold` sobre hacer pattern-matching directo?

Una de las ventajas es escribir código mas conciso. Nos ahorra escribir cada uno de los patrones ya que pattern-matching está en la definición de `fold`.

## 4. ¿Cuál es la diferencia entre los predicados definidos en Pred.hs y los tests?

En `Pred.hs` los predicados hablan sobre dibujos y son más cercanos a nuestra propia implementación, mientras que en los tests, hay predicados hablando de predicados que prueban el correcto funcionamiento de los mismos.

