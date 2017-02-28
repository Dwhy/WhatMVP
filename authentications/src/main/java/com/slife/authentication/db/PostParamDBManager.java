package com.slife.authentication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.slife.authentication.bean.PostParam;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

public class PostParamDBManager {
	private SlifeDB dbHelper;
	protected SQLiteDatabase db;
	private boolean isSucc=true;
	public  PostParamDBManager(Context context,String dbName,int version){
		dbHelper=new SlifeDB(context,dbName,version);
	}

	public synchronized boolean saveInfo(PostParam params){
		try {
			openDB(false);
			db.execSQL("insert into postParamsInfo(id,json,date,state,title,isHavePic,other,"
							+ "resultId,ip,url,sToken,funcName,funCode,appPackage,userName,resultTime,"
							+ "resultContent,broadcastPath,isPic,theId,path,broadcastState,level,submitCount)"
							+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new String[]{params.getId(),params.getJson(),params.getDate(),
							String.valueOf(params.getState()),params.getTitle(),String.valueOf(params.getIsHavePic()),
							params.getOther(),params.getResultId(),params.getIp(),
							params.getUrl(),params.getsToken(),params.getFuncName(),
							params.getFunCode(),params.getAppPackage(),params.getUsername(),
							params.getResultTime(),params.getResultContent(),params.getBroadcastPath(),
							String.valueOf(params.getIsPic()),params.getTheId(),params.getPath(),
							String.valueOf(params.getBroadcastState()),String.valueOf(params.getLevel()),
							String.valueOf(params.getSubmitCount())});
		} catch (SQLException e) {
			e.printStackTrace();
			isSucc=false;
			CrashReport.postCatchedException(e);
		}finally{
			closeDB();
		}
		return isSucc;
	}
	/**
	 * 查询某用户未提交数据数量
	 * @return
	 */
	public synchronized int getAllFailInfosByClerk(String clerk){
		int count=0;
		try {
			openDB(true);
			Cursor c=db.rawQuery("select count(*) from postParamsInfo where state!=1 and username=?",
					new String[]{clerk});
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				count=c.getInt(c.getColumnIndex("count(*)"));
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeDB();
		}
		return count;
	}

	public synchronized List<PostParam> getAllDataInfosBySuccess(){
		List<PostParam> pList=new ArrayList<PostParam>();
		PostParam p=null;
		try {
			openDB(true);
			Cursor c=db.rawQuery("select * from postParamsInfo where isPic=0 and state=1", null);
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				p=new PostParam();
				p.setAppPackage(c.getString(c.getColumnIndex("appPackage")));
				p.setBroadcastPath(c.getString(c.getColumnIndex("broadcastPath")));
				p.setDate(c.getString(c.getColumnIndex("date")));
				p.setFuncName(c.getString(c.getColumnIndex("funcName")));
				p.setFunCode(c.getString(c.getColumnIndex("funCode")));
				p.setId(c.getString(c.getColumnIndex("id")));
				p.setIp(c.getString(c.getColumnIndex("ip")));
				p.setIsHavePic(c.getInt(c.getColumnIndex("isHavePic")));
				p.setIsPic(c.getInt(c.getColumnIndex("isPic")));
				p.setJson(c.getString(c.getColumnIndex("json")));
				p.setOther(c.getString(c.getColumnIndex("other")));
				p.setPath(c.getString(c.getColumnIndex("path")));
				p.setResultContent(c.getString(c.getColumnIndex("resultContent")));
				p.setResultId(c.getString(c.getColumnIndex("resultId")));
				p.setResultTime(c.getString(c.getColumnIndex("resultTime")));
				p.setState(c.getInt(c.getColumnIndex("state")));
				p.setsToken(c.getString(c.getColumnIndex("sToken")));
				p.setTheId(c.getString(c.getColumnIndex("theId")));
				p.setTitle(c.getString(c.getColumnIndex("title")));
				p.setUrl(c.getString(c.getColumnIndex("url")));
				p.setUsername(c.getString(c.getColumnIndex("username")));
				p.setBroadcastState(c.getInt(c.getColumnIndex("broadcastState")));
				p.setLevel(c.getInt(c.getColumnIndex("level")));
				p.setSubmitCount(c.getInt(c.getColumnIndex("submitCount")));
				pList.add(p);
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			pList=null;
		}finally{
			closeDB();
		}
		return pList;
	}

