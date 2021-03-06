import math
import json
import jsonpickle
from textblob import TextBlob as tb
from inflection import singularize

def tf(word, blob):
    return blob.words.count(word) / len(blob.words)

def n_containing(word, bloblist):
    return sum(1 for blob in bloblist if word in blob.words)

def idf(word, bloblist):
    return math.log(len(bloblist) / (1 + n_containing(word, bloblist)))

def tfidf(word, blob, bloblist):
    return tf(word, blob) * idf(word, bloblist)

blacklist = ["and","english","1","2","3","4","5","6","7","8","9","0","states","united","etc","for","my","a","by","the","of","your","you","et","in","de","to","an","120",",","lol","l2","y"]

bloblist = []
tagList = []

class ItemTag(object):
	def __init__(self, name, tag):
		self.name = name
		self.tag = tag

with open('data.json') as json_data:
	jsonArray = json.load(json_data)
	for jsonStr in jsonArray:
		# if jsonStr['subjects'] is None:
			# blob = tb("")
		# else:
			# blob = tb(jsonStr['subjects'])

		query = ''+jsonStr['name'].lower()
		# queryText = ' '
		# for word in jsonStr['name'].split():
			# queryText +=  singularize(word.lower()) + ' '
		# query = queryText
		if jsonStr['summary'] is not None:
			# queryText = ' '
			# for word in jsonStr['summary'].split():
				# queryText +=  singularize(word.lower()) + ' '
			query += ' ' + jsonStr['summary'].lower()
		if jsonStr['subjects'] is not None:
			# queryText = ' '
			# for word in jsonStr['subjects'].split():
			# 	queryText +=  singularize(word.lower()) + ' '
			query += ' ' + jsonStr['subjects'].lower()
		if query is not None:
			blob = tb(""+query)
			bloblist.append(blob)
	for i, blob in enumerate(bloblist):
		print('\tTitle:'+jsonArray[i]['name'])
		scores = {word: tfidf(word, blob, bloblist) for word in blob.words}
		sorted_words = sorted(scores.items(), key=lambda x: x[1], reverse=True)
		tagJson = ItemTag(jsonArray[i]['name'], None)
		count = 0
		for word, score in sorted_words:
			if count <= 10:
				if word in blacklist:
					continue
				else:
					if tagJson.tag is None:
						tagJson.tag = word
					else:
						tagJson.tag += ', '+word
					count += 1
					print("Word: {}, TF-IDF: {}".format(word, round(score, 5)))
			else:
				break
		tagList.append(jsonpickle.encode(tagJson, unpicklable=False))
	with open('test_tfidf.json', 'w+') as outfile:
		json.dump(tagList, outfile)



	