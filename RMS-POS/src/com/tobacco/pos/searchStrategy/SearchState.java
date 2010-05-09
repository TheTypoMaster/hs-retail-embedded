package com.tobacco.pos.searchStrategy;

import java.util.ArrayList;

import android.util.Log;

public class SearchState {

	private static SearchState instance = new SearchState();
	
	/**
	 * search state for no search factor
	 */
//	public boolean all = false;
	public static final int ALL = 0;
	/**
	 * search state for time search factor
	 */
//	public boolean time = false;
	public static final int TIME = 1;
//	public final int TIME_START = 1;
//	public final int TIME_END = 2;
//	private String timeStartArg;
//	private String timeEndArg;
	
	/**
	 * search state for billNumber search factor
	 */
//	public boolean billNum = false;
	public static final int BILLNUM = 2;
//	private String billNumArg;
	
	/**
	 * search state for kind search factor
	 */
//	public boolean kind = false;
	public static final int KIND = 3;
//	private String kindArg;
	
	/**
	 * search state for goodsName search factor
	 */
//	public boolean name = false;
	public static final int NAME = 4;
//	private String nameArg;
	
	/**
	 * search state for operator search factor
	 */
//	public boolean operator = false;
	public static final int OPERATOR = 5;
//	private String operatorArg;
	
//	private ArrayList<String> selections;
//	
//	private ArrayList<String> selectionArgs;
//	
//	public String[] getSelections() {
//		String[] selArray = new String[selections.size()];
//		selections.toArray(selArray);
//		return selArray;
//	}
//
//	public String[] getSelectionArgs() {
//		String[] selArgsArray = new String[selectionArgs.size()];
//		selections.toArray(selArgsArray);
//		return selArgsArray;
//	}

//	/**
//	 * the count of the selection factor;
//	 */
//	private int factor = 0;
//	
//	public int getFactor(){
//		return factor;
//	}
	
//	/**
//	 * indicate the position of the time select factor
//	 */
//	private int timePosition;
//	
//	public int getTimePosition(){
//		return timePosition;
//	}
	
	private SearchState(){
		Log.i("TestStrategy", "create SearchState instance");
	}
	
	public static SearchState getInstance(){
		return instance;
	}
	
	private ArrayList<StrategyObject> strategyObjects = new ArrayList<StrategyObject>();
	
	public ArrayList<StrategyObject> getStrategyObjects() {
		return strategyObjects;
	}

	public void setSelectionFactor(int which, String[] selection, String[] selectionArgs){
		StrategyObject strategy = null;
		Log.i("TestStrategy", "which:"+which);
		switch(which){
		case ALL:
			strategy = new StrategyObject(ALL,null,null,"com.tobacco.pos.searchStrategy.AllSearchStrategy");
			break;
		case TIME:
			strategy = new StrategyObject(TIME,selection,selectionArgs,"com.tobacco.pos.searchStrategy.RangeSearchStrategy");
			break;
		case BILLNUM:
			strategy = new StrategyObject(BILLNUM,selection,selectionArgs,"com.tobacco.pos.searchStrategy.EqualSearchStrategy");
			break;
		case KIND:
			strategy = new StrategyObject(KIND,selection,selectionArgs,"com.tobacco.pos.searchStrategy.EqualSearchStrategy");
			break;
		case NAME:
			strategy = new StrategyObject(NAME,selection,selectionArgs,"com.tobacco.pos.searchStrategy.LikeSearchStrategy");
			break;
		case OPERATOR:
			strategy = new StrategyObject(OPERATOR,selection,selectionArgs,"com.tobacco.pos.searchStrategy.LikeSearchStrategy");
			break;
		}
		for(StrategyObject object : strategyObjects){
			if(object.getType()==strategy.getType())
				strategyObjects.remove(object);
		}
		strategyObjects.add(strategy);
		
//		if(strategyObjects.size()>1){
//			strategy = new StrategyObject(null,null,"com.tobacco.pos.searchStrategy.CompositeSearchStrategy");
//			strategyObjects.add(strategy);
//		}
	}
	
//	public void setArg(int which, String selection, String selectionArg){
//		selections.add(selection);
//		selectionArgs.add(selectionArg);
//		
//		switch(which){
//		case ALL:
//			all = true;
//			break;
//		case TIME_START:	
//			break;
//		case TIME_END:
//			time = true;
//			timePosition = factor;
//			factor++;
//			break;
//		case BILLNUM:
//			billNum = true;
//			factor++;
//			break;
//		case KIND:
//			kind = true;
//			factor++;
//			break;
//		case NAME:
//			name = true;
//			factor++;
//			break;
//		case OPERATOR:
//			operator = true;
//			factor++;
//			break;
//		}
//	}
	
	class StrategyObject {
		
		private int type;
		
		private String[] selections;
		
		private String[] selectionArgs;
		
		private String strategyClassName;
		
		public StrategyObject(int type, String[] selections,String[] selectionArgs,String className) {
			super();
			// TODO Auto-generated constructor stub
			this.type = type;
			this.selections = selections;
			this.selectionArgs = selectionArgs;
			this.strategyClassName = className;
			Log.i("TestStrategy", "genStrategyObject:"+"selections:"+selections+"selectionArgs:"+selectionArgs+
					"className:"+className);
		}

		public int getType() {
			return type;
		}

		public String[] getSelections() {
			return selections;
		}

		public String[] getSelectionArgs() {
			return selectionArgs;
		}

		public String getStrategyClassName() {
			return strategyClassName;
		}

		public void setStrategyClassName(String strategyClassName) {
			this.strategyClassName = strategyClassName;
		}
		
		
	}
}
