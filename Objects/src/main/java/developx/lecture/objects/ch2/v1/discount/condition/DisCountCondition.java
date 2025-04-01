package developx.lecture.objects.ch2.v1.discount.condition;

import developx.lecture.objects.ch2.v1.Screening;

public interface DisCountCondition {
    boolean isSatisfiedBy(Screening screening);
}
