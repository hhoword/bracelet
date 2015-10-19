package com.huayu.bracelet.vo;

public class UserInfo {
	private int id;//用户id long

	private String screen_name;//:昵称 string

	private String name;//:用户名 string

	private String  province;//:身份编号 int

	private int  city;//:城市编号 int

	private String location;//:地址 string

	private String desc;//:个性签名 string

	private String profile_img_url;//:头像url string url编码方式提供

	private String sex;//:性别 int 0表示女 1表示男 2或其他表示未知

	private String create_at;//:创建时间 string yyyy-MM-dd HH:mm:ss格式

	private String phonenum;//:电话号码 string

	private boolean online_status;//:在线状态 bool

	private int level;//:用户级别 int

	private int height;//:身高 float

	private int weight;//:体重 float
}
