package edu.northeastern.mainactivity.helpers;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

class FeelingLucky {
    private static final String WIKIMEDIA_API_URL = "https://en.wikipedia.org/w/api.php"; // Replace with the actual Wikimedia API URL

//    public static void main(String[] args) {
//        String query = "your search query"; // Replace with your actual search query
//
//        String url = getFirstSearchResult(query);
//        if (url != null) {
//            openWebpage(url);
//        } else {
//            System.out.println("No search results found.");
//        }
//    }

    private static String getFirstSearchResult(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String searchUrl = WIKIMEDIA_API_URL + "?action=query&list=search&format=json&srsearch=" + encodedQuery;

            // Send HTTP request to the Wikimedia API and get the response
            Scanner scanner = new Scanner(new URL(searchUrl).openStream(), "UTF-8");
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            // Parse the response JSON to extract the URL of the first search result
            // Replace the following logic with your actual implementation

            // Assuming the API response contains a 'search' object with an array of 'results' and each result has a 'url' property
            // Extract the URL of the first result
            String firstResultUrl = ""; // Replace with your logic to extract the URL

            return firstResultUrl;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // The openWebpage() method remains the same as in the previous response
}
