package server.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

import share.connection.ConnectionConstants;

import com.esotericsoftware.minlog.Log;

/**
 * class to get the ip
 * 
 * @author mirko conti
 * 
 */
public class ServerIp {

	private static final String LINK = "http://checkip.amazonaws.com";

	/**
	 * get my ip public (92.*.*.*)
	 * 
	 * @return ip public
	 */
	public String getMyPublicIp() {
		URL whatismyip;
		BufferedReader in = null;
		String ip = null;
		// for a fast test jump the connection on the URL
		if (Log.DEBUG) {
			return "debug test";
		}
		try {
			whatismyip = new URL(LINK);
			in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));
			ip = in.readLine();
		} catch (IOException e) {
			// fail connection
			Log.debug("getMyPublicIp", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Log.debug("getMyPublicIp close buffferReader", e);
				}
			}
		}
		return ip;
	}

	/**
	 * get my ip of lan (IP 198.168.xx.xx)
	 * 
	 * @return ip private
	 */
	public String getMyPrivateIp() {
		// This try will give the Public IP Address of the Host.
		String ip = null;
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface
					.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface iface = interfaces.nextElement();
				// filters out 127.0.0.1 and inactive interfaces
				if (iface.isLoopback() || !iface.isUp()) {
					continue;
				}
				Enumeration<InetAddress> addresses = iface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress addr = addresses.nextElement();
					if (!addr.getHostName().contains(":")) {
						ip = addr.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			Log.debug("getMyPrivateIp", e);
		}
		return ip;
	}

	/**
	 * return 127.0.0.1
	 * 
	 * @return
	 */
	public String getMyLocalIp() {
		return ConnectionConstants.IPLOCAL;
	}
}
