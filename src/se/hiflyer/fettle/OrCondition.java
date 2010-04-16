package se.hiflyer.fettle;

import com.google.common.collect.Lists;

import java.util.Collection;

public class OrCondition implements Condition {

	private final Collection<Condition> conditions;

	public OrCondition(Condition... conditions) {
		this.conditions = Lists.newArrayList(conditions);
	}

	@Override
	public boolean isSatisfied() {
		for (Condition condition : conditions) {
			if (condition.isSatisfied()) {
				return true;
			}
		}
		return false;
	}
}