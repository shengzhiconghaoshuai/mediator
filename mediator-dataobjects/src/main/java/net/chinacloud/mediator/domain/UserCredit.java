/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：UserCredit.java
 * 描述： 用户信用
 */
package net.chinacloud.mediator.domain;
/**
 * <信用等级>
 * <信用等级>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月10日
 * @since 2014年12月10日
 */
public class UserCredit {
	/**信用等级*/
	private Integer level;
	/**信用总分*/
	private Integer score;
	/**收到的评价总条数*/
	private Integer totalNum;
	/**收到的好评总条数*/
	private Integer goodNum;
	
	public UserCredit(Integer level, Integer score, Integer totalNum, Integer goodNum) {
		this.level = level;
		this.score = score;
		this.totalNum = totalNum;
		this.goodNum = goodNum;
	}
	
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getGoodNum() {
		return goodNum;
	}
	public void setGoodNum(Integer goodNum) {
		this.goodNum = goodNum;
	}

	@Override
	public String toString() {
		return "UserCredit [level=" + level + ", score=" + score
				+ ", totalNum=" + totalNum + ", goodNum=" + goodNum + "]";
	}
}
