from flask import Flask
from waitress import serve
from flask_restful import Api
from resources.book import BookResource, BookOnlyAuthorResource
from resources.author import AuthorResource, AuthorOnlyBooksResource
from db import db

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///books.db'
db.init_app(app)
api = Api(app)

if __name__ == '__main__':
    with app.app_context():
        db.create_all()
    api.add_resource(BookResource, '/books',  '/books/<int:id>')
    api.add_resource(AuthorResource, '/authors',  '/authors/<int:id>')
    api.add_resource(AuthorOnlyBooksResource, '/authoronlybooks/<int:id>')
    api.add_resource(BookOnlyAuthorResource, '/bookonlyauthor/<int:id>')
    print("Serving on port 5000... Press CTRL+C to quit")
    serve(app, port=5000)
