package com.cybridz.ruxtictactoe.helpers;

import android.net.ConnectivityManager;
import android.util.Log;

import com.cybridz.AbstractActivity;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class NetworkHelper {

    private final static Pattern IPV4_PATTERN = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    public static boolean isIPv4Address(String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    public static boolean isNetworkAvailable(ConnectivityManager connectivityService) {
        return getIPAddress() != null && connectivityService.getActiveNetworkInfo() != null && connectivityService.getActiveNetworkInfo().isConnected();
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
