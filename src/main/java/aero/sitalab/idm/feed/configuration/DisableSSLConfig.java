package aero.sitalab.idm.feed.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;


@Component
public class DisableSSLConfig {

    public DisableSSLConfig() {

        final Logger log = LoggerFactory.getLogger(this.getClass());

        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {

                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {

                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            log.info("SSL verification disabled");

        } catch (Exception e) {
            log.error("Error while trying to disable SSL verification: " + e.getMessage(), e);
        }

    }
}