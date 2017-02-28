package com.slife.authentication.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileWriter;
import java.io.IOException;

public class SlifeDB extends SQLiteOpenHelper{
	protected boolean isUpdate=false;
	public SlifeDB(Context context, String dbName, int version) {
		super(context, dbName, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			if (!db.toString().contains("data/data/")) {
				db.execSQL("create table if not exists postParamsInfo(id text primary key not null,"
						+ "json text,date text,title text,state integer,isHavePic integer,"
						+ "other text,ip text,url text,sToken text,funcName text,funCode text,"
						+ "appPackage text,username text,resultTime text,resultContent text,"
						+ "resultId text,broadcastPath text,broadcastState integer,isPic integer,"
						+ "theId text,path text,level integer,submitCount text)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void dropAllTable(SQLiteDatabase db){

	}
	/**
	 * 检测是否存在某列
	 * @param db 数据库对象
	 * @param tableName  表名
	 * @param fieldName  字段名
	 * @return boolean
	 */
	protected Boolean checkAddSubCodeColumn(SQLiteDatabase db, String tableName, String fieldName) {
		Boolean isExist;
//		String sqlForCheck = "PRAGMA table_info('" + tableName + "')";
		try {
			String sqlForCheck="select * from "+tableName;
			Cursor cr = db.rawQuery(sqlForCheck, null);
			if (-1==cr.getColumnIndex(fieldName)) {
				isExist=false;
			}else {
				isExist=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			isExist=false;
		}
		return isExist;
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion<2) {
			String sql="ALTER TABLE postParamsInfo Add submitCount text null;";
			try {
				if (!checkAddSubCodeColumn(db, "postParamsInfo", "submitCount")) {// 如果不存在字段，则新增字段
					db.execSQL(sql);
				}
				isUpdate=true;
			} catch (SQLException e) {
				e.printStackTrace();
				isUpdate=false;
/*				String path=db.getPath();
				path=path.substring(path.indexOf("."), path.length())+".sql";
				writeToSDCard(path, sql);*/
			}
		}

	}


	protected void writeToSDCard(String path, String content) {
		FileWriter writer = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(path, true);
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
