package org.culturegraph.mf.morph.functions;

import org.culturegraph.mf.morph.functions.model.Base64EncoderType;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author tgaengler
 */
public class Base64Test {

	private static final String UUID = "6c43dc70-4b65-4981-bcc5-b81e1778925f";
	private static final String SHORT_INPUT = "07248679";
	private static final String BASE64_STRING = "NmM0M2RjNzAtNGI2NS00OTgxLWJjYzUtYjgxZTE3Nzg5MjVm";
	private static final String SHORT_INPUT_BASE64_STRING = "MDcyNDg2Nzk=";
	private static final String SHORT_INPUT_BASE64_URL_STRING = "MDcyNDg2Nzk";

	@Test
	public void base64Test() {

		final String inputString = UUID;
		final String expectedResult = BASE64_STRING;

		processBase64(inputString, expectedResult);
		processBase64WURLEncoder(inputString, expectedResult);
	}

	@Test
	public void base64URLEncoderTest() {

		final String inputString = SHORT_INPUT;

		processBase64(inputString, SHORT_INPUT_BASE64_STRING);
		processBase64WURLEncoder(inputString, SHORT_INPUT_BASE64_URL_STRING);
	}

	private static void processBase64(final String inputString, final String expectedResult) {

		final Base64 base64 = new Base64();

		final String actualResult = base64.process(inputString);

		Assert.assertEquals(expectedResult, actualResult);
	}

	private static void processBase64WURLEncoder(final String inputString, final String expectedResult) {

		final Base64 base64 = new Base64();
		base64.setEncoderType(Base64EncoderType.URLEncoder.getName());

		final String actualResult = base64.process(inputString);

		Assert.assertEquals(expectedResult, actualResult);
	}
}
