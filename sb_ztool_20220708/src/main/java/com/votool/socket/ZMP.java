package com.votool.socket;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author zhangzhen
 * @data Aug 7, 2020
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZMP implements Serializable, Delayed {

	private static final long serialVersionUID = 1L;

	/**
	 * 唯一ID
	 */
	private String uuid;

	/**
	 * ZMPTypeEnum.type
	 */
	private Integer type;

	private String topic;

	private String content;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 延迟毫秒数，0表示不延迟
	 */
	private long delayMilliSeconds;

	/**
	 * 具体的此消息推送时间点，此对象创建的时刻 + delayMilliSeconds。
	 */
	private long scheduledMilliSeconds;

	@Override
	public int compareTo(final Delayed o) {
		final long d1 = this.getDelay(TimeUnit.MILLISECONDS);
		final long d2 = o.getDelay(TimeUnit.MILLISECONDS);
		final int compare = Long.compare(d1, d2);
		return compare;
	}

	@Override
	public long getDelay(final TimeUnit unit) {
		final long c = unit.convert(this.scheduledMilliSeconds - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		return c;
	}

}
