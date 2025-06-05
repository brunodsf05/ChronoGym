"""
WebIO stands for "Web Input Output". Here you can find mostly utilitary
functions for processing flask's input into data and data into flask's output.
"""
from flask import Response
import json



def split_line_into_words(line: str) -> list[str]:
    """i.e. ``"red,green,blue"`` → `` ["red", "green", "blue"]``"""
    return [word for raw in line.split(",") if (word := raw.strip())]



def serialize_json_into_string(data: list[dict]) -> str:
    text = json.dumps(data, ensure_ascii=False, separators=(',', ':'))
    return Response(text, content_type="application/json")
