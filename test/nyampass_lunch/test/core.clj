(ns nyampass-lunch.test.core
  (:use [nyampass-lunch.core])
  (:use [clojure.test]))

(deftest parse-mention-text-test
  (let [parsed-data (parse-mention-text "@tnoborio マクドナルド 10")]
    (is (= (:action parsed-data) :add))
    (is (= (-> parsed-data :data :name)
           "マクドナルド"))
    (is (= (-> parsed-data :data :weight)
           10)))
  (let [parsed-data (parse-mention-text "@tnoborio マクドナルド")]
    (is (= (:action parsed-data) :add))
    (is (= (-> parsed-data :data :name)
           "マクドナルド"))
    (is (= (-> parsed-data :data :weight)
           1)))
  (let [parsed-data (parse-mention-text "@tnoborio 削除 マクドナルド")]
    (is (= (:action parsed-data) :remove))
    (is (= (-> parsed-data :data :name)
           "マクドナルド"))))

;; (run-tests)