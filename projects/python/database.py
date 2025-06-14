exercises = [
    {
        "name": "@quick_burn",
        "icon": "/generic/fullbody",
        "tags": ["@calisthenics", "@cardio", "@fullbody", "@safe_to_do_alone"],
        "exercise": {
            "poses": [
                {
                    "id": "jumping_jack_start",
                    "name": "@jumping_jack_start",
                    "icon": "/pose/jumping_jack_start",
                },
                {
                    "id": "high_knees_right",
                    "name": "@high_knees_right",
                    "icon": "/pose/high_knees_right",
                },
                {
                    "id": "jumping_jack_end",
                    "name": "@jumping_jack_end",
                    "icon": "/pose/jumping_jack_end",
                },
                {
                    "id": "high_knees_left",
                    "name": "@high_knees_left",
                    "icon": "/pose/high_knees_left",
                },
            ],
            "transitions": [
                {
                    "id": "jumping_jacks",
                    "name": "Jumping Jack",
                    "poses": [
                        {"id": "jumping_jack_start", "time": 500},
                        {"id": "jumping_jack_end", "time": 500},
                    ],
                },
                {
                    "id": "high_knees",
                    "name": "Elevación de rodilla",
                    "poses": [
                        {"id": "high_knees_left", "time": 500},
                        {"id": "high_knees_right", "time": 500},
                    ],
                },
            ],
            "sets": [
                {"id": "rest_short", "type": "rest", "data": {"duration": 30000}},
                {
                    "id": "jumping_jacks",
                    "type": "set_dynamic",
                    "data": {
                        "name": "Jumping Jacks",
                        "transition": "jumping_jacks",
                        "repetitions": 20,
                    },
                },
                {
                    "id": "high_knees",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@high_knees",
                        "transition": "high_knees",
                        "repetitions": 20,
                    },
                },
            ],
            "queue": [
                "jumping_jacks",
                "rest_short",
                "high_knees",
                "rest_short",
                "jumping_jacks",
            ],
        },
    },
    {
        "name": "@upper_body_focus",
        "icon": "/pose/jumping_jack_start",
        "tags": [
            "@calisthenics",
            "@strength",
            "@upperbody",
            "@needs_bar",
            "@safe_to_do_alone",
        ],
        "exercise": {
            "poses": [
                {
                    "id": "pull_up_top",
                    "name": "@pull_up_top",
                    "icon": "/pose/pull_up_with_bar",
                },
                {
                    "id": "push_up_top",
                    "name": "@push_up_top",
                    "icon": "/pose/plank_hold",
                },
                {
                    "id": "pull_down_bottom",
                    "name": "@pull_down_bottom",
                    "icon": "/pose/pull_down_with_bar",
                },
                {
                    "id": "push_down_bottom",
                    "name": "@push_down_bottom",
                    "icon": "/pose/push_down_from_flat_floor",
                },
            ],
            "transitions": [
                {
                    "id": "push_ups",
                    "name": "@push_ups",
                    "poses": [
                        {"id": "push_up_top", "time": 1500},
                        {"id": "push_down_bottom", "time": 1500},
                    ],
                },
                {
                    "id": "pull_ups",
                    "name": "@pull_ups",
                    "poses": [
                        {"id": "pull_up_top", "time": 1500},
                        {"id": "pull_down_bottom", "time": 1500},
                    ],
                },
            ],
            "sets": [
                {"id": "rest", "type": "rest", "data": {"duration": 60000}},
                {
                    "id": "push_ups",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@push_ups",
                        "transition": "push_ups",
                        "repetitions": 15,
                    },
                },
                {
                    "id": "pull_ups",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@pull_ups",
                        "transition": "pull_ups",
                        "repetitions": 10,
                    },
                },
            ],
            "queue": ["push_ups", "rest", "pull_ups", "rest", "push_ups"],
        },
    },
    {
        "name": "@barbell_power",
        "icon": "/pose/barbell_squat_down",
        "tags": ["@gym", "@barbell", "@strength", "@needs_bar", "@better_acompanied"],
        "exercise": {
            "poses": [
                {
                    "id": "barbell_squat_down",
                    "name": "@barbell_squat_down",
                    "icon": "/pose/barbell_squat_down",
                },
                {
                    "id": "barbell_deadlift_up",
                    "name": "@barbell_deadlift_up",
                    "icon": "/pose/barbell_deadlift_up",
                },
                {
                    "id": "barbell_bench_press_up",
                    "name": "@barbell_bench_press_up",
                    "icon": "/pose/barbell_bench_press_up",
                },
                {
                    "id": "barbell_deadlift_down",
                    "name": "@barbell_deadlift_down",
                    "icon": "/pose/barbell_deadlift_down",
                },
                {
                    "id": "barbell_bench_press_down",
                    "name": "@barbell_bench_press_down",
                    "icon": "/pose/barbell_bench_press_down",
                },
                {
                    "id": "barbell_squat_up",
                    "name": "@barbell_squat_up",
                    "icon": "/pose/barbell_squat_up",
                },
            ],
            "transitions": [
                {
                    "id": "barbell_deadlift",
                    "name": "@barbell_deadlifts",
                    "poses": [
                        {"id": "barbell_deadlift_down", "time": 2500},
                        {"id": "barbell_deadlift_up", "time": 2500},
                    ],
                },
                {
                    "id": "barbell_squats",
                    "name": "@barbell_squats",
                    "poses": [
                        {"id": "barbell_squat_down", "time": 2000},
                        {"id": "barbell_squat_up", "time": 2000},
                    ],
                },
                {
                    "id": "barbell_bench_press",
                    "name": "@barbell_bench_press",
                    "poses": [
                        {"id": "barbell_bench_press_down", "time": 1500},
                        {"id": "barbell_bench_press_up", "time": 1500},
                    ],
                },
            ],
            "sets": [
                {
                    "id": "barbell_deadlift",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@barbell_deadlift",
                        "transition": "barbell_deadlift",
                        "repetitions": 6,
                    },
                },
                {
                    "id": "barbell_squats",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@barbell_squats",
                        "transition": "barbell_squats",
                        "repetitions": 8,
                    },
                },
                {"id": "rest_long", "type": "rest", "data": {"duration": 90000}},
                {
                    "id": "barbell_bench_press",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@barbell_bench_press",
                        "transition": "barbell_bench_press",
                        "repetitions": 8,
                    },
                },
            ],
            "queue": [
                "barbell_squats",
                "rest_long",
                "barbell_bench_press",
                "rest_long",
                "barbell_deadlift",
            ],
        },
    },
    {
        "name": "@dumbbell_strength",
        "icon": "/generic/dumbbell",
        "tags": ["@gym", "@dumbbells", "@fullbody", "@dumbbells", "@safe_to_do_alone"],
        "exercise": {
            "poses": [
                {
                    "id": "dumbbell_curl_up",
                    "name": "@dumbbell_curl_up",
                    "icon": "/pose/dumbbell_curl_up",
                },
                {
                    "id": "goblet_squat_up",
                    "name": "@goblet_squat_up",
                    "icon": "/pose/goblet_squat_up",
                },
                {
                    "id": "dumbbell_curl_down",
                    "name": "@dumbbell_curl_down",
                    "icon": "/pose/dumbbell_curl_down",
                },
                {
                    "id": "dumbbell_press_down",
                    "name": "@dumbbell_press_down",
                    "icon": "/pose/dumbbell_press_down",
                },
                {
                    "id": "dumbbell_press_up",
                    "name": "@dumbbell_press_up",
                    "icon": "/pose/dumbbell_press_up",
                },
                {
                    "id": "goblet_squat_down",
                    "name": "@goblet_squat_down",
                    "icon": "/pose/goblet_squat_down",
                },
            ],
            "transitions": [
                {
                    "id": "dumbbell_press",
                    "name": "@dumbell_press",
                    "poses": [
                        {"id": "dumbbell_press_up", "time": 1500},
                        {"id": "dumbbell_press_down", "time": 1500},
                    ],
                },
                {
                    "id": "goblet_squat",
                    "name": "@goblet_squat",
                    "poses": [
                        {"id": "goblet_squat_down", "time": 2000},
                        {"id": "goblet_squat_up", "time": 2000},
                    ],
                },
                {
                    "id": "dumbbell_curls",
                    "name": "Curl de bíceps con mancuernas",
                    "poses": [
                        {"id": "dumbbell_curl_up", "time": 1500},
                        {"id": "dumbbell_curl_down", "time": 1500},
                    ],
                },
            ],
            "sets": [
                {
                    "id": "dumbbell_press",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@dumbbell_press",
                        "transition": "dumbbell_press",
                        "repetitions": 10,
                    },
                },
                {"id": "rest", "type": "rest", "data": {"duration": 60000}},
                {
                    "id": "goblet_squat",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@goblet_squat",
                        "transition": "goblet_squat",
                        "repetitions": 10,
                    },
                },
                {
                    "id": "dumbbell_curls",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@dumbbell_curls",
                        "transition": "dumbbell_curls",
                        "repetitions": 12,
                    },
                },
            ],
            "queue": [
                "dumbbell_curls",
                "rest",
                "dumbbell_press",
                "rest",
                "goblet_squat",
            ],
        },
    },
    {
        "name": "@core_blaster",
        "icon": "/pose/plank_hold",
        "tags": ["@calisthenics", "@strength", "@core", "@safe_to_do_alone"],
        "exercise": {
            "poses": [
                {
                    "id": "plank_start",
                    "name": "@plank_start",
                    "icon": "/pose/plank_hold",
                },
                {
                    "id": "plank_hold",
                    "name": "@plank_hold",
                    "icon": "/pose/plank_hold",
                },
                {
                    "id": "leg_raise_up",
                    "name": "@leg_raise_up",
                    "icon": "/pose/leg_raise_up",
                },
                {
                    "id": "leg_raise_down",
                    "name": "@leg_raise_down",
                    "icon": "/pose/leg_raise_down",
                },
            ],
            "transitions": [
                {
                    "id": "leg_raises",
                    "name": "Elevación de piernas",
                    "poses": [
                        {"id": "leg_raise_up", "time": 1500},
                        {"id": "leg_raise_down", "time": 1500},
                    ],
                },
                {
                    "id": "plank",
                    "name": "@plank",
                    "poses": [
                        {"id": "plank_start", "time": 1000},
                        {"id": "plank_hold", "time": 30000},
                    ],
                },
            ],
            "sets": [
                {"id": "rest_short", "type": "rest", "data": {"duration": 30000}},
                {
                    "id": "leg_raises",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@leg_raises",
                        "transition": "leg_raises",
                        "repetitions": 12,
                    },
                },
                {
                    "id": "plank",
                    "type": "set_static",
                    "data": {
                        "name": "@plank",
                        "pose": "plank_start",
                        "duration": 60000,
                    },
                },
            ],
            "queue": ["leg_raises", "rest_short", "rest_short", "plank"],
        },
    },
    {
        "name": "@lower_body_strength",
        "icon": "/pose/bodyweight_squat_down",
        "tags": ["@calisthenics", "@strength", "@lowerbody", "@safe_to_do_alone"],
        "exercise": {
            "poses": [
                {
                    "id": "lunge_left_up",
                    "name": "@lunge_left_up",
                    "icon": "/pose/lunge_left_up",
                },
                {
                    "id": "lunge_left_down",
                    "name": "@lunge_left_down",
                    "icon": "/pose/lunge_left_down",
                },
                {
                    "id": "bodyweight_squat_up",
                    "name": "@bodyweight_squat_up",
                    "icon": "/pose/bodyweight_squat_up",
                },
                {
                    "id": "lunge_right_up",
                    "name": "@lunge_right_up",
                    "icon": "/pose/lunge_right_up",
                },
                {
                    "id": "bodyweight_squat_down",
                    "name": "@bodyweight_squat_down",
                    "icon": "/pose/bodyweight_squat_down",
                },
                {
                    "id": "lunge_right_down",
                    "name": "@lunge_right_down",
                    "icon": "/pose/lunge_right_down",
                },
            ],
            "transitions": [
                {
                    "id": "bodyweight_squats",
                    "name": "Sentadillas sin peso",
                    "poses": [
                        {"id": "bodyweight_squat_down", "time": 1500},
                        {"id": "bodyweight_squat_up", "time": 1500},
                    ],
                },
                {
                    "id": "lunges",
                    "name": "@lunges",
                    "poses": [
                        {"id": "lunge_left_down", "time": 1500},
                        {"id": "lunge_left_up", "time": 1500},
                        {"id": "lunge_right_down", "time": 1500},
                        {"id": "lunge_right_up", "time": 1500},
                    ],
                },
            ],
            "sets": [
                {"id": "rest_short", "type": "rest", "data": {"duration": 30000}},
                {
                    "id": "bodyweight_squats",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@bodyweight_squats",
                        "transition": "bodyweight_squats",
                        "repetitions": 15,
                    },
                },
                {
                    "id": "lunges",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@lunges",
                        "transition": "lunges",
                        "repetitions": 12,
                    },
                },
            ],
            "queue": [
                "bodyweight_squats",
                "rest_short",
                "lunges",
                "rest_short",
                "bodyweight_squats",
            ],
        },
    },
    {
        "name": "@fullbody_circuit",
        "icon": "/generic/fullbody",
        "tags": ["@calisthenics", "@fullbody", "@cardio", "@safe_to_do_alone"],
        "exercise": {
            "poses": [
                {"id": "pushup", "name": "@push_up_top", "icon": "/pose/plank_hold"},
                {
                    "id": "mountain_climber_left",
                    "name": "@mountain_climber_left",
                    "icon": "/pose/mountain_climber_left",
                },
                {"id": "idle", "name": "Parado", "icon": "/pose/jumping_jack_start"},
                {"id": "clap", "name": "Palmada", "icon": "/pose/jumping_jack_end"},
                {
                    "id": "mountain_climber_right",
                    "name": "@mountain_climber_right",
                    "icon": "/pose/mountain_climber_right",
                },
                {
                    "id": "pushdown",
                    "name": "@push_down_bottom",
                    "icon": "/pose/push_down_from_flat_floor",
                },
            ],
            "transitions": [
                {
                    "id": "mountain_climbers",
                    "name": "Mountain Climbers",
                    "poses": [
                        {"id": "mountain_climber_left", "time": 500},
                        {"id": "mountain_climber_right", "time": 500},
                    ],
                },
                {
                    "id": "burpees",
                    "name": "@burpees",
                    "poses": [
                        {"id": "idle", "time": 500},
                        {"id": "clap", "time": 1000},
                        {"id": "pushup", "time": 1000},
                        {"id": "pushdown", "time": 1000},
                        {"id": "pushup", "time": 1000},
                    ],
                },
            ],
            "sets": [
                {"id": "rest_short", "type": "rest", "data": {"duration": 30000}},
                {
                    "id": "mountain_climbers",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@mountain_climbers",
                        "transition": "mountain_climbers",
                        "repetitions": 20,
                    },
                },
                {
                    "id": "burpees",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@burpees",
                        "transition": "burpees",
                        "repetitions": 15,
                    },
                },
            ],
            "queue": [
                "mountain_climbers",
                "rest_short",
                "burpees",
                "rest_short",
                "mountain_climbers",
            ],
        },
    },
    {
        "name": "@machine_strength",
        "icon": "/pose/leg_press_up",
        "tags": ["@gym", "@machine", "@strength", "@better_acompanied"],
        "exercise": {
            "poses": [
                {
                    "id": "leg_press_down",
                    "name": "@leg_press_down",
                    "icon": "/pose/leg_press_down",
                },
                {
                    "id": "leg_press_up",
                    "name": "@leg_press_up",
                    "icon": "/pose/leg_press_up",
                },
                {
                    "id": "chest_press_down",
                    "name": "@chest_press_down",
                    "icon": "/pose/chest_press_down",
                },
                {
                    "id": "chest_press_up",
                    "name": "@chest_press_up",
                    "icon": "/pose/chest_press_up",
                },
            ],
            "transitions": [
                {
                    "id": "leg_press",
                    "name": "Prensa de piernas",
                    "poses": [
                        {"id": "leg_press_down", "time": 2000},
                        {"id": "leg_press_up", "time": 2000},
                    ],
                },
                {
                    "id": "chest_press",
                    "name": "Press de pecho en máquina",
                    "poses": [
                        {"id": "chest_press_down", "time": 1500},
                        {"id": "chest_press_up", "time": 1500},
                    ],
                },
            ],
            "sets": [
                {
                    "id": "leg_press",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@leg_press",
                        "transition": "leg_press",
                        "repetitions": 10,
                    },
                },
                {
                    "id": "chest_press",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@chest_press",
                        "transition": "chest_press",
                        "repetitions": 10,
                    },
                },
                {"id": "rest_long", "type": "rest", "data": {"duration": 90000}},
            ],
            "queue": [
                "leg_press",
                "rest_long",
                "chest_press",
                "rest_long",
                "leg_press",
            ],
        },
    },
    {
        "name": "@cable_training",
        "icon": "/pose/cable_row_end",
        "tags": [
            "@gym",
            "@cable",
            "@strength",
            "@needs_cable_machine",
            "@safe_to_do_alone",
        ],
        "exercise": {
            "poses": [
                {
                    "id": "cable_triceps_pushdown_end",
                    "name": "@cable_triceps_pushdown_end",
                    "icon": "/pose/cable_triceps_pushdown_end",
                },
                {
                    "id": "cable_row_end",
                    "name": "@cable_row_end",
                    "icon": "/pose/cable_row_end",
                },
                {
                    "id": "cable_row_start",
                    "name": "@cable_row_start",
                    "icon": "/pose/cable_row_start",
                },
                {
                    "id": "cable_triceps_pushdown_start",
                    "name": "@cable_triceps_pushdown_start",
                    "icon": "/pose/cable_triceps_pushdown_start",
                },
            ],
            "transitions": [
                {
                    "id": "cable_triceps_pushdowns",
                    "name": "Press triceps en polea",
                    "poses": [
                        {"id": "cable_triceps_pushdown_start", "time": 1500},
                        {"id": "cable_triceps_pushdown_end", "time": 1500},
                    ],
                },
                {
                    "id": "cable_rows",
                    "name": "@cable_rows",
                    "poses": [
                        {"id": "cable_row_start", "time": 1500},
                        {"id": "cable_row_end", "time": 1500},
                    ],
                },
            ],
            "sets": [
                {
                    "id": "cable_triceps_pushdowns",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@cable_triceps_pushdowns",
                        "transition": "cable_triceps_pushdowns",
                        "repetitions": 12,
                    },
                },
                {"id": "rest", "type": "rest", "data": {"duration": 60000}},
                {
                    "id": "cable_rows",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@cable_rows",
                        "transition": "cable_rows",
                        "repetitions": 12,
                    },
                },
            ],
            "queue": [
                "cable_rows",
                "rest",
                "cable_triceps_pushdowns",
                "rest",
                "cable_rows",
            ],
        },
    },
    {
        "name": "@barbell_power",
        "icon": "/pose/barbell_squat_down",
        "tags": ["@gym", "@barbell", "@strength", "@needs_bar", "@better_acompanied"],
        "exercise": {
            "poses": [
                {
                    "id": "barbell_squat_down",
                    "name": "@barbell_squat_down",
                    "icon": "/pose/barbell_squat_down",
                },
                {
                    "id": "barbell_deadlift_up",
                    "name": "@barbell_deadlift_up",
                    "icon": "/pose/barbell_deadlift_up",
                },
                {
                    "id": "barbell_deadlift_down",
                    "name": "@barbell_deadlift_down",
                    "icon": "/pose/barbell_deadlift_down",
                },
                {
                    "id": "barbell_squat_up",
                    "name": "@barbell_squat_up",
                    "icon": "/pose/barbell_squat_up",
                },
            ],
            "transitions": [
                {
                    "id": "barbell_deadlifts",
                    "name": "@barbell_deadlift",
                    "poses": [
                        {"id": "barbell_deadlift_down", "time": 2000},
                        {"id": "barbell_deadlift_up", "time": 2000},
                    ],
                },
                {
                    "id": "barbell_squats",
                    "name": "Sentadillas con barra",
                    "poses": [
                        {"id": "barbell_squat_down", "time": 2000},
                        {"id": "barbell_squat_up", "time": 2000},
                    ],
                },
            ],
            "sets": [
                {
                    "id": "barbell_deadlifts",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@barbell_deadlifts",
                        "transition": "barbell_deadlifts",
                        "repetitions": 8,
                    },
                },
                {
                    "id": "barbell_squats",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@barbell_squats",
                        "transition": "barbell_squats",
                        "repetitions": 8,
                    },
                },
                {"id": "rest_long", "type": "rest", "data": {"duration": 90000}},
            ],
            "queue": [
                "barbell_squats",
                "rest_long",
                "barbell_deadlifts",
                "rest_long",
                "barbell_squats",
            ],
        },
    },
    {
        "name": "@bodyweight_upper_body",
        "icon": "/pose/pull_up_with_bar",
        "tags": ["@calisthenics", "@upperbody", "@strength", "@safe_to_do_alone"],
        "exercise": {
            "poses": [
                {
                    "id": "pull_down_bar",
                    "name": "@pull_down",
                    "icon": "/pose/pull_down_with_bar",
                },
                {
                    "id": "pull_up_top_bar",
                    "name": "@pull_up_top",
                    "icon": "/pose/pull_up_with_bar",
                },
                {
                    "id": "push_down_from_floor",
                    "name": "@push_down_bottom",
                    "icon": "/pose/push_down_from_flat_floor",
                },
                {
                    "id": "push_up_from_floor",
                    "name": "@push_up_top",
                    "icon": "/pose/plank_hold",
                },
            ],
            "transitions": [
                {
                    "id": "push_ups",
                    "name": "@push_ups",
                    "poses": [
                        {"id": "push_up_from_floor", "time": 1500},
                        {"id": "push_down_from_floor", "time": 1500},
                    ],
                },
                {
                    "id": "pull_ups",
                    "name": "@pull_ups",
                    "poses": [
                        {"id": "pull_up_top_bar", "time": 1500},
                        {"id": "pull_down_bar", "time": 1500},
                    ],
                },
            ],
            "sets": [
                {"id": "rest", "type": "rest", "data": {"duration": 60000}},
                {
                    "id": "push_ups",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@push_ups",
                        "transition": "push_ups",
                        "repetitions": 15,
                    },
                },
                {
                    "id": "pull_ups",
                    "type": "set_dynamic",
                    "data": {
                        "name": "@pull_ups",
                        "transition": "pull_ups",
                        "repetitions": 10,
                    },
                },
            ],
            "queue": ["pull_ups", "rest", "push_ups", "rest", "pull_ups"],
        },
    },
]
