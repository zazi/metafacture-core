/*
 *  Copyright 2013 Deutsche Nationalbibliothek
 *
 *  Licensed under the Apache License, Version 2.0 the "License";
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.culturegraph.mf.stream.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.culturegraph.mf.exceptions.MetafactureException;
import org.culturegraph.mf.framework.DefaultObjectPipe;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.annotations.Description;
import org.culturegraph.mf.framework.annotations.In;
import org.culturegraph.mf.framework.annotations.Out;


/**
 * Opens a file and passes a reader for it to the receiver.
 * 
 * @author Markus Geipel
 * 
 */
/**
 * @author geipel
 * 
 */
@Description("Opens a file.")
@In(String.class)
@Out(java.io.Reader.class)
public final class FileOpener extends DefaultObjectPipe<String, ObjectReceiver<Reader>> implements Opener {

	private static final int DEFAULT_BUFFER_SIZE = 8 * 1024 * 1024;
	private int bufferSize = DEFAULT_BUFFER_SIZE;
	private String encoding = "UTF-8";

	/**
	 * Returns the encoding used to open the resource.
	 * 
	 * @return current default setting
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * Sets the encoding used to open the resource.
	 * 
	 * @param encoding
	 *            new encoding
	 */
	public void setEncoding(final String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @param bufferSize
	 *            in MB
	 */
	public void setBufferSize(final int bufferSize) {
		this.bufferSize = bufferSize * 1024 * 1024;
	}

	@Override
	public void process(final String file) {
		try {
			getReceiver().process(
					new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding), bufferSize));
		} catch (IOException e) {
			throw new MetafactureException(e);
		}
	}

}
