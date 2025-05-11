package bdisfer1410.gymapp.exercise.mock;

import java.util.List;

import bdisfer1410.gymapp.exercise.models.ExerciseRest;
import bdisfer1410.gymapp.exercise.models.movement.ExercisePose;
import bdisfer1410.gymapp.exercise.models.movement.ExerciseTransition;
import bdisfer1410.gymapp.exercise.models.sets.ExerciseSetDynamic;
import bdisfer1410.gymapp.exercise.models.sets.ExerciseSetStatic;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimation;

public class ExerciseMock {
    public static final List<TimerAnimation> CALISTHENICS = List.of(
            new ExerciseSetDynamic(
                    "Flexiones",
                    List.of(
                            new ExerciseTransition(new ExercisePose("Empujar para abajo", null), 1_000),
                            new ExerciseTransition(new ExercisePose("Empujar para arriba", null), 1_000)
                    ),
                    10
            ),
            new ExerciseRest(12_000),
            new ExerciseSetStatic(
                    "Plancha",
                    new ExercisePose("Empujar para arriba", null),
                    60_000
            )
    );

}
