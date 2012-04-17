(ns nyampass-lunch.restaurants
  (use clojure.java.io))

(def ^:dynamic *restaurants* (atom {}))

(defn restaurants []
  @*restaurants*)

(defn reset-restaurants! []
  (reset! *restaurants* {}))

(defn add! [& {:keys [name weight] :or {weight 1} :as restaurant}]
  (swap! *restaurants* assoc name {:name name
                                   :weight weight}))

(defn remove! [restaurant-name]
  (swap! *restaurants* dissoc restaurant-name))

(defn decide []
  (letfn [(f [total-weight [first & rest]]
             (if first
               (let [total-weight (+ total-weight (:weight first))]
                 (cons [total-weight first]
                       (f total-weight
                          rest)))))]
    (let [sum-weight (reduce + (map :weight (vals  @*restaurants*)))
          weight-index (rand-int sum-weight)
          restaurant-with-totall-weights (f 0 (vals (restaurants)))]
      (some (fn [[total-weight r]]
              (if (< weight-index total-weight)
                r))
            restaurant-with-totall-weights))))

(defn store! [path]
  (with-open [writer (writer path)]
    (.write writer (str @*restaurants*))))

(defn load! [path]
  (if (.exists (java.io.File. path))
    (reset! *restaurants*
            (read-string (slurp path)))))

