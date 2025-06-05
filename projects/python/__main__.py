"""
Ignored by eu.pythonanywhere.com, this code serves as the entry point for
executing this HTTP service locally.
"""
from app import app

if __name__ == "__main__":
    app.run(debug=True)