package com.huayu.bracelet.view;

import java.io.Serializable;

import com.huayu.bracelet.vo.HooHttpResult;

/**
 * 用户信息实体
 * 
 * @author Hendy
 * @since 1.4.1
 * @date 2014-10-28
 */
public class UserInfo extends HooHttpResult<UserInfo> implements Serializable {
	private static final long serialVersionUID = 1L;
	private String userId; // 用户ID
	private String mobile;// 手机号码
	private String cnName;// 中文昵称
	private String enName;// 英文昵称
	private String type;// 注册类型
	private String realName;// 真实姓名
	private String email;// 电邮地址
	private String portraitPic;// 头像Url
	private String registerDate;// 注册日期
	private String vip;// VIP
	private String birthdate;// 生日
	private String familyType;// 家庭类型
	private String mailAudit;// 邮箱状态
	private String integral;// 用户积分
	private String topNum;// 用户金币排名
	private String subscriberId;// 硬件id
	private String pwd; // 是否有密码标志
	private String userToken;// 用户密钥
	private String flag;// 是否是好友的标志
	private String gender;// 性别
	private String cityId;// 城市ID
	private String praiseNum;// 用户点赞数
	private String cityName;// 城市名称
	private String userAccount;// Tv玩家帐号

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPortraitPic() {
		return portraitPic;
	}

	public void setPortraitPic(String portraitPic) {
		this.portraitPic = portraitPic;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getFamilyType() {
		return familyType;
	}

	public void setFamilyType(String familyType) {
		this.familyType = familyType;
	}

	public String getMailAudit() {
		return mailAudit;
	}

	public void setMailAudit(String mailAudit) {
		this.mailAudit = mailAudit;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public String getTopNum() {
		return topNum;
	}

	public void setTopNum(String topNum) {
		this.topNum = topNum;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(String praiseNum) {
		this.praiseNum = praiseNum;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public UserInfo(String userId, String mobile, String cnName, String enName,
			String type, String realName, String email, String portraitPic,
			String registerDate, String vip, String birthdate,
			String familyType, String mailAudit, String integral,
			String topNum, String subscriberId, String pwd, String userToken,
			String flag, String gender, String cityId, String praiseNum,
			String cityName, String userAccount) {
		super();
		this.userId = userId;
		this.mobile = mobile;
		this.cnName = cnName;
		this.enName = enName;
		this.type = type;
		this.realName = realName;
		this.email = email;
		this.portraitPic = portraitPic;
		this.registerDate = registerDate;
		this.vip = vip;
		this.birthdate = birthdate;
		this.familyType = familyType;
		this.mailAudit = mailAudit;
		this.integral = integral;
		this.topNum = topNum;
		this.subscriberId = subscriberId;
		this.pwd = pwd;
		this.userToken = userToken;
		this.flag = flag;
		this.gender = gender;
		this.cityId = cityId;
		this.praiseNum = praiseNum;
		this.cityName = cityName;
		this.userAccount = userAccount;
	}

}
