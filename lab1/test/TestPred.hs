import Test.HUnit
import Pred
import Dibujo

-- Test para cambiar
testCambiar :: Test
testCambiar = do TestCase(assertEqual "Test cambiar" (figura 0) (cambiar (==1) (const (figura 0)) (figura 1)))

-- Test para allDib
testAllDib :: Test
testAllDib = do TestCase(assertEqual "Test allDib" False (allDib (=="Triangulo") 
                    (encimar (figura "Circulo") (figura "Triangulo")))) 

-- Test para anyDib
testAnyDib :: Test
testAnyDib = do TestCase(assertEqual "Test anyDib" True 
                    (anyDib (== "Triangulo") (encimar (figura "Triangulo")
                    (figura "Cuadrado"))))

-- Test para orP
testOrP :: Test
testOrP = do TestCase(assertEqual "Test orP" True (orP (== "Fig1") (=="Fig2") "Fig2"))

-- Test para andP
testAndP :: Test
testAndP = do TestCase(assertEqual "Test andP" False (andP (=="Triangulo") (=="Triangulo1") "Triangulo1"))

testPred :: Test
testPred = do TestList [testCambiar, testAllDib, testAnyDib, testOrP, testAndP]

-- si o si tiene que ser main para que el archivo sea ejecutable.
main :: IO Counts
main = do runTestTT testPred