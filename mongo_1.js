use student

show collections

db.createCollection("class3")

db.topic.insertMany([
  {
    "id" : "1",
    "name" : "Math",
    "description" : "Easy to learn, Easy to Solve"
  },
  {
    "id" : "2",
    "name" : "Psychology",
    "description" : "Easy to learn, Hard to live"
  },
  {
    "id" : "3",
    "name" : "Politics",
    "description" : "Hard to learn, Easy to do"
  },
  {
    "id" : "4",
    "name" : "History",
    "description" : "Interesting to learn, Hard to remember"
  },
  {
    "id" : "5",
    "name" : "Spirituality",
    "description" : "Easy to learn, Life comes to ease"
  }
]);

db.class3.find()

db.class3.find({"age":"11"})

db.class3.find({"age": {$gte:"11"}})

db.class3.find({"first-name":"Kajal", "age":"11"}) //and condition

db.class3.find({
	$or : [{"first-name":"Kajal"}, {"age":"14"}]
})

db.class3.find({
    "first-name":"Abdullah", 
    	$or : [{"last-name":"Kawle"}, {"age":"14"}]
}) 

By default update is used to replace content but $set is used to update the specific document with the data

db.class3.update(
{"_id": ObjectId("5cceed58cb098cf48cee5b31")},
{$set : {"first-name":"Abdul"}}
)

db.class3.update(
{"age":"11"},
{$set: {"address":"Malkajgiri"}},
{multi: true}			//to modify more than one record

)

db.class3.save({
 "_id" : ObjectId("5cceed58cb098cf48cee5b31"), "first-name":"Abdul","last-name":"Mohd","gender":"Male","roll-no":"22","age":"11", "address":"Secunderabad"   
})

db.class3.remove({
 "age":"11"   
}, 1)	//remove only one record

db.class3.find({"_id": ObjectId("5cceed58cb098cf48cee5b31")})

db.class3.find({"age":"11"})

db.class3.find()



db.class3.insert([
{
 "first-name": "Kajal",
 "last-name": "Kawle",
 "roll-no": "1",
 "gender" : "Female",
  "age" : "11",
    "address" : "NA"   
},
{
 "first-name": "Naveen",
 "last-name": "Kogle",
 "gender" : "Male",
  "age" : "12",
   "grade" : "A+"
},
{
 "first-name": "Chris",
 "last-name": "Sure",
 "roll-no": "12",
 "gender" : "Male",
    "address" : "NA"   
},
{
 "first-name": "Abdullah",
 "gender" : "Male",
  "age" : "14"
},
{
 "first-name": "Ram",
 "last-name": "Cody",
 "roll-no": "11",
 "gender" : "Male",
  "age" : "11"  
},
{
 "first-name": "Priya",
 "last-name": "Murthy",
 "gender" : "Female",
  "age" : "12",
   "grade" : "A",
    "address" : "NA"   
}
])
