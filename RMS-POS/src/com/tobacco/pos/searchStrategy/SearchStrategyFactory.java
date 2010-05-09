package com.tobacco.pos.searchStrategy;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

public class SearchStrategyFactory {

	private static SearchStrategyFactory instance = new SearchStrategyFactory();
	
	private SearchStrategyFactory(){
		Log.i("TestStrategy", "create SearchStrategyFactory instance");
	}
	
	public static SearchStrategyFactory getInstance(){
		return instance;
	}
	
	public ISearchStrategy getSearchStrategy(Context ctx, Uri contentUri, String[] projection, SearchState state){
		ISearchStrategy strategy = null;
		try {
			Log.i("TestStrategy", "strateObjects.size():"+state.getStrategyObjects().size());
			if(state.getStrategyObjects().size()==1){	
				SearchState.StrategyObject strategyObject = state.getStrategyObjects().get(0);
				strategy = (ISearchStrategy)Class.forName(strategyObject.getStrategyClassName()).newInstance();
				Log.i("TestStrategy", "strategy:"+strategy.toString());
				strategy.initStrategy(ctx, contentUri, projection, strategyObject);
				return strategy;
			}else{
				CompositeSearchStrategy composite = new CompositeSearchStrategy(ctx, contentUri, projection);
				for(SearchState.StrategyObject strategyObject : state.getStrategyObjects()){
					strategy = (ISearchStrategy)Class.forName(strategyObject.getStrategyClassName()).newInstance();
					strategy.initStrategy(ctx, contentUri, projection, strategyObject);
					composite.addStrategy(strategy);
				}
				return composite;
			}			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			state.getStrategyObjects().clear();
		}
		
		
		
		
//		switch(state.getFactor()){
//		case 0:
//			return new AllSearchStrategy();
//		case 1:
//			if(state.billNum){
//				return new BillNumSearchStrategy();
//			}else if(state.kind){
//				return new kindSearchStrategy();
//			}else if(state.name){
//				return new NameSearchStrategy();
//			}else if(state.operator){
//				return new OperatorSearchStrategy();
//			}else if(state.time){
//				return new TimeSearchStrategy();
//			}
//		case 2:
//		{
//			CompositeSearchStrategy composite = new CompositeSearchStrategy();
//			if(state.billNum){
//				composite.addStrategy(new BillNumSearchStrategy());
//			}else if(state.kind){
//				composite.addStrategy(new kindSearchStrategy());
//			}else if(state.name){
//				composite.addStrategy(new NameSearchStrategy());
//			}else if(state.operator){
//				composite.addStrategy(new OperatorSearchStrategy());
//			}else if(state.time){
//				composite.addStrategy(new TimeSearchStrategy());
//			}
//			return composite;
//		}
//		}
//		return null;
	}
}
