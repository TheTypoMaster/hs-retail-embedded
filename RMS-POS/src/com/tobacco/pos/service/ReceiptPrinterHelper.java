package com.tobacco.pos.service;

public class ReceiptPrinterHelper {
	
	static {
		System.loadLibrary("ReceiptPrinterHelper");
	}
	
	public native int open();
	public native void printHead(int fd, String tittle,
			String orderNum, String cashier, String date);
	public native void printBody(int fd, String name,
			String unitPrice, String count, String subtotal);
	public native void printFoot(int fd, String total, String receive, String change);
	public native void close(int fd);

}
