/*
 * Created on 2004-10-20:12:06:08 by 2709
 *
 * ====================================================================
 * 
 * File : ResourceHelper.java
 *
 */

package com.sophia.parser.util;

import java.io.*;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.net.URL;

/**
 * @author 2709
 * 2004-7-27
 */
public class ResourceHelper {

	private static ClassLoader nativeLoader = new ResourceHelper.NativeClassLoader();

	/**
	 * native(����)�Ƿ�����ļ�
	 * @param fileName
	 * @return
	 */
	public static boolean existNative(String fileName) {
		if(fileName == null)return false;
		InputStream is = null;
		is = getNativeInputStream(fileName);
		return (is != null);
	}

	/**
	 * �����ļ�������, �ļ�����˳��: classpath
	 * @param fileName
	 * @return
	 */
	public static InputStream getInputStream(String fileName)	{
		return getInputStream(fileName, ResourceHelper.class.getClassLoader());
	}

	/**
	 * ����native�ļ�������, �ļ�����˳��: native package
	 * @param fileName
	 * @return
	 */
	public static InputStream getNativeInputStream(String fileName)	{
		return getInputStream(fileName, nativeLoader);
	}

	/**
	 * �����ļ�������
	 * @param fileName
	 * @param loader
	 * @return
	 */
	private static InputStream getInputStream(String fileName, ClassLoader loader) {
		InputStream is = null;
		URL url = getResource(fileName, loader);
		try {
			if(url != null) is = url.openStream();
		} catch(Exception e) {}
		return is;		
	}

	/**
	 * �����ļ���ԴURL
	 * @param name
	 * @return
	 */
	public static URL getResource(String name) {
		return getResource(name, ResourceHelper.class.getClassLoader());
	}

	/**
	 * ����native(����)���ļ���ԴURL
	 * @param name
	 * @return
	 */
	public static URL getNativeResource(String name) {
		return nativeLoader.getResource(name);
	}

	private static URL getResource(String name, ClassLoader loader) {
		// First, try to locate this resource in the native jar package
		URL url = loader.getResource(name);

		// Next, try to locate this resource through the current
		// context classloader.
		if(url == null) url = Thread.currentThread().getContextClassLoader().getResource(name);

		// Next, try to locate this resource through this class's classloader
		if(url == null) url = ResourceHelper.class.getClassLoader().getResource(name);

		// Next, try to locate this resource through the system classloader
		if(url == null) url = ClassLoader.getSystemClassLoader().getResource(name);

		// Anywhere else we should look?

		return url;
	}

	/**
	 * ����native(����)����Դ�ļ�
	 * @param baseName
	 * @return
	 */
	public static ResourceBundle getBundle(String baseName) {
		return ResourceBundle.getBundle(baseName, Locale.getDefault(), nativeLoader);
	}

	/**
	 * ����native(����)����Դ�ļ�
	 * @param baseName
	 * @param locale
	 * @return
	 */
	public static ResourceBundle getBundle(String baseName, Locale locale) {
		return ResourceBundle.getBundle(baseName, locale, nativeLoader);
	}

	public static InputStream getImageStream(String name) throws IOException {
		InputStream is = ResourceHelper.class.getClassLoader().getResourceAsStream("resource/images/" + name);
		return is;
	}

	private static class NativeClassLoader extends ClassLoader {

		private NativeClassLoader() {
		}

		/**
		 * return the native jar url first
		 */
		public final URL getResource(String name) {
			URL url;
			try {
				Enumeration e = NativeClassLoader.class.getClassLoader().getResources(name);
				String className = NativeClassLoader.class.getName().replaceAll("\\.", "/") + ".class";
				URL classUrl = NativeClassLoader.class.getClassLoader().getResource(className);
				String prefix = classUrl.toString();
				prefix = prefix.substring(0, prefix.indexOf(className));
				while(e.hasMoreElements()) {
					url = (URL)e.nextElement();
					if(url.toString().startsWith(prefix)) return url;
				}
			} catch(IOException ioe) {}
			return null;//super.getResource(name);
		}
	}
}