package com.tigerjoys.shark.supersign.comm.sign;

import java.io.Serializable;

/**
 * 签名进度信息
 *
 */
public class SignProcess implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6332847074765487632L;

	/**
	 * 安装进度
	 */
	private int process;

	/**
	 * 状态信息
	 */
    private String status;

    /**
     * 如果安装进度为100%，则肯定生成了plist文件，此为plist文件的路径
     */
    private String plist;

    public SignProcess() {
    	
    }

    public SignProcess(int process) {
        this.process = process;
        this.status = "processing";
        this.plist = "";
    }

    public SignProcess(int process, String status, String plist) {
        this.process = process;
        this.status = status;
        this.plist = plist;
    }
    
    /**
     * 正在安装的对象
     * @param process - 安装进度，最高100
     * @return SignProcess
     */
    public static SignProcess process(int process) {
    	return new SignProcess(process);
    }
    
    /**
     * 安装成功的对象
     * @param plist - plist文件地址
     * @return SignProcess
     */
    public static SignProcess success(String plist) {
    	return new SignProcess(100, "successed", plist);
    }

	public int getProcess() {
		return process;
	}

	public void setProcess(int process) {
		this.process = process;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPlist() {
		return plist;
	}

	public void setPlist(String plist) {
		this.plist = plist;
	}
    
}
