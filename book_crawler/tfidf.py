import math
import json
import jsonpickle
from textblob import TextBlob as tb

def tf(word, blob):
    return blob.words.count(word) / len(blob.words)

def n_containing(word, bloblist):
    return sum(1 for blob in bloblist if word in blob.words)

def idf(word, bloblist):
    return math.log(len(bloblist) / (1 + n_containing(word, bloblist)))

def tfidf(word, blob, bloblist):
    return tf(word, blob) * idf(word, bloblist)



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
		if jsonStr['summary'] is not None:
			query += ' '+jsonStr['summary'].lower()
		# if jsonStr['subjects'] is not None:
		# 	query += ' '+jsonStr['subjects']
		if query is not None:
			blob = tb(""+query)
			bloblist.append(blob)
	for i, blob in enumerate(bloblist):
		print('\tTitle:'+jsonArray[i]['name'])
		scores = {word: tfidf(word, blob, bloblist) for word in blob.words}
		sorted_words = sorted(scores.items(), key=lambda x: x[1], reverse=True)
		tagJson = ItemTag(jsonArray[i]['name'], None)
		for word, score in sorted_words[:10]:
			if tagJson.tag is None:
				tagJson.tag = word
			else:
				tagJson.tag += ', '+word
			print("Word: {}, TF-IDF: {}".format(word, round(score, 5)))
		tagList.append(jsonpickle.encode(tagJson, unpicklable=False))
	with open('test_tfidf.json', 'w+') as outfile:
		json.dump(tagList, outfile)



	