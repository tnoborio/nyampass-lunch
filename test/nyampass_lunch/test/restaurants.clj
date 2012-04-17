(ns nyampass-lunch.test.restaurants
  (use [clojure.test])
  (require [nyampass-lunch.restaurants :as r]))

(deftest add-and-decide-restaurants-test
  (r/reset-restaurants!)
  (is (= (count (r/restaurants)) 0))
  (r/add! :name "はまずし")
  (is (= (count (r/restaurants)) 1))
  (let [{:keys [name weight] :as restaurant} (r/decide)]
    (is (= name "はまずし"))
    (is (= weight 1)))
  (r/add! :name "はまずし")
  (is (= (count (r/restaurants)) 1)))

(deftest decide-by-weight-restaurants-test
  (r/reset-restaurants!)
  (r/add! :name "はまずし" :weight 3)
  (r/add! :name "はなまるうどん" :weight 1)
  (let [decides (frequencies (for [_ (range 100)] (:name (r/decide))))]
    (is (get decides "はまずし"))
    (is (get decides "はなまるうどん"))
    (is (> (get decides "はまずし") (get decides "はなまるうどん")))))

(deftest remove-restaurant-test
  (r/reset-restaurants!)
  (r/add! :name "はまずし")
  (r/add! :name "はなまるうどん" :weight 4)
  (r/remove! "はまずし")
  (is (= (count (r/restaurants)) 1))
  (r/remove! "はなまるうどん")
  (is (= (count (r/restaurants)) 0)))

(deftest store-restaurants-test
  (r/reset-restaurants!)
  (r/add! :name "はまずし")
  (r/add! :name "はなまるうどん" :weight 4)
  (let [path "./test-restaurants"]
    (r/store! path)
    (r/reset-restaurants!)
    (is (= (count (r/restaurants)) 0))
    (r/load! path)
    (is (= (count (r/restaurants)) 2))))

;; (run-tests)