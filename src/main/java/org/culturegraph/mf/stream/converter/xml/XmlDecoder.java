/*
 * Copyright 2013, 2014 Deutsche Nationalbibliothek
 *
 * Licensed under the Apache License, Version 2.0 the "License";
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.culturegraph.mf.stream.converter.xml;

import java.io.IOException;
import java.io.Reader;

import javax.xml.stream.XMLInputFactory;

import org.culturegraph.mf.exceptions.MetafactureException;
import org.culturegraph.mf.framework.DefaultObjectPipe;
import org.culturegraph.mf.framework.XmlReceiver;
import org.culturegraph.mf.framework.annotations.Description;
import org.culturegraph.mf.framework.annotations.FluxCommand;
import org.culturegraph.mf.framework.annotations.In;
import org.culturegraph.mf.framework.annotations.Out;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;


/**
 * Reads an XML file and passes the XML events to a receiver.
 *
 * @author Christoph Böhme
 *
 */
@Description("Reads an XML file and passes the XML events to a receiver.")
@In(Reader.class)
@Out(XmlReceiver.class)
@FluxCommand("decode-xml")
public final class XmlDecoder
		extends DefaultObjectPipe<Reader, XmlReceiver> {
	
	private static final String SAX_PROPERTY_LEXICAL_HANDLER = "http://xml.org/sax/properties/lexical-handler";
	private static final String XERCES_FEATURES_NOTIFY_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
	private static final String XERCES_FEATURES_DISALLOW_DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl";
	private static final String XERCES_FEATURES_USE_ENTITY_RESOLVER2 = "http://xml.org/sax/features/use-entity-resolver2";
	private static final String XERCES_FEATURES_LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
	private static final String XERCES_FEATURES_RESOLVE_DTD_URIS = "http://xml.org/sax/features/resolve-dtd-uris";
	private static final String XERCES_FEATURES_EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
	private static final String XERCES_FEATURES_LOAD_DTD_GRAMMAR = "http://apache.org/xml/features/nonvalidating/load-dtd-grammar";

	private final XMLFilter xmlPreFilter;
	private final XMLFilter xmlFilter;

	public XmlDecoder() {
		super();
		try {
			final XMLReader saxReader = XMLReaderFactory.createXMLReader();
			saxReader.setFeature(XERCES_FEATURES_NOTIFY_BUILTIN_REFS, false);
			saxReader.setFeature(XERCES_FEATURES_DISALLOW_DOCTYPE_DECL, false);
			saxReader.setFeature(XERCES_FEATURES_USE_ENTITY_RESOLVER2, true);
			saxReader.setFeature(XERCES_FEATURES_LOAD_EXTERNAL_DTD, true);
			saxReader.setFeature(XERCES_FEATURES_RESOLVE_DTD_URIS, true);
			saxReader.setFeature(XERCES_FEATURES_EXTERNAL_PARAMETER_ENTITIES, true);
			saxReader.setFeature(XERCES_FEATURES_LOAD_DTD_GRAMMAR, true);
			xmlPreFilter = new XmlFilterEntityImpl(saxReader);
			xmlFilter = new XMLFilterImpl(xmlPreFilter);
		} catch (SAXException e) {
			throw new MetafactureException(e);
		}
	}

	@Override
	public void process(final Reader reader) {
		try {
			xmlFilter.parse(new InputSource(reader));
		} catch (IOException e) {
			throw new MetafactureException(e);
		} catch (SAXException e) {
			throw new MetafactureException(e);
		}
	}

	@Override
	protected void onSetReceiver() {
		xmlFilter.setContentHandler(getReceiver());
		xmlFilter.setDTDHandler(getReceiver());
		xmlFilter.setEntityResolver(getReceiver());
		xmlFilter.setErrorHandler(getReceiver());
		xmlPreFilter.setContentHandler(xmlFilter.getContentHandler());
		try {
			xmlFilter.setProperty(SAX_PROPERTY_LEXICAL_HANDLER, getReceiver());
		} catch (SAXNotRecognizedException e) {
			throw new MetafactureException(e);
		} catch (SAXNotSupportedException e) {
			throw new MetafactureException(e);
		}
	}

}
