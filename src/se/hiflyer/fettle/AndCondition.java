package se.hiflyer.fettle;

import com.google.common.collect.Lists;

import java.util.Collection;

public class AndCondition implements Condition {

	private final Collection<Condition> conditions;

	public AndCondition(Condition... conditions) {
		this.conditions = Lists.newArrayList(conditions);
	}

	@Override
	public boolean isSatisfied() {
		for (Condition condition : conditions) {
			if (!condition.isSatisfied()) {
				return false;
			}
		}
		return true;
	}
}
