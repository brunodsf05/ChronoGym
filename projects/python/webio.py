"""
WebIO stands for "Web Input Output". Here you can find mostly utilitary
functions for processing flask's input into data and data into flask's output.
"""
from flask import Response
import json



def serialize_json_into_string(data: list[dict]) -> str:
    text = json.dumps(data, ensure_ascii=False, separators=(',', ':'))
    return Response(text, content_type="application/json")
