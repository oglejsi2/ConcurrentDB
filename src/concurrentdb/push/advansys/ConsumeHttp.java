/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
    https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
 */
package concurrentdb.push.advansys;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ConsumeHttp {

	private final String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) throws Exception {
                int locResopnseCode = -1;
                String locResponseMsg = null;

		ConsumeHttp http = new ConsumeHttp();


//		http.sendGet("http://10.10.11.105:5555/index.php/newNexioNotificationEvent", "[{\"ID\": \"12\", \"ACTION_TYPE\": \"'INSERT'\", \"OBJECT_TYPE\": \"'PUSH'\",\"ID_OBJECT\": \"'1'\"}]",100,100);;
//                http.sendPost("http://10.10.11.105:5555/index.php/newNexioNotificationEvent", "[{\"ID\": \"12\", \"ACTION_TYPE\": \"'INSERT'\", \"OBJECT_TYPE\": \"'PUSH'\",\"ID_OBJECT\": \"'1'\"}]",1000,1000);
                String f = http.sendGet("https://10.10.11.131:8181/WATest/webresources/myervices/get", null,1000,10000);
//                String f = http.sendGet("https://www.google.com", null,1000,3000);
//                String f = http.sendPost("http://10.10.11.131:8080/WATest/webresources/myervices/post", null,1000,3000);
                
                System.out.println("locResopnseCode: " + locResopnseCode);
                System.out.println("locResponseMsg: " + locResponseMsg);
                System.out.println("return: " + f);

	}

	// HTTP GET request
        
        public static String test(String pUrl, String pParameters, int pConnectTimeout) {
            return pUrl;
        }
        
	public static String sendGet(String pUrl, String pParameters, int pConnectTimeout, int pReadTimeout ) {
            String locReturn = "";
            try {
                String url;
                if ((pParameters==null || pParameters.isEmpty()  )) {
                    url = pUrl;                                    
                } else {
                    url = pUrl + pParameters;                                    
                }
			
                
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(new KeyManager[0], new TrustManager[] {new ConsumeHttp.DefaultTrustManager()}, new SecureRandom());
                SSLContext.setDefault(ctx);                
                
                URL obj = new URL(url);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
                
                
                con.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });                
                
                
                con.setConnectTimeout(pConnectTimeout);
                con.setReadTimeout(pReadTimeout);
                
                // optional default is GET
                con.setRequestMethod("GET");
                
                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);
                
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                //print result
                System.out.println(response.toString());
                
                locReturn=responseCode + ":" + response.toString();                
                if (locReturn.length()>2000) {
                    locReturn=locReturn.substring(0, 1999);
                }
                return locReturn;
            } catch (MalformedURLException ex) {                
                Logger.getLogger(ConsumeHttp.class.getName()).log(Level.SEVERE, null, ex);
                locReturn="-1:MalformedURLException:" +ex.toString();
                if (locReturn.length()>2000) {
                    locReturn=locReturn.substring(0, 1999);
                }                                
                return locReturn;
            } catch (IOException ex) {
                Logger.getLogger(ConsumeHttp.class.getName()).log(Level.SEVERE, null, ex);
                locReturn="-2:IOException:"+ex.toString();
                if (locReturn.length()>2000) {
                    locReturn=locReturn.substring(0, 1999);
                }                
                return locReturn;                                 
                
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ConsumeHttp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (KeyManagementException ex) {
                Logger.getLogger(ConsumeHttp.class.getName()).log(Level.SEVERE, null, ex);
            }
            return locReturn;
	}                
	
        
        public static String sendPost(String pUrl, String pParameters, int pConnectTimeout, int pReadTimeout)  {
            String locReturn="";
            try {
                String url = pUrl;
                URL obj = new URL(url);
                //HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                
                //add reuqest header
                con.setRequestMethod("POST");
                //con.setRequestProperty("User-Agent", USER_AGENT);
//		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                con.setConnectTimeout(pConnectTimeout);
                con.setReadTimeout(pReadTimeout);


                String urlParameters = pParameters;

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                if (!(urlParameters==null ||  urlParameters.isEmpty()  ))  {
                    wr.writeBytes(urlParameters);
                }
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                locReturn=responseCode + ":" + response.toString();
                if (locReturn.length()>2000) {
                    locReturn=locReturn.substring(0, 1999);
                }
                return locReturn;
            } catch (MalformedURLException ex) {
                Logger.getLogger(ConsumeHttp.class.getName()).log(Level.SEVERE, null, ex);
                locReturn="-3:MalformedURLException:"+ex.toString();
                if (locReturn.length()>2000) {
                    locReturn=locReturn.substring(0, 1999);
                }                
                return locReturn;
            } catch (ProtocolException ex) {
                Logger.getLogger(ConsumeHttp.class.getName()).log(Level.SEVERE, null, ex);                
                locReturn="-4:ProtocolException:"+ex.toString();
                if (locReturn.length()>2000) {
                    locReturn=locReturn.substring(0, 1999);
                }                
                return locReturn;                               
            } catch (IOException ex) {
                Logger.getLogger(ConsumeHttp.class.getName()).log(Level.SEVERE, null, ex);
                locReturn="-5:IOException:"+ex.toString();
                if (locReturn.length()>2000) {
                    locReturn=locReturn.substring(0, 1999);
                }                
                return locReturn;                                                
            }
        }
        
   private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }        

}