package developx.lecture.objects.ch2.v1.discount.policy;

import developx.lecture.objects.ch2.v1.Money;
import developx.lecture.objects.ch2.v1.Screening;
import developx.lecture.objects.ch2.v1.discount.condition.DisCountCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DisCountPolicy {

    private List<DisCountCondition> conditions = new ArrayList<>();

    public DisCountPolicy(DisCountCondition... conditions) {
        this.conditions = Arrays.asList(conditions);
    }

    public Money calculateDiscountAmount(Screening screening) {
        for (DisCountCondition each : conditions) {
            if (each.isSatisfiedBy(screening)) {
                return getDiscountAmount(screening);
            }
        }
        return Money.ZERO;
    }

    protected abstract Money getDiscountAmount(Screening screening);
}