	public synchronized List<PostParam> getAllDataInfos(){
		List<PostParam> pList=new ArrayList<PostParam>();
		PostParam p=null;
		try {
			openDB(true);
			Cursor c=db.rawQuery("select * from postParamsInfo where isPic=0 order by date desc", null);
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				p=new PostParam();
				p.setAppPackage(c.getString(c.getColumnIndex("appPackage")));
				p.setBroadcastPath(c.getString(c.getColumnIndex("broadcastPath")));
				p.setDate(c.getString(c.getColumnIndex("date")));
				p.setFuncName(c.getString(c.getColumnIndex("funcName")));
				p.setFunCode(c.getString(c.getColumnIndex("funCode")));
				p.setId(c.getString(c.getColumnIndex("id")));
				p.setIp(c.getString(c.getColumnIndex("ip")));
				p.setIsHavePic(c.getInt(c.getColumnIndex("isHavePic")));
				p.setIsPic(c.getInt(c.getColumnIndex("isPic")));
				p.setJson(c.getString(c.getColumnIndex("json")));
				p.setOther(c.getString(c.getColumnIndex("other")));
				p.setPath(c.getString(c.getColumnIndex("path")));
				p.setResultContent(c.getString(c.getColumnIndex("resultContent")));
				p.setResultId(c.getString(c.getColumnIndex("resultId")));
				p.setResultTime(c.getString(c.getColumnIndex("resultTime")));
				p.setState(c.getInt(c.getColumnIndex("state")));
				p.setsToken(c.getString(c.getColumnIndex("sToken")));
				p.setTheId(c.getString(c.getColumnIndex("theId")));
				p.setTitle(c.getString(c.getColumnIndex("title")));
				p.setUrl(c.getString(c.getColumnIndex("url")));
				p.setUsername(c.getString(c.getColumnIndex("username")));
				p.setBroadcastState(c.getInt(c.getColumnIndex("broadcastState")));
				p.setLevel(c.getInt(c.getColumnIndex("level")));
				pList.add(p);
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			pList=null;
		}finally{
			closeDB();
		}
		return pList;
	}
	/**
	 * 获取所有附件信息
	 * @return
	 */
	public synchronized List<PostParam> getAllPictureInfos(){
		List<PostParam> pList=new ArrayList<PostParam>();
		PostParam p=null;
		try {
			openDB(true);
			Cursor c=db.rawQuery("select * from postParamsInfo where isPic=1", null);
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				p=new PostParam();
				p.setAppPackage(c.getString(c.getColumnIndex("appPackage")));
				p.setBroadcastPath(c.getString(c.getColumnIndex("broadcastPath")));
				p.setDate(c.getString(c.getColumnIndex("date")));
				p.setFuncName(c.getString(c.getColumnIndex("funcName")));
				p.setFunCode(c.getString(c.getColumnIndex("funCode")));
				p.setId(c.getString(c.getColumnIndex("id")));
				p.setIp(c.getString(c.getColumnIndex("ip")));
				p.setIsHavePic(c.getInt(c.getColumnIndex("isHavePic")));
				p.setIsPic(c.getInt(c.getColumnIndex("isPic")));
				p.setJson(c.getString(c.getColumnIndex("json")));
				p.setOther(c.getString(c.getColumnIndex("other")));
				p.setPath(c.getString(c.getColumnIndex("path")));
				p.setResultContent(c.getString(c.getColumnIndex("resultContent")));
				p.setResultId(c.getString(c.getColumnIndex("resultId")));
				p.setResultTime(c.getString(c.getColumnIndex("resultTime")));
				p.setState(c.getInt(c.getColumnIndex("state")));
				p.setsToken(c.getString(c.getColumnIndex("sToken")));
				p.setTheId(c.getString(c.getColumnIndex("theId")));
				p.setTitle(c.getString(c.getColumnIndex("title")));
				p.setUrl(c.getString(c.getColumnIndex("url")));
				p.setUsername(c.getString(c.getColumnIndex("username")));
				p.setBroadcastState(c.getInt(c.getColumnIndex("broadcastState")));
				p.setLevel(c.getInt(c.getColumnIndex("level")));
				p.setSubmitCount(c.getInt(c.getColumnIndex("submitCount")));
				pList.add(p);
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			pList=null;
		}finally{
			closeDB();
		}
		return pList;
	}

	public synchronized List<PostParam> getAllInfos(){
		List<PostParam> pList=new ArrayList<PostParam>();
		PostParam p=null;
		try {
			openDB(true);
			Cursor c=db.rawQuery("select * from postParamsInfo", null);
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				p=new PostParam();
				p.setAppPackage(c.getString(c.getColumnIndex("appPackage")));
				p.setBroadcastPath(c.getString(c.getColumnIndex("broadcastPath")));
				p.setDate(c.getString(c.getColumnIndex("date")));
				p.setFuncName(c.getString(c.getColumnIndex("funcName")));
				p.setFunCode(c.getString(c.getColumnIndex("funCode")));
				p.setId(c.getString(c.getColumnIndex("id")));
				p.setIp(c.getString(c.getColumnIndex("ip")));
				p.setIsHavePic(c.getInt(c.getColumnIndex("isHavePic")));
				p.setIsPic(c.getInt(c.getColumnIndex("isPic")));
				p.setJson(c.getString(c.getColumnIndex("json")));
				p.setOther(c.getString(c.getColumnIndex("other")));
				p.setPath(c.getString(c.getColumnIndex("path")));
				p.setResultContent(c.getString(c.getColumnIndex("resultContent")));
				p.setResultId(c.getString(c.getColumnIndex("resultId")));
				p.setResultTime(c.getString(c.getColumnIndex("resultTime")));
				p.setState(c.getInt(c.getColumnIndex("state")));
				p.setsToken(c.getString(c.getColumnIndex("sToken")));
				p.setTheId(c.getString(c.getColumnIndex("theId")));
				p.setTitle(c.getString(c.getColumnIndex("title")));
				p.setUrl(c.getString(c.getColumnIndex("url")));
				p.setUsername(c.getString(c.getColumnIndex("username")));
				p.setBroadcastState(c.getInt(c.getColumnIndex("broadcastState")));
				p.setLevel(c.getInt(c.getColumnIndex("level")));
				p.setSubmitCount(c.getInt(c.getColumnIndex("submitCount")));
				pList.add(p);
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			pList=null;
		}finally{
			closeDB();
		}
		return pList;
	}

