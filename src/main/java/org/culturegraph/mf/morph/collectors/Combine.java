/*
 * Copyright 2013, 2014 Deutsche Nationalbibliothek Licensed under the Apache License, Version 2.0 the "License"; you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.culturegraph.mf.morph.collectors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.culturegraph.mf.morph.Metamorph;
import org.culturegraph.mf.morph.NamedValueSource;
import org.culturegraph.mf.util.StringUtil;

/**
 * Corresponds to the <code>&lt;collect&gt;</code> tag.
 *
 * @author Markus Michael Geipel
 */
public final class Combine extends AbstractFlushingCollect {

	private final Map<String, String>	variables	= new HashMap<String, String>();
	private final Set<NamedValueSource>	sources		= new HashSet<NamedValueSource>();
	private final Set<NamedValueSource>	sourcesLeft	= new HashSet<NamedValueSource>();

	public Combine(final Metamorph metamorph) {
		super(metamorph);
	}

	@Override
	protected void emit() {

		final String name = StringUtil.format(getName(), variables);
		final String value = StringUtil.format(getValue(), variables);

		System.out.println(((Combine) this).getName() + " in emit with name = '" + name + "' :: value = '" + value + "' :: recordCount = '"
				+ getRecordCount() + "' :: entityCount = '" + getEntityCount() + "'");

		if (getIncludeSubEntities()) {

			if (!getHierarchicalEntityEmitBuffer().containsKey(name)) {

				getHierarchicalEntityEmitBuffer().put(name, new LinkedList<String>());
			}

			getHierarchicalEntityEmitBuffer().get(name).add(value);

			return;
		}

		emit(name, value);
	}

	private void emit(String name, String value) {
		getNamedValueReceiver().receive(name, value, this, getRecordCount(), getEntityCount());
	}

	protected void emitHierarchicalEntityBuffer() {

		for (final Map.Entry<String, List<String>> entry : getHierarchicalEntityEmitBuffer().entrySet()) {

			final String name = entry.getKey();

			for (final String value : entry.getValue()) {

				emit(name, value);
			}
		}
	}

	@Override
	protected boolean isComplete() {

		System.out.println(((Combine) this).getName() + " in isComplete with = '" + sourcesLeft.isEmpty() + "'");

		return sourcesLeft.isEmpty();
	}

	@Override
	protected void receive(final String name, final String value, final NamedValueSource source) {

		System.out.println(((Combine) this).getName() + " in receive with name = '" + name + "' :: value = '" + value + "'");

		variables.put(name, value);
		sourcesLeft.remove(source);
	}

	@Override
	public void onNamedValueSourceAdded(final NamedValueSource namedValueSource) {
		sources.add(namedValueSource);
		sourcesLeft.add(namedValueSource);
	}

	@Override
	protected void clear() {

		System.out.println(((Combine) this).getName() + " in clear");

		sourcesLeft.addAll(sources);
		variables.clear();

		if (getIncludeSubEntities()) {

			getHierarchicalEntityEmitBuffer().clear();
		}
	}

}
