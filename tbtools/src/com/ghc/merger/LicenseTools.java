package com.ghc.merger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LicenseTools {

	private static final String LTIME_KEY = "xx-t-v";

	private static final String LMAC_KEY = "xx-m-v";

	private static String getMCode() throws Exception {

		// 获取网卡，获取地址
		try {
			InetAddress ia = InetAddress.getLocalHost();
			byte[] mac = NetworkInterface.getByInetAddress(ia)
					.getHardwareAddress();
			StringBuffer sb = new StringBuffer("");
			for (int i = 0; i < mac.length; i++) {
				if (i != 0) {
					sb.append("-");
				}
				// 字节转换为整数
				int temp = mac[i] & 0xff;
				String str = Integer.toHexString(temp);
				if (str.length() == 1) {
					sb.append("0" + str);
				} else {
					sb.append(str);
				}
			}
			String code = sb.toString().toUpperCase();
			return code;
		} catch (UnknownHostException e) {
			throw new Exception("电脑没有联网,请联网!");
		} catch (SocketException e) {
			throw new Exception("电脑没有联网,请联网!");
		}
	}

	public static String getUniCode() throws Exception {
		String code = getMCode();
		try {
			// 生成一个MD5加密计算摘要
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        // 计算md5函数
	        md.update(code.getBytes());
	        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
	        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
	        return new BigInteger(1, md.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return code;
	}

	public static long getTimeCode(long time) {
		return ~time;
	}

	/**
	 * 0: 授权成功 1: 没有授权文件 2: 机器码不对 3: 授权过期
	 * 
	 * @return
	 */
	public static int doCheck() {
		try {
			File licenseFile = new File("License");
			if (licenseFile.exists()) {
				InputStreamReader licenseFileisr = new InputStreamReader(
						new FileInputStream(licenseFile), "UTF-8");
				BufferedReader licenseFileIO = new BufferedReader(
						licenseFileisr);
				String tmpLine = null;
				Map<String, String> mapper = new HashMap<String, String>();
				while ((tmpLine = licenseFileIO.readLine()) != null) {
					String[] split = tmpLine.split(":");
					if (split.length > 1) {
						mapper.put(split[0].trim(), split[1].trim());
					}
				}
				String mCode = mapper.get(LMAC_KEY);
				if (mCode == null || !mCode.equals(LicenseTools.getPriMCode())) {
					return 2;
				}

				String mTime = mapper.get(LTIME_KEY);
				if (mTime == null
						|| LicenseTools.getTimeCode(Long.parseLong(mTime)) < System
								.currentTimeMillis()) {
					return 3;
				}
			}
			return 1;
			//
			// OutputStreamWriter outW = new OutputStreamWriter(
			// new FileOutputStream(licenseFile), "UTF-8");
			// BufferedWriter writer = new BufferedWriter(outW);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return 0;
	}

	private static String getPriMCode() throws Exception {
		String uniCode = getUniCode();
		return getPriMCode(uniCode);
	}

	private static String getPriMCode(String uniCode) throws Exception {
		if (uniCode != null) {
			uniCode = 312 + uniCode + "ghc";
			
			// 生成一个MD5加密计算摘要
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        // 计算md5函数
	        md.update(uniCode.getBytes());
	        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
	        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
	        return new BigInteger(1, md.digest()).toString(16);
		}
		return null;
	}

	public static String importLic(File importFile) {

		InputStreamReader licenseFileisr = null;
		BufferedReader licenseFileIO = null;

		OutputStreamWriter outW = null;
		BufferedWriter writer = null;

		try {
			licenseFileisr = new InputStreamReader(new FileInputStream(
					importFile), "UTF-8");
			licenseFileIO = new BufferedReader(licenseFileisr);
			String tmpLine = null;
			List<String> licenseFileLines = new ArrayList<String>();
			Map<String, String> mapper = new HashMap<String, String>();
			while ((tmpLine = licenseFileIO.readLine()) != null) {
				licenseFileLines.add(tmpLine);
				String[] split = tmpLine.split(":");
				if (split.length > 1) {
					mapper.put(split[0].trim(), split[1].trim());
				}
			}
			String mCode = mapper.get(LMAC_KEY);
			if (mCode == null || !mCode.equals(LicenseTools.getPriMCode())) {
				return "该授权文件和本电脑唯一标示码不匹配！";
			}

			String mTime = mapper.get(LTIME_KEY);
			if (mTime == null
					|| LicenseTools.getTimeCode(Long.parseLong(mTime)) < System
							.currentTimeMillis()) {
				return "该授权文件已过期，请重新购买！";
			}

			File licenseFile = new File("License");
			
//			licenseFile.createNewFile();  
			// R ： 只读文件属性。A：存档文件属性。S：系统文件属性。H：隐藏文件属性。  
			String sets = "attrib +H \"" + licenseFile.getAbsolutePath() + "\"";  
			// 运行命令  
			Runtime.getRuntime().exec(sets);  
			 
			outW = new OutputStreamWriter(new FileOutputStream(licenseFile),
					"UTF-8");
			writer = new BufferedWriter(outW);

			writer.write("请不要修复该文件内的任何内容，否则授权将失效，后果自负！！！非常重要！！！非常非常重要！！！");
			writer.newLine();
			for (String line : licenseFileLines) {
				writer.write(line);
				writer.newLine();
			}
			writer.flush();

		} catch (Exception e) {
			return e.getMessage();
		} finally {
			if (licenseFileIO != null) {
				try {
					licenseFileIO.close();
				} catch (IOException e) {
				}
			}

			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	public static String exportLic(String exprotFile, String month, String code) {
		OutputStreamWriter outW = null;
		BufferedWriter writer = null;

		try {
			int monthInt = Integer.parseInt(month.trim());
			if(monthInt<0){
				monthInt = 12*10;
			}
			long monthTime = monthInt * 31 * 24 * 60 * 60 * 1000;
			outW = new OutputStreamWriter(new FileOutputStream(exprotFile+"\\"+code+"的授权证书.ghc"),
					"UTF-8");
			writer = new BufferedWriter(outW);

			writer.write(LMAC_KEY + ":" + getPriMCode(code));
			writer.newLine();
			writer.write(LTIME_KEY + ":"
					+ getTimeCode(System.currentTimeMillis() + monthTime));

		} catch (Exception e) {
			return e.getMessage();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
			if (outW != null) {
				try {
					outW.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

}
