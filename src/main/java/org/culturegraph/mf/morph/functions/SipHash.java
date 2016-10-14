package org.culturegraph.mf.morph.functions;

import java.nio.charset.StandardCharsets;

import com.github.emboss.siphash.SipKey;

/**
 * Created by tgaengler on 14.10.16.
 */
public class SipHash extends AbstractSimpleStatelessFunction {

	// Values from Appendix A of https://131002.net/siphash/siphash.pdf
	// as well as http://git.io/siphash-spec-key-ref#L12
	private static final SipKey SPEC_KEY = new SipKey(SipHash.bytesOf(
			0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
			0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f));

	@Override
	protected String process(final String value) {

		return String.valueOf(generateHash(value));
	}

	private static long generateHash(final String hashString) {

		return com.github.emboss.siphash.SipHash.digest(SipHash.SPEC_KEY, hashString.getBytes(StandardCharsets.UTF_8));
	}

	private static byte[] bytesOf(final Integer... bytes) {

		final byte[] ret = new byte[bytes.length];

		for (int i = 0; i < bytes.length; i++) {

			ret[i] = bytes[i].byteValue();
		}

		return ret;
	}
}
