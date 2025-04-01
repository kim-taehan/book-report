package developx.lecture.objects.ch2.v1.discount.policy;

import developx.lecture.objects.ch2.v1.Money;
import developx.lecture.objects.ch2.v1.Screening;

public class NonDiscountPolicy extends DisCountPolicy {
    @Override
    protected Money getDiscountAmount(Screening screening) {
        return Money.ZERO;
    }
}
