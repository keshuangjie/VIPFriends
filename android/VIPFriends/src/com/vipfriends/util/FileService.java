package com.vipfriends.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.vipfriends.MyApplication;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

public class FileService {

	public static final String VIPFRIENDS_ROOT_NAME = "vipfriends";
	public static final String VIPFRIENDS_ROOT_DIR = File.separator + VIPFRIENDS_ROOT_NAME;// 定义本应用在SD卡上所使用的文件夹


	public static final String IMAGE_CHILD_DIR = VIPFRIENDS_ROOT_DIR + File.separator + "images";// image 子目录
	public static final String JSON_CHILD_DIR = VIPFRIENDS_ROOT_DIR + File.separator + "json";// json 子目录
	public static final String CACHE_CHILD_DIR = VIPFRIENDS_ROOT_DIR + File.separator + "cache";

	// 内置存储空间的目录类型
	public static final int INTERNAL_TYPE_FILE = 1;
	public static final int INTERNAL_TYPE_CACHE = 2;

	public static boolean isReady() {
		return externalMemoryAvailable();
	}
	
	public static void createRootDir(){
		if(CommonUtil.checkSDcard()){
			File file = new File(Environment.getExternalStorageDirectory() + VIPFRIENDS_ROOT_DIR);
			DLog.i("FileService", "createRootDir:file=" + file.getAbsolutePath());
			if(!file.exists()){
				boolean falg = file.mkdirs();
				DLog.i("FileService", "mkdirs=" + falg);
			}
		}
	}

	public static boolean externalMemoryAvailable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static boolean isUTF8Buf(byte[] buffers, long l) {
		if (buffers == null || l <= 0)
			return false;

		// 有BOM头
		if (l >= 3 && (buffers[0] & 0xff) == 0xEF
				&& (buffers[1] & 0xff) == 0xBB && (buffers[2] & 0xff) == 0xBF) {
			return true;
		}

		// 没有BOM头的文件判断
		byte[] szMaxBuf = new byte[7]; // UTF8字符最长为6个字节
		int nUTF8CurPos = 0;
		int nBadCount = 0;
		int nGoodCount = 0;

		while (nUTF8CurPos < l) {
			szMaxBuf[0] = buffers[nUTF8CurPos];
			nUTF8CurPos++;
			if (((szMaxBuf[0] & 0xff) & 0x80) == 0)// Ansi字母
			{
				int n = 0;
				continue;
			} else if (((szMaxBuf[0] & 0xff) & 0xE0) == 0xC0)// 两位
			{
				if (nUTF8CurPos + 1 == l) {// 每次读取MAX_BUFFERSIZE,可能有不完整的UTF8字符串
					break;
				}
				szMaxBuf[1] = buffers[nUTF8CurPos];
				if (((szMaxBuf[1] & 0xff) & 0x80) == 0x80)
					nGoodCount++;
				else
					nBadCount++;
				nUTF8CurPos++;
			} else if (((szMaxBuf[0] & 0xff) & 0xF0) == 0xE0) // 三位
			{
				if (nUTF8CurPos + 2 >= l) {// 每次读取MAX_BUFFERSIZE,可能有不完整的UTF8字符串
					break;
				}
				System.arraycopy(buffers, nUTF8CurPos, szMaxBuf, 1, 2);
				// memcpy(szMaxBuf+1,buffers+ nUTF8CurPos, 2);

				if (((szMaxBuf[1] & 0xff) & 0x80) == 0x80
						&& ((szMaxBuf[2] & 0xff) & 0x80) == 0x80)
					nGoodCount++;
				else
					nBadCount++;
				nUTF8CurPos += 2;
			} else if (((szMaxBuf[0] & 0xff) & 0xF8) == 0xF0)// 四位
			{
				if (nUTF8CurPos + 3 >= l) {// 每次读取MAX_BUFFERSIZE,可能有不完整的UTF8字符串
					break;
				}
				System.arraycopy(buffers, nUTF8CurPos, szMaxBuf, 1, 3);
				// memcpy(szMaxBuf+1,buffers+ nUTF8CurPos,3);
				if (((szMaxBuf[1] & 0xff) & 0x80) == 0x80
						&& ((szMaxBuf[2] & 0xff) & 0x80) == 0x80
						&& ((szMaxBuf[3] & 0xff) & 0x80) == 0x80)
					nGoodCount++;
				else
					nBadCount++;
				nUTF8CurPos += 3;
			} else if (((szMaxBuf[0] & 0xff) & 0xFC) == 0xF8)// 五位
			{
				if (nUTF8CurPos + 4 >= l) {// 每次读取MAX_BUFFERSIZE,可能有不完整的UTF8字符串
					break;
				}
				System.arraycopy(buffers, nUTF8CurPos, szMaxBuf, 1, 4);
				// memcpy(szMaxBuf+1,buffers+ nUTF8CurPos, 4);

				if (((szMaxBuf[1] & 0xff) & 0x80) == 0x80
						&& ((szMaxBuf[2] & 0xff) & 0x80) == 0x80
						&& ((szMaxBuf[3] & 0xff) & 0x80) == 0x80
						&& ((szMaxBuf[4] & 0xff) & 0x90) == 0x80)
					nGoodCount++;
				else
					nBadCount++;
				nUTF8CurPos += 4;
			} else if (((szMaxBuf[0] & 0xff) & 0xFE) == 0xFC)// 六位
			{
				if (nUTF8CurPos + 5 >= l) {// 每次读取MAX_BUFFERSIZE,可能有不完整的UTF8字符串
					break;
				}
				System.arraycopy(buffers, nUTF8CurPos, szMaxBuf, 1, 5);
				// memcpy(szMaxBuf+1,buffers+ nUTF8CurPos, 5);

				if (((szMaxBuf[1] & 0xff) & 0x80) == 0x80
						&& ((szMaxBuf[2] & 0xff) & 0x80) == 0x80
						&& ((szMaxBuf[3] & 0xff) & 0x80) == 0x80
						&& ((szMaxBuf[4] & 0xff) & 0x80) == 0x80
						&& ((szMaxBuf[5] & 0xff) & 0x80) == 0x80)
					nGoodCount++;
				else
					nBadCount++;
				nUTF8CurPos += 5;
			} else {
				nBadCount++;
			}
		}

		return (nGoodCount > nBadCount);
	}

