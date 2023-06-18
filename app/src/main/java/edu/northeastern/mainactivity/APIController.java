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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class APIController {
    private static String baseURLString = "https://api.wikimedia.org/feed/v1/wikipedia/en/";
    private static String searchURLString = "https://api.wikimedia.org/core/v1/wikipedia/en/search/page";
    private static String randomURLString = "https://en.wikipedia.org/api/rest_v1/page/random/summary";

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

    public static ArrayList<String> getDailyArticles(int numArticles) {
        // Returns JSON string data of daily article for the current date

        SimpleDateFormat today = new SimpleDateFormat("yyyy/MM/dd");
        Date curDate = new Date();

        ArrayList<String> dailyArticles = new ArrayList<String>();
        String todayString = today.format(curDate);
        URL queryURL = checkURLFormation(baseURLString + "featured/" + todayString);
        String apiResponse = getRequest(queryURL);
        dailyArticles.add(apiResponse);

        for (int i=0; i<numArticles-1; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(curDate);
            cal.add(Calendar.DAY_OF_YEAR, -1);
            curDate = cal.getTime();

            String curDateString = today.format(curDate);
            queryURL = checkURLFormation(baseURLString + "featured/" + curDateString);
            apiResponse = getRequest(queryURL);

            dailyArticles.add( apiResponse);
        }

        return dailyArticles;
    }

    public static String getLuckyArticle() {
        URL queryURL = checkURLFormation(randomURLString);
        String apiResponse = getRequest(queryURL);
        return apiResponse;
    }


    public static String getSearchResult(String queryString, int numReturns) {
        URL queryURL = checkURLFormation(searchURLString);
        Map<String, String> params = new HashMap<String, String>();

        params.put("q", queryString);
        params.put("limit", String.valueOf(numReturns));

        URL queryURLWithParams = addParamsToURL(queryURL, params);

        Log.d("log", "Calling API at: " + queryURLWithParams.toString());
        String apiResponse = getRequest(queryURLWithParams);

        if (apiResponse == null) {
            Log.d("log", "Empty return from API!");
        } else {
            // Log.e("log", apiResponse);
            Log.d("log", "API response received");
        }
        return apiResponse;
    }
}
