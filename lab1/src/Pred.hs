module Pred
  ( Pred
  , cambiar
  , anyDib
  , allDib
  , orP
  , andP
  ) where
    
import Dibujo

type Pred a = a -> Bool

-- Dado un predicado sobre básicas, cambiar todas las que satisfacen
-- el predicado por la figura básica indicada por el segundo argumento.
cambiar :: Pred a -> (a -> Dibujo a) -> Dibujo a -> Dibujo a
cambiar predicado funcion = mapDib (\x -> if predicado x then funcion x else figura x)

-- Le aplica el predicado a las figuras y va combinando los booleanos obtenidos,
-- usando la función de argumento.
combinador :: Pred a -> (Bool -> Bool -> Bool) -> Dibujo a -> Bool
combinador predicado funcion =
  foldDib
    predicado -- (a -> b)
    id        -- (b -> b) función identidad, devuelve el argumento sin modificarlo.
    id        -- (b -> b) idem
    id        -- (b -> b) idem
    combina   -- (Float -> Float -> b -> b -> b)
    combina   -- (Float -> Float -> b -> b -> b)
    funcion   -- (b -> b -> b)
  where
    -- Auxiliar que aplica la combinación booleana que se pasó como argumento.
    combina _ _ = funcion

-- Alguna básica satisface el predicado.
anyDib :: Pred a -> Dibujo a -> Bool
anyDib predicado = combinador predicado (||)

-- Todas las básicas satisfacen el predicado.
allDib :: Pred a -> Dibujo a -> Bool
allDib predicado = combinador predicado (&&)

-- Los dos predicados se cumplen para el elemento recibido.
andP :: Pred a -> Pred a -> a -> Bool
andP predicado1 predicado2 elemento = predicado1 elemento && predicado2 elemento

-- Algún predicado se cumple para el elemento recibido.
orP :: Pred a -> Pred a -> a -> Bool
orP predicado1 predicado2 elemento = predicado1 elemento || predicado2 elemento
