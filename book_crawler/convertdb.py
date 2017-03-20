import sqlite3
import json

db = sqlite3.connect("data.db")

c = db.cursor()
c.execute('''create table if not exists books
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
			 abstract text,
			 onlineName text,
			 onlineText text,
			 tag text
			)''')

#sqlitebiter file db.json -o db.db