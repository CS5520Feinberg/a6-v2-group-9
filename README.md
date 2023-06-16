# Summer 2023 CS5520 Mobile App Development Group 9 Assignment App

Jialin Huang, Kevin Lin, Akshat Alkesh Gandhi, Vaibhav Garg, Shashank Manjunath

## Backend Notes

- Make sure to call backend in a new thread! An example is included in the
  MainActivity class. Not doing this will lead to an error!
- I encountered errors using the emulator, and ended up testing this on a
  physical device (Google Pixel 6a) to get it to work
- APIController Class Notes
  - Methods:
    - getDailyArticles() - Returns daily feature article for the current day
    - getSearchResult(String queryString, int numReturns) - Returns
      numReturns search results for query queryString
      - Pass query string as a regular string with spaces, e.g. "cantor set", "solar system", "earth"
      - We use the addParamsToURL method to add the query and number of returns to the API query
  - Data is returned as a JSON-formatted string 

- APIMiddleWare -  (Don't use APIController in the activity)
  - There are two functions and they both will return formatted json object as string
    - getDailyArticle(Context context) - handles the getDailyArticles() from APIController
    - searchArticles(String queryString, int numReturns, Context context) - handles getSearchResult() from APIController
  - While calling these methods in an activity, use "getActivityContext()" to send context in the params.