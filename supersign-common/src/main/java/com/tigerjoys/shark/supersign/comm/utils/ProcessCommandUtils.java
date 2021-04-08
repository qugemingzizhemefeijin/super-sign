package com.tigerjoys.shark.supersign.comm.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tigerjoys.nbs.common.utils.StringUtils;

/**
 * 命令行执行
 *
 */
public final class ProcessCommandUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessCommandUtils.class);
	
	/**
	 * 执行命令行
	 * @param commend - 命令
	 * @return boolean
	 */
	public static boolean processCommend(String... command) {
		return processCommend(null, command);
	}

	/**
	 * 执行命令行
	 * @param handler - 命令输出文字处理
	 * @param commend - 命令
	 * @return boolean
	 */
	public static boolean processCommend(IProcessMsgHandler handler, String... command) {
		LOGGER.info(StringUtils.join(command, " "));
    	Process proc = null;
        try {
        	ProcessBuilder builder = new ProcessBuilder();
        	builder.command(command);  
            builder.redirectErrorStream(true);//通知进程生成器是否合并标准错误和标准输出。 
            
            proc = builder.start();
            
            ProcessStreamThread outputStream = new ProcessStreamThread(proc.getInputStream() , handler);
            outputStream.start();
            
            proc.waitFor();
        	
            //如果成功跑到这里了，则代表成功了。
            return true;
        } catch (Exception e) {
        	LOGGER.error(e.getMessage() , e);
        	
        	return false;
        } finally {
        	if(proc != null) {
        		proc.destroy();
        	}
        }
	}
	
	/**
	 * 执行命令行
	 * @param command - 命令
	 * @return boolean
	 */
	public static boolean processCommend(List<String> command) {
		return processCommend(null, command);
	}
	
	/**
	 * 执行命令行
	 * @param handler - 命令输出文字处理
	 * @param commend - 命令
	 * @return boolean
	 */
	public static boolean processCommend(IProcessMsgHandler handler, List<String> command) {
		LOGGER.info(StringUtils.join(command, " "));
    	Process proc = null;
        try {
        	ProcessBuilder builder = new ProcessBuilder();
        	builder.command(command);  
            builder.redirectErrorStream(true);//通知进程生成器是否合并标准错误和标准输出。 
            
            proc = builder.start();
            
            ProcessStreamThread outputStream = new ProcessStreamThread(proc.getInputStream() , handler);
            outputStream.start();
            
            proc.waitFor();
        	
            //如果成功跑到这里了，则代表成功了。
            return true;
        } catch (Exception e) {
        	LOGGER.error(e.getMessage() , e);
        	
        	return false;
        } finally {
        	if(proc != null) {
        		proc.destroy();
        	}
        }
	}
	
	private ProcessCommandUtils() {
		
	}

}
