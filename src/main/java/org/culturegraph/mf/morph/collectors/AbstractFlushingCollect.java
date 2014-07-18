/*
 * Copyright 2013, 2014 Deutsche Nationalbibliothek Licensed under the Apache License, Version 2.0 the "License"; you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.culturegraph.mf.morph.collectors;

import org.culturegraph.mf.morph.Metamorph;

/**
 * Common basis for {@link Entity}, {@link Combine} etc.
 *
 * @author Markus Michael Geipel
 * @author Christoph Böhme
 */
public abstract class AbstractFlushingCollect extends AbstractCollect {

	public AbstractFlushingCollect(final Metamorph metamorph) {
		super(metamorph);
	}

	@Override
	public final void flush(final int recordCount, final int entityCount) {

		System.out.println(this + "in flush with recordCount = '" + recordCount + "' :: entityCount = '" + entityCount + "'");

		if (isSameRecord(recordCount) && sameEntityConstraintSatisfied(entityCount) && isConditionMet()) {

			System.out.println(this + "in flush => emit");

			emit();
			if (getReset()) {

				System.out.println(this + "in flush => reset");

				resetCondition();
				clear();
			}
		}
	}

}
