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
  (first (vals @*restaurants*)))

(defn store! [path]
  (with-open [writer (writer path)]
    (.write writer (str @*restaurants*))))

(defn load! [path]
  (if (.exists (java.io.File. path))
    (reset! *restaurants*
            (read-string (slurp path)))))

