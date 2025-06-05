def filter_exercises(exercises: list[dict], tags_exclusive: list[str], tags_inclusive: list[str]) -> list[dict]:
    """
    Returns a list of exercises that follows both tag filters:
    1. Exclusive tags must appear all together.
    2. In addition, one or more inclusive tags must appear.
    """
    filtered_exercises: list[dict] = []

    for exercise in exercises:
        tags = exercise["tags"]
        ok_exclusive = True
        ok_inclusive = True

        if len(tags_exclusive) > 0:
            for extag in tags_exclusive:
                if extag not in tags:
                    ok_exclusive = False # Not every exclusive tag is inside exercise tag
                    break

        if len(tags_inclusive) > 0:
            for intag in tags_inclusive:
                if intag in tags:
                    break
            else:
                ok_inclusive = False # At least one inclusive tag couldn't be found

        if ok_exclusive and ok_inclusive:
            filtered_exercises.append(exercise)

    return filtered_exercises