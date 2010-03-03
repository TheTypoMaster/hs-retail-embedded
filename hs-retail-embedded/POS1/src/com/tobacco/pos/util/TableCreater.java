package com.tobacco.pos.util;

import static android.provider.BaseColumns._ID;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context; 
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TableCreater extends SQLiteOpenHelper {


	private static final String DATABASE_NAME = "POS.db";// ���ݿ�����

	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_NAME1 = "UserInfo";// �û��������Ա������Ա

	private static final String TABLE_NAME2 = "GoodsKind";// ��Ʒ����

	private static final String TABLE_NAME3 = "Unit";// ��λ��

	private static final String TABLE_NAME4 = "Goods";// ��Ʒ��

	private static final String TABLE_NAME5 = "GoodsPrice";// ��Ʒ�۸��

	private static final String TABLE_NAME6 = "VIPInfo";// ��Ա��Ϣ��

	private static final String TABLE_NAME7 = "SalesBill";// ���۵�

	private static final String TABLE_NAME8 = "SalesItem";// ������

	private static final String TABLE_NAME9 = "PurchaseBill";// ������

	private static final String TABLE_NAME10 = "PurchaseItem";// ������

	private static final String TABLE_NAME15 = "Manufacturer";// ����

	public TableCreater(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	// ������
	public void createTable(SQLiteDatabase db) {

		// �����û���
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

		// ������Ʒ����
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

		// ������λ��
		try {
			db.query(TABLE_NAME3, null, null, null, null, null, null);
		} catch (Exception e) {
			db.execSQL("create table " + TABLE_NAME3 + " ( " + _ID
					+ " integer primary key autoincrement,"
					+ " name varchar(20) not null unique )");
			
			this.initUnit(db);
		}

		//�������ұ�
		
		try{
			db.query(TABLE_NAME15, null, null, null, null, null, null);
		}catch(Exception e){
			db.execSQL("create table " + TABLE_NAME15 + " ( " + _ID
					+ " integer primary key autoincrement,"
					+ " mName varchar(50) not null unique )");
			
			this.initManufacturer(db);
		}
		// ������Ʒ��
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

		// ������Ʒ�۸��
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

		// ������Ա��Ϣ��
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

		// �������۵�
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

		// ����������
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
		

		// ����������
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

		// ����������
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
		value.put("comment", "��һ�Ž�����");
		db.insertOrThrow(TABLE_NAME9, null, value);
		
		value.clear();
		value.put("pBillNum", "P2");
		value.put("operId", 1);
		Date d2 = new Date();
		value.put("time", d2.toLocaleString());
		value.put("comment", "�ڶ��Ž�����");
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
		value.put("name", "��");
		db.insertOrThrow(TABLE_NAME3, null, value);
		
		value.clear();
		value.put("name", "��");
		db.insertOrThrow(TABLE_NAME3, null, value);
		
		value.clear();
		value.put("name", "��");
		db.insertOrThrow(TABLE_NAME3, null, value);
		
		value.clear();
		value.put("name", "��");
		db.insertOrThrow(TABLE_NAME3, null, value);
		
		return true;
	}
	private boolean initUserInfo(SQLiteDatabase db){
		
		ContentValues value = new ContentValues();
		
		value.clear();
		value.put("userName", "laoda");
		value.put("password", "123");
		value.put("permission", 0);//0��Ȩ�ޱ�1��
		value.put("status", 0);//�û�Ĭ�������ߵ�,0Ϊ����,1Ϊ����
		db.insertOrThrow(TABLE_NAME1, null , value);
		
		value.clear();
		value.put("userName", "laoer");
		value.put("password", "abc");
		value.put("permission", 1);
		value.put("status", 0);//�û�Ĭ�������ߵ�
		db.insertOrThrow(TABLE_NAME1, null , value);
		
		return true;
	}
	private boolean initManufacturer(SQLiteDatabase db){
		
		ContentValues value = new ContentValues();
		
		value.clear();
		value.put("mName", "���Ҿ��̳�");
		db.insertOrThrow(TABLE_NAME15, null, value);
		
		value.clear();
		value.put("mName", "���ž��̳�");
		db.insertOrThrow(TABLE_NAME15, null, value);
		
		value.clear();
		value.put("mName", "���ݾ��̳�");
		db.insertOrThrow(TABLE_NAME15, null, value);
		
		value.clear();
		value.put("mName", "���ݾ��̳�");
		db.insertOrThrow(TABLE_NAME15, null, value);
		
		return true;
	}
	private boolean initGoods(SQLiteDatabase db){
		
		ContentValues value = new ContentValues();
		
		value.clear();
		value.put("goodsCode", "G0001");
		value.put("goodsName", "����ɽ");
		value.put("manufacturerId", 1);
		value.put("codeForShort", "1");
		value.put("goodsFormat", "");
		value.put("kindId", 8);
		db.insertOrThrow(TABLE_NAME4, null, value);
		
		value.clear();
		value.put("goodsCode", "G0002");
		value.put("goodsName", "��÷");
		value.put("manufacturerId", 1);
		value.put("codeForShort", "2");
		value.put("goodsFormat", "");
		value.put("kindId", 8);
		db.insertOrThrow(TABLE_NAME4, null, value);
		
		value.clear();
		value.put("goodsCode", "G0003");
		value.put("goodsName", "һƷ÷");
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
		value.put("VIPName", "١����");
		value.put("VIPDiscount", 0.9);
		db.insertOrThrow(TABLE_NAME6, null, value);
		
		value.clear();
		value.put("VIPNum", "VIP0002");
		value.put("VIPName", "��չ��");
		value.put("VIPDiscount", 0.85);
		db.insertOrThrow(TABLE_NAME6, null, value);
		
		value.clear();
		value.put("VIPNum", "VIP0003");
		value.put("VIPName", "��ܽ��");
		value.put("VIPDiscount", 0.9);
		db.insertOrThrow(TABLE_NAME6, null, value);
		
		value.clear();
		value.put("VIPNum", "VIP0004");
		value.put("VIPName", "�����");
		value.put("VIPDiscount", 0.9);
		db.insertOrThrow(TABLE_NAME6, null, value);
		
		value.clear();
		value.put("VIPNum", "VIP0005");
		value.put("VIPName", "�����");
		value.put("VIPDiscount", 0.8);
		db.insertOrThrow(TABLE_NAME6, null, value);
		
		return true;
	}
	private boolean initGoodsPrice(SQLiteDatabase db){

		ContentValues value = new ContentValues();
		
		value.clear();
		value.put("goodsId", 1);//1����Ʒ"����ɽ"��ÿ"��"����ʱ6.0Ԫ���ۼ�7.0Ԫ
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
	private boolean initGoodsKind(SQLiteDatabase db) {// �������ݱ�����һЩ��ʼ����

		ContentValues value = new ContentValues();
		value.put("name", "�·�");
		value.put("parent", 0);
		value.put("level", 1);
		value.put("comment", "�·����࣬���е����ﶼ�Ǹ��������");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "��װ");
		value.put("parent", 1);
		value.put("level", 2);
		value.put("comment", "��װMan");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "Ůװ");
		value.put("parent", 1);
		value.put("level", 2);
		value.put("comment", "ŮװWoman");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "�����޷�");
		value.put("parent", 2);
		value.put("level", 3);
		value.put("comment", "�������ˣ����¶Ȳ��з��");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "�п�");
		value.put("parent", 2);
		value.put("level", 3);
		value.put("comment", "�Ǻǣ��ɱ����˴�����ร�Ҫ�����ܴ���");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "ͯװ");
		value.put("parent", 1);
		value.put("level", 2);
		value.put("comment", "ͯװ����");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "ŮʽΧ��");
		value.put("parent", 3);
		value.put("level", 3);
		value.put("comment", "���ˣ���Ҫ����������");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "����");
		value.put("parent", 0);
		value.put("level", 1);
		value.put("comment", "���̲����Ǻ�˧���������Ҫ�");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "ˮ��");
		value.put("parent", 0);
		value.put("level", 1);
		value.put("comment", "���ˮ��������������");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "���");
		value.put("parent", 8);
		value.put("level", 2);
		value.put("comment", "��о���");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "�ľ�");
		value.put("parent", 0);
		value.put("level", 1);
		value.put("comment", "ѧ���ıر�֮��");

		db.insert(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "Ǧ��");
		value.put("parent", 11);
		value.put("level", 2);
		value.put("comment", "ʹ��ʱҪע�⻷��");

		db.insertOrThrow(TABLE_NAME2, null, value);

		value.clear();
		value.put("name", "�ֱ�");
		value.put("parent", 11);
		value.put("level", 2);
		value.put("comment", "�ɹ���ʿ�ıر�֮��");

		db.insertOrThrow(TABLE_NAME2, null, value);

		return true;
	}

	
}
