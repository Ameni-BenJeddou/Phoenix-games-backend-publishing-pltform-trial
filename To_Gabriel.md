## Dear Gabriel
Hope you are having fun in your farm

This is a little document for me to keep track of my steps and progress.
Normally I would let this be in a private document but since you are taking care of so many mountain goats I think it will help
you understand my process better (maybe even have some constructive tips to improve it).

## First read

So I took a look at the readme and the project and what I gathered these things

* A post api that receives a list of json commands (for batch reception) 
* We need a java class to "map" the command 
* Each command logic will be done in a separate function to ensure the simplicity of other commands
* CQRS pattern? 
* DaoInMemory in a map noted
* Test coverage is at 100% class 
* Good to consider adding mutation tests to ensure we keep that percentage 
* Gabriel did such a great job

## Mutation tests
I learned how to integrate mutation tests in gradle for you Gabriel. I hope you are proud of this 
achievement like I am.

### Your own mutation test results
![your mutation test results](src/main/resources/first_mutation_test_results.png)

## Let's start

First I created the command structure : this is how we will map our json command into an object for manipulation.

I created the base structure for the endpoint and the three functions where I will implement the command 
processing logic. 

The endpoint accepts multiple commands so i probably will need to consider how i will ensure the handling of correct and incorrect
commands (bad types or bad user id).

For now this will do. My next step is going to be implementing tests to get a better vision of how the logic will look like. 

### Moving to tests

I have implemented some tests to demonstrate how the wanted processing of commands will be.

Doing these tests i realised there are some problem cases that need to be handled :
* Case of incorrect command type
* Case of invalid User Id in the command 
* What should we do in the case of correct command but properties that do not exist (accept the command and add the property or count the command as invalid) will send an email tomorrow to inquire.

### Refactoring
I was on the bus and i realized: why am i giving the endpoint the responsibility of calling the specific service function 
This is the responsibility of the service, and it should be handled accordingly.
So I will do some changes to reflect that in the code. Both the service and it's tests will be changed and I will add the invalid type test in the service tests

For the case of non-existing properties we found that the properties will be added with the initial values being 0 for increment and empty array for collect

I wrote some not very well coded attempt at handling incorrect commands in our list of commands (they turned out to be actually pretty decently coded). 

See Gabriel I want to be able to accept the correct commands and give back to the user the incorrect ones with the error message.
If I don't find a way to do that I will simply resolve to not accepting all the list if one command is incorrect but that does not sound ideal to me.

Tomorrow I will add some more tests for the cases of non-existing properties and then start implementing the logic behind the process.

### Oups

Dear Gabriel , i got the case of the tunnel vision. 

In the tests i kept trying to assert the property with it's assigned value . so i was depending on the Users current state which is .. not smart 

I changed it so that the property value is retrieved from the current state of the user, I compare it with a dynamically created expected value

I added 4 tests to test the cases of non-existing properties being handled

For now, I decided that for the collect method, the value will be a List of UserProfilePropertyValues and for the Increment method the allowed value will be an int and replace values should not be null =>
For this reason will add more tests to ensure Proper handling.

I think Tests are done and I will start implementing methods. I think i will sleep on it first, maybe I think of some other case to test for

After all I'm still not in a hurry(What I didn't know, I had some wisdom tooth problems which resulted in me eventually being in a hurry xD ) and this is turning out to be quite a fun little project, and I honestly can't wait for the feedback. 

## The all mighty collect

Well , i come across a wall. 

I was working with the intention to make everything as general as possible so that it is easier to add new commands.

For example i made the equals compare to string values so that the testing is easier and i kept it that all values are object that get cast for manipulation.

My problem was with the collect methode.

=======> i did in fact fix it by working some brain cells xD

## Endpoint 

I created the endpoint. It should process all commands even if some would throw an exception.

It returns an accepted response with a map indicating where the errors have occurred (if they did).

I am going to write some integration tests.

## Final steps 

I changed up the response a bit : Just for the sake of logging our results properly: now if all the commands are invalid then it returns a bad request
if some are correct and some are false it returns accepted but with the map of the state of the commands 
and if all are correct returns ok response.

I wrote some integration tests and corrected the unit test.

## Mutation test results 
![your mutation test results](src/main/resources/My final mutation test results.png)

I did add a bit of test strength and mutation coverage, but I personally would want to improve that (i'm not very satisfied)
BUT I did already take a lot of time and Gabriel needs my work so I will try and put my perfectionism to rest for this time and call it a job done.

(I say a job done but I will probably keep working on this after I send it to you to appease my own conscience plus it is a good training project)