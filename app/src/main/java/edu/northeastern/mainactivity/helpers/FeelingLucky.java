package edu.northeastern.mainactivity.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class FeelingLucky implements Runnable{
    private static final String WIKIMEDIA_API_URL = "https://en.wikipedia.org/w/api.php"; // Replace with the actual Wikimedia API URL

    private static String getFirstSearchResult(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String searchUrl = WIKIMEDIA_API_URL + "?action=query&list=search&format=json&srsearch=" + encodedQuery;

            // Send HTTP request to the Wikimedia API and get the response
            Scanner scanner = new Scanner(new URL(searchUrl).openStream(), "UTF-8");
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            // Parse the response JSON to extract the URL of the first search result
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject queryObject = jsonResponse.getJSONObject("query");
            JSONArray searchResults = queryObject.getJSONArray("search");

            if (searchResults.length() > 0) {
                JSONObject firstResult = searchResults.getJSONObject(0);
                int pageId = firstResult.getInt("pageid");

                // Get the URL of the page using the page ID
                String pageUrl = getPageUrl(pageId);
                return pageUrl;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getPageUrl(int pageId) {
        try {
            String pageUrl = WIKIMEDIA_API_URL + "?action=query&prop=info&pageids=" + pageId + "&inprop=url&format=json";

            // Send HTTP request to the Wikimedia API and get the response
            Scanner scanner = new Scanner(new URL(pageUrl).openStream(), "UTF-8");
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            // Parse the response JSON to extract the URL of the page
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject queryObject = jsonResponse.getJSONObject("query");
            JSONObject pagesObject = queryObject.getJSONObject("pages");
            JSONObject pageObject = pagesObject.getJSONObject(Integer.toString(pageId));
            pageUrl = pageObject.getString("fullurl");

            return pageUrl;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public void run() {
        String query = "love is new death"; // Replace with your actual search query

        String firstResultUrl = getFirstSearchResult(query);

        if (firstResultUrl != null) {
            System.out.println(firstResultUrl);
           // openWebpage(firstResultUrl);
        } else {
            System.out.println("No search results found.");
        }
    }
}
