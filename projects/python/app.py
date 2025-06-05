from flask import Flask, request

import database
import search
import webio



app = Flask(__name__)



@app.route("/")
def main():
    tags_exclusive = webio.split_line_into_words(request.args.get("exclusive", ""))
    tags_inclusive = webio.split_line_into_words(request.args.get("inclusive", ""))
    filtered_exercises = search.filter_exercises(database.exercises, tags_exclusive, tags_inclusive)
    http_response = webio.serialize_json_into_string(filtered_exercises)
    return http_response
