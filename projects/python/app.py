from flask import Flask

import database
import webio



app = Flask(__name__)



@app.route("/")
def main():
    filtered_exercises = database.exercises
    http_response = webio.serialize_json_into_string(filtered_exercises)
    return http_response
