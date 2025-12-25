package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.utilities.Base62;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Snowflake based id generator
 */
@Service
public class IdGenerator {

	public static void main() {
		IdGenerator generator = new IdGenerator(1);
		System.out.println(generator.getStringId());
	}

	/**
	 * Date and time (GMT): Wednesday, January 1, 2025 12:00:00 AM
	 */
	private static final long EPOCH = 1735689600000L;

	private static final int NODE_BITS = 10;
	private static final int SEQ_BITS = 12;
	private static final long MAX_SEQ = (1L << SEQ_BITS) - 1;
	private final long nodeId;
	private long lastTs = -1;
	private long seq = 0;

	public IdGenerator(@Value("${node.id}") long nodeId) {
		if (nodeId < 0 || nodeId >= (1L << NODE_BITS)) {
			throw new IllegalArgumentException("invalid nodeId");
		}
		this.nodeId = nodeId;
	}

	public synchronized String getStringId() {
		long id = getId();
		return Base62.encode(id);
	}

	@SneakyThrows
	public synchronized long getId() {
		long ts = System.currentTimeMillis();

		if (ts < lastTs) {
			throw new IllegalStateException("clock moved backwards");
		}

		if (ts == lastTs) {
			seq = (seq + 1) & MAX_SEQ;
			if (seq == 0) {
				do {
					Thread.sleep(1);
					ts = System.currentTimeMillis();
				} while (ts <= lastTs);
			}
		} else {
			seq = 0;
		}

		lastTs = ts;

		return ((ts - EPOCH) << (NODE_BITS + SEQ_BITS))
			| (nodeId << SEQ_BITS)
			| seq;
	}


}
