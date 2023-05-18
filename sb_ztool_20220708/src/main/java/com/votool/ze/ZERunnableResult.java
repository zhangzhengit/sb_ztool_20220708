package com.votool.ze;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * ZERunnable 的 结果
 *
 * @author zhangzhen
 * @date 2022年12月2日
 *
 */
@Data
@AllArgsConstructor
public class ZERunnableResult {

	/**
	 * 当前 ZERunnable 对象在一组对象中的位置
	 */
	private final int index;

	/**
	 * 当前 ZERunnable 是否被安排了执行
	 */
	private final boolean executed;

}
