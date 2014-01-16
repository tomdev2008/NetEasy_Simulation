package com.simulation.neteasy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.simulation.neteasy.constant.DBConstant;
import com.simulation.neteasy.constant.DBConstant.TABLE;
import com.simulation.neteasy.constant.DBConstant.TABLE.TABLE_LINE;
import com.simulation.neteasy.constant.DBConstant.TAB_COLLECT.COLLECT_TABLE_LINE;
/**
 * 
 * @author 紫枫
 *
 */
public class DBHelp extends SQLiteOpenHelper {

	public DBHelp(Context context) {
		super(context, DBConstant.DATABASE.DATANASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + DBConstant.TABLE.TABLE_NAME + "("
				+ TABLE_LINE.ID + " integer , " + TABLE_LINE.EMAIL + " text, "
				+ TABLE_LINE.PASSWORD + " text, " + TABLE.TABLE_LINE.ADDRESS
				+ " text, " + TABLE_LINE.HEADIMAGE + " BLOB, "
				+ TABLE_LINE.FLAG + " text" + ")";
		String collect = "create table " + DBConstant.TAB_COLLECT.TABLE_NAME
				+ "(" + COLLECT_TABLE_LINE.ID
				+ " integer  primary key autoincrement ,"
				+ COLLECT_TABLE_LINE.NEWS_ID + " integer , "
				+ COLLECT_TABLE_LINE.NEWS_IMG + " blob , "
				+ COLLECT_TABLE_LINE.NEWS_TITLE + " text "+")";
		db.execSQL(sql);
		db.execSQL(collect);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
