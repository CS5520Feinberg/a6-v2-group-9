package edu.northeastern.mainactivity;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

// to beautify json response
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Iterator;
import java.util.List;

public class APIMiddleware {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    static JsonParser parser = new JsonParser();

    public static JsonObject getDailyArticle(Context context) {
        String apiResponse = APIController.getDailyArticles();
        handleErrorResponse(context, apiResponse);
        JsonObject jsonArticle = (JsonObject) JsonParser.parseString(apiResponse);
        return (JsonObject) JsonParser.parseString(String.valueOf(jsonArticle.get("tfa")));
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
