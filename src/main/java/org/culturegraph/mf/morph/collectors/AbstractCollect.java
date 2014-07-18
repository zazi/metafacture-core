/*
 * Copyright 2013, 2014 Deutsche Nationalbibliothek Licensed under the Apache License, Version 2.0 the "License"; you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.culturegraph.mf.morph.collectors;

import org.culturegraph.mf.morph.AbstractNamedValuePipe;
import org.culturegraph.mf.morph.Metamorph;
import org.culturegraph.mf.morph.NamedValueSource;

/**
 * Common basis for {@link Entity}, {@link Combine} etc.
 *
 * @author Markus Michael Geipel
 * @author Christoph Böhme
 */
public abstract class AbstractCollect extends AbstractNamedValuePipe implements Collect {

	private int					oldRecord;
	private int					oldEntity;
	private boolean				resetAfterEmit;
	private boolean				sameEntity;
	private String				name;
	private String				value;
	private final Metamorph		metamorph;
	private boolean				waitForFlush;
	private boolean				conditionMet;
	private boolean				includeSubEntities;

	private NamedValueSource	conditionSource;

	public AbstractCollect(final Metamorph metamorph) {
		super();
		this.metamorph = metamorph;
	}

	protected final Metamorph getMetamorph() {
		return metamorph;
	}

	public final void setIncludeSubEntities(final boolean includeSubEntitiesArg) {

		System.out.println(this + "in includeSubEntities with = '" + includeSubEntitiesArg + "'");

		includeSubEntities = includeSubEntitiesArg;
	}

	protected final boolean getIncludeSubEntities() {

		System.out.println(this + "in includeSubEntities with = '" + includeSubEntities + "'");

		return includeSubEntities;
	}

	protected final int getRecordCount() {

		System.out.println(this + "in getEntityCount with = '" + oldRecord + "'");

		return oldRecord;
	}

	protected final int getEntityCount() {

		System.out.println(this + "in getEntityCount with = '" + oldEntity + "'");

		return oldEntity;
	}

	protected final boolean isConditionMet() {

		System.out.println(this + "in isConditionMet with = '" + conditionMet + "'");

		return conditionMet;
	}

	protected final void setConditionMet(final boolean conditionMet) {

		System.out.println(this + "in setConditionMet with = '" + conditionMet + "'");

		this.conditionMet = conditionMet;
	}

	protected final void resetCondition() {

		System.out.println(this + "in resetCondition with = '" + (conditionSource == null) + "'");

		setConditionMet(conditionSource == null);
	}

	@Override
	public final void setWaitForFlush(final boolean waitForFlush) {

		System.out.println(this + "in setWaitForFlush with = '" + waitForFlush + "'");

		this.waitForFlush = waitForFlush;
		// metamorph.addEntityEndListener(this, flushEntity);
	}

	@Override
	public final void setSameEntity(final boolean sameEntity) {

		System.out.println(this + "in setSameEntity with = '" + sameEntity + "'");

		this.sameEntity = sameEntity;
	}

	public final boolean getReset() {

		System.out.println(this + "in getReset with = '" + resetAfterEmit + "'");

		return resetAfterEmit;
	}

	@Override
	public final void setReset(final boolean reset) {

		System.out.println(this + "in setReset with = '" + reset + "'");

		this.resetAfterEmit = reset;
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final void setName(final String name) {
		this.name = name;
	}

	@Override
	public final void setConditionSource(final NamedValueSource source) {

		System.out.println(this + "in setConditionSource with = '" + source + "'");

		conditionSource = source;
		conditionSource.setNamedValueReceiver(this);
		resetCondition();
	}

	public final String getValue() {

		System.out.println(this + "in getValue with = '" + value + "'");

		return value;
	}

	/**
	 * @param value the value to set
	 */
	public final void setValue(final String value) {

		System.out.println(this + "in setValue with = '" + value + "'");

		this.value = value;
	}

	protected final void updateCounts(final int currentRecord, final int currentEntity) {

		System.out.println(this + "in updateCounts with currentRecord = '" + currentRecord + "' :: currentEntity = '" + currentEntity + "'");

		if (!isSameRecord(currentRecord)) {
			resetCondition();
			clear();
			oldRecord = currentRecord;
		}
		if (resetNeedFor(currentEntity)) {
			resetCondition();
			clear();
		}
		oldEntity = currentEntity;
	}

	private boolean resetNeedFor(final int currentEntity) {

		System.out.println(this + "in resetNeedFor with currentEntity = '" + currentEntity + "' :: sameEntity = '" + sameEntity
				+ "' :: oldEntity = '" + oldEntity + "' => '" + (sameEntity && oldEntity != currentEntity) + "'");

		if(getIncludeSubEntities()) {

			return sameEntity && oldEntity > currentEntity;
		}

		return sameEntity && oldEntity != currentEntity;
	}

	protected final boolean isSameRecord(final int currentRecord) {

		System.out.println(this + "in isSameRecord with = '" + currentRecord + "'");

		return currentRecord == oldRecord;
	}

	@Override
	public final void receive(final String name, final String value, final NamedValueSource source, final int recordCount, final int entityCount) {

		updateCounts(recordCount, entityCount);

		if (source == conditionSource) {
			conditionMet = true;
		} else {
			receive(name, value, source);
		}

		if (!waitForFlush && isConditionMet() && isComplete()) {
			emit();
			if (resetAfterEmit) {
				resetCondition();
				clear();
			}
		}
	}

	protected final boolean sameEntityConstraintSatisfied(final int entityCount) {

		System.out.println(this + "in sameEntityConstraintSatisfied with entityCount = '" + entityCount + "' :: sameEntity = '" + sameEntity
				+ "' :: oldEntity = '" + oldEntity + "' => '" + (!sameEntity || oldEntity == entityCount) + "'");

		if(getIncludeSubEntities()) {

			return !sameEntity || oldEntity <= entityCount;
		}

		return !sameEntity || oldEntity == entityCount;
	}

	protected abstract void receive(final String name, final String value, final NamedValueSource source);

	protected abstract boolean isComplete();

	protected abstract void clear();

	protected abstract void emit();

}
