package edu.northeastern.mainactivity;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class APIController {

    private static Handler apiHandler = new Handler();
    private static String baseURLString = "https://api.wikimedia.org/feed/v1/wikipedia/en/";

    public static String getRequest(URL url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            InputStream inputStream = conn.getInputStream();
            String resp = convertStreamToString(inputStream);
            return resp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String convertStreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String len;

            while((len=bufferedReader.readLine()) != null) {
                stringBuilder.append(len);
            }
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static URL checkURLFormation(String urlString) {
        try {
            URL target_url = new URL(urlString);
            return target_url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDailyArticles() {
        SimpleDateFormat today = new SimpleDateFormat("YYYY/mm/dd");
        String todayString = today.format(new Date());
        URL queryURL = checkURLFormation(baseURLString + "featured/" + todayString);

        String response = getRequest(queryURL);
        return response;
    }

    public static List<String> getLuckyArticle() {
        return null;
    }

    public static List<String> getSearchResult(String queryString) {
        return null;
    }
}
