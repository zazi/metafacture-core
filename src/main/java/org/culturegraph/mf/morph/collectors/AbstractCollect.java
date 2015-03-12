/*
 *  Copyright 2013, 2014 Deutsche Nationalbibliothek
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
package org.culturegraph.mf.morph.collectors;

import org.culturegraph.mf.morph.AbstractNamedValuePipe;
import org.culturegraph.mf.morph.Metamorph;
import org.culturegraph.mf.morph.NamedValueSource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Common basis for {@link Entity}, {@link Combine} etc.
 *
 * @author Markus Michael Geipel
 * @author Christoph Böhme
 *
 */
public abstract class AbstractCollect extends AbstractNamedValuePipe
		implements Collect {

	private int oldRecord;
	private int oldEntity;
	private boolean resetAfterEmit;
	private boolean sameEntity;
	private String name;
	private String value;
	private final Metamorph metamorph;
	private boolean waitForFlush;
	private boolean conditionMet;
	private boolean				includeSubEntities;
	private int					currentHierarchicalEntity	= 0;
	private int					oldHierarchicalEntity		= 0;
	private Map<String, List<String>> hierarchicalEntityEmitBuffer;
	private Integer matchEntity;

	private NamedValueSource conditionSource;

	public AbstractCollect(final Metamorph metamorph) {
		super();
		this.metamorph = metamorph;
	}

	protected final Metamorph getMetamorph() {
		return metamorph;
	}

	public final void setIncludeSubEntities(final boolean includeSubEntitiesArg) {

		System.out.println(this.getName() + " in includeSubEntities with = '" + includeSubEntitiesArg + "'");

		includeSubEntities = includeSubEntitiesArg;

		if (includeSubEntities) {

			hierarchicalEntityEmitBuffer = new LinkedHashMap<String, List<String>>();
		}
	}

	protected final boolean getIncludeSubEntities() {

		System.out.println(this.getName() + " in includeSubEntities with = '" + includeSubEntities + "'");

		return includeSubEntities;
	}

	protected final Map<String, List<String>> getHierarchicalEntityEmitBuffer() {

		return hierarchicalEntityEmitBuffer;
	}

	protected boolean isHierarchicalEntityEmitBufferFilled() {

		return hierarchicalEntityEmitBuffer != null && !hierarchicalEntityEmitBuffer.isEmpty();
	}

	protected final Integer getMatchEntity() {

		return matchEntity;
	}

	protected void setMatchEntity(final Integer matchEntityArg) {

		matchEntity = matchEntityArg;
	}

	protected final int getRecordCount() {

		System.out.println(this.getName() + " in getRecordCount (oldRecord) with = '" + oldRecord + "'");

		return oldRecord;
	}

	protected final int getEntityCount() {

		System.out.println(this.getName() + " in getEntityCount with (oldEntity) = '" + oldEntity + "'");

		return oldEntity;
	}

	protected final boolean isConditionMet() {

		System.out.println(this.getName() + " in isConditionMet with = '" + conditionMet + "'");

		return conditionMet;
	}

	protected final void setConditionMet(final boolean conditionMet) {

		System.out.println(this.getName() + " in setConditionMet with = '" + conditionMet + "'");

		this.conditionMet = conditionMet;
	}

	protected final void resetCondition() {

		System.out.println(this.getName() + " in resetCondition with = '" + (conditionSource == null) + "'");

		setConditionMet(conditionSource == null);
	}

	@Override
	public final void setWaitForFlush(final boolean waitForFlush) {

		System.out.println(this.getName() + " in setWaitForFlush with = '" + waitForFlush + "'");

		this.waitForFlush = waitForFlush;
		// metamorph.addEntityEndListener(this, flushEntity);
	}

	@Override
	public final void setSameEntity(final boolean sameEntity) {

		System.out.println(this.getName() + " in setSameEntity with = '" + sameEntity + "'");

		this.sameEntity = sameEntity;
	}

	public final boolean getReset() {

		System.out.println(this.getName() + " in getReset with = '" + resetAfterEmit + "'");

		return resetAfterEmit;
	}

	@Override
	public final void setReset(final boolean reset) {

		System.out.println(this.getName() + " in setReset with = '" + reset + "'");

		this.resetAfterEmit = reset;
	}

	@Override
	public final String getName() {

		// System.out.println(name + " in getName with = '" + name + "'");

		return name;
	}

	@Override
	public final void setName(final String name) {

		System.out.println(name + " in setName with = '" + name + "'");

		this.name = name;
	}

	@Override
	public final void setConditionSource(final NamedValueSource source) {

		StringBuilder sb = new StringBuilder();

		sb.append(this.getName() + " in setConditionSource with = '" + source + "'");

		if (source != null && Combine.class.isInstance(source)) {

			final Combine combine = (Combine) source;

			sb.append(" :: combine.name = '" + combine.getName() + "' :: combine.value = '" + combine.getName() + "' :: combine.isConditionMet = '"
					+ combine.isConditionMet() + "' :: combine.entityCount = '" + combine.getEntityCount() + "' :: combine.recordCount = '"
					+ combine.getRecordCount() + "'");

		}

		System.out.println(sb.toString());

		conditionSource = source;
		conditionSource.setNamedValueReceiver(this);
		resetCondition();
	}

	protected final NamedValueSource getConditionSource() {

		System.out.println(this.getName() + " in getConditionSource with = '" + conditionSource + "'");

		return conditionSource;
	}

	public final String getValue() {

		System.out.println(this.getName() + " in getValue with = '" + value + "'");

		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public final void setValue(final String value) {

		System.out.println(this.getName() + " in setValue with = '" + value + "'");

		this.value = value;
	}

	protected final void updateCounts(final int currentRecord, final int currentEntity) {

		System.out.println(this.getName() + " in updateCounts with currentRecord = '" + currentRecord + "' :: currentEntity = '"
				+ currentEntity + "'");

		if (!isSameRecord(currentRecord)) {

			System.out.println(this.getName() + " in updateCounts => is not same record");

			resetCondition();
			clear();
			oldRecord = currentRecord;
		}
		if (resetNeedFor(currentEntity)) {

			System.out.println(this.getName() + " in updateCounts => reset needed for");

			resetCondition();
			clear();
		}
		oldEntity = currentEntity;
	}

	protected void updateHierarchicalEntity(final int entityCount) {

		System.out.println("in updateHierarchicalEntiy with entityCount = '" + entityCount + "' :: oldHierarchicalEntity = '" + oldHierarchicalEntity
				+ "' :: currentHierarchicalEntity = '" + currentHierarchicalEntity + "'");

		oldHierarchicalEntity = currentHierarchicalEntity;
		currentHierarchicalEntity = entityCount;
	}

	private boolean resetNeedFor(final int currentEntity) {

System.out.println(this.getName() + " in resetNeedFor with currentEntity = '" + currentEntity + "' :: sameEntity = '"
				+ sameEntity + "' :: oldEntity = '" + oldEntity + "' => '" + reset + "' :: currentHierarchicalEntity = '" + currentHierarchicalEntity
				+ "' :: oldHierarchicalEntity = '" + oldHierarchicalEntity + "'");

		if (getIncludeSubEntities()) {

			if (sameEntity) {

				return false;
			}
		}

		return sameEntity && oldEntity != currentEntity;
	}

	protected final boolean isSameRecord(final int currentRecord) {

		System.out.println(this.getName() + " in isSameRecord with = '" + currentRecord + "' :: oldRecord = " + oldRecord + "'");

		return currentRecord == oldRecord;
	}

	@Override
	public final void receive(final String name, final String value, final NamedValueSource source, final int recordCount, final int entityCount) {

		System.out.println(this.getName() + " in receive with name = '" + name + "' :: value = '" + value + "' :: source = '" + source
				+ "' :: recordCount = '" + recordCount + "' :: entityCount = '" + entityCount + "'");

		updateCounts(recordCount, entityCount);

		if (source == conditionSource) {
			
			System.out.println(this.getName() + " in receive with name => conditionMet (source == conditionSource)");

			conditionMet = true;

			if(getIncludeSubEntities() && Collect.class.isInstance(conditionSource)) {

				if(((Collect) conditionSource).getName().equals(name)) {

					final boolean condition = Boolean.valueOf(value);

					if(condition) {

						matchEntity = entityCount;
					} else {

						// do something with matchEntity, e.g., reset
						matchEntity = null;
						conditionMet = false;
					}

					return;
				}
			}
		} else {
			receive(name, value, source);
		}

		if(getIncludeSubEntities()) {

			System.out.println(this.getName() + " in receive => for includeSubEntities");

			if(isConditionMet() && isComplete() && matchEntity != null && matchEntity <= entityCount) {

				System.out.println(this.getName() + " in receive => emit for includeSubEntities");

				emit();
			}

			return;
		}

		if (!waitForFlush && isConditionMet() && isComplete()) {

			System.out.println(this.getName() + " in receive => emit");

			emit();
			if (resetAfterEmit) {

				System.out.println(this.getName() + " in recei => emit => reset after emit");

				resetCondition();
				clear();
			}
		}
	}

	protected final boolean sameEntityConstraintSatisfied(final int entityCount) {

		System.out.println(this.getName() + " in sameEntityConstraintSatisfied with entityCount = '" + entityCount
				+ "' :: sameEntity = '" + sameEntity + "' :: oldEntity = '" + oldEntity + "' :: currentHierarchuicalEntity = '"
				+ currentHierarchicalEntity + "' :: oldHierarchicalEntity = '" + oldHierarchicalEntity + "' => '"
				+ (!sameEntity || oldHierarchicalEntity <= entityCount) + "'");

		if (getIncludeSubEntities()) {

			return !sameEntity || oldHierarchicalEntity <= entityCount;
		}

		return !sameEntity || oldEntity == entityCount;
	}

	protected abstract void receive(final String name, final String value,
			final NamedValueSource source);

	protected abstract boolean isComplete();

	protected abstract void clear();

	protected abstract void emit();

}
