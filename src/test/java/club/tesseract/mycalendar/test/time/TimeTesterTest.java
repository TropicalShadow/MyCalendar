package club.tesseract.mycalendar.test.time;

import club.tesseract.mycalendar.calendar.time.RepeatedTimeCondition;
import club.tesseract.mycalendar.calendar.time.SimpleTimedCondition;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TimeTesterTest {


    @Test
    public void testRepeat(){
        RepeatedTimeCondition condition = new RepeatedTimeCondition(31, 372);

        Assertions.assertEquals(
                ImmutableSet.copyOf(List.of(0L, 31L, 62L, 93L, 124L, 155L, 186L, 217L, 248L, 279L, 310L, 341L)),
                ImmutableSet.copyOf(condition.getTimes())
        );
    }

    @Test
    public void testRepeatIteration(){
        RepeatedTimeCondition condition = new RepeatedTimeCondition(31, 372);

        for(long i = 0; i < 372; i++){
            Assertions.assertEquals(
                    i % 31 == 0,
                    condition.test(i)
            );
        }
    }

    @Test
    public void testSimple(){
        SimpleTimedCondition condition = new SimpleTimedCondition(10L);

        Assertions.assertEquals(
                List.of(10L),
                condition.getTimes()
        );
    }

}
