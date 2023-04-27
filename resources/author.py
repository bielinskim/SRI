from flask_restful import Resource, reqparse
from db import db
from flask import jsonify, url_for
from models.author import Author
from datetime import date

parser = reqparse.RequestParser()
parser.add_argument('first_name', type=str, required=True)
parser.add_argument('last_name', type=str, required=True)
parser.add_argument('birth_date', type=str, required=True)

class AuthorResource(Resource):
    def get(self, id=None):
        try:
            if id is None:
                authors = Author.query.all()
                authors_list = []

                for author in authors:
                    author_dict = author.as_dict()

                    if author.books:
                        books = [book.as_dict() for book in author.books]
                        author_dict['books'] = books 
                    else:
                        author_dict['books'] = []
                    
                    authors_list.append(author_dict)

                response = {
                    'authors': authors_list,
                    'links': {
                        'self': url_for('authorresource', _external=True),
                    }
                }
                return jsonify(response)
            else:
                author = Author.query.get_or_404(id)
                author_dict = author.as_dict()

                books = [book.as_dict() for book in author.books]
                author_dict['books'] = books 

                response = {
                    'author': author_dict,
                    'links': {
                        'self': url_for('authorresource', id=id, _external=True),
                        'related': url_for('authoronlybooksresource', id=id, _external=True)
                    }
                }

                return jsonify(response)
        except Exception as e:
            db.session.rollback()
            raise e

    def post(self):
        try:
            params = parser.parse_args()
            author = Author(first_name=params['first_name'], last_name=params['last_name'], birth_date=date.fromisoformat(params['birth_date']))
            db.session.add(author)
            db.session.commit()

            author_dict = author.as_dict()
            author_dict['books'] = [] 

            return jsonify(author_dict)
        except Exception as e:
            db.session.rollback()
            raise e
        
    def put(self, id):
        try:
            params = parser.parse_args()
            author = Author.query.get_or_404(id)
            author.first_name = params['first_name']
            author.last_name = params['last_name']
            author.birth_date = date.fromisoformat(params['birth_date'])
            db.session.commit()

            author_dict = author.as_dict()
            if author.books:
                books = [book.as_dict() for book in author.books]
                author_dict['books'] = books
            else:
                author_dict['books'] = []

            return jsonify(author_dict)
        except Exception as e:
            db.session.rollback()
            raise e

    def delete(self, id):
        try:
            author = Author.query.get_or_404(id)
            db.session.delete(author)
            db.session.commit()
            return jsonify({'deleted': True})
        except Exception as e:
            db.session.rollback()
            raise e
    
class AuthorOnlyBooksResource(Resource):
    def get(self, id):
        try:
            author = Author.query.get_or_404(id)
            books = [book.as_dict() for book in author.books]
            return jsonify(books)
        except Exception as e:
            db.session.rollback()
            raise e