	public synchronized List<PostParam> getPictureInfosById(String id){
		List<PostParam> lpList=new ArrayList<PostParam>();
		PostParam p=null;
		try {
			openDB(true);
			Cursor c=db.rawQuery("select * from postParamsInfo where theId=? and isPic=1",
					new String[]{id});
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				p=new PostParam();
				p.setAppPackage(c.getString(c.getColumnIndex("appPackage")));
				p.setBroadcastPath(c.getString(c.getColumnIndex("broadcastPath")));
				p.setDate(c.getString(c.getColumnIndex("date")));
				p.setFuncName(c.getString(c.getColumnIndex("funcName")));
				p.setFunCode(c.getString(c.getColumnIndex("funCode")));
				p.setId(c.getString(c.getColumnIndex("id")));
				p.setIp(c.getString(c.getColumnIndex("ip")));
				p.setIsHavePic(c.getInt(c.getColumnIndex("isHavePic")));
				p.setIsPic(c.getInt(c.getColumnIndex("isPic")));
				p.setJson(c.getString(c.getColumnIndex("json")));
				p.setOther(c.getString(c.getColumnIndex("other")));
				p.setPath(c.getString(c.getColumnIndex("path")));
				p.setResultContent(c.getString(c.getColumnIndex("resultContent")));
				p.setResultId(c.getString(c.getColumnIndex("resultId")));
				p.setResultTime(c.getString(c.getColumnIndex("resultTime")));
				p.setState(c.getInt(c.getColumnIndex("state")));
				p.setsToken(c.getString(c.getColumnIndex("sToken")));
				p.setTheId(c.getString(c.getColumnIndex("theId")));
				p.setTitle(c.getString(c.getColumnIndex("title")));
				p.setUrl(c.getString(c.getColumnIndex("url")));
				p.setUsername(c.getString(c.getColumnIndex("username")));
				p.setBroadcastState(c.getInt(c.getColumnIndex("broadcastState")));
				p.setLevel(c.getInt(c.getColumnIndex("level")));
				p.setSubmitCount(c.getInt(c.getColumnIndex("submitCount")));
				lpList.add(p);
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			p=null;
		}finally{
			closeDB();
		}
		return lpList;
	}

	public synchronized PostParam getPictureInfoByPath(String path){
		PostParam p=null;
		try {
			openDB(true);
			Cursor c=db.rawQuery("select * from postParamsInfo where path=? and isPic=1",
					new String[]{path});
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				p=new PostParam();
				p.setAppPackage(c.getString(c.getColumnIndex("appPackage")));
				p.setBroadcastPath(c.getString(c.getColumnIndex("broadcastPath")));
				p.setDate(c.getString(c.getColumnIndex("date")));
				p.setFuncName(c.getString(c.getColumnIndex("funcName")));
				p.setFunCode(c.getString(c.getColumnIndex("funCode")));
				p.setId(c.getString(c.getColumnIndex("id")));
				p.setIp(c.getString(c.getColumnIndex("ip")));
				p.setIsHavePic(c.getInt(c.getColumnIndex("isHavePic")));
				p.setIsPic(c.getInt(c.getColumnIndex("isPic")));
				p.setJson(c.getString(c.getColumnIndex("json")));
				p.setOther(c.getString(c.getColumnIndex("other")));
				p.setPath(c.getString(c.getColumnIndex("path")));
				p.setResultContent(c.getString(c.getColumnIndex("resultContent")));
				p.setResultId(c.getString(c.getColumnIndex("resultId")));
				p.setResultTime(c.getString(c.getColumnIndex("resultTime")));
				p.setState(c.getInt(c.getColumnIndex("state")));
				p.setsToken(c.getString(c.getColumnIndex("sToken")));
				p.setTheId(c.getString(c.getColumnIndex("theId")));
				p.setTitle(c.getString(c.getColumnIndex("title")));
				p.setUrl(c.getString(c.getColumnIndex("url")));
				p.setUsername(c.getString(c.getColumnIndex("username")));
				p.setBroadcastState(c.getInt(c.getColumnIndex("broadcastState")));
				p.setLevel(c.getInt(c.getColumnIndex("level")));
				p.setSubmitCount(c.getInt(c.getColumnIndex("submitCount")));
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			p=null;
		}finally{
			closeDB();
		}
		return p;
	}

