(defn abstract_linear [f]
    (fn [t1, t2]
        (if (vector? t1)
            (mapv (abstract_linear f) t1 t2)
            (f t1 t2))))

(def t+ (abstract_linear +))
(def t- (abstract_linear -))
(def t* (abstract_linear *))
(def td (abstract_linear /))

(def v+ t+)
(def v- t-)
(def v* t*)
(def vd td)

(defn v*s [v, s] (mapv (fn [a] (* a s)) v))
(defn scalar [v1, v2] (apply + (v* v1 v2)))
(defn vect [v1, v2]
    (let [[a1 a2 a3] v1 [b1 b2 b3] v2]
         [(- (* a2 b3) (* a3 b2))
          (- (* a3 b1) (* a1 b3))
          (- (* a1 b2) (* a2 b1))]))

(def m+ t+)
(def m- t-)
(def m* t*)
(def md td)

(defn transpose [m] (apply mapv vector m))
(defn m*s [m, s] (mapv (fn [a] (v*s a s)) m))
(defn m*v [m, v] (mapv (fn [a] (scalar a v)) m))
(defn m*m [m1, m2] (mapv (fn [v1] (mapv (fn [v2] (scalar v1 v2)) (transpose m2))) m1))

(def c+ t+)
(def c- t-)
(def c* t*)
(def cd td)
