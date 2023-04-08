from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy
from datetime import date
from waitress import serve

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///books.db'
db = SQLAlchemy(app)


class Book(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(100))
    author = db.Column(db.String(50))
    total_pages = db.Column(db.Integer)
    published_date = db.Column(db.Date)

    def as_dict(self):
        return {'id': self.id, 'title': self.title, 'author': self.author, 'total_pages': self.total_pages, 'published_date': self.published_date}


@app.route('/books/<int:book_id>', methods=['GET'])
def get_book(book_id):
    product = Book.query.get_or_404(book_id)
    return jsonify(product.as_dict())


@app.route('/books', methods=['GET'])
def get_books():
    books = Book.query.all()
    books_list = [book.as_dict() for book in books]
    return jsonify(books_list)


@app.route('/books', methods=['POST'])
def create_book():
    try:
        book = Book(title=request.json['title'], author=request.json['author'],
                    total_pages=request.json['total_pages'], published_date=date.fromisoformat(request.json['published_date']))
        db.session.add(book)
        db.session.commit()
        return jsonify(book.as_dict()), 200
    except Exception as e:
        db.session.rollback()
        print(e)
        return jsonify({'error': 'Nie udało się dodać książki'}), 400


@app.route('/books/<int:book_id>', methods=['PUT'])
def update_book(book_id):
    try:
        book = Book.query.get_or_404(book_id)
        book.title = request.json['title']
        book.author = request.json['author']
        book.total_pages = request.json['total_pages']
        book.published_date = date.fromisoformat(
            request.json['published_date'])
        db.session.commit()
        return jsonify(book.as_dict()), 200
    except Exception as e:
        db.session.rollback()
        print(e)
        return jsonify({'error': 'Nie udało się zaktualizować książki'}), 400


@app.route('/books/<int:book_id>', methods=['DELETE'])
def delete_book(book_id):
    try:
        book = Book.query.get_or_404(book_id)
        db.session.delete(book)
        db.session.commit()
        return jsonify({'deleted': True})
    except Exception as e:
        db.session.rollback()
        print(e)
        return jsonify({'error': 'Nie udało się usunąć książki'}), 400


if __name__ == '__main__':
    with app.app_context():
        db.create_all()
    print("Serving on port 5000... Press CTRL+C to quit")
    serve(app, port=5000)
