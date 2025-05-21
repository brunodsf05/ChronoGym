package bdisfer1410.gymapp.exercise.mock;

import java.util.List;

import bdisfer1410.gymapp.exercise.models.routine.ExerciseRest;
import bdisfer1410.gymapp.exercise.models.routine.movement.ExercisePose;
import bdisfer1410.gymapp.exercise.models.routine.movement.ExerciseTransition;
import bdisfer1410.gymapp.exercise.models.routine.sets.ExerciseSetDynamic;
import bdisfer1410.gymapp.exercise.models.routine.sets.ExerciseSetStatic;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;

public class ExerciseMock {
    public static final TimerAnimationQueue CALISTHENICS;
    public static final TimerAnimationQueue TIMERS = new TimerAnimationQueue(List.of(
            /*
            new ExerciseSetStatic("Rojo", new ExercisePose("", null), 10_000),
            new ExerciseSetStatic("Verde", new ExercisePose("", null), 10_000),
            */

            new ExerciseSetStatic("Azul", new ExercisePose("", null), 2_000)
    ));

    static {
        ExerciseSetDynamic pushups = new ExerciseSetDynamic(
                "Flexiones",
                List.of(
                        new ExerciseTransition(new ExercisePose("Empujar para abajo", null), 1_000),
                        new ExerciseTransition(new ExercisePose("Empujar para arriba", null), 1_000)
                ),
                3
        );

        ExerciseSetStatic plank = new ExerciseSetStatic(
                "Plancha",
                new ExercisePose("Empujar para arriba", null),
                3_000
        );

        ExerciseRest rest = new ExerciseRest(6_000);

        CALISTHENICS = new TimerAnimationQueue(List.of(
                pushups, rest, plank, rest,
                pushups, rest, plank, rest,
                pushups, rest, plank
        ));
    }
}
