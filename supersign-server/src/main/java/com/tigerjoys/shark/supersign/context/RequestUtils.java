package com.tigerjoys.shark.supersign.context;

/**
 * 存储线程变量
 *
 */
public class RequestUtils {
	
	private static ThreadLocal<BeatContext> beatThreadLocal = new ThreadLocal<BeatContext>();
	
	/**
	 * 绑定BeatContext到当前的线程中
	 * @param beat - BeatContext
	 */
	public static void bindBeatContextToCurrentThread(BeatContext beat) {
	    beatThreadLocal.set(beat);
	}
	
	/**
	 * 查看当前是否绑定了线程变量
	 * @return boolean
	 */
	public static boolean isBindThreadLocalVariable(){
		return beatThreadLocal.get() != null;
	}
	
	/**
	 * 获得当前的BeatContext
	 * @return BeatContext
	 */
	public static BeatContext getCurrent() {
		BeatContext beat = beatThreadLocal.get();
	    if (beat == null) {
	    	throw new IllegalStateException("BeatContext");
	    }

	    return beat;
	}
	
	/**
	 * 将线程变量值移除
	 */
	public static void remove() {
		beatThreadLocal.remove();
	}

}
