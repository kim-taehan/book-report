package developx.lecture.objects.ch2.v1.discount.policy;

import developx.lecture.objects.ch2.v1.Money;
import developx.lecture.objects.ch2.v1.Screening;
import developx.lecture.objects.ch2.v1.discount.condition.DisCountCondition;

public class AmountDiscountPolicy extends DisCountPolicy {

    private Money discountAmount;

    public AmountDiscountPolicy(Money discountAmount, DisCountCondition... conditions) {
        super(conditions);
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return discountAmount;
    }
}
