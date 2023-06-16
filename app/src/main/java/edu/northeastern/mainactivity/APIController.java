package edu.northeastern.mainactivity;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class APIController {
    private static String baseURLString = "https://api.wikimedia.org/feed/v1/wikipedia/en/";
    private static String searchURLString = "https://api.wikimedia.org/core/v1/wikipedia/en/search/page";

    public static String getRequest(URL url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

    public static URL addParamsToURL(URL url, Map<String, String> params) {
        // NOTE @shashankmanjunath: There must be an easier way to do this, but I can't find it....
        StringBuilder urlStringBuilder = new StringBuilder(url.toString() + "?");
        int num_steps = params.size();
        int counter = 1;
        for (Map.Entry<String, String> paramIter: params.entrySet()) {
            urlStringBuilder.append(paramIter.getKey().replace(" ", "+"));
            urlStringBuilder.append("=");
            urlStringBuilder.append(paramIter.getValue().replace(" ", "+"));

            if (counter != num_steps) {
                urlStringBuilder.append("&");
                counter += 1;
            }
        }
        String urlString = urlStringBuilder.toString();
        URL urlWithParams = checkURLFormation(urlString);
        return urlWithParams;
    }

    public static String getDailyArticles() {
        // Returns JSON string data of daily article for the current date
        SimpleDateFormat today = new SimpleDateFormat("yyyy/MM/dd");
        String todayString = today.format(new Date());
        URL queryURL = checkURLFormation(baseURLString + "featured/" + todayString);

        Log.e("log", queryURL.toString());
        String apiResponse = getRequest(queryURL);
        Log.e("log", apiResponse);
        return apiResponse;
    }

    public static String getLuckyArticle() {
        // NOTE @shashankmanjunath: Leaving this one for @VaibhavGarg to do

        return null;
    }


    public static String getSearchResult(String queryString, int numReturns) {
        URL queryURL = checkURLFormation(searchURLString);
        Map<String, String> params = new HashMap<String, String>();

        params.put("q", queryString);
        params.put("limit", String.valueOf(numReturns));

        URL queryURLWithParams = addParamsToURL(queryURL, params);

        Log.e("log", queryURLWithParams.toString());
        String apiResponse = getRequest(queryURLWithParams);

        if (apiResponse == null) {
            Log.e("log", "Empty return from API!");
        } else {
            Log.e("log", apiResponse);
        }
        return apiResponse;
    }
}
