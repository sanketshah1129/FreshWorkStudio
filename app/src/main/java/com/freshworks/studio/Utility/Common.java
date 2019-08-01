package com.freshworks.studio.Utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Common {
    public static String Fragment_Trending = "Trending";
    public static String Fragment_Favourite = "Favourites";

    public static String NETWORK_ERROR_MSG = "Network Not Available";

    public static String ENTER_SEARCH_QUERY = "Please enter search text";


    public static String DATA_ERROR_MSG = "Error occured while getting data..";
    public static String WEBSERVICE_ERROR_MSG = "Data Sync couldn't completed properly. There might be connection problem with server." +
            " Please try to SYNC again.";
    public static String SYNC_SUCCESSFULLY_MSG = "Data Synced Successfully";
    public static String SYNC_ERROR_MSG = "Data Sync couldn't completed properly. There might be connection problem with server." +
            " Please try to SYNC again.";


    public static String Giphy_App_Key = "N01fulald9bbwszL76mcHG5EjnWvMT1c";


    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
