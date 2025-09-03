(ns ai.bont.axiom-appender-bridge
  (:gen-class
    :extends ch.qos.logback.core.UnsynchronizedAppenderBase
    :name ai.bont.AxiomLogbackAppender
    :init init
    :state state
    :methods [[setApiDomain [String] void]
              [setApiToken [String] void]
              [setDatasetName [String] void]
              [setEncoder [net.logstash.logback.encoder.LogstashEncoder] void]]))

(defn -init []
  [[] (atom {})])

(defn -append [this e]
  (try
    (require 'ai.bont.axiom-appender-sender)
    (let [sender-fn (resolve 'ai.bont.axiom-appender-sender/send-message!)]
      (if sender-fn
        (sender-fn @(.state this) e)
        (println "ERROR: Could not resolve sender function")))
    (catch Exception ex
      (println "ERROR in -append:" (.getMessage ex))
      (.printStackTrace ex))))

(defn -setEncoder [this encoder]
  (swap! (.state this) assoc :encoder encoder))

(defn -setApiDomain [this api-domain]
  (swap! (.state this) assoc :api-domain api-domain))

(defn -setApiToken [this api-token]
  (swap! (.state this) assoc :api-token api-token))

(defn -setDatasetName [this dataset-name]
  (swap! (.state this) assoc :dataset-name dataset-name))
