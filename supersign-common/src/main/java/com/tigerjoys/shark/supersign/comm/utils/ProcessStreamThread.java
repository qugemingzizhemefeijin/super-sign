package com.tigerjoys.shark.supersign.comm.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 线程循环读取输出流
 *
 */
public class ProcessStreamThread extends Thread {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessStreamThread.class);
	
	private static final IProcessMsgHandler DEFAULT_MSG_HANDLER = new IProcessMsgHandler() {

		@Override
		public void hand(String msg) {
			LOGGER.info(msg);
		}
		
	};
	
	private InputStream is;
	private IProcessMsgHandler handler;
	
	public ProcessStreamThread(InputStream is) {
		this(is , DEFAULT_MSG_HANDLER);
	}
	
	public ProcessStreamThread(InputStream is , IProcessMsgHandler handler) {
		this.is = is;
		this.handler = handler;
	}
	
	@Override
	public void run() {
		InputStreamReader streamReader = null;
		BufferedReader bufferReader = null;
		try {
			streamReader = new InputStreamReader(this.is);
			bufferReader = new BufferedReader(streamReader);
			String line = null;
			while ((line = bufferReader.readLine()) != null) {
				if(handler != null) {
					handler.hand(line);
				}
			}
		} catch (IOException ex) {
			LOGGER.error("read stream from exec error", ex);
		} finally {
			if (bufferReader != null) {
				try {
					bufferReader.close();
				} catch (IOException ex) {
					LOGGER.error("close BufferedReader error when exec command", ex);
				}
			}
			if (streamReader != null) {
				try {
					streamReader.close();
				} catch (IOException ex) {
					LOGGER.error("close InputStreamReader error when exec command", ex);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException ex) {
					LOGGER.error("close InputStream error when exec command", ex);
				}
			}
		}
	}

}