	public synchronized PostParam getInfosById(String id){
		PostParam p=new PostParam();
		try {
			openDB(true);
			Cursor c=db.rawQuery("select * from postParamsInfo where id=? and isPic=0",
					new String[]{id});
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				p.setAppPackage(c.getString(c.getColumnIndex("appPackage")));
				p.setBroadcastPath(c.getString(c.getColumnIndex("broadcastPath")));
				p.setDate(c.getString(c.getColumnIndex("date")));
				p.setFuncName(c.getString(c.getColumnIndex("funcName")));
				p.setFunCode(c.getString(c.getColumnIndex("funCode")));
				p.setId(c.getString(c.getColumnIndex("id")));
				p.setIp(c.getString(c.getColumnIndex("ip")));
				p.setIsHavePic(c.getInt(c.getColumnIndex("isHavePic")));
				p.setIsPic(c.getInt(c.getColumnIndex("isPic")));
				p.setJson(c.getString(c.getColumnIndex("json")));
				p.setOther(c.getString(c.getColumnIndex("other")));
				p.setPath(c.getString(c.getColumnIndex("path")));
				p.setResultContent(c.getString(c.getColumnIndex("resultContent")));
				p.setResultId(c.getString(c.getColumnIndex("resultId")));
				p.setResultTime(c.getString(c.getColumnIndex("resultTime")));
				p.setState(c.getInt(c.getColumnIndex("state")));
				p.setsToken(c.getString(c.getColumnIndex("sToken")));
				p.setTheId(c.getString(c.getColumnIndex("theId")));
				p.setTitle(c.getString(c.getColumnIndex("title")));
				p.setUrl(c.getString(c.getColumnIndex("url")));
				p.setUsername(c.getString(c.getColumnIndex("username")));
				p.setBroadcastState(c.getInt(c.getColumnIndex("broadcastState")));
				p.setLevel(c.getInt(c.getColumnIndex("level")));
				p.setSubmitCount(c.getInt(c.getColumnIndex("submitCount")));
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeDB();
		}
		return p;
	}

	public synchronized PostParam getInfosByResultId(String id,String funcode){
		PostParam p=new PostParam();
		try {
			openDB(true);
			Cursor c=db.rawQuery("select * from postParamsInfo where resultContent like ? "
							+ "and isPic=0 and funCode=?",
					new String[]{"%"+id+"%",funcode});
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				p.setAppPackage(c.getString(c.getColumnIndex("appPackage")));
				p.setBroadcastPath(c.getString(c.getColumnIndex("broadcastPath")));
				p.setDate(c.getString(c.getColumnIndex("date")));
				p.setFuncName(c.getString(c.getColumnIndex("funcName")));
				p.setFunCode(c.getString(c.getColumnIndex("funCode")));
				p.setId(c.getString(c.getColumnIndex("id")));
				p.setIp(c.getString(c.getColumnIndex("ip")));
				p.setIsHavePic(c.getInt(c.getColumnIndex("isHavePic")));
				p.setIsPic(c.getInt(c.getColumnIndex("isPic")));
				p.setJson(c.getString(c.getColumnIndex("json")));
				p.setOther(c.getString(c.getColumnIndex("other")));
				p.setPath(c.getString(c.getColumnIndex("path")));
				p.setResultContent(c.getString(c.getColumnIndex("resultContent")));
				p.setResultId(c.getString(c.getColumnIndex("resultId")));
				p.setResultTime(c.getString(c.getColumnIndex("resultTime")));
				p.setState(c.getInt(c.getColumnIndex("state")));
				p.setsToken(c.getString(c.getColumnIndex("sToken")));
				p.setTheId(c.getString(c.getColumnIndex("theId")));
				p.setTitle(c.getString(c.getColumnIndex("title")));
				p.setUrl(c.getString(c.getColumnIndex("url")));
				p.setUsername(c.getString(c.getColumnIndex("username")));
				p.setBroadcastState(c.getInt(c.getColumnIndex("broadcastState")));
				p.setLevel(c.getInt(c.getColumnIndex("level")));
				p.setSubmitCount(c.getInt(c.getColumnIndex("submitCount")));
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeDB();
		}
		return p;
	}


