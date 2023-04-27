from db import db

class Book(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(100))
    author_id = db.Column(db.Integer, db.ForeignKey('author.id'))
    total_pages = db.Column(db.Integer)
    published_date = db.Column(db.Date)

    def as_dict(self):
        return {
            'id': self.id, 
            'title': self.title, 
            'total_pages': self.total_pages, 
            'published_date': self.published_date
            }