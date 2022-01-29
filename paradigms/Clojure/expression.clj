;_____________HW9_____________
(defn constant [v] (fn [_] v))

(defn variable [v]
  (fn [m] (contains? m v) (m v)))

(defn abstract [f]
  (fn [& args] (fn [m] (apply f (mapv #(% m) args)))))

(def d (fn ([lonely] (/ (double 1) (double lonely)))
         ([first & rest] (/ (double first) (double (apply * rest))))))

(defn n [first & _] (- first))

(defn a [& args] (/ (apply + args) (count args)))

(defn g [& args] (Math/pow (Math/abs (apply * args)) (/ 1 (count args))))

(defn h [& args] (/ (double (count args)) (apply + (mapv #(/ 1 (double %)) args))))

(defn m [& args]
  (let [avg (apply a args)]
    (/ (apply + (mapv (fn [x] (* (- x avg) (- x avg))) args)) (count args))))

(def negate   (abstract n))
(def add      (abstract +))
(def subtract (abstract -))
(def multiply (abstract *))
(def divide   (abstract d))
(def sin      (abstract #(Math/sin %)))
(def cos      (abstract #(Math/cos %)))
(def sinh     (abstract #(Math/sinh %)))
(def cosh     (abstract #(Math/cosh %)))
(def sum      (abstract +))
(def avg      (abstract a))
(def mean     (abstract a))
(def varn     (abstract m))

(def Func_op {
              'negate negate
              '+ add
              '- subtract
              '* multiply
              '/ divide
              'sinh sinh
              'cosh cosh
              'sin sin
              'cos cos
              'sum sum
              'avg avg
              'mean mean
              'varn varn
              })

(defn parseFunction [line]
  (letfn
    [(parse [expr]
          (cond
            (number? expr) (constant expr)
            (symbol? expr) (variable (str expr))
            :else (apply (get Func_op (first expr)) (mapv parse (rest expr)))))]
    (parse (read-string line))))

;_____________HW10_____________
(defn proto-get
  "Returns object property respecting the prototype chain"
  ([obj key] (proto-get obj key nil))
  ([obj key default]
   (cond
     (contains? obj key) (obj key)
     (contains? obj :proto) (proto-get (obj :proto) key default)
     :else default)))

(defn proto-call
  "Calls object method respecting the prototype chain"
  [this key & args]
  (apply (proto-get this key) this args))

(defn field [key]
  "Creates field"
  (fn
    ([this] (proto-get this key))
    ([this def] (proto-get this key def))))

(defn method
  "Creates method"
  [key] (fn [this & args] (apply proto-call this key args)))

(declare Constant)
(declare Variable)
(declare Negate)
(declare Add)
(declare Subtract)
(declare Multiply)
(declare Divide)
(declare Sinh)
(declare Cosh)
(declare Sum)
(declare Avg)
(declare ArithMean)
(declare GeomMean)
(declare HarmMean)
(declare Zero)
(declare One)

(def _args    (field :args))
(def _value   (field :value))
(def _var     (field :var))
(def _f       (field :f))
(def _operand (field :operand))
(def evaluate       (method :evaluate))
(def toString       (method :toString))
(def diff           (method :diff))
(def toStringSuffix (method :toStringSuffix))

(defn CreateConstant [this value] (assoc this :value value))

(def ConstantPrototype
  {
   :toString #(str (format "%.1f" (_value %)))
   :evaluate (fn [this _] (_value this))
   :diff     (fn [_ _] Zero)
   :toStringSuffix toString
   })

(defn CreateVariable [this var] (assoc this :var var))

(def VariablePrototype
  {
   :toString #(str (_var %))
   :evaluate #(%2 (_var %1))
   :diff     #(if (= %2 (_var %1)) One Zero)
   :toStringSuffix toString
   })

(defn CreateOperation [this & args] (assoc this :args args))

(def OperationPrototype
  {
   :toString #(clojure.string/join ["("
                                    (_operand %) " "
                                    (clojure.string/join " " (for [arg (_args %)] (toString arg)))
                                    ")"])
   :evaluate (fn [this m] (apply (:f this) (mapv #(evaluate % m) (:args this))))
   :toStringSuffix #(clojure.string/join ["("
                                          (clojure.string/join " " (for [arg (_args %)] (toString arg)))
                                          " " (_operand %)
                                          ")"])
   })

(defn constructor
  ([ctor proto] #(apply ctor {:proto proto} [%]))
  ([ctor proto f operand diff] (fn [& args] (apply ctor {:proto proto :f f :operand operand :diff diff} args))))

(defn Create [operand & args]
  (let [f (first args) diff (second args)] (cond
    (.equals operand "const") (constructor CreateConstant ConstantPrototype)
    (.equals operand "var") (constructor CreateVariable VariablePrototype)
    :else (constructor CreateOperation OperationPrototype f operand diff))))

(defn diffMulDiv [s]
  (fn [this var]
    (let [a (first (_args this)) da (diff a var)]
      (cond
        (= 1 (count (_args this))) da
        :else (let [b (apply Multiply (rest (_args this))) db (diff b var)]
                (cond
                  (.equals s "*") (Add (Multiply da b) (Multiply a db))
                  :else (Divide (Subtract (Multiply da b) (Multiply a db)) (Multiply b b))))))))

(defn diffAddSub [s]
  (fn [this var]
    (let [das (mapv (fn [x] (diff x var)) (_args this))]
      (cond
        (.equals s "+") (apply Add das)
        (.equals s "-") (apply Subtract das)
        :else (apply Sum das)))))

(defn diffNSC [s]
  (fn [this var]
    (let [a (first (_args this)) da (diff a var)]
      (cond
        (.equals s "negate") da
        (.equals s "sinh") (Multiply (Cosh a) da)
        :else (Multiply (Sinh a) da)))))

(def Constant  (Create "const"))
(def Variable  (Create "var"))
(def Negate    (Create "negate" n (diffNSC "negate")))
(def Add       (Create "+" + (diffAddSub "+")))
(def Subtract  (Create "-" - (diffAddSub "-")))
(def Multiply  (Create "*" * (diffMulDiv "*")))
(def Divide    (Create "/" d (diffMulDiv "/")))
(def Sinh      (Create "sinh" #(Math/sinh %) (diffNSC "sinh")))
(def Cosh      (Create "cosh" #(Math/cosh %) (diffNSC "cosh")))
(def Sum       (Create "sum" + (diffAddSub "sum")))
(def Avg       (Create "avg" a (diffMulDiv "avg")))
(def ArithMean (Create "arith-mean" a (diffMulDiv "arith-mean")))
(def GeomMean  (Create "geom-mean" g (diffMulDiv "geom-mean")))
(def HarmMean  (Create "harm-mean" h (diffMulDiv "harm-mean")))

(def Zero (Constant 0.0))
(def One  (Constant 1.0))

(def Obj_op {
             'negate Negate
             '+ Add
             '- Subtract
             '* Multiply
             '/ Divide
             'sinh Sinh
             'cosh Cosh
             'sum Sum
             'avg Avg
             'arith-mean ArithMean
             'geom-mean GeomMean
             'harm-mean HarmMean
             })

(defn parseObject [line]
  (letfn [(parse [expr]
            (cond
              (number? expr) (Constant expr)
              (symbol? expr) (Variable (str expr))
              :else (apply (get Obj_op (first expr)) (mapv parse (rest expr)))))]
    (parse (read-string line)))
  )