	//
	// http://unicode.org/faq/utf_bom.html#BOM
	// A: Here are some guidelines to follow:
	//
	// 1. A particular protocol (e.g. Microsoft conventions for .txt files) may
	// require use of the BOM on certain Unicode data streams, such as files.
	// When you need to conform to such a protocol, use a BOM.
	// 2. Some protocols allow optional BOMs in the case of untagged text. In
	// those cases,
	// 2.1 Where a text data stream is known to be plain text, but of unknown
	// encoding, BOM can be used as a signature. If there is no BOM, the
	// encoding could be anything.
	// 2.2 Where a text data stream is known to be plain Unicode text (but not
	// which endian), then BOM can be used as a signature. If there is no BOM,
	// the text should be interpreted as big-endian.
	// 3. Some byte oriented protocols expect ASCII characters at the beginning
	// of a file. If UTF-8 is used with these protocols, use of the BOM as
	// encoding form signature should be avoided.
	// 4. Where the precise type of the data stream is known (e.g. Unicode
	// big-endian or Unicode little-endian), the BOM should not be used. In
	// particular, whenever a data stream is declared to be UTF-16BE, UTF-16LE,
	// UTF-32BE or UTF-32LE a BOM must not be used. (See also Q: What is the
	// difference between UCS-2 and UTF-16?.)
	public static boolean convertFile(final String inFile,
			final String outFile, final String outCharset) {
		if (TextUtils.isEmpty(inFile) || TextUtils.isEmpty(outFile)
				|| TextUtils.isEmpty(outCharset))
			return false;
		boolean converted = false;
		FileInputStream inStream = null;
		FileOutputStream outStream = null;
		String inCharset = "UTF-8";
		String defaultCharset = "GBK";
		File file = null;
		boolean hasBOM = false;
		try {
			file = new File(inFile);
			inStream = new FileInputStream(inFile);
			outStream = new FileOutputStream(outFile);
			long length = file.length();
			if (file.canRead()) {
				byte[] buffers = new byte[(int) length];
				if (buffers != null) {
					inStream.read(buffers, 0, (int) length);
					if (length >= 2 && (buffers[0] & 0xff) == 0xFE
							&& (buffers[1] & 0xff) == 0xFF) {
						// UTF-16, big-endian
						inCharset = "UTF-16BE";
						hasBOM = true;
					} else if (length >= 2 && (buffers[0] & 0xff) == 0xFF
							&& (buffers[1] & 0xff) == 0xFE) {
						// UTF-16, little-endian
						inCharset = "UTF-16LE";
						hasBOM = true;
					} else if (length >= 4 && (buffers[0] & 0xff) == 0x00
							&& (buffers[1] & 0xff) == 0x00
							&& (buffers[2] & 0xff) == 0xFE
							&& (buffers[3] & 0xff) == 0xFF) {
						// UTF-32, big-endian
						inCharset = "UTF-32BE";
						hasBOM = true;
					} else if (length >= 4 && (buffers[0] & 0xff) == 0xFF
							&& (buffers[1] & 0xff) == 0xFE
							&& (buffers[2] & 0xff) == 0x00
							&& (buffers[3] & 0xff) == 0x00) {
						// UTF-32, little-endian
						inCharset = "UTF-32LE";
						hasBOM = true;
					} else if (length >= 3 && (buffers[0] & 0xff) == 0xEF
							&& (buffers[1] & 0xff) == 0xBB
							&& (buffers[2] & 0xff) == 0xBF) {
						// UTF-8
						inCharset = "UTF-8";
					}

					if (hasBOM) {
						String content = new String(buffers, inCharset);
						if (!TextUtils.isEmpty(content)) {
							outStream.write(content.getBytes(outCharset));
						}
					} else {
						if (isUTF8Buf(buffers, length)) {
							byte[] bom = new byte[3];
							bom[0] = (byte) 0xEF;
							bom[1] = (byte) 0xBB;
							bom[2] = (byte) 0xBF;
							outStream.write(bom);
							String content = new String(buffers, inCharset);
							if (!TextUtils.isEmpty(content)) {
								outStream.write(content.getBytes(outCharset));
							}
						} else {
							inCharset = defaultCharset;
							// 先判断BOM
							if (length < 2) {
								inCharset = defaultCharset;
							} else if (length < 4) {
								if ((buffers[0] & 0xff) == 0xFE
										&& (buffers[1] & 0xff) == 0xFF) {
									// UTF-16, big-endian
									inCharset = "UTF-16BE";
									hasBOM = true;
								} else if ((buffers[0] & 0xff) == 0xFF
										&& (buffers[1] & 0xff) == 0xFE) {
									// UTF-16, little-endian
									inCharset = "UTF-16LE";
									hasBOM = true;
								}
							}
							String content = new String(buffers, inCharset);
							if (!TextUtils.isEmpty(content)) {
								if (!hasBOM) {
									byte[] bom = new byte[3];
									bom[0] = (byte) 0xEF;
									bom[1] = (byte) 0xBB;
									bom[2] = (byte) 0xBF;
									outStream.write(bom);
								}
								outStream.write(content.getBytes(outCharset));
							}
						}
					}

					outStream.flush();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inStream != null)
					inStream.close();
				if (outStream != null)
					outStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return converted;
	}


	/**
	 * 保存内容到SDCard
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            文件内容
	 * @throws Exception
	 */
	public void saveToSDCard(String filename, String content) throws Exception {
		saveToSDCard(null, filename, content);
	}

	/**
	 * 保存文件
	 */
	public static boolean saveToSDCard(Directory directory, String fileName,
			String content) {
		return saveToSDCard(directory, fileName, content, Context.MODE_PRIVATE);
	}

	/**
	 * 保存文件
	 */
	public static boolean saveToSDCard(Directory directory, String fileName,
			String content, int mode) {
		if (null == content) {
			return false;
		}
		return saveToSDCard(directory, fileName, content.getBytes(), mode);
	}

	/**
	 * 保存文件
	 */
	public static boolean saveToSDCard(Directory directory, String fileName,
			byte[] data) {
		if (null == data) {
			return false;
		}
		return saveToSDCard(directory, fileName, data, Context.MODE_PRIVATE);
	}

	/**
	 * 保存文件
	 */
	public static boolean saveToSDCard(Directory directory, String fileName,
			byte[] data, int mode) {

		File dir = directory.getDir();

		File file = new File(dir, fileName);
		FileOutputStream outStream = null;

		try {
			outStream = new FileOutputStream(file);
			if (null != outStream) {
				outStream.write(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != outStream) {
				try {
					outStream.close();
				} catch (IOException e) {
				}
			}
		}
		return true;

	}

	/**
	 * 保存内容
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            文件内容
	 * @throws Exception
	 */
	public void save(String filename, String content) throws Exception {
		FileOutputStream outStream = MyApplication.getContext().openFileOutput(
				filename, Context.MODE_PRIVATE);
		outStream.write(content.getBytes());
		outStream.close();
	}

	/**
	 * 以追加方式保存内容
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            文件内容
	 * @throws Exception
	 */
	public void saveAppend(String filename, String content) throws Exception {// ctrl+shift+y变小写/x变大写
		FileOutputStream outStream = MyApplication.getContext().openFileOutput(
				filename, Context.MODE_APPEND);
		outStream.write(content.getBytes());
		outStream.close();
	}

	/**
	 * 保存内容,允许其他应用对其进行读访问
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            文件内容
	 * @throws Exception
	 */
	public void saveReadable(String filename, String content) throws Exception {// ctrl+shift+y变小写/x变大写
		FileOutputStream outStream = MyApplication.getContext().openFileOutput(
				filename, Context.MODE_WORLD_READABLE);
		outStream.write(content.getBytes());
		outStream.close();
	}

	/**
	 * 保存内容,允许其他应用对其进行写访问
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            文件内容
	 * @throws Exception
	 */
	public void saveWriteable(String filename, String content) throws Exception {// ctrl+shift+y变小写/x变大写
		FileOutputStream outStream = MyApplication.getContext().openFileOutput(
				filename, Context.MODE_WORLD_WRITEABLE);
		outStream.write(content.getBytes());
		outStream.close();
	}

	/**
	 * 保存内容,允许其他应用对其进行读写访问
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            文件内容
	 * @throws Exception
	 */
	public void saveReadableWriteable(String filename, String content)
			throws Exception {// ctrl+shift+y变小写/x变大写
		FileOutputStream outStream = MyApplication.getContext().openFileOutput(
				filename,
				Context.MODE_APPEND + Context.MODE_WORLD_WRITEABLE
						+ Context.MODE_WORLD_READABLE);
		outStream.write(content.getBytes());
		outStream.close();
	}

	/**
	 * 读取内容
	 * 
	 * @param filename
	 *            文件名称
	 * @return 文件内容
	 * @throws Exception
	 */
	public String read(String filename) throws Exception {
		FileInputStream inStream = MyApplication.getContext().openFileInput(
				filename);
		byte[] data = readInputStream(inStream);
		return new String(data);
	}

	/**
	 * 读取内容
	 * 
	 * @param filename
	 *            文件名称
	 * @return 文件内容
	 * @throws Exception
	 */
	public byte[] readAsByteArray(String filename) throws Exception {
		FileInputStream inStream = MyApplication.getContext().openFileInput(
				filename);
		return readInputStream(inStream);
	}

	// 手机：自带存储空间，外部插进来SDCard

	private byte[] readInputStream(FileInputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		outStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 目录封装类
	 */
	public static class Directory {

		public static final int INTERNAL = 1;
		public static final int EXTERNAL = 2;

		private File dir;
		private String path;
		private int space;

		public Directory(String path, int space) {
			this(new File(path), space);
		}

		public Directory(File dir, int space) {
			this.dir = dir;
			this.space = space;
		}

		public File getDir() {
			return dir;
		}

		public void setDir(File dir) {
			this.dir = dir;
		}

		public int getSpace() {
			return space;
		}

		public void setSpace(int space) {
			this.space = space;
		}

		public String getPath() {
			if (null == path && null != dir) {
				path = dir.getAbsolutePath();
			}
			return path;
		}

		public void setPath(String path) {
			if (null == getPath() || !getPath().equals(path)) {
				dir = new File(path);
				this.path = path;
			}
		}

	}

}
