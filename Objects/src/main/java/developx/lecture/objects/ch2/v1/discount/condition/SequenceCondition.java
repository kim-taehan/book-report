package developx.lecture.objects.ch2.v1.discount.condition;

import developx.lecture.objects.ch2.v1.Screening;
import developx.lecture.objects.ch2.v1.discount.condition.DisCountCondition;

public class SequenceCondition implements DisCountCondition {

    private int sequence;

    public SequenceCondition(int sequence) {
        this.sequence = sequence;
    }

    @Override
    public boolean isSatisfiedBy(Screening screening) {
        return screening.isSequence(sequence);
    }
}
