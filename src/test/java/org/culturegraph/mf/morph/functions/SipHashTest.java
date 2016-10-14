package org.culturegraph.mf.morph.functions;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author tgaengler
 */
public class SipHashTest {

	private static final String UUID = "6c43dc70-4b65-4981-bcc5-b81e1778925f";
	private static final long SIP_HASH = 5944121602695340819L;
	private static final String SIP_HASH_STRING = String.valueOf(SIP_HASH);

	@Test
	public void sipHashTest() {

		final String inputString = UUID;
		final String expectedResult = SIP_HASH_STRING;

		processSipHash(inputString, expectedResult);
	}

	private static void processSipHash(final String inputString, final String expectedResult) {

		final SipHash sipHash = new SipHash();

		final String actualResult = sipHash.process(inputString);

		Assert.assertEquals(expectedResult, actualResult);
	}
}
