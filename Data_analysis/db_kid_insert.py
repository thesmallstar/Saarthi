from pymongo import MongoClient
client = MongoClient()
db = client.locations
kid= db.kid
fp = open("db_kid.txt","r")
f = fp.readlines()
i=0
post = []
for x in f:
     if i==0:
             post.append({"lat": float((x.split())[0]), "long": float((x.split())[1])})
             i=i+1
     else:
             post.append({"lat": float((x.split())[0]), "long": float((x.split())[1])})

for i in post:
     kid.insert_one(i)
