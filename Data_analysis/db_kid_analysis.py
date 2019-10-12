import bs4
import re
import googlemaps

fp = open("2019_7_page_5.html", encoding ="utf8")
soup = bs4.BeautifulSoup(fp,'lxml')
addr = []
age = []
addr_filter = []


for (i,j) in zip(soup.findAll(id = re.compile('^ContentPlaceHolder1_gdvMissingRegistrationdetails_lbllinkedfir')), soup.findAll(id = re.compile('^ContentPlaceHolder1_gdvMissingRegistrationdetails_missingpersonage_'))):
    if(i.text.find('रहात्या')==-1 & i.text.find('रहाच्या')==-1 &i.text.find('राहते')==-1 & i.text.find('राहच्या')==-1 & i.text.find('राहत्या')==-1 & i.text.find('रहाते')==-1 ):
        addr.append(i.text)
        age.append(j.text)

for i in range(len(addr)):
    if int(age[i]) <= 20:
        addr_filter.append(addr[i])

fp = open("db_kid.txt","a",encoding="utf8")
fp1 = open("dbn_kid.txt","a",encoding="utf8")
gmaps = googlemaps.Client(key='AIzaSyBNbSHpD55qfU-m2WbrhoPEEIAHFEIIKVE')
for i in addr_filter:
    result = gmaps.geocode(i)
    if (len(result)!=0) :
        lat = result[0]["geometry"]["location"]["lat"]
        lon = result[0]["geometry"]["location"]["lng"]
        fp.write(str(lat) + " " + str(lon) +  "\n")
        fp1.write(str(lat) + " " + str(lon) +"\n" + " [" + i +"]" + "\n")
fp.close()
fp1.close()        
    


    
