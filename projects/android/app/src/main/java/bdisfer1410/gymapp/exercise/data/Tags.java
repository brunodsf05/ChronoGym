package bdisfer1410.gymapp.exercise.data;

import java.util.Map;

import bdisfer1410.gymapp.R;

public class Tags {
    public static final Map<String, Integer> data = Map.ofEntries(
            Map.entry("calisthenics", R.string.file_json_string_calisthenics),
            Map.entry("gym", R.string.file_json_string_gym),
            Map.entry("cardio", R.string.file_json_string_cardio),
            Map.entry("fullbody", R.string.file_json_string_fullbody),
            Map.entry("upperbody", R.string.file_json_string_upperbody),
            Map.entry("strength", R.string.file_json_string_strength),
            Map.entry("needs_bar", R.string.file_json_string_needs_bar),
            Map.entry("needs_dumbell", R.string.file_json_string_needs_dumbell),
            Map.entry("safe_to_do_alone", R.string.file_json_string_safe_to_do_alone),
            Map.entry("better_acompanied", R.string.file_json_string_better_acompanied),
            Map.entry("core", R.string.file_json_string_core),
            Map.entry("lowerbody", R.string.file_json_string_lowerbody),
            Map.entry("flexibility", R.string.file_json_string_flexibility),
            Map.entry("machine", R.string.file_json_string_machine),
            Map.entry("cable", R.string.file_json_string_cable),
            Map.entry("needs_cable_machine", R.string.file_json_string_needs_cable_machine)
    );
}
