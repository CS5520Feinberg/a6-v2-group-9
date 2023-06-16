package edu.northeastern.mainactivity;

import android.content.Context;
import android.widget.Toast;

// to beautify json response
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class APIMiddleware {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    static JsonParser parser = new JsonParser();

    public static String getDailyArticle(Context context) {
        String apiResponse = APIController.getDailyArticles();
        handleErrorResponse(context, apiResponse);
        return gson.toJson(parser.parse(apiResponse));
    }

    public static String searchArticles(String queryString, int numReturns, Context context) {
        String apiResponse = APIController.getSearchResult(queryString, numReturns);
        handleErrorResponse(context,apiResponse);
        return gson.toJson(parser.parse(apiResponse));
    }
    private static void showErrorToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private static void handleErrorResponse(Context context,String apiResponse) {
        if (apiResponse == null) {
            // Handle empty response or network error
            // display an error message or perform fallback behavior
             showErrorToast(context,"Failed to fetch data. Please check your internet connection.");
        } else {
            // Handle error responses from the API
            // You can parse the JSON response and check for specific error codes or messages
            // For example:
             if (apiResponse.contains("error")) {
                 showErrorToast(context,"An error occurred. Please try again later.");
             }
        }
    }
}