	public synchronized PostParam getDataById(String id){
		PostParam p=new PostParam();
		try {
			openDB(true);
			Cursor c=db.rawQuery("select * from postParamsInfo where id=?",
					new String[]{id});
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				p.setAppPackage(c.getString(c.getColumnIndex("appPackage")));
				p.setBroadcastPath(c.getString(c.getColumnIndex("broadcastPath")));
				p.setDate(c.getString(c.getColumnIndex("date")));
				p.setFuncName(c.getString(c.getColumnIndex("funcName")));
				p.setFunCode(c.getString(c.getColumnIndex("funCode")));
				p.setId(c.getString(c.getColumnIndex("id")));
				p.setIp(c.getString(c.getColumnIndex("ip")));
				p.setIsHavePic(c.getInt(c.getColumnIndex("isHavePic")));
				p.setIsPic(c.getInt(c.getColumnIndex("isPic")));
				p.setJson(c.getString(c.getColumnIndex("json")));
				p.setOther(c.getString(c.getColumnIndex("other")));
				p.setPath(c.getString(c.getColumnIndex("path")));
				p.setResultContent(c.getString(c.getColumnIndex("resultContent")));
				p.setResultId(c.getString(c.getColumnIndex("resultId")));
				p.setResultTime(c.getString(c.getColumnIndex("resultTime")));
				p.setState(c.getInt(c.getColumnIndex("state")));
				p.setsToken(c.getString(c.getColumnIndex("sToken")));
				p.setTheId(c.getString(c.getColumnIndex("theId")));
				p.setTitle(c.getString(c.getColumnIndex("title")));
				p.setUrl(c.getString(c.getColumnIndex("url")));
				p.setUsername(c.getString(c.getColumnIndex("username")));
				p.setBroadcastState(c.getInt(c.getColumnIndex("broadcastState")));
				p.setLevel(c.getInt(c.getColumnIndex("level")));
				p.setSubmitCount(c.getInt(c.getColumnIndex("submitCount")));
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeDB();
		}
		return p;
	}

	/**
	 * 查询最后一条提交失败的数据
	 * @return
	 */
	public synchronized PostParam getLastDataInfosByFail(){
		PostParam p=null;
		try {
			openDB(true);
			Cursor c=db.rawQuery("select * from postParamsInfo where state=0 and isPic=0 order by date desc limit 1",null);
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				p=new PostParam();
				p.setAppPackage(c.getString(c.getColumnIndex("appPackage")));
				p.setBroadcastPath(c.getString(c.getColumnIndex("broadcastPath")));
				p.setDate(c.getString(c.getColumnIndex("date")));
				p.setFuncName(c.getString(c.getColumnIndex("funcName")));
				p.setFunCode(c.getString(c.getColumnIndex("funCode")));
				p.setId(c.getString(c.getColumnIndex("id")));
				p.setIp(c.getString(c.getColumnIndex("ip")));
				p.setIsHavePic(c.getInt(c.getColumnIndex("isHavePic")));
				p.setIsPic(c.getInt(c.getColumnIndex("isPic")));
				p.setJson(c.getString(c.getColumnIndex("json")));
				p.setOther(c.getString(c.getColumnIndex("other")));
				p.setPath(c.getString(c.getColumnIndex("path")));
				p.setResultContent(c.getString(c.getColumnIndex("resultContent")));
				p.setResultId(c.getString(c.getColumnIndex("resultId")));
				p.setResultTime(c.getString(c.getColumnIndex("resultTime")));
				p.setState(c.getInt(c.getColumnIndex("state")));
				p.setsToken(c.getString(c.getColumnIndex("sToken")));
				p.setTheId(c.getString(c.getColumnIndex("theId")));
				p.setTitle(c.getString(c.getColumnIndex("title")));
				p.setUrl(c.getString(c.getColumnIndex("url")));
				p.setUsername(c.getString(c.getColumnIndex("username")));
				p.setBroadcastState(c.getInt(c.getColumnIndex("broadcastState")));
				p.setLevel(c.getInt(c.getColumnIndex("level")));
				p.setSubmitCount(c.getInt(c.getColumnIndex("submitCount")));
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			p=null;
		}finally{
			closeDB();
		}
		return p;
	}
	/**
	 * 查询所有提交失败的数据
	 * @return
	 */
	public synchronized List<PostParam> getAllDataInfosByFail(){
		List<PostParam> ppList = new ArrayList<PostParam>();
		PostParam p=null;
		try {
			openDB(true);
			Cursor c=db.rawQuery("select * from postParamsInfo where state=0 or state=-1 and isPic=0",null);
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				p=new PostParam();
				p.setAppPackage(c.getString(c.getColumnIndex("appPackage")));
				p.setBroadcastPath(c.getString(c.getColumnIndex("broadcastPath")));
				p.setDate(c.getString(c.getColumnIndex("date")));
				p.setFuncName(c.getString(c.getColumnIndex("funcName")));
				p.setFunCode(c.getString(c.getColumnIndex("funCode")));
				p.setId(c.getString(c.getColumnIndex("id")));
				p.setIp(c.getString(c.getColumnIndex("ip")));
				p.setIsHavePic(c.getInt(c.getColumnIndex("isHavePic")));
				p.setIsPic(c.getInt(c.getColumnIndex("isPic")));
				p.setJson(c.getString(c.getColumnIndex("json")));
				p.setOther(c.getString(c.getColumnIndex("other")));
				p.setPath(c.getString(c.getColumnIndex("path")));
				p.setResultContent(c.getString(c.getColumnIndex("resultContent")));
				p.setResultId(c.getString(c.getColumnIndex("resultId")));
				p.setResultTime(c.getString(c.getColumnIndex("resultTime")));
				p.setState(c.getInt(c.getColumnIndex("state")));
				p.setsToken(c.getString(c.getColumnIndex("sToken")));
				p.setTheId(c.getString(c.getColumnIndex("theId")));
				p.setTitle(c.getString(c.getColumnIndex("title")));
				p.setUrl(c.getString(c.getColumnIndex("url")));
				p.setUsername(c.getString(c.getColumnIndex("username")));
				p.setBroadcastState(c.getInt(c.getColumnIndex("broadcastState")));
				p.setLevel(c.getInt(c.getColumnIndex("level")));
				p.setSubmitCount(c.getInt(c.getColumnIndex("submitCount")));
				ppList.add(p);
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeDB();
		}
		return ppList;
	}

