import scrapy
from book_crawler.items import BookItem

class BooksSpider(scrapy.Spider):
	name = "books"
	page = 0

	def start_requests(self):
		urls = [
		#'http://www.worldcat.org/search?q=kw%3Aconversation+su%3A"english%20language"&dblist=638&fq=yr%3A2014..2017+%3E+%3E+ln%3Aeng+%3E+-fm%3Afic+%3E+-fm%3Ajuv&qt=facet_fm%3A_audience',
		#'http://www.worldcat.org/search?q=kw%3Aconversation+beginner+su%3A"english%20language"&dblist=638&fq=yr%3A2014..2017+%3E+%3E+ln%3Aeng+%3E+-fm%3Afic+%3E+-fm%3Ajuv&qt=facet_fm%3A_audience',
		#'http://www.worldcat.org/search?q=kw%3Aconversation+intermediate+su%3A"english%20language"&dblist=638&fq=yr%3A2014..2017+%3E+%3E+ln%3Aeng+%3E+-fm%3Afic+%3E+-fm%3Ajuv&qt=facet_fm%3A_audience',
		#'http://www.worldcat.org/search?q=kw%3Aconversation+basic+su%3A"english%20language"&dblist=638&fq=yr%3A2014..2017+%3E+%3E+ln%3Aeng+%3E+-fm%3Afic+%3E+-fm%3Ajuv&qt=facet_fm%3A_audience',
		#'http://www.worldcat.org/search?q=kw%3Aconversation+advanced+su%3A"english%20language"&dblist=638&fq=yr%3A2014..2017+%3E+%3E+ln%3Aeng+%3E+-fm%3Afic+%3E+-fm%3Ajuv&qt=facet_fm%3A_audience',
		'http://600tuvungtoeic.com/index.php?mod=lesson&id=1',
		]

		for url in urls:
			yield scrapy.Request(url=url, callback=self.parse)

	def parse(self, response):
		item = BookItem()
		item['name'] = response.css("lesson-content").extract_first()
		yield item
		# for detail in response.css("td.result.details"):
		# 	yield scrapy.Request(response.urljoin(detail.css("div.name a::attr(href)").extract_first()),
		# 						 callback=self.parse_detail)

		# next_page = response.xpath("//td[@align='right']/a[text()='Next']/@href").extract_first()
		# BooksSpider.page += 1
		# if next_page is not None and BooksSpider.page <= 75:
		# 	next_page = "http://www.worldcat.org/"+next_page
		# 	yield scrapy.Request(next_page, callback=self.parse)


	def parse_detail(self, response):
		item = BookItem()
		item['name'] = response.css('div#bibdata h1.title::text').extract_first()
		item['coverLink'] = 'http:'+response.css('div#bib-cont div#cover img.cover::attr(src)').extract_first()
		item['author'] = response.css('td#bib-author-cell a::text').extract_first()
		item['publisher'] = response.css('td#bib-publisher-cell::text').extract_first()	 
		edition_format = response.xpath("//span[@id='editionFormatType']/span")
		item['edition_format'] = edition_format.xpath("text()").extract_first() + edition_format.xpath("following-sibling::text()").extract_first()
		item['summary'] = response.css('td#bib-summary-cell div#summary::text').extract_first()
		item['subjects'] = ""
		for subject in response.css('ul#subject-terms-detailed li.subject-term'):
			item['subjects'] += subject.css('a::text').extract_first()+"\n"
		item['genre_form'] = response.css('tr#details-genre td::text').extract_first()
		item['docType'] = response.css('tr#details-doctype td::text').extract_first()
		item['note'] = response.css('tr#details-notes td::text').extract_first()
		item['description'] = response.css('tr#details-description td::text').extract_first()
		item['content'] = response.css('tr#details-contents td::text').extract_first()
		item['abstract'] = response.css('div.abstracttxt::text').extract_first()
		item['onlineName'] = response.css('div#links-all856 p a::text').extract_first()
		item['onlineLink'] = response.css('div#links-all856 p a::attr(title)').extract_first()

		oclcno = response.css('tr#details-oclcno td::text').extract_first()
		if oclcno is not None:
			request = scrapy.Request('http://www.worldcat.org/wcpa/servlet/org.oclc.lac.ui.buying.AjaxBuyingLinksServlet?serviceCommand=getBuyingLinks&oclcno='+oclcno, callback=self.parse_seller)
			request.meta['item'] = item
			yield request
		else:
			item['sellerName'] = response.css('td.seller a::text').extract_first()
			item['sellerLink'] = response.css('td.seller a::attr(href)').extract_first()
			item['sellerPrice'] = response.css('td.price::text').extract_first()		
			yield item

	def parse_seller(self, response):
		item = response.meta['item']

		item['sellerName'] = response.css('td.seller a::text').extract_first()
		item['sellerLink'] = response.css('td.seller a::attr(href)').extract_first()
		item['sellerPrice'] = response.css('td.price::text').extract_first()		

		yield item
