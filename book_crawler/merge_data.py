import math
import json
import jsonpickle

class BookItem(object):
	tag = ''

	def __init__(self, name, coverLink, author, publisher, edition_format, summary, subjects, sellerName, sellerLink, sellerPrice, genre_form, docType, note, description, content, abstract, onlineName, onlineLink):
		self.name = name
		self.coverLink = coverLink
		self.author = author
		self.publisher = publisher
		self.edition_format = edition_format
		self.summary = summary
		self.subjects = subjects
		self.sellerName = sellerName
		self.sellerLink = sellerLink
		self.sellerPrice = sellerPrice
		self.genre_form = genre_form
		self.docType = docType
		self.note = note
		self.description = description
		self.content = content
		self.abstract = abstract
		self.onlineName = onlineName
		self.onlineLink = onlineLink

bookList =[]

with open('data.json') as json_data:
	bookArray = json.load(json_data)
with open('data_book_tag.json') as json_data:
	bookTagArray = json.load(json_data)

for bookJson in bookArray:
	bookTag = next((x for x in bookTagArray if x['name'] == bookJson['name']), None)
	if bookTag is not None:
		book = BookItem(bookJson['name'], bookJson['coverLink'], bookJson['author'], \
			bookJson['publisher'], bookJson['edition_format'], bookJson['summary'], \
			bookJson['subjects'], bookJson['sellerName'], bookJson['sellerLink'], \
			bookJson['sellerPrice'], bookJson['genre_form'], bookJson['docType'], \
			bookJson['note'], bookJson['description'], bookJson['content'], \
			bookJson['abstract'], bookJson.get('onlineName', None), bookJson.get('onlineLink', None))
		book.tag = bookTag['tag']

	bookList.append(book)
	print(book.name+":\n"+book.tag)
print("Book list: "+str(len(bookList)))

with open('merged_data.json', 'w+') as outfile:
		bookListJson = jsonpickle.encode(bookList, unpicklable=False)
		json.dump(bookListJson, outfile)