	/**
	 * 查询所有提交失败的数据
	 * @return
	 */
	public synchronized List<PostParam> getAllPictureInfosByFail(){
		List<PostParam> ppList = new ArrayList<PostParam>();
		PostParam p=null;
		try {
			openDB(true);
			Cursor c=db.rawQuery("select * from postParamsInfo where state=0 and isPic=1",null);
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				p=new PostParam();
				p.setAppPackage(c.getString(c.getColumnIndex("appPackage")));
				p.setBroadcastPath(c.getString(c.getColumnIndex("broadcastPath")));
				p.setDate(c.getString(c.getColumnIndex("date")));
				p.setFuncName(c.getString(c.getColumnIndex("funcName")));
				p.setFunCode(c.getString(c.getColumnIndex("funCode")));
				p.setId(c.getString(c.getColumnIndex("id")));
				p.setIp(c.getString(c.getColumnIndex("ip")));
				p.setIsHavePic(c.getInt(c.getColumnIndex("isHavePic")));
				p.setIsPic(c.getInt(c.getColumnIndex("isPic")));
				p.setJson(c.getString(c.getColumnIndex("json")));
				p.setOther(c.getString(c.getColumnIndex("other")));
				p.setPath(c.getString(c.getColumnIndex("path")));
				p.setResultContent(c.getString(c.getColumnIndex("resultContent")));
				p.setResultId(c.getString(c.getColumnIndex("resultId")));
				p.setResultTime(c.getString(c.getColumnIndex("resultTime")));
				p.setState(c.getInt(c.getColumnIndex("state")));
				p.setsToken(c.getString(c.getColumnIndex("sToken")));
				p.setTheId(c.getString(c.getColumnIndex("theId")));
				p.setTitle(c.getString(c.getColumnIndex("title")));
				p.setUrl(c.getString(c.getColumnIndex("url")));
				p.setUsername(c.getString(c.getColumnIndex("username")));
				p.setBroadcastState(c.getInt(c.getColumnIndex("broadcastState")));
				p.setLevel(c.getInt(c.getColumnIndex("level")));
				p.setSubmitCount(c.getInt(c.getColumnIndex("submitCount")));
				ppList.add(p);
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeDB();
		}
		return ppList;
	}
	/**
	 * 获取最近多少条数据的集合
	 * @param count 多少条数据
	 * @return 最近count条数据集合
	 */
	public synchronized List<PostParam>getLatelyCountDataInfos(int count){
		List<PostParam> lp=new ArrayList<PostParam>();
		PostParam p=null;
		try {
			openDB(true);
			Cursor c=db.rawQuery("select * from postParamsInfo where isPic=0 order by date desc limit "+count,null);
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				p=new PostParam();
				p.setAppPackage(c.getString(c.getColumnIndex("appPackage")));
				p.setBroadcastPath(c.getString(c.getColumnIndex("broadcastPath")));
				p.setDate(c.getString(c.getColumnIndex("date")));
				p.setFuncName(c.getString(c.getColumnIndex("funcName")));
				p.setFunCode(c.getString(c.getColumnIndex("funCode")));
				p.setId(c.getString(c.getColumnIndex("id")));
				p.setIp(c.getString(c.getColumnIndex("ip")));
				p.setIsHavePic(c.getInt(c.getColumnIndex("isHavePic")));
				p.setIsPic(c.getInt(c.getColumnIndex("isPic")));
				p.setJson(c.getString(c.getColumnIndex("json")));
				p.setOther(c.getString(c.getColumnIndex("other")));
				p.setPath(c.getString(c.getColumnIndex("path")));
				p.setResultContent(c.getString(c.getColumnIndex("resultContent")));
				p.setResultId(c.getString(c.getColumnIndex("resultId")));
				p.setResultTime(c.getString(c.getColumnIndex("resultTime")));
				p.setState(c.getInt(c.getColumnIndex("state")));
				p.setsToken(c.getString(c.getColumnIndex("sToken")));
				p.setTheId(c.getString(c.getColumnIndex("theId")));
				p.setTitle(c.getString(c.getColumnIndex("title")));
				p.setUrl(c.getString(c.getColumnIndex("url")));
				p.setUsername(c.getString(c.getColumnIndex("username")));
				p.setBroadcastState(c.getInt(c.getColumnIndex("broadcastState")));
				p.setLevel(c.getInt(c.getColumnIndex("level")));
				p.setSubmitCount(c.getInt(c.getColumnIndex("submitCount")));
				lp.add(p);
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeDB();
		}
		return lp;
	}

