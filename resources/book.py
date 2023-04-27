from flask_restful import Resource, reqparse
from flask import jsonify, url_for
from db import db
from datetime import date
from models.book import Book

parser = reqparse.RequestParser()
parser.add_argument('title', type=str, required=True)
parser.add_argument('author_id', type=int, required=True)
parser.add_argument('total_pages', type=int, required=True)
parser.add_argument('published_date', type=str, required=True)

class BookResource(Resource):
    try:
        def get(self, id=None):
            if id is None:
                books = Book.query.all()
                books_list = [book.as_dict() for book in books]
                response = {
                    'books': books_list,
                    'links': {
                        'self': url_for('bookresource', _external=True)
                    }
                }
                return jsonify(response)
            else:
                book = Book.query.get_or_404(id)
                book_dict = book.as_dict()

                if book.author is not None:
                    book_dict['author'] = book.author.as_dict()

                response = {
                    'book': book_dict,
                    'links': {
                        'self': url_for('bookresource', id=id, _external=True),
                        'related': url_for('bookonlyauthorresource', id=id, _external=True)
                    }
                }
                return jsonify(response)
    except Exception as e:
        db.session.rollback()
        raise e

    def post(self):
        try:
            params = parser.parse_args()
            book = Book(title=params['title'], author_id=params['author_id'], total_pages=params['total_pages'], published_date=date.fromisoformat(params['published_date']))
            db.session.add(book)
            db.session.commit()

            book_dict = book.as_dict()

            if book.author is not None:
                book_dict['author'] = book.author.as_dict()

            return jsonify(book_dict)
        except Exception as e:
            db.session.rollback()
            raise e

    def put(self, id):
        try:
            params = parser.parse_args()
            book = Book.query.get_or_404(id)
            book.title = params['title']
            book.total_pages = params['total_pages']
            book.author_id = params['author_id']
            book.published_date = date.fromisoformat(params['published_date'])
            db.session.commit()

            book_dict = book.as_dict()

            if book.author is not None:
                book_dict['author'] = book.author.as_dict()
            
            return jsonify(book_dict)
        except Exception as e:
            db.session.rollback()
            raise e

    def delete(self, id):
        try:
            book = Book.query.get_or_404(id)
            db.session.delete(book)
            db.session.commit()
            return jsonify({'deleted': True})
        except Exception as e:
            db.session.rollback()
            raise e
        
class BookOnlyAuthorResource(Resource):
    def get(self, id):
        try:
            book = Book.query.get_or_404(id)
            author = None

            if book.author is not None:
                author = book.author.as_dict()
            
            return jsonify(author)
        except Exception as e:
            db.session.rollback()
            raise e