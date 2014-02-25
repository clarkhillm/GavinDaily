(ns
  ^{:author cWX205128}
  test.Main
  (:import [org.apache.http.impl.client HttpClients BasicCredentialsProvider]
           [org.apache.http.client.methods HttpGet]
           [org.apache.http HttpHost]
           [org.apache.http.impl.conn DefaultProxyRoutePlanner]
           [org.apache.http.auth AuthScope UsernamePasswordCredentials]
           [java.security KeyStore]
           [java.io FileInputStream File]
           [org.apache.http.conn.ssl SSLContexts TrustSelfSignedStrategy SSLConnectionSocketFactory]
           [org.apache.http.client.utils URIBuilder]))

(def gitHubToken "256293739d62438c92cf1a45debe281f740ac532")

(def trustStore (. KeyStore (getInstance (. KeyStore getDefaultType))))
(. trustStore (load
                (FileInputStream. (File. "C:\\Users\\cwx205128\\.keystore"))
                (. "changeit" toCharArray)))

(def sslcontext (.. SSLContexts custom (useTLS)
                  (loadTrustMaterial trustStore (TrustSelfSignedStrategy.))
                  build))

(def sslsf (SSLConnectionSocketFactory. sslcontext (. SSLConnectionSocketFactory ALLOW_ALL_HOSTNAME_VERIFIER)))

(def credsProvider (BasicCredentialsProvider.))
(.. credsProvider (setCredentials
                    (AuthScope. "openproxy.huawei.com",8080)
                    (UsernamePasswordCredentials. "cwx205128", "@Edn1aEf")))

(def credsProvider_git (BasicCredentialsProvider.))
(.. credsProvider_git (setCredentials
                    (AuthScope. "api.github.com",433)
                    (UsernamePasswordCredentials. "clarkhillm@gmail.com", "clarkHill&&12@&")))

(def hy_proxy (HttpHost. "openproxy.huawei.com",8080))
(def routePlanner (DefaultProxyRoutePlanner. hy_proxy))

(def http_client (.. HttpClients custom
  
                   (setDefaultCredentialsProvider credsProvider)
                   (setRoutePlanner routePlanner)
                   (setHostnameVerifier (. SSLConnectionSocketFactory ALLOW_ALL_HOSTNAME_VERIFIER))
                   (setSSLSocketFactory sslsf)
                   build))


(def httpGet (HttpGet. "https://api.github.com"))


(. httpGet (addHeader "Authorization" "token 256293739d62438c92cf1a45debe281f740ac532"))

(println (alength (.. httpGet getAllHeaders)))

(def response (. http_client execute httpGet))

(println (.. response getStatusLine getStatusCode))
(println (.. response getStatusLine getReasonPhrase))
(println (.. response getStatusLine toString))


(def inputStream (.. response getEntity getContent))

(with-open [r inputStream]
  (loop [c (.read r)]
    (if (not= c -1)
      (do (print (char c)) (recur (.read r))))))