	/**
	 * 查询最后一条提交失败的照片
	 * @return
	 */
	public synchronized PostParam getLastPictureInfosByFail(){
		PostParam p=null;
		try {
			openDB(true);
			Cursor c=db.rawQuery("select * from postParamsInfo where state=0 and isPic=1 order by date desc limit 1",null);
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				p=new PostParam();
				p.setAppPackage(c.getString(c.getColumnIndex("appPackage")));
				p.setBroadcastPath(c.getString(c.getColumnIndex("broadcastPath")));
				p.setDate(c.getString(c.getColumnIndex("date")));
				p.setFuncName(c.getString(c.getColumnIndex("funcName")));
				p.setFunCode(c.getString(c.getColumnIndex("funCode")));
				p.setId(c.getString(c.getColumnIndex("id")));
				p.setIp(c.getString(c.getColumnIndex("ip")));
				p.setIsHavePic(c.getInt(c.getColumnIndex("isHavePic")));
				p.setIsPic(c.getInt(c.getColumnIndex("isPic")));
				p.setJson(c.getString(c.getColumnIndex("json")));
				p.setOther(c.getString(c.getColumnIndex("other")));
				p.setPath(c.getString(c.getColumnIndex("path")));
				p.setResultContent(c.getString(c.getColumnIndex("resultContent")));
				p.setResultId(c.getString(c.getColumnIndex("resultId")));
				p.setResultTime(c.getString(c.getColumnIndex("resultTime")));
				p.setState(c.getInt(c.getColumnIndex("state")));
				p.setsToken(c.getString(c.getColumnIndex("sToken")));
				p.setTheId(c.getString(c.getColumnIndex("theId")));
				p.setTitle(c.getString(c.getColumnIndex("title")));
				p.setUrl(c.getString(c.getColumnIndex("url")));
				p.setUsername(c.getString(c.getColumnIndex("username")));
				p.setBroadcastState(c.getInt(c.getColumnIndex("broadcastState")));
				p.setLevel(c.getInt(c.getColumnIndex("level")));
				p.setSubmitCount(c.getInt(c.getColumnIndex("submitCount")));
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			p=null;
		}finally{
			closeDB();
		}
		return p;
	}



	public synchronized List<PostParam> getNotReivedInfos(){
		List<PostParam> pList=new ArrayList<PostParam>();
		PostParam p=null;
		try {
			openDB(true);
			Cursor c=db.rawQuery("select * from postParamsInfo where broadcastState=0", null);
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				p=new PostParam();
				p.setAppPackage(c.getString(c.getColumnIndex("appPackage")));
				p.setBroadcastPath(c.getString(c.getColumnIndex("broadcastPath")));
				p.setDate(c.getString(c.getColumnIndex("date")));
				p.setFuncName(c.getString(c.getColumnIndex("funcName")));
				p.setFunCode(c.getString(c.getColumnIndex("funCode")));
				p.setId(c.getString(c.getColumnIndex("id")));
				p.setIp(c.getString(c.getColumnIndex("ip")));
				p.setIsHavePic(c.getInt(c.getColumnIndex("isHavePic")));
				p.setIsPic(c.getInt(c.getColumnIndex("isPic")));
				p.setJson(c.getString(c.getColumnIndex("json")));
				p.setOther(c.getString(c.getColumnIndex("other")));
				p.setPath(c.getString(c.getColumnIndex("path")));
				p.setResultContent(c.getString(c.getColumnIndex("resultContent")));
				p.setResultId(c.getString(c.getColumnIndex("resultId")));
				p.setResultTime(c.getString(c.getColumnIndex("resultTime")));
				p.setState(c.getInt(c.getColumnIndex("state")));
				p.setsToken(c.getString(c.getColumnIndex("sToken")));
				p.setTheId(c.getString(c.getColumnIndex("theId")));
				p.setTitle(c.getString(c.getColumnIndex("title")));
				p.setUrl(c.getString(c.getColumnIndex("url")));
				p.setUsername(c.getString(c.getColumnIndex("username")));
				p.setBroadcastState(c.getInt(c.getColumnIndex("broadcastState")));
				p.setLevel(c.getInt(c.getColumnIndex("level")));
				p.setSubmitCount(c.getInt(c.getColumnIndex("submitCount")));
				pList.add(p);
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			pList=null;
		}finally{
			closeDB();
		}
		return pList;
	}

