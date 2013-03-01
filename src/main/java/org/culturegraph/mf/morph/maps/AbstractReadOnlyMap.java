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
package org.culturegraph.mf.morph.maps;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;

/**
 * @author "Markus Michael Geipel"
 *
 * @param <K>
 * @param <V>
 */
public abstract class  AbstractReadOnlyMap<K,V> implements Map<K, V> {

	@Override
	public final int size() {
		throw new NotImplementedException();
	}

	@Override
	public final boolean isEmpty() {
		throw new NotImplementedException();
	}

	@Override
	public final boolean containsKey(final Object key) {
		return get(key)!=null;
	}

	@Override
	public final boolean containsValue(final Object value) {
		throw new NotImplementedException();
	}



	@Override
	public final V put(final K key, final V value) {
		throw new NotImplementedException();
	}

	@Override
	public final V remove(final Object key) {
		throw new NotImplementedException();
	}

	@Override
	public final void putAll(final Map<? extends K, ? extends V> m) {
		throw new NotImplementedException();
		
	}

	@Override
	public final void clear() {
		throw new NotImplementedException();
		
	}

	@Override
	public final Set<K> keySet() {
		throw new NotImplementedException();
	}

	@Override
	public final Collection<V> values() {
		throw new NotImplementedException();
	}

	@Override
	public final Set<java.util.Map.Entry<K, V>> entrySet() {
		throw new NotImplementedException();
	}

}
