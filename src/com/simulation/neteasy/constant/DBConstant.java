package com.simulation.neteasy.constant;
/**
 * 
 * @author 紫枫
 *
 */
public final  class DBConstant {

	public final static class DATABASE{
		public final static String DATANASE_NAME = "wangyi.db";
	}
	public final static class APP_DATABASE{
		public final static String APP_DATABASE_NAME = "appDown.db";
		public final static class APP_DOWN_TAB{
			public final static String APP_TAB_NAME = "downInfo";
			public final static String ID = "_id";
			public final static String NAME = "name";
			public final static String APP_ID = "appId";
			public final static String THREADID = "threadId";
			public final static String DOWNLOAD_LENGTH = "length";
			public final static String STAET = "state";
			public final static String APPICON = "appIcon";
			public final static String MAXLENGTH = "maxLength";
		}
	}
	
	public final static class TABLE{
		public final static String TABLE_NAME = "userInfo";
		public final static class TABLE_LINE{
			public final static String ID = "_id";
			public final static String EMAIL = "email";
			public final static String PASSWORD = "password";
			public final static String HEADIMAGE= "headImage";
			public final static String ADDRESS= "address";
			public final static String FLAG = "flag";
		}
	}
	
	public final static class TAB_COLLECT{
		public final static String TABLE_NAME = "collect";
		public final static class COLLECT_TABLE_LINE{
			public final static String ID = "_id";
			public final static String NEWS_ID = "newsId";
			public final static String NEWS_IMG = "newsImg";
			public final static String NEWS_TITLE= "title";
		}
	}
}
