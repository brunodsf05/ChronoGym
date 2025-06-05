def filter_exercises(exercises: list[dict], tags_exclusive: list[str], tags_inclusive: list[str]) -> list[dict]:
    """
    Returns a list of exercises that follows both tag filters:
    1. Exclusive tags must appear all together.
    2. In addition, one or more inclusive tags must appear.
    """
    return exercises