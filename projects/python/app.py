from flask import Flask, request

import database
import webio



app = Flask(__name__)



@app.route("/")
def main():
    tags_exclusive = webio.split_line_into_words(request.args.get("exclusive", ""))
    tags_inclusive = webio.split_line_into_words(request.args.get("inclusive", ""))
    filtered_exercises = database.exercises
    http_response = webio.serialize_json_into_string(filtered_exercises)
    return http_response
