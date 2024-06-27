-- Obtenemos la función assertEqual para comprobar igualdades de manera polimórfica.
-- Tambien traemos el entorno de funciones para estructurar los tests como TestCase y TestList.
import Dibujo
import Test.HUnit

-- Test para figura general.
testfigura :: Test
testfigura = do TestCase(assertEqual "Triangulo" (figura "Triangulo") (figura "Triangulo"))

--Testeo de r90, se asume que si esta funciona también funcionan r180 y r270.
testR90 :: Test
testR90 = do TestCase(assertEqual "r90" (r90 (figura "Triangulo")) (r90(figura "Triangulo")))

-- Test r180 habiendo testeado antes r90
testR180 :: Test
testR180 = do TestCase(assertEqual "r180" (r90(r90(figura "Triangulo"))) (r180 (figura "Triangulo")))

-- Test r270
testR270 :: Test
testR270 = do TestCase(assertEqual "r270" (r90(r90(r90(figura "Triangulo")))) (r270 (figura "Triangulo")))

-- Test para rot45
testRotar45 :: Test
testRotar45 = do TestCase(assertEqual "Rotar45" (rot45 (figura "Triangulo")) (rot45(figura "Triangulo")))

-- Test para encimar
testencimar :: Test
testencimar = do TestCase(assertEqual "Encimar" (encimar (figura "Triangulo") (figura "Circulo"))
                    (encimar (figura "Triangulo") (figura "Circulo")))

-- Test apilar
testapilar :: Test
testapilar = do TestCase(assertEqual "Apilar" (apilar 1.0 1.0 (figura "Triangulo") (figura "Circulo")) 
                    (apilar 1.0 1.0 (figura "Triangulo") (figura "Circulo")))

-- Test juntar
testjuntar :: Test
testjuntar = do TestCase(assertEqual "Juntar" (juntar 1.0 1.0 (figura "Triangulo") 
                    (figura "Circulo")) (juntar 1.0 1.0 (figura "Triangulo") (figura "Circulo")))

-- Test para espejar
testespejar :: Test
testespejar = do TestCase(assertEqual "Espejar" (espejar (figura "Triangulo")) 
                    (espejar(figura "Triangulo")))

-- Test Composicion
testComp :: Test
testComp = do TestCase(assertEqual "Composicion" 1 (comp 1 id 0))

-- Test MapDib
testMapDib :: Test
testMapDib = do TestCase (assertEqual "MapDib" (figura "Triangulo") (mapDib figura (figura "Triangulo")))

-- Test foldDib
testFoldDib :: Test
testFoldDib = do TestCase (do 
                    let fig = r90 (figura 1)
                    let fig2 = r90 (figura "figura2")
                    let funcion = \x -> figura "figura2"
                    --Caso que se corresponde a una rotación
                    let ret = foldDib funcion r90 rot45 espejar apilar juntar encimar fig 
                    assertEqual "FoldDib" ret fig2)

-- Test ciclar
testCiclar :: Test
testCiclar = do TestCase (assertEqual "Ciclar" (cuarteto (figura "Triangulo") 
                    (r90(figura "Triangulo")) (r180(figura "Triangulo")) (r270 (figura "Triangulo"))) 
                    (ciclar (figura "Triangulo")))

runAllTests :: Test
runAllTests = do TestList [testComp, testMapDib, testFoldDib, testCiclar, testfigura, testR90, testR180, testR270, testespejar, testRotar45, testapilar, testjuntar, testencimar]

main :: IO Counts
main = do runTestTT runAllTests
