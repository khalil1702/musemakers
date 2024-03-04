package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
public class FilterBadWord {
    public static String filter(String msg) {
        String result="";
        try {
            String apiURL="https://www.purgomalum.com/service/plain?text="+URLEncoder.encode(msg,"UTF-8");
            URL url=new URL(apiURL);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while((line=reader.readLine())!=null){
                result+=line;
            }
            reader.close();

        } catch (MalformedURLException ex) {
            System.out.println(ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }
}