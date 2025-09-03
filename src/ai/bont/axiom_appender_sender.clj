(ns ai.bont.axiom-appender-sender
  (:require
    [hato.client :as hato]))

(def http-client (hato/build-http-client {:version :http-1.1}))

(defn send-message!
  [{:keys [api-domain api-token dataset-name encoder]} event]
  (when (and api-domain api-token dataset-name encoder)
    (try
      (hato/post (str "https://" api-domain "/v1/datasets/" dataset-name "/ingest")
                 {:http-client  http-client
                  :content-type :json
                  :headers      {"Authorization" (str "Bearer " api-token)
                                 "X-Axiom-Dataset" dataset-name}
                  :body         (String. (.encode encoder event))})
      (catch Exception e
        (println "Failed to send to Axiom:" (.getMessage e))))))
