package com.simulation.neteasy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.simulation.neteasy.constant.DBConstant;
/**
 * 
 * @author 紫枫
 *
 */
public class AppDBHelp extends SQLiteOpenHelper {

	public AppDBHelp(Context context) {
		super(context, DBConstant.APP_DATABASE.APP_DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table "
				+ DBConstant.APP_DATABASE.APP_DOWN_TAB.APP_TAB_NAME
				+ " ("+DBConstant.APP_DATABASE.APP_DOWN_TAB.ID+" integer primary key autoincrement, "
				+ DBConstant.APP_DATABASE.APP_DOWN_TAB.APP_ID + " integer ,"
				+ DBConstant.APP_DATABASE.APP_DOWN_TAB.NAME + " text ,"
				+ DBConstant.APP_DATABASE.APP_DOWN_TAB.THREADID + " integer, "
				+ DBConstant.APP_DATABASE.APP_DOWN_TAB.DOWNLOAD_LENGTH + " integer ,"
				+ DBConstant.APP_DATABASE.APP_DOWN_TAB.STAET + " integer," 
				+ DBConstant.APP_DATABASE.APP_DOWN_TAB.APPICON+" BLOB," 
				+DBConstant.APP_DATABASE.APP_DOWN_TAB.MAXLENGTH+" integer)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
