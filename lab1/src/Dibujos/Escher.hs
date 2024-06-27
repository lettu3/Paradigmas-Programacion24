
module Dibujos.Escher where
import Dibujo
import Interp
import FloatingPic
import Grilla(grilla)
import qualified Graphics.Gloss.Data.Point.Arithmetic as V
import Graphics.Gloss
import Graphics.Gloss.Data.Vector

-- Supongamos que eligen.
type Escher = Bool

interpBas :: Output Escher
interpBas True x y w = pictures [line $ base x y w, interior x y w]
    where base x y w = map (x V.+) [(0,0), w, y, (0,0)]
          interior x y w = color black $ polygon $ base  (x V.+ ((half w) V.+ half(half w))) ((half(half y))) (half(half w))
interpBas False x y w = Blank

-- El dibujo u. Basado en el artículo de Henderson.
dibujoU :: Dibujo Escher -> Dibujo Escher
--dibujoU p = encimar4 (espejar (rot45 p))
dibujoU figura1 = encimar (encimar figura2 (r90(figura2))) (encimar (r180(figura2)) (r270(figura2)))
    where figura2 = espejar(r90(figura1))

-- El dibujo t. Basado en el artículo de Henderson.
dibujoT :: Dibujo Escher -> Dibujo Escher
dibujoT p = encimar p (encimar p2 p3) 
    where 
        p2 = espejar(rot45(p))
        p3 = (r270(p2))

vacio :: Dibujo Escher
--vacio = _
vacio = figura False
--vacio figura _

-- Esquina con nivel de detalle en base a la figura p.
-- Muy parecido a los lados
esquina :: Int -> Dibujo Escher -> Dibujo Escher
esquina 1 p = cuarteto vacio vacio vacio dibU where dibU = dibujoU p
esquina n p = cuarteto (esquina (n-1) p) (lado (n-1) p) (rotar (lado (n-1) p)) dibU where dibU = dibujoU p

-- Lado con nivel de detalle.
-- Se define un lado y después lo generalizamos a n lados recursivos.
lado :: Int -> Dibujo Escher -> Dibujo Escher
lado 1 p = cuarteto vacio vacio (rotar dibT) dibT where dibT = dibujoT p
lado n p = cuarteto (lado (n-1) p) (lado (n-1) p) (espejar(dibT)) dibT where dibT = dibujoT p

-- Por suerte no tenemos que poner el tipo!
-- Combinador de las nueve figuras hace column . map row

noneto p q r s t u v w x = apilar 1 2 (juntar 1 2 p (juntar 1 1 q r)) 
                           (apilar 1 1 (juntar 1 2 s (juntar 1 1 t u)) (juntar 1 2 v (juntar 1 1 w x)))

-- El dibujo de Escher:
escher :: Int -> Escher -> Dibujo Escher
--Usamos squarelimit2 de Henderson pero generalizada con n. La usamos con un triangulo como forma básica.
escher n p = noneto 
        (esquina n (figura p))
        (lado n (figura p)) --lado de arriba
        (espejar(esquina n (figura p))) 
        (rotar (lado n (figura p))) --lado izquierdo.
        (u) 
        (espejar(rotar(lado n (figura p)))) --lado derecho
        (rotar (esquina n (figura p))) 
        (r180 (lado n (figura p))) --lado de abajo
        (r180 (esquina n (figura p)))
    where
        u = dibujoU (figura p)
                       
escherConf :: Conf
escherConf = Conf {
    name = "Escher",
    pic = escher 6 True,
    bas = interpBas
}