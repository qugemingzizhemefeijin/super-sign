package com.tigerjoys.shark.supersign.comm.utils;

/**
 * 时间格式化
 *
 */
public final class TimeFormatUtis {

	/**
	 * 将毫秒转化天时分秒毫秒
	 * @param ms - 毫秒
	 * @return String 1小时，30分钟
	 */
    public static String formatTimeby(long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;
 
        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        
        StringBuilder sb = new StringBuilder();
        if(day > 0) {
            sb.append(day>10?String.valueOf(day):("0"+day)).append("天");
        }
        
        if(hour > 0) {
            sb.append(hour).append("小时");
        }
        
        if(minute > 0) {
            sb.append(minute).append("分钟");
        }
        
        if(second > 0) {
        	if(second < 10) {
            	sb.append("0");
            }
            sb.append(second).append("秒");
        }
        
        return sb.toString();
    }
    
    private TimeFormatUtis() {
    	
    }

}
