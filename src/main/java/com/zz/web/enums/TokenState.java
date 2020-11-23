package com.zz.web.enums;

/**
 * 枚举，定义token的三种状态
 * @author Z sir
 *
 */
public enum TokenState {
	/**
	 * 过期
	 */
	EXPIRED("EXPIRED"),
	/**
	 * 无效(token不合法)
	 */
	INVALID("INVALID"),
	/**
	 * 有效的
	 */
	VALID("VALID");

	private final String  state;

	private TokenState(String state){
		this.state = state;
	}

}  
