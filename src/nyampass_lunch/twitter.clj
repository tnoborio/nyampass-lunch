(ns nyampass-lunch.twitter
  (import [twitter4j TwitterFactory StatusUpdate]
          [twitter4j.conf ConfigurationBuilder]
          [twitter4j.auth RequestToken AccessToken]))

(defn- factory [{:keys [consumer-key consumer-secret] :as twitter-config}]
  (TwitterFactory. (.build (doto (ConfigurationBuilder.)
                             (.setOAuthConsumerKey consumer-key)
                             (.setOAuthConsumerSecret consumer-secret)))))

(defn access-token [{:keys [token secret] :as token-config}]
   (AccessToken. token secret))
  
(defn twitter [access-token twitter-config]
  (.getInstance (factory twitter-config) access-token))

(defn tweet [twitter status]
  (.updateStatus twitter status))

(defn mention-texts [twitter]
  (map #(.getText %) (.getMentions twitter)))
