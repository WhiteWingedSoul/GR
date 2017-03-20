# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class BookItem(scrapy.Item):
    name = scrapy.Field()
    coverLink = scrapy.Field()
    author = scrapy.Field()
    publisher = scrapy.Field()
    edition_format = scrapy.Field()
    summary = scrapy.Field()
    subjects = scrapy.Field()
    sellerName = scrapy.Field()
    sellerLink = scrapy.Field()
    sellerPrice = scrapy.Field()
    genre_form = scrapy.Field()
    docType = scrapy.Field()
    note = scrapy.Field()
    description = scrapy.Field()
    content = scrapy.Field()
    abstract = scrapy.Field()
    onlineName = scrapy.Field()
    onlineLink = scrapy.Field()
