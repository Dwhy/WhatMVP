package com.slife.authentication.bean;

public class PostParam {
	private String id;			//主键 生成的guid
	private String date;		//提交时间
	private String json;		//提交内容
	private String title;		//提交标题
	private int state;			//提交状态 0：未提交	1.已提交	10.数据已提交，附件未提交
	private int isHavePic;		//是否有附件 0.没有	1.有
	private String other;		//其他预留字段
	private String ip;			//访问的ip
	private String url;			//访问的路径
	private String sToken;  	//sToken
	private String funcName;	//访问的方法名
	private String funCode;		//调用方法
	private String appPackage;	//应用包名
	private String username;	//数据提交者
	private String resultTime;	//服务器返回时间
	private String resultContent;	//返回内容
	private String resultId;	//服务器添加成功返回的ID
	private String broadcastPath;	//发送广播路径
	private int broadcastState;	//0未接收		1.已接收
	private int isPic;			//是否图片 0.否		1.是
	private String theId;		//所属数据ID (若为空说明仅做照片不做附件)
	private String path;		//照片路径
	private int level;			//数据等级
	private int submitCount; 	//提交次数
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getIsHavePic() {
		return isHavePic;
	}
	public void setIsHavePic(int isHavePic) {
		this.isHavePic = isHavePic;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getsToken() {
		return sToken;
	}
	public void setsToken(String sToken) {
		this.sToken = sToken;
	}
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public String getFunCode() {
		return funCode;
	}
	public void setFunCode(String funCode) {
		this.funCode = funCode;
	}
	public String getAppPackage() {
		return appPackage;
	}
	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getResultTime() {
		return resultTime;
	}
	public void setResultTime(String resultTime) {
		this.resultTime = resultTime;
	}
	public String getResultContent() {
		return resultContent;
	}
	public void setResultContent(String resultContent) {
		this.resultContent = resultContent;
	}
	public String getResultId() {
		return resultId;
	}
	public void setResultId(String resultId) {
		this.resultId = resultId;
	}
	public String getBroadcastPath() {
		return broadcastPath;
	}
	public void setBroadcastPath(String broadcastPath) {
		this.broadcastPath = broadcastPath;
	}
	public int getIsPic() {
		return isPic;
	}
	public void setIsPic(int isPic) {
		this.isPic = isPic;
	}
	public String getTheId() {
		return theId;
	}
	public void setTheId(String theId) {
		this.theId = theId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public PostParam() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getBroadcastState() {
		return broadcastState;
	}
	public void setBroadcastState(int broadcastState) {
		this.broadcastState = broadcastState;
	}

	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

	public int getSubmitCount() {
		return submitCount;
	}
	public void setSubmitCount(int submitCount) {
		this.submitCount = submitCount;
	}
	@Override
	public String toString() {
		return "PostParam [id=" + id + ", date=" + date + ", json=" + json + ", title=" + title + ", state=" + state
				+ ", isHavePic=" + isHavePic + ", other=" + other + ", ip=" + ip + ", url=" + url + ", sToken=" + sToken
				+ ", funcName=" + funcName + ", funCode=" + funCode + ", appPackage=" + appPackage + ", username="
				+ username + ", resultTime=" + resultTime + ", resultContent=" + resultContent + ", resultId="
				+ resultId + ", broadcastPath=" + broadcastPath + ", broadcastState=" + broadcastState + ", isPic="
				+ isPic + ", theId=" + theId + ", path=" + path + ", level=" + level + ", submitCount=" + submitCount
				+ "]";
	}



}
