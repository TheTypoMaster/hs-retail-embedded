package com.tobacco.pos.util;

import static android.provider.BaseColumns._ID;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context; 
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TableCreater extends SQLiteOpenHelper {


	private static final String DATABASE_NAME = "POS.db";// 数据库名称

	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_NAME1 = "UserInfo";// 用户表，如管理员，销售员

	private static final String TABLE_NAME2 = "GoodsKind";// 商品类别表

	private static final String TABLE_NAME3 = "Unit";// 单位表

	private static final String TABLE_NAME4 = "Goods";// 商品表

	private static final String TABLE_NAME5 = "GoodsPrice";// 商品价格表

	private static final String TABLE_NAME6 = "VIPInfo";// 会员信息表

	private static final String TABLE_NAME7 = "SalesBill";// 销售单

	private static final String TABLE_NAME8 = "SalesItem";// 销售项

	private static final String TABLE_NAME9 = "PurchaseBill";// 进货单

	private static final String TABLE_NAME10 = "PurchaseItem";// 进货项

	private static final String TABLE_NAME15 = "Manufacturer";// 厂家

	public TableCreater(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	// 创建表
	public void createTable(SQLiteDatabase db) {

		// 创建用户表
		try {
			db.query(TABLE_NAME1, null, null, null, null, null, null);
		} catch (Exception e) {
			db.execSQL("create table " + TABLE_NAME1 + " ( " + _ID
					+ " integer primary key autoincrement,"
					+ " userName varchar(50) not null unique, "
					+ " password varchar(50) ,"
					+ " permission integer not null,"
					+ " status integer not null )");
			
			this.initUserInfo(db);
		}

		// 创建商品类别表
		try {
			db.query(TABLE_NAME2, null, null, null, null, null, null);
		} catch (Exception e) {
			db.execSQL("create table " + TABLE_NAME2 + " ( " + _ID
					+ " integer primary key autoincrement,"
					+ " name varchar(50) not null, "
					+ " parent integer references " + TABLE_NAME2 + " ( " + _ID
					+ " )," + " level integer not null, "
					+ " comment varchar(100) )");
			
			this.initGoodsKind(db);
		}

		// 创建单位表
		try {
			db.query(TABLE_NAME3, null, null, null, null, null, null);
		} catch (Exception e) {
			db.execSQL("create table " + TABLE_NAME3 + " ( " + _ID
					+ " integer primary key autoincrement,"
					+ " name varchar(20) not null unique )");
			
			this.initUnit(db);
		}

		//创建厂家表
		
		try{
			db.query(TABLE_NAME15, null, null, null, null, null, null);
		}catch(Exception e){
			db.execSQL("create table " + TABLE_NAME15 + " ( " + _ID
					+ " integer primary key autoincrement,"
					+ " mName varchar(50) not null unique )");
			
			this.initManufacturer(db);
		}
		// 创建商品表
		try {
			db.query(TABLE_NAME4, null, null, null, null, null, null);
		} catch (Exception e) {
			db.execSQL("create table " + TABLE_NAME4 + " ( " + _ID
					+ " integer primary key autoincrement,"
					+ " goodsCode varchar(50) not null unique ,"
					+ "goodsName varchar(50) not null,"
					+ " manufacturerId integer references  " + TABLE_NAME15 + " ( " + _ID + " ),"
					+ "codeForShort vachar(50) ,"
					+ "goodsFormat varchar(50)not null,"
					+ "kindId integer references " + TABLE_NAME2 + " (" + _ID
					+ ") )");
			
			this.initGoods(db);
		}

		// 创建商品价格表
		try {
			 db.query(TABLE_NAME5, null, null, null, null, null, null);
		} catch (Exception e) {
			db.execSQL("create table " + TABLE_NAME5 + " ( " + _ID
							+ " integer primary key autoincrement,"
							+ " goodsId integer references " + TABLE_NAME4
							+ " (" + _ID + " ) ,"
							+ "unitId integer references " + TABLE_NAME3 + "( "
							+ _ID + " )," + " barcode varchar(20) unique ,"
							+ "inPrice double not null,"
							+ "outPrice double not null )");
			
			this.initGoodsPrice(db);
		}

		// 创建会员信息表
		try {
			db.query(TABLE_NAME6, null, null, null, null, null, null);
		} catch (Exception e) {
			db.execSQL("create table " + TABLE_NAME6 + " ( " + _ID
					+ " integer primary key autoincrement,"
					+ " VIPNum varchar(20) unique not null, "
					+ "VIPName varchar(20) not null ,"
					+ "VIPDiscount double not null)");
			
			this.initVIPInfo(db);
		}

		// 创建销售单
		try {
			db.query(TABLE_NAME7, null, null, null, null, null, null);
		} catch (Exception e) {
			db.execSQL("create table " + TABLE_NAME7 + " ( " + _ID
					+ " integer primary key autoincrement,"
					+ " sBillNum varchar(20) unique not null, "
					+ "operId integer references " + TABLE_NAME1 + " ( " + _ID + " ),"
					+ "time date not null,"
					+ "VIPId integer references " + TABLE_NAME6 + " ( " + _ID + " ))");
			
			this.initSalesBill(db);
		}

		// 创建销售项
		try {
			db.query(TABLE_NAME8, null, null, null, null, null, null);
		} catch (Exception e) {
			db.execSQL("create table " + TABLE_NAME8 + " ( " + _ID
					+ " integer primary key autoincrement,"
					+ " salesBillId integer references "+ TABLE_NAME7 + " ( " + _ID + " ), "
					+ "sGoodsNum integer not null ,"
				    + "sPriceId integer references " + TABLE_NAME5 + " ( " + _ID + " ))");
			
			this.initSalesItem(db);
		}
		

		// 创建进货单
		try {
			db.query(TABLE_NAME9, null, null, null, null, null, null);
		} catch (Exception e) {
			db.execSQL("create table " + TABLE_NAME9 + " ( " + _ID
					+ " integer primary key autoincrement,"
					+ " pBillNum varchar(20) unique not null, "
					+ " operId integer references " + TABLE_NAME1 + " ( " + _ID + " ),"
					+ " time date not null ," 
					+ " comment varchar(255))");
			
			this.initPurchaseBill(db);
		}

		// 创建进货项
		try {
			db.query(TABLE_NAME10, null, null, null, null, null, null);
		} catch (Exception e) {
			db.execSQL("create table " + TABLE_NAME10 + " ( " + _ID
					+ " integer primary key autoincrement,"
					+ " purchaseBillId integer references "+ TABLE_NAME9 + " ( " + _ID + " ), "
					+ "pGoodsNum integer not null ,"
				    + "pPriceId integer references " + TABLE_NAME5 + " ( " + _ID + " ))");
			
			this.initPurchaseItem(db);
		}
		
		
	}

	private boolean initPurchaseItem(SQLiteDatabase db) {
		
//		ContentValues value = new ContentValues();
//		
//		value.clear();
//		value.put("purchaseBillId", 1);
//		value.put("pGoodsNum", 3);
//		value.put("pPriceId", 1);
//		db.insertOrThrow(TABLE_NAME10, null, value);
//		
//		value.clear();
//		value.put("purchaseBillId", 1);
//		value.put("pGoodsNum", 3);
//		value.put("pPriceId", 2);
//		db.insertOrThrow(TABLE_NAME10, null, value);

		return true;
		
	}

	private boolean initPurchaseBill(SQLiteDatabase db) {
		
		ContentValues value = new ContentValues();
		
		value.clear();
		value.put("pBillNum", "P1");
		value.put("operId", 1);
		Date d1 = new Date();
		value.put("time", d1.toLocaleString());
		value.put("comment", "第一张进货单");
		db.insertOrThrow(TABLE_NAME9, null, value);
		
		value.clear();
		value.put("pBillNum", "P2");
		value.put("operId", 1);
		Date d2 = new Date();
		value.put("time", d2.toLocaleString());
		value.put("comment", "第二张进货单");
		db.insertOrThrow(TABLE_NAME9, null, value);
		
		return true;
		
	}

	private boolean initSalesItem(SQLiteDatabase db) {
	
		ContentValues value = new ContentValues();
		
		value.clear();
		value.put("salesBillId", 1);
		value.put("sGoodsNum", 2);
		value.put("sPriceId", 1);
		db.insertOrThrow(TABLE_NAME8, null, value);
		
		value.clear();
		value.put("salesBillId", 1);
		value.put("sGoodsNum", 3);
		value.put("sPriceId", 2);
		db.insertOrThrow(TABLE_NAME8, null, value);

		value.clear();
		value.put("salesBillId", 2);
		value.put("sGoodsNum", 3);
		value.put("sPriceId", 2);
		db.insertOrThrow(TABLE_NAME8, null, value);
		
		return true;
		
	}

	private boolean initSalesBill(SQLiteDatabase db) {
		 
		ContentValues value = new ContentValues();
		
		value.clear();
		value.put("sBillNum", "S00001");
		value.put("operId", 2);
		Date d1 = new Date();
		value.put("time", d1.toLocaleString());
		value.put("VIPId", 1);
		db.insertOrThrow(TABLE_NAME7, null , value);
		
		value.clear();
		value.put("sBillNum", "S00002");
		value.put("operId", 2);
		Date d2 = new Date();
		value.put("time", d2.toLocaleString());
		value.put("VIPId", 2);
		db.insertOrThrow(TABLE_NAME7, null , value);
		
		return true;
		
	}

	private boolean initUnit(SQLiteDatabase db){
		ContentValues value = new ContentValues();
		
		value.clear();
		value.put("name", "包");
		db.insertOrThrow(TABLE_NAME3, null, value);
		
		value.clear();
		value.put("name", "条");
		db.insertOrThrow(TABLE_NAME3, null, value);
		
		value.clear();
		value.put("name", "箱");
		db.insertOrThrow(TABLE_NAME3, null, value);
		
		value.clear();
		value.put("name", "斤");
		db.insertOrThrow(TABLE_NAME3, null, value);
		
		return true;
	}
	private boolean initUserInfo(SQLiteDatabase db){
		
		ContentValues value = new ContentValues();
		
		value.clear();
		value.put("userName", "laoda");
		value.put("password", "123");
		value.put("permission", 0);//0的权限比1大
		value.put("status", 0);//用户默认是下线的,0为下线,1为上线
		db.insertOrThrow(TABLE_NAME1, null , value);
		
		value.clear();
		value.put("userName", "laoer");
		value.put("password", "abc");
		value.put("permission", 1);
		value.put("status", 0);//用户默认是下线的
		db.insertOrThrow(TABLE_NAME1, null , value);
		
		return true;
	}
	private boolean initManufacturer(SQLiteDatabase db){
		
		ContentValues value = new ContentValues();
		
		value.clear();
		value.put("mName", "龙岩卷烟厂");
		db.insertOrThrow(TABLE_NAME15, null, value);
		
		value.clear();
		value.put("mName", "厦门卷烟厂");
		db.insertOrThrow(TABLE_NAME15, null, value);
		
		value.clear();
		value.put("mName", "漳州卷烟厂");
		db.insertOrThrow(TABLE_NAME15, null, value);
		
		value.clear();
		value.put("mName", "福州卷烟厂");
		db.insertOrThrow(TABLE_NAME15, null, value);
		
		return true;
	}
	private boolean initGoods(SQLiteDatabase db){
		
		ContentValues value = new ContentValues();
		
		value.clear();
		value.put("goodsCode", "G0001");
		value.put("goodsName", "红塔山");
		value.put("manufacturerId", 1);
		value.put("codeForShort", "1");
		value.put("goodsFormat", "");
		value.put("kindId", 8);
		db.insertOrThrow(TABLE_NAME4, null, value);
		
		value.clear();
		value.put("goodsCode", "G0002");
		value.put("goodsName", "红梅");
		value.put("manufacturerId", 1);
		value.put("codeForShort", "2");
		value.put("goodsFormat", "");
		value.put("kindId", 8);
		db.insertOrThrow(TABLE_NAME4, null, value);
		
		value.clear();
		value.put("goodsCode", "G0003");
		value.put("goodsName", "一品梅");
		value.put("manufacturerId", 2);
		value.put("codeForShort", "3");
		value.put("goodsFormat", "");
		value.put("kindId", 8);
		db.insertOrThrow(TABLE_NAME4, null, value);
		
		return true;
	}
	
	private boolean initVIPInfo(SQLiteDatabase db) {
		
		ContentValues value = new ContentValues();
		
		value.clear();
		value.put("VIPNum", "VIP0001");
		value.put("VIPName", "佟湘玉");
		value.put("VIPDiscount", 0.9);
		db.insertOrThrow(TABLE_NAME6, null, value);
		
		value.clear();
		value.put("VIPNum", "VIP0002");
		value.put("VIPName", "白展堂");
		value.put("VIPDiscount", 0.85);
		db.insertOrThrow(TABLE_NAME6, null, value);
		
		value.clear();
		value.put("VIPNum", "VIP0003");
		value.put("VIPName", "郭芙蓉");
		value.put("VIPDiscount", 0.9);
		db.insertOrThrow(TABLE_NAME6, null, value);
		
		value.clear();
		value.put("VIPNum", "VIP0004");
		value.put("VIPName", "吕轻侯");
		value.put("VIPDiscount", 0.9);
		db.insertOrThrow(TABLE_NAME6, null, value);
		
		value.clear();
		value.put("VIPNum", "VIP0005");
		value.put("VIPName", "李大嘴");
		value.put("VIPDiscount", 0.8);
		db.insertOrThrow(TABLE_NAME6, null, value);
		
		return true;
	}
	private boolean initGoodsPrice(SQLiteDatabase db){

		ContentValues value = new ContentValues();
		
		value.clear();
		value.put("goodsId", 1);//1号商品"红塔山"，每"包"进货时6.0元，售价7.0元
		value.put("unitId", 1);
		value.put("inPrice", 6.0);
		value.put("outPrice", 7.0);
		db.insertOrThrow(TABLE_NAME5, null, value);
		
		value.clear();
		value.put("goodsId", 2);
		value.put("unitId", 2);
		value.put("inPrice", 50);
		value.put("outPrice", 60);
		db.insertOrThrow(TABLE_NAME5, null, value);
		
		return true;
	}
	private boolean initGoodsKind(SQLiteDatabase db) {// 建完数据表，插入一些初始数据

		ContentValues value = new ContentValues();
		value.put("name", "衣服");
		value.put("parent", 0);
		value.put("level", 1);
		value.put("comment", "衣服分类，所有的衣物都是该类的子类");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "男装");
		value.put("parent", 1);
		value.put("level", 2);
		value.put("comment", "男装Man");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "女装");
		value.put("parent", 1);
		value.put("level", 2);
		value.put("comment", "女装Woman");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "男羽绒服");
		value.put("parent", 2);
		value.put("level", 3);
		value.put("comment", "冬天来了，有温度才有风度");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "男裤");
		value.put("parent", 2);
		value.put("level", 3);
		value.put("comment", "呵呵，可别忘了穿裤子喔，要不就糗大了");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "童装");
		value.put("parent", 1);
		value.put("level", 2);
		value.put("comment", "童装世界");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "女式围巾");
		value.put("parent", 3);
		value.put("level", 3);
		value.put("comment", "冷了，不要冻到脖子了");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "卷烟");
		value.put("parent", 0);
		value.put("level", 1);
		value.put("comment", "抽烟并不是很帅，身体更重要喔");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "水果");
		value.put("parent", 0);
		value.put("level", 1);
		value.put("comment", "多吃水果，对身体有益");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "软盒");
		value.put("parent", 8);
		value.put("level", 2);
		value.put("comment", "软盒卷烟");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "文具");
		value.put("parent", 0);
		value.put("level", 1);
		value.put("comment", "学生的必备之物");

		db.insert(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "铅笔");
		value.put("parent", 11);
		value.put("level", 2);
		value.put("comment", "使用时要注意环保");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "钢笔");
		value.put("parent", 11);
		value.put("level", 2);
		value.put("comment", "成功人士的必备之物");

		db.insertOrThrow(TABLE_NAME2, null, value);

		return true;
	}

	
}