	/**
	 * 获取与此相关联的父级数据
	 * @return
	 */
	public synchronized PostParam getDataInfosByPicture(String thdId){
		PostParam p=null;
		try {
			openDB(true);
			Cursor c=db.rawQuery("select * from postParamsInfo where id=?",
					new String[]{thdId});
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				p=new PostParam();
				p.setAppPackage(c.getString(c.getColumnIndex("appPackage")));
				p.setBroadcastPath(c.getString(c.getColumnIndex("broadcastPath")));
				p.setDate(c.getString(c.getColumnIndex("date")));
				p.setFuncName(c.getString(c.getColumnIndex("funcName")));
				p.setFunCode(c.getString(c.getColumnIndex("funCode")));
				p.setId(c.getString(c.getColumnIndex("id")));
				p.setIp(c.getString(c.getColumnIndex("ip")));
				p.setIsHavePic(c.getInt(c.getColumnIndex("isHavePic")));
				p.setIsPic(c.getInt(c.getColumnIndex("isPic")));
				p.setJson(c.getString(c.getColumnIndex("json")));
				p.setOther(c.getString(c.getColumnIndex("other")));
				p.setPath(c.getString(c.getColumnIndex("path")));
				p.setResultContent(c.getString(c.getColumnIndex("resultContent")));
				p.setResultId(c.getString(c.getColumnIndex("resultId")));
				p.setResultTime(c.getString(c.getColumnIndex("resultTime")));
				p.setState(c.getInt(c.getColumnIndex("state")));
				p.setsToken(c.getString(c.getColumnIndex("sToken")));
				p.setTheId(c.getString(c.getColumnIndex("theId")));
				p.setTitle(c.getString(c.getColumnIndex("title")));
				p.setUrl(c.getString(c.getColumnIndex("url")));
				p.setUsername(c.getString(c.getColumnIndex("username")));
				p.setBroadcastState(c.getInt(c.getColumnIndex("broadcastState")));
				p.setLevel(c.getInt(c.getColumnIndex("level")));
				p.setSubmitCount(c.getInt(c.getColumnIndex("submitCount")));
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			p=null;
		}finally{
			closeDB();
		}
		return p;
	}

	/**
	 * 根据theId查找该数据下未提交附件的总数
	 * @return
	 */
	public synchronized int getFailPictureCountByThdId(String theId){
		int count=0;
		try {
			openDB(true);
			Cursor c=db.rawQuery("select count(*) from postParamsInfo where theId=? and state=0",
					new String[]{theId});
			while (c.moveToNext()) {	//[id, address, transitId, transitName, cardId]
				count=c.getInt(c.getColumnIndex("count(*)"));
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			count=0;
		}finally{
			closeDB();
		}
		return count;
	}

	public synchronized boolean deleteInfo(String id){
		try {
			openDB(false);
			db.execSQL("delete from postParamsInfo where id=?",new String[]{id});
		} catch (SQLException e) {
			e.printStackTrace();
			isSucc=false;
		}finally{
			closeDB();
		}
		return isSucc;
	}

	public synchronized boolean updateInfo(PostParam tb){
		try {
			openDB(false);
			ContentValues cv=new ContentValues();
			cv.put("appPackage", tb.getAppPackage());
			cv.put("broadcastPath", tb.getBroadcastPath());
			cv.put("date", tb.getDate());
			cv.put("funcName", tb.getFuncName());
			cv.put("funCode", tb.getFunCode());
			cv.put("id", tb.getId());
			cv.put("ip", tb.getIp());
			cv.put("json", tb.getJson());
			cv.put("other", tb.getOther());
			cv.put("path", tb.getPath());
			cv.put("resultContent", tb.getResultContent());
			cv.put("resultId", tb.getResultId());
			cv.put("resultTime", tb.getResultTime());
			cv.put("sToken", tb.getsToken());
			cv.put("theId", tb.getTheId());
			cv.put("title", tb.getTitle());
			cv.put("url", tb.getUrl());
			cv.put("username", tb.getUsername());
			cv.put("isHavePic", tb.getIsHavePic());
			cv.put("isPic", tb.getIsPic());
			cv.put("state", tb.getState());
			cv.put("broadcastState", tb.getBroadcastState());
			cv.put("level", tb.getLevel());
			cv.put("submitCount", tb.getSubmitCount());
			db.update("postParamsInfo", cv, "id=?",
					new String[]{tb.getId()});
		} catch (Exception e) {
			e.printStackTrace();
			isSucc=false;
		}finally{
			closeDB();
		}
		return isSucc;
	}

	public synchronized boolean deleteInfoByErroDatas(){
		try {
			openDB(false);
			db.execSQL("delete from postParamsInfo where funCode=''");
		} catch (SQLException e) {
			e.printStackTrace();
			isSucc=false;
		}finally{
			closeDB();
		}
		return isSucc;
	}

	public synchronized boolean deleteInfo(){
		try {
			openDB(false);
			db.execSQL("delete from postParamsInfo");
		} catch (SQLException e) {
			e.printStackTrace();
			isSucc=false;
		}finally{
			closeDB();
		}
		return isSucc;
	}
	/**
	 * 打开数据库
	 * @param 是否读取
	 */
	protected void openDB(boolean isRead){
		if (isRead) {
			db=dbHelper.getReadableDatabase();
		}else {
			db=dbHelper.getWritableDatabase();
		}
		if (!isSucc) {
			isSucc=true;
		}
	}

	/**
	 * 关闭数据库
	 */
	private void closeDB(){
		if (db!=null) {
			if (db.isOpen()) {
				db.close();
				db=null;
			}
		}
	}
}
