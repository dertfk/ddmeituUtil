package com.common.data;

public class BaseResponse {
	
	private String code = "000000";
	private Object msg = "SUCCESS";
	private Object data = null;

	public BaseResponse(){
	}
	
	public BaseResponse(Object data){
		this.data = data;
	}
	
	public BaseResponse(String code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
  public BaseResponse(String code, String msg, String data){
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getMsg() {
		return msg;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "BaseResponse [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
}

