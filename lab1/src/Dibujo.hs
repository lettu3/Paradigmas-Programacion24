module Dibujo (Dibujo, figura, encimar, apilar, juntar, espejar,
              ciclar, comp, rotar, rot45, (^^^), (.-.), (///), mapDib, foldDib, r90, r180, r270, cuarteto, encimar4
    ) where

-- nuestro lenguaje 
data Dibujo a = Figura a
                | Rotar (Dibujo a) | Espejar (Dibujo a)
                | Rot45 (Dibujo a) |  Apilar Float Float (Dibujo a) (Dibujo a)
                | Juntar Float Float (Dibujo a) (Dibujo a)
                | Encimar (Dibujo a) (Dibujo a) deriving (Eq, Show)

-- tipo Figura, usado para pruebas. El tipo dibujo es polimórfico
--data Figura = Triangulo | Rectangulo | Circulo
--              | Cuadrado | Linea deriving (Eq, Show)
-- combinadores
infixr 6 ^^^

infixr 7 .-.

infixr 8 ///

comp :: Int -> (a -> a) -> a -> a
comp 0 _ = id
comp n f = f . comp (n-1) f

-- Funciones constructoras
figura :: a -> Dibujo a
figura = Figura

encimar :: Dibujo a -> Dibujo a -> Dibujo a
encimar= Encimar

apilar :: Float -> Float -> Dibujo a -> Dibujo a -> Dibujo a
apilar = Apilar

juntar  :: Float -> Float -> Dibujo a -> Dibujo a -> Dibujo a
juntar = Juntar

rot45 :: Dibujo a -> Dibujo a
rot45 = Rot45

rotar :: Dibujo a -> Dibujo a
rotar = Rotar

espejar :: Dibujo a -> Dibujo a
espejar = Espejar

(^^^) :: Dibujo a -> Dibujo a -> Dibujo a
d1 ^^^ d2 = encimar d1 d2

(.-.) :: Dibujo a -> Dibujo a -> Dibujo a
d1 .-. d2 = apilar 1 1 d1 d2

(///) :: Dibujo a -> Dibujo a -> Dibujo a
d1 /// d2 = juntar 1 1 d1 d2

-- rotaciones
r90 :: Dibujo a -> Dibujo a
r90 = Rotar

r180 :: Dibujo a -> Dibujo a
r180 = comp 2 r90

r270 :: Dibujo a -> Dibujo a
r270 = comp 3 r90

-- una figura repetida con las cuatro rotaciones, superimpuestas.
encimar4 :: Dibujo a -> Dibujo a
encimar4 x = x ^^^ r90 x ^^^ r180 x ^^^ r270 x

-- cuatro figuras en un cuadrante.
---------------
-- | d1 | d2 |
-- | d3 | d4 |
---------------
cuarteto :: Dibujo a -> Dibujo a -> Dibujo a -> Dibujo a -> Dibujo a
cuarteto d1 d2 d3 d4 = (d1 /// d2) .-. (d3 /// d4)

-- un cuarteto donde se repite la imagen, rotada (¡No confundir con encimar4!)
ciclar :: Dibujo a -> Dibujo a
ciclar x = cuarteto x (r90 x) (r180 x) (r270 x)

-- map para nuestro lenguaje
mapDib :: (a -> Dibujo b) -> Dibujo a -> Dibujo b
-- verificar que las operaciones satisfagan
-- 1. map figura = id
-- 2. map (g . f) = mapDib g . mapDib f

--Para Figura se aplica directamente la función, no se puede reducir más.
mapDib funcion (Figura fig) =  funcion fig
-- Aplico recursivamente la función a los dibujos hasta llegar a la figura.
mapDib funcion (Rotar fig) = Rotar (mapDib funcion fig)
mapDib funcion (Rot45 fig) = Rot45 (mapDib funcion fig)
mapDib funcion (Apilar numero1 numero2 fig1 fig2) = Apilar numero1 numero2 (mapDib funcion fig1) (mapDib funcion fig2)
mapDib funcion (Juntar numero1 numero2 fig1 fig2) = Juntar numero1 numero2 (mapDib funcion fig1) (mapDib funcion fig2)
mapDib funcion (Encimar fig1 fig2) = Encimar (mapDib funcion fig1) (mapDib funcion fig2)
mapDib funcion (Espejar fig) = Espejar (mapDib funcion fig)

-- Principio de recursión para Dibujos.
foldDib ::
  (a -> b) ->
  (b -> b) ->
  (b -> b) ->
  (b -> b) ->
  (Float -> Float -> b -> b -> b) ->
  (Float -> Float -> b -> b -> b) ->
  (b -> b -> b) ->
  Dibujo a -> b

foldDib f fRot fRot45 fEs fAp fJun fEn dib =
  case dib of Figura fig               -> f fig
              Rotar dib'                -> fRot $ foldDib f fRot fRot45 fEs fAp fJun fEn dib'
              Rot45 dib'                -> fRot45 $ foldDib f fRot fRot45 fEs fAp fJun fEn dib'
              Espejar dib'              -> fEs $ foldDib f fRot fRot45 fEs fAp fJun fEn dib'
              Apilar n1 n2 dib1 dib2   -> fAp  n1 n2 (foldDib f fRot fRot45 fEs fAp fJun fEn dib1) (foldDib f fRot fRot45 fEs fAp fJun fEn dib2)
              (Juntar n1 n2 dib1 dib2) -> fJun n1 n2 (foldDib f fRot fRot45 fEs fAp fJun fEn dib1) (foldDib f fRot fRot45 fEs fAp fJun fEn dib2)
              (Encimar dib1 dib2)      -> fEn  (foldDib f fRot fRot45 fEs fAp fJun fEn dib1) (foldDib f fRot fRot45 fEs fAp fJun fEn dib2)

figuras :: Dibujo a -> [a]
figuras = foldDib 
            (: []) 
            id 
            id 
            id
            (\_ _ f1 f2  -> f1 ++ f2)
            (\_ _ f1 f2  -> f1 ++ f2)
            (++)
