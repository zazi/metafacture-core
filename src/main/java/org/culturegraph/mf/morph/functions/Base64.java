package org.culturegraph.mf.morph.functions;

import static java.util.Base64.getUrlEncoder;

/**
 * Created by tgaengler on 14.10.16.
 */
public class Base64 extends AbstractSimpleStatelessFunction {

	private static final java.util.Base64.Encoder ENCODER = getUrlEncoder();

	@Override
	protected String process(final String value) {

		return generateHash(value);
	}

	private static String generateHash(final String hashString) {

		return ENCODER.encodeToString(hashString.getBytes());
	}
}
