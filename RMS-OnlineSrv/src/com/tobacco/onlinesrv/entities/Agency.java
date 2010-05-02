package com.tobacco.onlinesrv.entities;

import com.tobacco.onlinesrv.provider.AgencyProvider;
import android.net.Uri;

public class Agency {
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ AgencyProvider.CONTENT_URI + "/agency");
	public static final String KEY_ID = "id";
	public static final String KEY_NAME = "name";
	public static final String KEY_ADDRESS = "address";
}
