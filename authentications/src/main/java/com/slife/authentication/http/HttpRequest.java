package com.slife.authentication.http;
/**
 *  返回结果类
 * @author JingY
 */
public class HttpRequest {
	private String resultStatus;		//状态
	private String resultDescription;	//描述
	private String resultContent;		//内容
	private long resultContentLength;	//长度
	public String getResultStatus() {
		return resultStatus;
	}
	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}
	public String getResultDescription() {
		return resultDescription;
	}
	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}
	public String getResultContent() {
		return resultContent;
	}
	public void setResultContent(String resultContent) {
		this.resultContent = resultContent;
	}
	public long getResultContentLength() {
		return resultContentLength;
	}
	public void setResultContentLength(long resultContentLength) {
		this.resultContentLength = resultContentLength;
	}
	public HttpRequest(String resultStatus, String resultDescription,
					   String resultContent, long resultContentLength) {
		super();
		this.resultStatus = resultStatus;
		this.resultDescription = resultDescription;
		this.resultContent = resultContent;
		this.resultContentLength = resultContentLength;
	}
	public HttpRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "HttpRequest [resultStatus=" + resultStatus
				+ ", resultDescription=" + resultDescription
				+ ", resultContent=" + resultContent + ", resultContentLength="
				+ resultContentLength + "]";
	}


}
