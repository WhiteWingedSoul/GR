import sqlite3

db = sqlite3.connect("data.db")

c = db.cursor()
c.execute('''create table books
			(id integer primary key,
			 name text,
			 author text,
			 publisher text,
			 edition_format text,
			 summary text,
			 subjects text,
			 sellerName text,
			 sellerLink text,
			 sellerPrice text,
			 genre_form text,
			 docType text,
			 note text,
			 description text,
			 content text,
			 abstract text
			)''')
c.close()