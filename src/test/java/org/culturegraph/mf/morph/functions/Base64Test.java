package org.culturegraph.mf.morph.functions;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author tgaengler
 */
public class Base64Test {

	private static final String UUID = "6c43dc70-4b65-4981-bcc5-b81e1778925f";
	private static final String BASE64_STRING = "NmM0M2RjNzAtNGI2NS00OTgxLWJjYzUtYjgxZTE3Nzg5MjVm";

	@Test
	public void base64Test() {

		final String inputString = UUID;
		final String expectedResult = BASE64_STRING;

		processBase64(inputString, expectedResult);
	}

	private static void processBase64(final String inputString, final String expectedResult) {

		final Base64 base64 = new Base64();

		final String actualResult = base64.process(inputString);

		Assert.assertEquals(expectedResult, actualResult);
	}
}
