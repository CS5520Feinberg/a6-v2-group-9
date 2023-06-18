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

    public static JsonObject getRandomArticle(Context context) {
        String apiResponse = APIController.getLuckyArticle();
        handleErrorResponse(context, apiResponse);
        JsonObject jsonReturn = (JsonObject) JsonParser.parseString(apiResponse);
        return jsonReturn;
    }


    public static JsonArray searchArticles(String queryString, int numReturns, Context context) {
        String apiResponse = APIController.getSearchResult(queryString, numReturns);
        handleErrorResponse(context, apiResponse);
        JsonObject jsonReturn = (JsonObject) JsonParser.parseString(apiResponse);
        return (JsonArray) jsonReturn.get("pages");
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
        // } else {
        //     // Handle error responses from the API
        //     // You can parse the JSON response and check for specific error codes or messages
        //     // For example:
        //     if (apiResponse.contains("error")) {
        //         Log.e("log","An error occurred. Please try again later.");
        //         showErrorToast(context,"An error occurred. Please try again later.");
        //     }
        // }
    }
}
