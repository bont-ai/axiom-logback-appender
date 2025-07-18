(ns ai.bont.axiom-logback-appender
  (:require
    [hato.client :as hato])
  (:gen-class
    :extends ch.qos.logback.core.UnsynchronizedAppenderBase
    :name ai.bont.AxiomLogbackAppender
    :init init
    :state state
    :methods [[setApiDomain [String] void]
              [setApiToken [String] void]
              [setDatasetName [String] void]
              [setEncoder [net.logstash.logback.encoder.LogstashEncoder] void]]))

(def http-client (hato/build-http-client {}))

(defn send-message!
  [{:keys [api-domain api-token dataset-name encoder]} event]
  (try
    (hato/post (str "https://" api-domain "/v1/datasets/" dataset-name "/ingest")
               {:http-client  http-client
                :content-type :json
                :headers      {"Authorization"   (str "Bearer " api-token)
                               "X-Axiom-Dataset" dataset-name}
                :body         (String. (.encode encoder event))})
    (catch Exception e
      (println "Failed to send log to Axiom:" (.getMessage e)))))

(defn -init []
  [[] (atom {})])

(defn -append [this e]
  (send-message! @(.state this) e))

(defn -setEncoder [this encoder]
  (swap! (.state this) assoc :encoder encoder))

(defn -setApiDomain [this api-domain]
  (swap! (.state this) assoc :api-domain api-domain))

(defn -setApiToken [this api-token]
  (swap! (.state this) assoc :api-token api-token))

(defn -setDatasetName [this dataset-name]
  (swap! (.state this) assoc :dataset-name dataset-name))
