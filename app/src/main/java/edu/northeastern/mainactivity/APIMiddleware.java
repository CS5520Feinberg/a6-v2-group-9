package edu.northeastern.mainactivity;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

// to beautify json response
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class APIMiddleware {
    private static String articleURLBase = "https://en.wikipedia.org/wiki/";

    public static ArrayList<JsonObject> getDailyArticle(Context context, int numArticles) {
        ArrayList<String> apiResponse = APIController.getDailyArticles(numArticles);
        ArrayList<JsonObject> dailyArticlesJson = new ArrayList<JsonObject>();

        for (int i=0; i<apiResponse.size(); i++) {
            String singleApiResponse = apiResponse.get(i);
            handleErrorResponse(context, singleApiResponse);
            JsonObject jsonArticle = (JsonObject) JsonParser.parseString(singleApiResponse);
            String featureArticle = String.valueOf(jsonArticle.get("tfa"));
            dailyArticlesJson.add((JsonObject) JsonParser.parseString(featureArticle));
        }
        return dailyArticlesJson;
    }

    public static String getRandomArticle(Context context) {
        String apiResponse = APIController.getLuckyArticle();
        handleErrorResponse(context, apiResponse);
        JsonObject jsonReturn = (JsonObject) JsonParser.parseString(apiResponse);
        JsonObject jsonContent = (JsonObject) jsonReturn.get("content_urls");
        JsonObject mobileContent = (JsonObject) jsonContent.get("desktop");
        String mobileURL = String.valueOf(mobileContent.get("page")).replace("\"", "");
        return mobileURL;
    }


    public static JsonArray searchArticles(String queryString, int numReturns, Context context) {
        String apiResponse = APIController.getSearchResult(queryString, numReturns);
        handleErrorResponse(context, apiResponse);
        JsonObject jsonReturn = (JsonObject) JsonParser.parseString(apiResponse);

        // Adding URL to JSON for each returned object
        JsonArray pages = (JsonArray) jsonReturn.get("pages");

        for (int i=0; i<pages.size(); i++) {
            JsonObject pagesElement = (JsonObject) pages.get(i);
            String key = String.valueOf(pagesElement.get("key")).replace("\"", "");
            pagesElement.addProperty("url", String.format("%s%s", articleURLBase, key));
        }

        return pages;
    }
    private static void showErrorToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private static void handleErrorResponse(Context context,String apiResponse) {
        if (apiResponse == null) {
            // Handle empty response or network error
            // display an error message or perform fallback behavior
            Log.e("log","Failed to fetch data. Please check your internet connection.");
            showErrorToast(context,"Failed to fetch data. Please check your internet connection.");
        }
    }
}
