package com.tobacco.pos.service;

public class BarcodeReaderHelper {

	static {
		System.loadLibrary("BarcodeReaderHelper");
	}
	public native String getBarcode();
}
