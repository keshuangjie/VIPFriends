/**
 * BunnyRene 
 *com.android.volley.toolbox
 ***
 *下午4:48:30
 */
package com.android.volley.Compatible;

/**
 * @author BunnyRene
 * 
 */
public class Headers {
	// header parsing constant
	/**
	 * indicate HTTP 1.0 connection close after the response
	 */
	public final static int CONN_CLOSE = 1;
	/**
	 * indicate HTTP 1.1 connection keep alive
	 */
	public final static int CONN_KEEP_ALIVE = 2;

	// initial values.
	public final static int NO_CONN_TYPE = 0;
	public final static long NO_TRANSFER_ENCODING = 0;
	public final static long NO_CONTENT_LENGTH = -1;

	// header strings
	public final static String TRANSFER_ENCODING = "transfer-encoding";
	public final static String CONTENT_LEN = "content-length";
	public final static String CONTENT_TYPE = "content-type";
	public final static String CONTENT_ENCODING = "content-encoding";
	public final static String CONN_DIRECTIVE = "connection";

	public final static String LOCATION = "location";
	public final static String PROXY_CONNECTION = "proxy-connection";

	public final static String WWW_AUTHENTICATE = "www-authenticate";
	public final static String PROXY_AUTHENTICATE = "proxy-authenticate";
	public final static String CONTENT_DISPOSITION = "content-disposition";
	public final static String ACCEPT_RANGES = "accept-ranges";
	public final static String EXPIRES = "expires";
	public final static String CACHE_CONTROL = "cache-control";
	public final static String LAST_MODIFIED = "last-modified";
	public final static String ETAG = "etag";
	public final static String SET_COOKIE = "set-cookie";
	public final static String PRAGMA = "pragma";
	public final static String REFRESH = "refresh";
	public final static String X_PERMITTED_CROSS_DOMAIN_POLICIES = "x-permitted-cross-domain-policies";
}
