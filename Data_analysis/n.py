import bs4
import re
import googlemaps
import json
from datetime import datetime
from googletrans import Translator

fp = open("2019_page_1.html", encoding ="utf8")
soup = bs4.BeautifulSoup(fp,'lxml')
addr = []
addrt = []
t = Translator()

for i in soup.findAll(id = re.compile('^ContentPlaceHolder1_gdvMissingRegistrationdetails_lbllinkedfir')):
    if(i.text.find('रहात्या')==-1 & i.text.find('रहाच्या')==-1 &i.text.find('राहते')==-1 & i.text.find('राहच्या')==-1 & i.text.find('राहत्या')==-1 & i.text.find('रहाते')==-1 ):
        addr.append(i.text+"\n")




