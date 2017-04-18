package com.yt.app.frame.id;

public class IdWorker {
	private long sequence = 0L;
	private long workerId;
	private final long twepoch = 1361753741828L;
	private final long workerIdBits = 4L;
	private final long maxWorkerId = -1L ^ -1L << workerIdBits;
	private final long sequenceBits = 10L;
	private final long workerIdShift = sequenceBits;
	private final long timestampLeftShift = sequenceBits + workerIdBits;
	private final long sequenceMask = -1L ^ -1L << sequenceBits;
	private long lastTimestamp = -1L;
	
	public IdWorker(final long workerId) {
		super();
		if (workerId > this.maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("服务器ID不能大于%d小于0", this.maxWorkerId));
		}
		this.workerId = workerId;
	}

	public void setWorkerId(final long workerId) {
		if (workerId > this.maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("服务器ID不能大于%d小于0", this.maxWorkerId));
		}
		this.workerId = workerId;
	}

	public synchronized long nextId() {
		long timestamp = this.timeGen();
		if (this.lastTimestamp == timestamp) {
			this.sequence = (this.sequence + 1) & this.sequenceMask;
			if (this.sequence == 0) {
				timestamp = this.tilNextMillis(this.lastTimestamp);
			}
		} else {
			this.sequence = 0;
		}
		if (timestamp < this.lastTimestamp) {
			try {
				throw new Exception(String.format("波若波罗蜜时间倒退了", this.lastTimestamp - timestamp));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.lastTimestamp = timestamp;
		long nextId = ((timestamp - twepoch << timestampLeftShift)) | (this.workerId << this.workerIdShift) | (this.sequence);
		return nextId;
	}

	private long tilNextMillis(final long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = this.timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}
}