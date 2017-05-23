package com.ghc.merger;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BatchFileMerger {

	private Frame parent = null;

	private String xkbFilePath = "D:\\make_by_guohuichao\\选款表.csv";

	private String ysbtDir = "D:\\make_by_guohuichao\\分裂预设标题表";

	private String origFilePath = "D:\\make_by_guohuichao\\orig";

	public BatchFileMerger(String home) {
		if (home != null) {
			File file = new File(home);
			if (file.exists()) {
				this.xkbFilePath = home + "选款表.csv";
				this.ysbtDir = home + "分裂预设标题表";
				this.origFilePath = home + "orig";
			}
		}
	}

	public BatchFileMerger(Frame parent, String xkbFilePath, String ysbtDir,
			String origFilePath) {
		this.parent = parent;
		if (!isEmpty(xkbFilePath)) {
			this.xkbFilePath = xkbFilePath;
		}
		if (!isEmpty(ysbtDir)) {
			this.ysbtDir = ysbtDir;
		}
		if (!isEmpty(origFilePath)) {
			this.origFilePath = origFilePath;
		}
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.length() <= 0) {
			return true;
		}
		return false;
	}

	public BatchFileMerger fileStartMerge() {
		Map<String, String[]> mapper = this.getMapper(xkbFilePath);
		if (mapper == null || mapper.size() < 1) {
			return this;
		}
//		File target = new File(home + "\\target");
//		if (!target.exists()) {
//			target.mkdirs();
//		}
		File[] origFiles = getOrigFiles();
		for (File file : origFiles) {
			mergeFile(mapper, file);
		}
		return this;
	}

	private File[] getOrigFiles() {
		File file = new File(origFilePath);
		if (file.exists() && file.isDirectory()) {
			File[] csvFiles = file.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					if (pathname.isFile()
							&& pathname.getName().toLowerCase().endsWith(".csv")) {
						return true;
					}
					return false;
				}
			});
			return csvFiles;
		}else if(file.isFile() && file.getName().toLowerCase().endsWith(".csv")){
			return new File[]{file};
		}
		return new File[0];
	}

	public void mergeFile(Map<String, String[]> mapper, File targetFile) {
		try {
			FileInputStream in = new FileInputStream(targetFile);
			String targetCharCode = codeString(targetFile.getPath());
			InputStreamReader targetFileisr = new InputStreamReader(in,
					targetCharCode);
			BufferedReader targetFileIO = new BufferedReader(targetFileisr);
			String[] targetFileHead = new String[3];
			Map<String, String> targetKV = new HashMap<String, String>();
			int i = 0;
			String tmpTarget = null;
			String preKey = "";
			while ((tmpTarget = targetFileIO.readLine()) != null) {
				if (i < 3) {
					targetFileHead[i] = tmpTarget;
				} else {
					if (tmpTarget.startsWith("\"")) {
						// int index = tmpTarget.indexOf(',');
						// if(index<0){
						// }
						int index = tmpTarget.indexOf('\t');
						if (index > 0) {
							String k = tmpTarget.substring(0, index);
							String v = tmpTarget.substring(index);
							preKey = k;
							targetKV.put(k, v);
						}
					} else {
						if (targetKV.containsKey(preKey)) {
							String value = targetKV.get(preKey);
							value = value + "\r\n" + tmpTarget;
							targetKV.put(preKey, value);
						}

					}

				}
				i++;
			}
			targetFileIO.close();

			Map<String, String> fileValue = new HashMap<String, String>();
			Set<String> targetKey = targetKV.keySet();
			Set<Entry<String, String[]>> entrySet = mapper.entrySet();
			for (String key : targetKey) {
				for (Entry<String, String[]> entry : entrySet) {
					boolean find = true;
					for (String kw : entry.getValue()) {
						if (key.indexOf(kw) < 0) {
							find = false;
						}
					}
					if (find) {
						fileValue.put(entry.getKey(), targetKV.get(key));
						break;
					}
				}
			}

			Set<Entry<String, String>> fileEntity = fileValue.entrySet();
			File outer = targetFile;//new File(home + "\\target\\" + targetFile.getName());
			OutputStreamWriter outW = new OutputStreamWriter(
					new FileOutputStream(outer), targetCharCode);
			BufferedWriter writer = new BufferedWriter(outW);
			for (int headIndex = 0; headIndex < targetFileHead.length; headIndex++) {
				if (headIndex != 0) {
					writer.newLine();
				}
				writer.write(targetFileHead[headIndex]);
			}

			for (Entry<String, String> fileV : fileEntity) {
				String value = fileV.getValue();
				// if(spliter == "\t"){
				// value = value.replaceAll(",", "\t");
				// }
				String currentFile = ysbtDir + "\\" + fileV.getKey();
				String file = currentFile + ".csv";
				File origFile = new File(file);
				if (!origFile.exists()) {
					origFile = new File(currentFile+".CSV");
					if(!origFile.exists()){
						MessageDialog.showMessage(parent, file + "  文件不存在");
						continue;
					}
				}
				FileInputStream targetIN = new FileInputStream(origFile);
				InputStreamReader origFileisr = new InputStreamReader(targetIN,
						codeString(file));
				BufferedReader origFileR = new BufferedReader(origFileisr);
				String tmpOrig = null;
				while ((tmpOrig = origFileR.readLine()) != null) {
					tmpOrig = tmpOrig.trim();
					writer.newLine();
					writer.write(tmpOrig + value);
				}
				origFileR.close();
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			MessageDialog.showMessage(parent, e.getMessage());
		}

	}

	public Map<String, String[]> getMapper(String xkbPath) {
		try {
			String mapperFileName = xkbPath;
			FileInputStream in = new FileInputStream(mapperFileName);
			InputStreamReader mapperIOisr = new InputStreamReader(in,
					codeString(mapperFileName));
			BufferedReader mapperIO = new BufferedReader(mapperIOisr);
			Map<String, String[]> mapper = new HashMap<String, String[]>();
			String tmpMapper = null;
			while ((tmpMapper = mapperIO.readLine()) != null) {
				String[] fields = tmpMapper.split(",");
				if (fields.length > 3) {
					String key = fields[1];
					String keyws = fields[3];
					if (key != null && keyws != null) {
						String[] keywords = keyws.trim().split(" ");
						mapper.put(key, keywords);
					}
				}
			}
			mapperIO.close();
			return mapper;
		} catch (IOException e) {
			MessageDialog.showMessage(parent, e.getMessage());
		}
		MessageDialog.showMessage(parent, xkbPath + " 数据不正确");
		return null;
	}

//	public static void main(String[] args) {
//		String home = "D:\\make_by_guohuichao\\";
//		if (args.length > 0) {
//			home = args[0];
//		}
//		new BatchFileMerger(home).start().fileStartMerge().end();
//
//	}

	/**
	 * 判断文件的编码格式
	 * 
	 * @param fileName
	 *            :file
	 * @return 文件编码格式
	 * @throws IOException
	 * @throws Exception
	 */
	public static String codeString(String file) throws IOException {
		FileInputStream bin = new FileInputStream(file);
		int p = (bin.read() << 8) + bin.read();
		String code = null;

		switch (p) {
		case 0xefbb:
			code = "UTF-8";
			break;
		case 0xfffe:
			code = "UTF-16LE";
			break;
		case 0xfeff:
			code = "UTF-16BE";
			break;
		case 8369:
			code = "GB2312";
			break;
		default:
			code = "GBK";
		}
		// bin.reset();
		bin.close();
		return code;
	}

}
