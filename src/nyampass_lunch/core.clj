(ns nyampass-lunch.core
  (require [nyampass-lunch
            [twitter :as twitter]
            [restaurants :as restaurants]]
           [clojure.string :as strings]))

(def twitter-config
     {:consumer-key "ilJ4v04USwglD0ocJJPw"
      :consumer-secret "WCL4wEikloE6V6oh9T5a6AeSM8whb4z6tpmIdpZ2AM"})

(def token-config
     {:token "549898679-I2jeyvjnIALFHZQqgHRCFDBcA9o1Qt9bisQGIEn8"
      :secret "PKYWyt2woYwwsQ4FnOhORnfCmNSVdiUM4HhCO91NNs"})

(def restaurants-path "/Users/tokusei/work/nyampass/nyampass-lunch/restaurants")

(defn parse-mention-text [text]
  (prn :mention text)
  (let [fields (strings/split text #" +")]
    (if (= (second fields)
           "削除")
      {:action :remove
       :data {:name (nth fields 2)}}
      (let [[_ restarutant-name & [weight]] fields]
        (try
          (when restarutant-name
            {:action :add
             :data {:name restarutant-name
                    :weight (or (and weight (Integer/parseInt weight))
                                1)}})
          (catch NumberFormatException e nil))))))
            

(defn mentions [twitter-instance]
  (let [texts (twitter/mention-texts twitter-instance)]
    (doseq [text texts]
      (when-let [{:keys [action data]} (parse-mention-text text)]
        (case action
              :add
              (apply restaurants/add!
                     (apply concat (seq data)))
              :remove
              (restaurants/remove! (:name data))))))
  (restaurants/store! restaurants-path))
  
(defn tweet [twitter-instance]
  (let [restaurant (restaurants/decide)]
    (twitter/tweet twitter-instance
                    (:name restaurant))))

(defn -main [& [mode]]
  (restaurants/load! restaurants-path)
  (let [token (twitter/access-token token-config)
        twitter-instance (twitter/twitter token twitter-config)]
    (case (keyword mode)
          :mentions (mentions twitter-instance)
          :test (prn (restaurants/decide))
          (tweet twitter-instance))))

;; (-main "mentions")
;; (-main "test")
