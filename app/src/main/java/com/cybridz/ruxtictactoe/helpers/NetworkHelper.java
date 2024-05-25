package com.cybridz.ruxtictactoe.helpers;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.cybridz.AbstractActivity;
import com.cybridz.ruxtictactoe.StartActivity;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class NetworkHelper {
    /**
     * Validates an IPv4 address.
     *
     * @param input the IP address as a String.
     * @return true if the input parameter is a valid IPv4 address.
     */
    public static boolean isIPv4Address(String input) {
        Pattern IPV4_PATTERN = Pattern.compile(
                "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
        );
        return IPV4_PATTERN.matcher(input).matches();
    }

    public static boolean isNetworkAvailable(ConnectivityManager connectivityService) {
        NetworkInfo activeNetworkInfo = connectivityService.getActiveNetworkInfo();
        String ipv4 = getIPAddress();
        return ipv4 != null && activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public static void exitIfNetworkUnavailable(ConnectivityManager connectivityManager) {
        if (!isNetworkAvailable(connectivityManager)) {
            Log.d(AbstractActivity.LOGGER_KEY, "No network");
            System.exit(0);
        }
    }

    public static String getIPAddress() {
        try {
            List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : networkInterfaces) {
                List<InetAddress> inetAddresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress inetAddress : inetAddresses) {
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        if (NetworkHelper.isIPv4Address(hostAddress))
                            return hostAddress;
                    }
                }
            }
        } catch (SocketException e) {
            Log.e(AbstractActivity.LOGGER_KEY, "Unable to get host address.", e);
        }
        return null;
    }
}
