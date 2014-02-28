(ns
  ^{:author cWX205128}
  main.Main
  (:use [clojure-csv.core])
  (:require [clojure.string :as cs]))

(with-open
  [rdr (clojure.java.io/reader
         "C:\\Users\\cwx205128\\Desktop\\aaaa.csv")]
  (loop [line (line-seq rdr)]
    (if (nil? (first line))
      nil
      (recur
        (do
          (let [
                 v (cs/split (first line) (re-pattern ","))
                 f (first v)
                 l (last v)
                 m (first (rest v))
                 ]
            (println (str "legend.metric." f "." (cs/replace l " " "") "=") (cs/trim m))
            )
          (rest line))))
    )
  )
