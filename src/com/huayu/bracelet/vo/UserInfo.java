package com.huayu.bracelet.vo;

public class UserInfo {
	private int id;//用户id long

	private String screen_name;//:昵称 string

	private String name;//:用户名 string
	
	private String pwd;//密码

	private String  province;//:身份编号 int

	private int  city;//:城市编号 int

	private String location;//:地址 string

	private String desc;//:个性签名 string

	private String profile_img_url;//:头像url string url编码方式提供

	private int sex;//:性别 int 0表示女 1表示男 2或其他表示未知

	private String create_at;//:创建时间 string yyyy-MM-dd HH:mm:ss格式

	private String phonenum;//:电话号码 string

	private boolean online_status;//:在线状态 bool

	private int level;//:用户级别 int

	private int height;//:身高 float

	private int weight;//:体重 float
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getProfile_img_url() {
		return profile_img_url;
	}

	public void setProfile_img_url(String profile_img_url) {
		this.profile_img_url = profile_img_url;
	}


	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getCreate_at() {
		return create_at;
	}

	public void setCreate_at(String create_at) {
		this.create_at = create_at;
	}

	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public boolean isOnline_status() {
		return online_status;
	}

	public void setOnline_status(boolean online_status) {
		this.online_status = online_status;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	
	
}
