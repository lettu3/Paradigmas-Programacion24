module Dibujos.Grilla where

import Dibujo (Dibujo, figura, juntar, apilar, rot45, rotar, encimar, espejar)
import FloatingPic(Conf(..), Output, half, zero)
import qualified Graphics.Gloss.Data.Point.Arithmetic as V
import Graphics.Gloss ( Picture, blue, red, color, black, line, scale, pictures, text, translate, blank )
import Data.List.NonEmpty (nubBy)


colorear :: Picture -> Picture
colorear  = color black 

type Basica = String

-- String -> Floating Pic
interpBase :: Output Basica
interpBase n (dx, dy) (hx, hy) (wx, wy) = translate dx dy (scale 0.17 0.17 (text n))

--scaleDib :: FloatText
--scale(p.h)(p.w) p
--translate x y p

row :: [Dibujo a] -> Dibujo a
row [] = error "row: no puede ser vacío"
row [d] = d
row (d:ds) = juntar 1 (fromIntegral $ length ds) d (row ds)

column :: [Dibujo a] -> Dibujo a
column [] = error "column: no puede ser vacío"
column [d] = d
column (d:ds) = apilar 1 (fromIntegral $ length ds) d (column ds)

grilla :: [[Dibujo a]] -> Dibujo a
grilla = column . map row

tuplasFilas :: Int -> Int -> [(Int, Int)] 
tuplasFilas 0 0  = []
tuplasFilas numFila 1 = [(numFila - 1, 0)]
tuplasFilas numFila cantCol = tuplasFilas (numFila) (cantCol-1) ++ [(numFila - 1, cantCol - 1)]

crearFilas :: [(Int, Int)] -> [Dibujo String]
crearFilas [(x,y)] = [figura ( "(" ++ show (x)++ ", " ++ show(y) ++ ")" )]
crearFilas ((x, y) : xs) = figura ("(" ++ show x ++ ", " ++ show y ++ ")") : crearFilas xs

picGrilla :: Dibujo String
picGrilla  = grilla[crearFilas (tuplasFilas 1 8),
                     crearFilas (tuplasFilas 2 8),
                     crearFilas (tuplasFilas 3 8),
                     crearFilas (tuplasFilas 4 8),
                     crearFilas (tuplasFilas 5 8),
                     crearFilas (tuplasFilas 6 8),
                     crearFilas (tuplasFilas 7 8),
                     crearFilas (tuplasFilas 8 8)
                     ]
              
grillaConf:: Conf
grillaConf = Conf {
    name = "Grilla",
    pic = picGrilla,
    bas = interpBase
}
