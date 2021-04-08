package com.tigerjoys.shark.supersign.enums;

import java.util.HashMap;
import java.util.Map;

import com.tigerjoys.nbs.common.utils.Tools;

/**
 * 公用各种状态的标志位。<br>
 * 1-9[含]为正常显示状态，查询Status的时候请用status < 10。<br>
 * 11-29[含]是 统一的非显示非删除状态<br>
 * 30-49[含]为自定义的非显示非删除状态<br>
 * 99删除，50[含]以后是自定义删除状态<br>
 * <b>自定义的请务必写入到具体的状态描述中。10保留</b>
 *
 */
public enum EStatus {
	
	AVAILABLE(1, "正常"),
	UNDER_REVIEW(11,"审核中"),
	MASK(12,"屏蔽"),
	ALLEGE(13,"申述"),
	UNAVAILABLE(14,"停用"),
	LOCK(15,"锁定"),
	SLEEP(16,"睡眠"),
	AUDIT_NO_THROUGHT(17,"审核未过"),
	DRAFT(21,"草稿"),
	DELETED(99,"删除");
	
	/**
	 * 状态值
	 */
	private int code;

	/**
	 * 描述
	 */
	private String memo;
	
	private static final Map<Integer , String> err_desc = new HashMap<Integer , String>();
	
	static {
		for(EStatus refer : EStatus.values()) {
			err_desc.put(refer.getCode(), refer.getMemo());
		}
	}
	
	EStatus(){}
	
	EStatus(int code, String memo){
		this.code = code;
		this.memo = memo;
	}
	
	public static String getDescByCode(int code) {
		return Tools.formatString(err_desc.get(code) , "未知");
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}
