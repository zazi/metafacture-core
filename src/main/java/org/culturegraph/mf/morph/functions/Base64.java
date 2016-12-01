package org.culturegraph.mf.morph.functions;

import org.culturegraph.mf.morph.functions.model.Base64EncoderType;

/**
 * Created by tgaengler on 14.10.16.
 */
public class Base64 extends AbstractSimpleStatelessFunction {

	private java.util.Base64.Encoder encoder;

	private Base64EncoderType encoderType;

	public void setEncoderType(final String encoderTypeStr) {

		encoderType = Base64EncoderType.getByName(encoderTypeStr);
	}

	@Override
	protected String process(final String value) {

		return generateHash(getEncoder(), value);
	}

	private java.util.Base64.Encoder getEncoder() {

		if(encoder == null) {

			final Base64EncoderType finalEncoderType;

			if (encoderType != null) {

				finalEncoderType = encoderType;
			} else {

				finalEncoderType = Base64EncoderType.DefaultEncoder;
			}

			switch (finalEncoderType) {

				case DefaultEncoder:

					encoder = java.util.Base64.getEncoder();

					break;
				case URLEncoder:

					encoder = java.util.Base64.getUrlEncoder().withoutPadding();

					break;
			}

			return encoder;
		} else {

			return encoder;
		}
	}

	private static String generateHash(final java.util.Base64.Encoder encoder, final String hashString) {

		return encoder.encodeToString(hashString.getBytes());
	}
}
