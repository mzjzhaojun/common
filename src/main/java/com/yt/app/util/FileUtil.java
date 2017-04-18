/**
 * 
 */
package com.yt.app.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

/**
 * 文件处理
 * 
 * @author zj
 *
 */
public class FileUtil {

	public static final String DOT = ".";
	public static final String SLASH_ONE = "/";
	public static final String SLASH_TWO = "\\";

	public static void writeFile(File file, byte[] b) {
		ByteArrayInputStream bis = null;
		FileOutputStream out = null;
		try {
			bis = new ByteArrayInputStream(b);
			out = new FileOutputStream(file);
			byte[] by = new byte[1024];
			int len;
			while ((len = bis.read(b)) != -1) {
				out.write(by, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public static File getFile(String path, String filename) {
		path = checkDirectory(path);
		if (StringUtils.isNotEmpty(path)) {
			return new File(path + filename);
		}
		return null;
	}

	public static String checkDirectory(String path) {
		if (StringUtils.isBlank(path)) {
			return "";
		} else {
			path = path.replaceAll("\\s", "");
			if (!(path.endsWith("/") || path.endsWith("\\"))) {
				path += File.separator;
			}
			return path;
		}
	}

	public static void writeFile(String file, byte[] b) {
		writeFile(new File(file), b);
	}

	public static byte[] readFile(File file) {
		ByteArrayOutputStream ois = null;
		FileInputStream fis = null;
		try {
			ois = new ByteArrayOutputStream();
			fis = new FileInputStream(file);
			byte[] b = new byte[1024];
			int len;
			while ((len = fis.read(b)) != -1) {
				ois.write(b, 0, len);
			}
			return ois.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.flush();
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {

					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return null;

	}

	public static void writeFile(InputStream is, File file) {
		byte[] b = new byte[1024];
		FileOutputStream fos = null;
		file.length();
		try {
			fos = new FileOutputStream(file);
			int len;
			while ((len = is.read(b)) != -1) {
				fos.write(b, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public static boolean isImage(File file) {
		if (file.isDirectory()) {
			return false;
		}
		String fileName = file.getName();
		int len = fileName.indexOf(".");
		String imagesPattern = fileName.substring(len + 1, fileName.length()).toLowerCase();
		if ("jpg".equals(imagesPattern)) {
			return true;
		} else if ("bmp".equals(imagesPattern)) {
			return true;
		} else if ("gif".equals(imagesPattern)) {
			return true;
		} else if ("psd".equals(imagesPattern)) {
			return true;
		} else if ("pcx".equals(imagesPattern)) {
			return true;
		} else if ("png".equals(imagesPattern)) {
			return true;
		} else if ("dxf".equals(imagesPattern)) {
			return true;
		} else if ("cdr".equals(imagesPattern)) {
			return true;
		} else if ("ico".equals(imagesPattern)) {
			return true;
		} else if ("bmp".equals(imagesPattern)) {
			return true;
		} else if ("jpeg".equals(imagesPattern)) {
			return true;
		} else if ("svg".equals(imagesPattern)) {
			return true;
		} else if ("wmf".equals(imagesPattern)) {
			return true;
		} else if ("emf".equals(imagesPattern)) {
			return true;
		} else if ("eps".equals(imagesPattern)) {
			return true;
		} else if ("tga".equals(imagesPattern)) {
			return true;
		} else if ("nef".equals(imagesPattern)) {
			return true;
		} else if ("tif".equals(imagesPattern)) {
			return true;
		} else if ("tiff".equals(imagesPattern)) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean isAudio(File file) {
		if (file.isDirectory()) {
			return false;
		}
		String fileName = file.getName();
		int len = fileName.indexOf(".");
		String audioPattern = fileName.substring(len + 1, fileName.length()).toLowerCase();
		if ("au".equals(audioPattern)) {
			return true;
		} else if ("aiff".equals(audioPattern)) {
			return true;
		} else if ("wav".equals(audioPattern)) {
			return true;
		} else if ("midi".equals(audioPattern)) {
			return true;
		} else if ("rfm".equals(audioPattern)) {
			return true;
		} else if ("amr".equals(audioPattern)) {
			return true;
		} else if ("aac".equals(audioPattern)) {
			return true;
		} else if ("mp3".equals(audioPattern)) {
			return true;
		} else if ("ogg".equals(audioPattern)) {
			return true;
		} else if ("pcm".equals(audioPattern)) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean isVideo(File file) {
		if (file.isDirectory()) {
			return false;
		}
		String fileName = file.getName();
		int len = fileName.indexOf(".");
		String audioPattern = fileName.substring(len + 1, fileName.length()).toLowerCase();
		if ("mp4".equals(audioPattern)) {
			return true;
		} else if ("avi".equals(audioPattern)) {
			return true;
		} else if ("rmvb".equals(audioPattern)) {
			return true;
		} else if ("3gp".equals(audioPattern)) {
			return true;
		} else if ("mkv".equals(audioPattern)) {
			return true;
		} else if ("flv".equals(audioPattern)) {
			return true;
		} else if ("wmv".equals(audioPattern)) {
			return true;
		} else if ("dvd".equals(audioPattern)) {
			return true;
		} else if ("vcd".equals(audioPattern)) {
			return true;
		} else if ("mpg".equals(audioPattern)) {
			return true;
		} else {
			return false;
		}

	}

	public static String getWithoutExtension(String fileName) {
		String ext = StringUtils.substring(fileName, 0, StringUtils.lastIndexOf(fileName, DOT));
		return StringUtils.trimToEmpty(ext);
	}

	public static String getExtension(String fileName) {
		if (StringUtils.INDEX_NOT_FOUND == StringUtils.indexOf(fileName, DOT))
			return StringUtils.EMPTY;
		String ext = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, DOT));
		return StringUtils.trimToEmpty(ext);
	}

	public static boolean isExtension(String fileName, String ext) {
		return StringUtils.equalsIgnoreCase(getExtension(fileName), ext);
	}

	public static boolean hasExtension(String fileName) {
		return !isExtension(fileName, StringUtils.EMPTY);
	}

	public static String trimExtension(String ext) {
		return getExtension(DOT + ext);
	}

	public static String fillExtension(String fileName, String ext) {
		fileName = replacePath(fileName + DOT);
		ext = trimExtension(ext);
		if (!hasExtension(fileName)) {
			return fileName + getExtension(ext);
		}
		if (!isExtension(fileName, ext)) {
			return getWithoutExtension(fileName) + getExtension(ext);
		}
		return fileName;
	}

	public static boolean isFile(String fileName) {
		return hasExtension(fileName);
	}

	public static boolean isFolder(String fileName) {
		return !hasExtension(fileName);
	}

	public static String replacePath(String path) {
		return StringUtils.replace(StringUtils.trimToEmpty(path), SLASH_ONE, SLASH_TWO);
	}

	public static String trimLeftPath(String path) {
		if (isFile(path))
			return path;
		path = replacePath(path);
		String top = StringUtils.left(path, 1);
		if (StringUtils.equalsIgnoreCase(SLASH_TWO, top))
			return StringUtils.substring(path, 1);
		return path;
	}

	public static String trimRightPath(String path) {
		if (isFile(path))
			return path;
		path = replacePath(path);
		String bottom = StringUtils.right(path, 1);
		if (StringUtils.equalsIgnoreCase(SLASH_TWO, bottom))
			return StringUtils.substring(path, 0, path.length() - 2);
		return path + SLASH_TWO;
	}

	public static String trimPath(String path) {
		path = StringUtils.replace(StringUtils.trimToEmpty(path), SLASH_ONE, SLASH_TWO);
		path = trimLeftPath(path);
		path = trimRightPath(path);
		return path;
	}

	public static String bulidFullPath(String... paths) {
		StringBuffer sb = new StringBuffer();
		for (String path : paths) {
			sb.append(trimPath(path));
		}
		return sb.toString();
	}
}
