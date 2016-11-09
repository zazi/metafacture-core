package org.culturegraph.mf.morph.functions;

import java.util.concurrent.atomic.AtomicBoolean;

import org.culturegraph.mf.morph.NamedValueSource;

/**
 * Created by tgaengler on 08.11.16.
 */
public class Head extends Buffer {

	private final AtomicBoolean takeFirst = new AtomicBoolean();


	@Override
	public void receive(String name, String value, NamedValueSource source, int recordCount, int entityCount) {

		if(takeFirst.compareAndSet(false, true)) {

			super.receive(name, value, source, recordCount, entityCount);
		}
	}

	@Override
	public void flush(int recordCount, int entityCount) {

		super.flush(recordCount, entityCount);

		takeFirst.set(false);
	}
}
