package developx.lecture.objects.ch2.v1.discount.policy;

import developx.lecture.objects.ch2.v1.Money;
import developx.lecture.objects.ch2.v1.Screening;
import developx.lecture.objects.ch2.v1.discount.condition.DisCountCondition;

public class PercentDiscountPolicy extends DisCountPolicy {
    private double percent;

    public PercentDiscountPolicy(double percent, DisCountCondition... conditions) {
        super(conditions);
        this.percent = percent;
    }

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return screening.getMovieFee().times(percent);
    }
}
