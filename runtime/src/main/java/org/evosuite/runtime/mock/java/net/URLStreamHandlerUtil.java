package org.evosuite.runtime.mock.java.net;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Class used to call methods on URLStreamHandler by reflection
 * @author arcuri
 *
 */
public class URLStreamHandlerUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(URLStreamHandlerUtil.class);
	
	private static Method openConnectionMethod;
	private static Method parseURLMethod;
	
	static{
		try {
			openConnectionMethod = URLStreamHandler.class.getDeclaredMethod("openConnection", URL.class, Proxy.class);
			openConnectionMethod.setAccessible(true);
			
			parseURLMethod = URLStreamHandler.class.getDeclaredMethod("parseURL", URL.class, String.class, int.class, int.class);
			parseURLMethod.setAccessible(true);
			
		} catch (NoSuchMethodException | SecurityException e) {
			logger.error("Failed to initialize due to reflection problems: "+e.getMessage());
		}
		
	}
	
	public static void parseURL(URLStreamHandler handler, URL url, String spec, int start, int limit){
		try {
			parseURLMethod.invoke(handler, url, spec, start, limit);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("Failed due to reflection problems: "+e.getMessage());
		}
	}
	
	public static URLConnection openConnection(URLStreamHandler handler, URL url, Proxy proxy){
		try {
			return (URLConnection)openConnectionMethod.invoke(handler, url, proxy);
		} catch (IllegalAccessException |  InvocationTargetException e) {
			logger.error("Failed due to reflection problems: "+e.getMessage());
			return null;
		}
	}
}