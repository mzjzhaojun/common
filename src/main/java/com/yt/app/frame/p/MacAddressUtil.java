package com.yt.app.frame.p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MacAddressUtil {

	public static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}

	public static String getWindowXPMACAddress(String execStr) {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(execStr);
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.indexOf("本地连接") != -1) 
					continue;

				index = line.toLowerCase().indexOf("physical address");
				if (index != -1) {
					index = line.indexOf(":");
					if (index != -1) {
						mac = line.substring(index + 1).trim();
					}
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			bufferedReader = null;
			process = null;
		}
		return mac;
	}

	public static String getWindow7MACAddress() {
		InetAddress ia = null;
		try {
			ia = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		byte[] mac = null;
		try {
			mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			String s = Integer.toHexString(mac[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}
		return sb.toString().toUpperCase();
	}

	public static String getLinuxMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("ifconfig eth0");
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				index = line.toLowerCase().indexOf("硬件地址");
				if (index != -1) {
					mac = line.substring(index + 4).trim();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			bufferedReader = null;
			process = null;
		}
		return mac;
	}

	public static String getUnixMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("ifconfig eth0");
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				index = line.toLowerCase().indexOf("hwaddr");
				if (index != -1) {
					mac = line.substring(index + "hwaddr".length() + 1).trim();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			bufferedReader = null;
			process = null;
		}

		return mac;
	}

	public static String getMacAddress() {
		String os = getOSName();
		String execStr = getSystemRoot() + "/system32/ipconfig /all";
		String mac = null;
		if (os.startsWith("windows")) {
			if (os.equals("windows xp")) {// xp
				mac = getWindowXPMACAddress(execStr);
			} else if (os.equals("windows 2003")) {// 2003
				mac = getWindowXPMACAddress(execStr);
			} else {// win7
				mac = getWindow7MACAddress();
			}
		} else if (os.startsWith("linux")) {
			mac = getLinuxMACAddress();
		} else {
			mac = getUnixMACAddress();
		}
		return mac;
	}

	public static String getSystemRoot() {
		String cmd = null;
		String os = null;
		String result = null;
		String envName = "windir";
		os = System.getProperty("os.name").toLowerCase();
		if (os.startsWith("windows")) {
			cmd = "cmd /c SET";
		} else {
			cmd = "env";
		}
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			InputStreamReader isr = new InputStreamReader(p.getInputStream());
			BufferedReader commandResult = new BufferedReader(isr);
			String line = null;
			while ((line = commandResult.readLine()) != null) {
				line = line.toLowerCase();
				if (line.indexOf(envName) > -1) {
					result = line.substring(line.indexOf(envName) + envName.length() + 1);
					return result;
				}
			}
		} catch (Exception e) {
			System.out.println(" error: " + cmd + ":" + e);
		}
		return null;
	}

}
