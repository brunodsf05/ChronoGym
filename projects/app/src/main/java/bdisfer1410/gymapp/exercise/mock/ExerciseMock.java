package bdisfer1410.gymapp.exercise.mock;

import java.util.List;

import bdisfer1410.gymapp.exercise.models.ExerciseRest;
import bdisfer1410.gymapp.exercise.models.movement.ExercisePose;
import bdisfer1410.gymapp.exercise.models.movement.ExerciseTransition;
import bdisfer1410.gymapp.exercise.models.sets.ExerciseSetDynamic;
import bdisfer1410.gymapp.exercise.models.sets.ExerciseSetStatic;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimation;

public class ExerciseMock {
    public static final List<TimerAnimation> CALISTHENICS;

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

        CALISTHENICS  = List.of(
                pushups, rest, plank, rest,
                pushups, rest, plank, rest,
                pushups, rest, plank
        );
    }
}
