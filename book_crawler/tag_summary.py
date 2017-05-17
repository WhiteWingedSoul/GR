import math
import json
import jsonpickle
from textblob import TextBlob as tb

class ItemTag(object):
	def __init__(self, name, score, relevantTag):
		self.name = name
		self.score = score
		self.relevantTag = relevantTag
		# self.childTag = ""
	def __str__(self):
		return "\tName: "+self.name+", score:"+str(self.score)

class ChildTag(object):
	def __init__(self, name, score):
		self.name = name
		self.score = score
		self.realScore = 0

class CompareTag(object):
	def __init__(self, tag, compareScore, realScore):
		self.tag = tag
		self.compareScore = compareScore
		self.realScore = realScore

tagList = []
itemTagList = []

def extract_time(json):
    try:
        # Also convert to int since update_time will be string.  When comparing
        # strings, "10" is smaller than "2".
        return int(json['score'])
    except KeyError:
        return 0

with open('test_tfidf.json') as json_data:
	jsonArray = json.load(json_data)
	for index, jsonStr in enumerate(jsonArray):
		#For each tag found
		for tagName in jsonStr['tag'].split(", "):
			#Add it to list
			if tagName not in tagList:
				#Search the whole array for relevant tag and its score:
				tagList.append(tagName)
				itemTag = ItemTag(tagName,0,[])
				otherTagNameList = ''

				for jsonStr2 in jsonArray[index:]:
					if tagName in jsonStr2['tag'].split(", "):
						itemTag.score += 1
						for otherTagName in jsonStr2['tag'].split(", "):
							if otherTagName != tagName:
								otherTag = next((x for x in itemTag.relevantTag if x.name == otherTagName), None)
								if otherTag is None:
									otherTag = ChildTag(otherTagName,0)
									otherTag.score += 1
									itemTag.relevantTag.append(otherTag)
									otherTagNameList += ' '+otherTagName
								else:
									otherTag.score += 1
									
				

				# if itemTag.score >=5 :
				itemTag.relevantTag = sorted(itemTag.relevantTag, key=lambda items: items.score, reverse = True)
					# itemTagList.append(jsonpickle.encode(itemTag, unpicklable=False))
				itemTagList.append(itemTag)
				print('\tTag:'+tagName)
				print('Relevant tags:'+otherTagNameList)



				# itemTagList.append(itemTag)
				# print('\tTag:'+tagName)
				# print('Relevant tags:'+otherTagNameList)
	# tagscore >= 10, tagcount <=10 -> selectTag
	print("Done basic network!"+str(len(itemTagList)))
	for itemTag in itemTagList:
		selectedTagList = []
		childTagRealTag = next((x for x in itemTagList if x.name == itemTag.relevantTag[0].name), None)
		# if childTagRealTag is not None:
			# itemTag.relevantTag[0].realScore = childTagRealTag.score
			# selectedTagList.append(CompareTag(itemTag.relevantTag[0],itemTag.relevantTag[0].score/itemTag.score, childTagRealTag.score))
		# else:
			# selectedTagList.append(CompareTag(itemTag.relevantTag[0],0, 0))
		for childTag in itemTag.relevantTag:
			# highest appear/total
			# if childTag.score>=5:
			childTagRealTag = next((x for x in itemTagList if x.name == childTag.name), None)
			if childTagRealTag is not None and childTag.score/itemTag.score >= 0.4:
				childTag.realScore = childTagRealTag.score
				selectTag = CompareTag(childTag, childTag.score/itemTag.score, childTagRealTag.score)

			# if childTag.score >= 10 and len(selectedTagList) <=10:
				selectedTagList.append(selectTag)
		if selectedTagList is not None:
		# selectedTagList = sorted(selectedTagList, key=lambda items:items.realScore, reverse = True)
			selectedTagList = sorted(selectedTagList, key=lambda items:items.compareScore, reverse = True)
		#if selectTag no member -> get best single child tag in tag ( tag has best score in whole list) ????
		# if len(selectedTag) == 0:
		# 	bestChild = itemTag.relevantTag[0]
		# 	bestChildScore = 0
		# 	for childTag in itemTag.relevantTag:
		# 		childTagRealTag = next((x for x in itemTagList if x.name == childTag.name), None)
		# 		if childTagRealTag is not None:
		# 			if childTagRealTag.score > bestChildScore:
		# 				bestChildScore = childTagRealTag.score
		# 				bestChild = childTag
		# 	selectedTag.append(bestChild)

			itemTag.relevantTag = []
			print("Done relevant tag for: "+itemTag.name)
			for selectTag in selectedTagList:
				# if selectTag.realScore>itemTag.score:
				itemTag.relevantTag.append(selectTag.tag)
				# else:
				# itemTag.childTag += selectTag.tag.name + " "
		# itemTag.relevantTag = sorted(itemTag.relevantTag, key=lambda items: items.score, reverse = True)
		# itemTag.relevantTag = selectedTagList
		if itemTag.score<5:
			itemTag.relevantTag = {x for x in itemTag.relevantTag if x.score>itemTag.score}
			if itemTag.relevantTag is None:
				itemTagList.remove(itemTag)

	itemTagList  = {x for x in itemTagList if x.score>=5 or (x.score < 5 and len(x.relevantTag) > 0)}
	
	print(str(len(itemTagList)))


	# TODO limit(=5) relevant tag, unlimit childTag, delete unimportant one
	# phan biet childTag (score<TagScore) va relevant Tag (score > TagScore)
	with open('test_tag_network.json', 'w+') as outfile:
		sortedList = sorted(itemTagList, key=lambda items: items.score, reverse = True)
		sortedJson = jsonpickle.encode(sortedList, unpicklable=False)
		json.dump(sortedJson, outfile)
