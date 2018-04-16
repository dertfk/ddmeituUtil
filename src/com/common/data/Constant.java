package com.common.data;


/**
 * 常量信息类
 * 
 * @author hao.hu
 */
public class Constant {

	private static final boolean IS_TEST = false;
	
	// 阿里云测试账号
	private static final String TEST_ACCESSID = "LTAIrDdJODmKB5Qn";
	private static final String TEST_ACCESSKEY = "0SRkjTRalExZvuaH70cHcp312C7w8e";
	private static final String TEST_STS_ACCESSID = "LTAI6hTvR7dNlp0S";
	private static final String TEST_STS_ACCESSKEY = "T8Wv1DUO71HcVYhlUCC4U6Ys1orow2";
	private static final String TEST_STS_ROLEARN = "acs:ram::1136051292329249:role/readandwrite";
	private static final String TEST_BUCKET_WECHAT = "ali-flash-test";//"v3-wehcat-test";

	// 阿里云正式账号
	private static final String FORMAL_ACCESSID = "LTAIrDdJODmKB5Qn";
	private static final String FORMAL_ACCESSKEY = "0SRkjTRalExZvuaH70cHcp312C7w8e";
	private static final String FORMAL_STS_ACCESSID = "LTAI6hTvR7dNlp0S";
	private static final String FORMAL_STS_ACCESSKEY = "T8Wv1DUO71HcVYhlUCC4U6Ys1orow2";
	private static final String FORMAL_STS_ROLEARN = "acs:ram::1136051292329249:role/readandwrite";
	private static final String FORMAL_BUCKET_WECHAT = "ali-v3-wechat";
	
	// 阿里云上传设置
	public static final String ENDPOINT = "oss-cn-shenzhen";
	public static final String ACCESSID = IS_TEST ? TEST_ACCESSID : FORMAL_ACCESSID;
	public static final String ACCESSKEY = IS_TEST ? TEST_ACCESSKEY : FORMAL_ACCESSKEY;
	public static final String BUCKET_WECHAT = IS_TEST ? TEST_BUCKET_WECHAT : FORMAL_BUCKET_WECHAT;
	public static final String WSC_URL = "http://" + BUCKET_WECHAT + ".img-cn-shenzhen.aliyuncs.com";
	public static final String WSC_IMG_URL = "http://" + BUCKET_WECHAT + ".oss-cn-shenzhen.aliyuncs.com";

	// 阿里云STS设置
	// 目前只有"cn-hangzhou"这个region可用, 不要使用填写其他region的值
	public static final String STS_REGION = "cn-hangzhou";
	// 当前 STS API 版本
	public static final String STS_API_VERSION = "2015-04-01";
	// 子账号信息
	public static final String STS_ACCESSID = IS_TEST ? TEST_STS_ACCESSID : FORMAL_STS_ACCESSID;
	public static final String STS_ACCESSKEY = IS_TEST ? TEST_STS_ACCESSKEY : FORMAL_STS_ACCESSKEY;
	// 角色信息 需要在 RAM 控制台上获取
	public static final String STS_ROLEARN = IS_TEST ? TEST_STS_ROLEARN : FORMAL_STS_ROLEARN;
	// RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
	// 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '.' '@' 字母和数字等字符
	// 具体规则请参考API文档中的格式要求
	public static final String STS_ROLESESSIONNAME = "alice-001";


}
