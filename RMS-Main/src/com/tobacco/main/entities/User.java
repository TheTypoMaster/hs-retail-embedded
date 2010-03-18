package com.tobacco.main.entities;

import com.tobacco.main.provider.AccountProvider;

import android.net.Uri;

/**
 * @author zwd
 *
 */
public class User {
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AccountProvider.CONTENT_URI + "/user");

	public static final String DEFAULT_SORT_ORDER = "modified DESC";
	
	public static final String _ID = "id";
	
	public static final String USERNAME = "username";
	
	public static final String PASSWORD = "password";
	
	public static final String PRIV = "priv";
	
	public static final String STATUS = "status";
}
