package org.culturegraph.mf.morph.functions.model;

/**
 * @author tgaengler
 */
public enum Base64EncoderType {

	/**
	 * Type for default base64 encoder.
	 */
	DefaultEncoder("DEFAULT_ENCODER"),

	/**
	 * Type for URL conform base64 encoder.
	 */
	URLEncoder("URL_ENCODER");

	/**
	 * The name of the value type.
	 */
	private final String name;

	/**
	 * Gets the name of the base64 encoder type.
	 *
	 * @return the name of the base64 encoder type
	 */
	public String getName() {

		return name;
	}

	/**
	 * Creates a new base64 encoder type with the given name.
	 *
	 * @param nameArg the name of the base64 encoder type.
	 */
	Base64EncoderType(final String nameArg) {

		this.name = nameArg;
	}

	/**
	 * Gets the base64 encoder type by the given name.<br>
	 *
	 * @param name the name of the base64 encoder type
	 * @return the appropriated base64 encoder type
	 */
	public static Base64EncoderType getByName(final String name) {

		for (final Base64EncoderType base64EncoderType : Base64EncoderType.values()) {

			if (base64EncoderType.name.equals(name)) {

				return base64EncoderType;
			}
		}

		throw new IllegalArgumentException(name);
	}

	/**
	 * {@inheritDoc}<br>
	 * Returns the name of the base64 encoder type.
	 *
	 * @see Enum#toString()
	 */
	@Override
	public String toString() {

		return name;
	}
}
