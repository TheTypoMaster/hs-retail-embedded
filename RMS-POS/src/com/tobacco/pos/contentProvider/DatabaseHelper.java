package com.tobacco.pos.contentProvider;
 
import static android.provider.BaseColumns._ID;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{
	private static final String TAG = "lyq";

	private static final String DATABASE_NAME = "AllTables.db";
	private static final int DATABASE_VERSION = 1;

	// table name constants
	public static final String GOODS = "Goods";
	public static final String GOODSKIND = "GoodsKind";
	public static final String GOODSPRICE = "GoodsPrice";
	public static final String MANUFACTURER = "Manufacturer";
	public static final String PURCHASEBILL = "PurchaseBill";
	public static final String PURCHASEITEM = "PurchaseItem";
	public static final String SALESBILL = "SalesBill";
	public static final String SALESITEM = "SalesItem";
	public static final String UNIT = "Unit";
	public static final String USERINFO = "UserInfo";
	public static final String VIPINFO = "VIPInfo";
	 
	private SQLiteDatabase db = null;
	
	public DatabaseHelper(Context contex) {
		
		super(contex, DATABASE_NAME, null, DATABASE_VERSION);
	
		db = contex.openOrCreateDatabase(DATABASE_NAME, 2, null);
		createtable(db);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	private void createtable(SQLiteDatabase db) {
	
		// create new tables
		
		//创建商品表
		try{
			db.query(GOODS, null, null, null, null, null, null);
		}
		catch(Exception e){
			db.execSQL("create table if not exists " + GOODS + " ( " + _ID
				+ " integer primary key autoincrement,"
				+ " goodsCode varchar(50) not null unique ,"
				+ "goodsName varchar(50) not null,"
				+ " manufacturerId integer references  Manufacturer  ( " + _ID + " ),"
				+ "goodsFormat varchar(50)not null,"
				+ "kindId integer references GoodsKind  (" + _ID
				+ ") )");
			initGoods(db);
		}
		
		//创建商品种类表
		try{
			db.query(GOODSKIND, null, null, null, null, null, null);
		}
		catch(Exception e){
		db.execSQL("create table if not exists " + GOODSKIND + " ( " + _ID
				+ " integer primary key autoincrement,"
				+ " name varchar(50) not null, "
				+ " parent integer references " + GOODSKIND + " ( " + _ID
				+ " )," + " level integer not null, "
				+ " comment varchar(100) )");
			initGoodsKind(db);
		}
		
		//创建商品价格表
		try {
			db.query(GOODSPRICE, null, null, null, null, null, null);
		} catch (Exception e) {
		db.execSQL("create table if not exists " + GOODSPRICE + " ( " + _ID
				+ " integer primary key autoincrement,"
				+ " goodsId integer references Goods " 
				+ " (" + _ID + " ) ,"
				+ "unitId integer references Unit ( "
				+ _ID + " )," + " barcode varchar(20) unique ,"
				+ "inPrice double not null,"
				+ "outPrice double not null )");
		initGoodsPrice(db);
		}
		
		//创建厂家表
		try {
			db.query(MANUFACTURER, null, null, null, null, null, null);
		} catch (Exception e) {
		db.execSQL("create table if not exists " + MANUFACTURER + " ( " + _ID
				+ " integer primary key autoincrement,"
				+ " mName varchar(50) not null unique )");
		initManufacturer(db);
		}
		
		//创建进货单表
		try {
			db.query(PURCHASEBILL, null, null, null, null, null, null);
		} catch (Exception e) {
		db.execSQL("create table if not exists " + PURCHASEBILL + " ( " + _ID
				+ " integer primary key autoincrement,"
				+ " pBillNum varchar(20) unique not null, "
				+ " operId integer references UserInfo ( " + _ID + " ),"
				+ " time date not null ," 
				+ " comment varchar(255))");
		initPurchaseBill(db);
		}

		//创建进货项表
		try {
			db.query(PURCHASEITEM, null, null, null, null, null, null);
		} catch (Exception e) {
		db.execSQL("create table if not exists " + PURCHASEITEM + " ( " + _ID
				+ " integer primary key autoincrement,"
				+ " purchaseBillId integer references PurchaseBill ( " + _ID + " ), "
				+ "pGoodsNum integer not null ,"
			    + "pPriceId integer references GoodsPrice ( " + _ID + " ))");
		initPurchaseItem(db);
		}
		
		//创建销售表
		try {
			db.query(SALESBILL, null, null, null, null, null, null);
		} catch (Exception e) {
		db.execSQL("create table  if not exists " + SALESBILL + " ( " + _ID
				+ " integer primary key autoincrement,"
				+ " sBillNum varchar(20) unique not null, "
				+ " operId integer references UserInfo ( " + _ID + " ),"
				+ " time date not null,"
				+ " VIPId integer references VIPInfo ( " + _ID + " ),"
				+ " payMoney double not null )");
		initSalesBill(db);
		}
		
		//创建销售项表
		try {
			db.query(SALESITEM, null, null, null, null, null, null);
		} catch (Exception e) {
		db.execSQL("create table if not exists " + SALESITEM + " ( " + _ID
				+ " integer primary key autoincrement,"
				+ " salesBillId integer references SalesBill ( " + _ID + " ), "
				+ " sGoodsNum integer not null ,"
			    + " barcode varchar(20) references GoodsPrice ( barcode ),"
			    + " inPrice double not null, " 
			    + " outPrice double not null )" );
		initSalesItem(db);
		}
		
		//创建单位表
		try {
			db.query(UNIT, null, null, null, null, null, null);
		} catch (Exception e) {
		db.execSQL("create table if not exists " + UNIT + " ( " + _ID
				+ " integer primary key autoincrement,"
				+ " name varchar(20) not null unique )");
		initUnit(db);
		}
		
		//创建用户表
		try {
			db.query(USERINFO, null, null, null, null, null, null);
		} catch (Exception e) {
			db.execSQL("create table if not exists " + USERINFO + " ( " + _ID
					+ " integer primary key autoincrement,"
					+ " userName varchar(50) not null unique, "
					+ " password varchar(50) ,"
					+ " permission integer not null,"
					+ " status integer not null )");
			initUserInfo(db);
		}
		
		//创建VIP信息表
		try {
			db.query(VIPINFO, null, null, null, null, null, null);
		} catch (Exception e) {
			db.execSQL("create table if not exists " + VIPINFO + " ( "
					+ _ID + " integer primary key autoincrement,"
					+ " VIPNum varchar(20) unique not null, "
					+ "VIPName varchar(20) not null ,"
					+ "VIPDiscount double not null)");

			initVIPInfo(db);
		}
	}
	
	 private boolean initGoods(SQLiteDatabase db){
			
			ContentValues value = new ContentValues();
			
			value.clear();
			value.put("goodsCode", "G1");
			value.put("goodsName", "红塔山");
			value.put("manufacturerId", 1);
			value.put("goodsFormat", "");
			value.put("kindId", 8);
			db.insertOrThrow(GOODS, null, value);
			
			value.clear();
			value.put("goodsCode", "G2");
			value.put("goodsName", "黄梅");
			value.put("manufacturerId", 1);
			value.put("goodsFormat", "");
			value.put("kindId", 10);
			db.insertOrThrow(GOODS, null, value);
			
			value.clear();
			value.put("goodsCode", "G3");
			value.put("goodsName", "哈德门");
			value.put("manufacturerId", 2);
			value.put("goodsFormat", "");
			value.put("kindId", 8);
			db.insertOrThrow(GOODS, null, value);
			
			value.clear();
			value.put("goodsCode", "G4");
			value.put("goodsName", "古田");
			value.put("manufacturerId", 1);
			value.put("goodsFormat", "");
			value.put("kindId", 8);
			db.insertOrThrow(GOODS, null, value);
			
			value.clear();
			value.put("goodsCode", "G5");
			value.put("goodsName", "七匹狼");
			value.put("manufacturerId", 1);
			value.put("goodsFormat", "");
			value.put("kindId", 8);
			db.insertOrThrow(GOODS, null, value);
			
			value.clear();
			value.put("goodsCode", "G6");
			value.put("goodsName", "一品梅");
			value.put("manufacturerId", 1);
			value.put("goodsFormat", "");
			value.put("kindId", 8);
			db.insertOrThrow(GOODS, null, value);
			
			value.clear();
			value.put("goodsCode", "G7");
			value.put("goodsName", "黄山");
			value.put("manufacturerId", 1);
			value.put("goodsFormat", "");
			value.put("kindId", 8);
			db.insertOrThrow(GOODS, null, value);
			
			value.clear();
			value.put("goodsCode", "G8");
			value.put("goodsName", "白衬衫");
			value.put("manufacturerId", 5);
			value.put("goodsFormat", "");
			value.put("kindId", 2);
			db.insertOrThrow(GOODS, null, value);
			
			value.clear();
			value.put("goodsCode", "G9");
			value.put("goodsName", "香芋饼");
			value.put("manufacturerId", 6);
			value.put("goodsFormat", "");
			value.put("kindId", 14);
			db.insertOrThrow(GOODS, null, value);
			
			value.clear();
			value.put("goodsCode", "G10");
			value.put("goodsName", "麦克笔");
			value.put("manufacturerId", 9);
			value.put("goodsFormat", "");
			value.put("kindId", 11);
			db.insertOrThrow(GOODS, null, value);
			
			value.clear();
			value.put("goodsCode", "G11");
			value.put("goodsName", "宣纸");
			value.put("manufacturerId", 9);
			value.put("goodsFormat", "");
			value.put("kindId", 11);
			db.insertOrThrow(GOODS, null, value);
			
			return true;
		}
		
	 private boolean initGoodsKind(SQLiteDatabase db) {
			
			ContentValues value = new ContentValues();
			value.put("name", "衣服");
			value.put("parent", 0);
			value.put("level", 1);
			value.put("comment", "衣服分类，所有的衣物都是该类的子类");

			db.insertOrThrow(GOODSKIND, null, value);

			value.clear();
			value.put("name", "衣服->男装");
			value.put("parent", 1);
			value.put("level", 2);
			value.put("comment", "男装Man");

			db.insertOrThrow(GOODSKIND, null, value);

			value.clear();
			value.put("name", "衣服->女装");
			value.put("parent", 1);
			value.put("level", 2);
			value.put("comment", "女装Woman");

			db.insertOrThrow(GOODSKIND, null, value);

			value.clear();
			value.put("name", "衣服->男装->男羽绒服");
			value.put("parent", 2);
			value.put("level", 3);
			value.put("comment", "冬天来了，有温度才有风度");

			db.insertOrThrow(GOODSKIND, null, value);

			value.clear();
			value.put("name", "衣服->男装->男裤");
			value.put("parent", 2);
			value.put("level", 3);
			value.put("comment", "呵呵，可别忘了穿裤子喔，要不就糗大了");

			db.insertOrThrow(GOODSKIND, null, value);

			value.clear();
			value.put("name", "衣服->童装");
			value.put("parent", 1);
			value.put("level", 2);
			value.put("comment", "童装世界");

			db.insertOrThrow(GOODSKIND, null, value);

			value.clear();
			value.put("name", "衣服->女装->女式围巾");
			value.put("parent", 3);
			value.put("level", 3);
			value.put("comment", "冷了，不要冻到脖子了");

			db.insertOrThrow(GOODSKIND, null, value);

			value.clear();
			value.put("name", "卷烟");
			value.put("parent", 0);
			value.put("level", 1);
			value.put("comment", "抽烟并不是很帅，身体更重要喔");

			db.insertOrThrow(GOODSKIND, null, value);

			value.clear();
			value.put("name", "水果");
			value.put("parent", 0);
			value.put("level", 1);
			value.put("comment", "多吃水果，对身体有益");

			db.insertOrThrow(GOODSKIND, null, value);

			value.clear();
			value.put("name", "卷烟->软盒");
			value.put("parent", 8);
			value.put("level", 2);
			value.put("comment", "软盒卷烟");

			db.insertOrThrow(GOODSKIND, null, value);

			value.clear();
			value.put("name", "文具");
			value.put("parent", 0);
			value.put("level", 1);
			value.put("comment", "学生的必备之物");

			db.insert(GOODSKIND, null, value);

			value.clear();
			value.put("name", "文具->铅笔");
			value.put("parent", 11);
			value.put("level", 2);
			value.put("comment", "使用时要注意环保");

			db.insertOrThrow(GOODSKIND, null, value);

			value.clear();
			value.put("name", "文具->钢笔");
			value.put("parent", 11);
			value.put("level", 2);
			value.put("comment", "成功人士的必备之物");

			db.insertOrThrow(GOODSKIND, null, value);
			
			value.clear();
			value.put("name", "饼干");
			value.put("parent", 0);
			value.put("level", 1);
			value.put("comment", "民以食为天");

			db.insertOrThrow(GOODSKIND, null, value);

			return true;
		}
		private boolean initGoodsPrice(SQLiteDatabase db){

			ContentValues value = new ContentValues();
			
			value.clear();
			value.put("goodsId", 1);
			value.put("unitId", 1);
			value.put("barcode", "gb1");
			value.put("inPrice", 6.0);
			value.put("outPrice", 7.0);
			db.insertOrThrow(GOODSPRICE, null, value);
			
			value.clear();
			value.put("goodsId", 2);
			value.put("unitId", 2);
			value.put("barcode", "gb2");
			value.put("inPrice", 50);
			value.put("outPrice", 60);
			db.insertOrThrow(GOODSPRICE, null, value);
			
			value.clear();
			value.put("goodsId", 3);
			value.put("unitId", 2);
			value.put("barcode", "gb3");
			value.put("inPrice", 70);
			value.put("outPrice", 80);
			db.insertOrThrow(GOODSPRICE, null, value);
			
			value.clear();
			value.put("goodsId", 4);
			value.put("unitId", 3);
			value.put("barcode", "gb4");
			value.put("inPrice", 55);
			value.put("outPrice", 64);
			db.insertOrThrow(GOODSPRICE, null, value);
			
			value.clear();
			value.put("goodsId", 5);
			value.put("unitId", 3);
			value.put("barcode", "gb5");
			value.put("inPrice", 90);
			value.put("outPrice", 100);
			db.insertOrThrow(GOODSPRICE, null, value);
			
			value.clear();
			value.put("goodsId", 6);
			value.put("unitId", 3);
			value.put("barcode", "gb6");
			value.put("inPrice", 100);
			value.put("outPrice", 110);
			db.insertOrThrow(GOODSPRICE, null, value);
			
			value.clear();
			value.put("goodsId", 7);
			value.put("unitId", 1);
			value.put("barcode", "gb7");
			value.put("inPrice", 10);
			value.put("outPrice", 12);
			db.insertOrThrow(GOODSPRICE, null, value);
			
			value.clear();
			value.put("goodsId", 7);
			value.put("unitId", 2);
			value.put("barcode", "gb8");
			value.put("inPrice", 100);
			value.put("outPrice", 120);
			db.insertOrThrow(GOODSPRICE, null, value);
			
			value.clear();
			value.put("goodsId", 8);
			value.put("unitId", 5);
			value.put("barcode", "gb9");
			value.put("inPrice", 233);
			value.put("outPrice", 250);
			db.insertOrThrow(GOODSPRICE, null, value);
			
			value.clear();
			value.put("goodsId", 9);
			value.put("unitId", 1);
			value.put("barcode", "gb10");
			value.put("inPrice", 1.4);
			value.put("outPrice", 2);
			db.insertOrThrow(GOODSPRICE, null, value);
			
			value.clear();
			value.put("goodsId", 10);
			value.put("unitId", 6);
			value.put("barcode", "gb11");
			value.put("inPrice", 4);
			value.put("outPrice", 6);
			db.insertOrThrow(GOODSPRICE, null, value);
			
			value.clear();
			value.put("goodsId", 11);
			value.put("unitId", 7);
			value.put("barcode", "gb12");
			value.put("inPrice", 1);
			value.put("outPrice", 1.5);
			db.insertOrThrow(GOODSPRICE, null, value);
			
			return true;
		}
		private boolean initManufacturer(SQLiteDatabase db){
			
			ContentValues value = new ContentValues();
			
			value.clear();
			value.put("mName", "龙岩卷烟厂");
			db.insertOrThrow(MANUFACTURER, null, value);
			
			value.clear();
			value.put("mName", "厦门卷烟厂");
			db.insertOrThrow(MANUFACTURER, null, value);
			
			value.clear();
			value.put("mName", "泉州卷烟厂");
			db.insertOrThrow(MANUFACTURER, null, value);
			
			value.clear();
			value.put("mName", "福州卷烟厂");
			db.insertOrThrow(MANUFACTURER, null, value);
			
			value.clear();
			value.put("mName", "福州衣服厂");
			db.insertOrThrow(MANUFACTURER, null, value);
			
			value.clear();
			value.put("mName", "漳州饼干厂");
			db.insertOrThrow(MANUFACTURER, null, value);
			
			value.clear();
			value.put("mName", "莆田拉面馆");
			db.insertOrThrow(MANUFACTURER, null, value);
			
			value.clear();
			value.put("mName", "泉州安踏");
			db.insertOrThrow(MANUFACTURER, null, value);
			
			
			value.clear();
			value.put("mName", "北京文具厂");
			db.insertOrThrow(MANUFACTURER, null, value);
			
			return true;
		}
		
		private boolean initPurchaseBill(SQLiteDatabase db) {
			SimpleDateFormat dateFormater = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
			
			ContentValues value = new ContentValues();
			
			value.clear();
			value.put("pBillNum", "P1");
			value.put("operId", 1);
			Date d1 = new Date();
			value.put("time", dateFormater.format(d1));
			value.put("comment", "第一张进货单");
			db.insertOrThrow(PURCHASEBILL, null, value);
			
			value.clear();
			value.put("pBillNum", "P2");
			value.put("operId", 1);
			Date d2 = new Date();
			if(d1.getSeconds()<57)
				d2.setSeconds(d1.getSeconds()+3);
			value.put("time", dateFormater.format(d2));
			value.put("comment", "第二张进货单");
			db.insertOrThrow(PURCHASEBILL, null, value);
			
			return true;
			
		}
		
		private boolean initPurchaseItem(SQLiteDatabase db) {
			
			ContentValues value = new ContentValues();
			
			value.clear();
			value.put("purchaseBillId", 1);
			value.put("pGoodsNum", 4);
			value.put("pPriceId", 7);
			
			db.insertOrThrow(PURCHASEITEM, null, value);
			
			value.clear();
			value.put("purchaseBillId", 1);
			value.put("pGoodsNum", 10);
			value.put("pPriceId", 3);
			
			db.insertOrThrow(PURCHASEITEM, null, value);
			
			value.clear();
			value.put("purchaseBillId", 1);
			value.put("pGoodsNum", 10);
			value.put("pPriceId", 2);
			
			db.insertOrThrow(PURCHASEITEM, null, value);
			
			value.clear();
			value.put("purchaseBillId", 1);
			value.put("pGoodsNum", 10);
			value.put("pPriceId", 9);
			
			db.insertOrThrow(PURCHASEITEM, null, value);
			
			value.clear();
			value.put("purchaseBillId", 1);
			value.put("pGoodsNum", 3);
			value.put("pPriceId", 1);
			
			db.insertOrThrow(PURCHASEITEM, null, value);
			
			value.clear();
			value.put("purchaseBillId", 2);
			value.put("pGoodsNum", 2);
			value.put("pPriceId", 7);
			
			db.insertOrThrow(PURCHASEITEM, null, value);
			
			value.clear();
			value.put("purchaseBillId", 2);
			value.put("pGoodsNum", 4);
			value.put("pPriceId", 8);
			
			db.insertOrThrow(PURCHASEITEM, null, value);
			
			value.clear();
			value.put("purchaseBillId", 2);
			value.put("pGoodsNum", 3);
			value.put("pPriceId", 6);
			
			db.insertOrThrow(PURCHASEITEM, null, value);
			return true;
			
		}
		private boolean initSalesBill(SQLiteDatabase db) {
			 
			return true;
			
		}
		private boolean initSalesItem(SQLiteDatabase db) {

			return true;
			
		}
		private boolean initUnit(SQLiteDatabase db){
			ContentValues value = new ContentValues();
			
			value.clear();
			value.put("name", "包");
			db.insertOrThrow(UNIT, null, value);
			
			value.clear();
			value.put("name", "条");
			db.insertOrThrow(UNIT, null, value);
			
			value.clear();
			value.put("name", "箱");
			db.insertOrThrow(UNIT, null, value);
			
			value.clear();
			value.put("name", "斤");
			db.insertOrThrow(UNIT, null, value);
			
			value.clear();
			value.put("name", "件");
			db.insertOrThrow(UNIT, null, value);
			
			value.clear();
			value.put("name", "支");
			db.insertOrThrow(UNIT, null, value);
			
			value.clear();
			value.put("name", "张");
			db.insertOrThrow(UNIT, null, value);
			
			return true;
		}
		private boolean initUserInfo(SQLiteDatabase db){
			
			ContentValues value = new ContentValues();
			
			value.clear();
			value.put("userName", "laoda");
			value.put("password", "123");
			value.put("permission", 0);
			value.put("status", 0);
			db.insertOrThrow(USERINFO, null , value);
			
			value.clear();
			value.put("userName", "laoer");
			value.put("password", "abc");
			value.put("permission", 1);
			value.put("status", 0);
			db.insertOrThrow(USERINFO, null , value);
			
			return true;
		}
		private boolean initVIPInfo(SQLiteDatabase db) {
			ContentValues value = new ContentValues();

			value.clear();
			value.put("VIPNum", "common");
			value.put("VIPName", "普通客户");
			value.put("VIPDiscount", 1);
			db.insertOrThrow(VIPINFO, null, value);
			
			value.clear();
			value.put("VIPNum", "vip0001");
			value.put("VIPName", "佟湘玉");
			value.put("VIPDiscount", 0.9);
			db.insertOrThrow(VIPINFO, null, value);

			value.clear();
			value.put("VIPNum", "vip0002");
			value.put("VIPName", "白展堂");
			value.put("VIPDiscount", 0.85);
			db.insertOrThrow(VIPINFO, null, value);

			value.clear();
			value.put("VIPNum", "vip0003");
			value.put("VIPName", "吕轻侯");
			value.put("VIPDiscount", 0.9);
			db.insertOrThrow(VIPINFO, null, value);

			value.clear();
			value.put("VIPNum", "vip0004");
			value.put("VIPName", "郭芙蓉");
			value.put("VIPDiscount", 0.9);
			db.insertOrThrow(VIPINFO, null, value);

			value.clear();
			value.put("VIPNum", "vip0005");
			value.put("VIPName", "莫小贝");
			value.put("VIPDiscount", 0.8);
			db.insertOrThrow(VIPINFO, null, value);

			return true;
		}
}
