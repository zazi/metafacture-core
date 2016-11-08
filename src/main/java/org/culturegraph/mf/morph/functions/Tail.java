package org.culturegraph.mf.morph.functions;

import java.util.concurrent.atomic.AtomicBoolean;

import org.culturegraph.mf.morph.NamedValueSource;

/**
 * Created by tgaengler on 08.11.16.
 */
public class Tail extends Buffer {

	private final AtomicBoolean droppedFirst = new AtomicBoolean();


	@Override
	public void receive(String name, String value, NamedValueSource source, int recordCount, int entityCount) {

		if(droppedFirst.compareAndSet(false, true)) {

			return;
		}

		super.receive(name, value, source, recordCount, entityCount);
	}

	@Override
	public void flush(int recordCount, int entityCount) {

		super.flush(recordCount, entityCount);

		droppedFirst.set(false);
	}
}
