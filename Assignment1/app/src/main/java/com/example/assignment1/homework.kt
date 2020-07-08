package com.example.assignment1
// Shant Ahuja
// CST407 Mobile Development with Android
// Su2020

import java.util.Random

/* 
 * Welcome to Kotlin. For this assignment, please fill in the following functions 
 * based on the examples. DO NOT edit the code outside of the functions. DO NOT edit 
 * the function headers or return types!
*/

data class Programmer(val name: String, val level: Int)


/* In Kotlin, the "when" expression allows you to easily match objects based on known
 * values. It is more versatile then the Java "switch", and we will use it a ton in 
 * Android.
 * 
 * Fill in the function below to return a different String (choose whatever you want)
 * if their level is from 8 to 10 (inclusive), 3 to 7 (inclusive), 0 to 2 (inclusive), 
 * or outside all of those ranges. YOU MUST use a when() expression!
 * Ex:
 * val programmerOne = Programmer("Anton", 10)
 * val programmerTwo = Programmer("iOS TA", 1)
 * println(whichProgrammer(programmerOne))
 * println(whichProgrammer(programmerTwo))
 * 
 * ##################
 * 
 * "Wow, great programming skill!" 
 * "Your skill needs improvement!"
 */
fun whichProgrammer(programmer: Programmer): String {
	when (programmer.level) {
		0, 1, 2 -> return "This programmer at a beginner level."
		3, 4, 5, 6, 7 -> return "This programmer is at a basic to intermediate level."
		in 8..10 -> return "This programmer is at an advanced level."
	}
	return "Programmer's level is not between 0-10"
}


/* 
 * Fill in this function to return the top three programmers (by level), highest to lowest.
 * DO NOT use for/while loops! Instead, try to lean on Kotlin's built in List functions like 
 * .sortedWith() 
 * Ex:
 * val listOfProgrammers = listOf(
 * 		Programmer("Jim", 1),
 * 		Programmer("Pam", 3),
 * 		Programmer("Darryl", 5),
 * 		Programmer("Dwight", -10),
 * 		Programmer("Steve Carell", 10))
 * println(getTopThree(listOfProgrammers))
 * 
 * ##########################
 * 
 * [Programmer(name=Steve Carell, level=10), 
 * 	Programmer(name=Darryl, level=5), 
 * 	Programmer(name=Pam, level=3)]
 */
fun getTopThree(listOfProgrammers: List<Programmer>): List<Programmer> {
	return listOfProgrammers.sortedWith(compareByDescending { item -> item.level }).take(3)
}

/*
 * The built in Pair() data class in Kotlin makes creating tuples of two a breeze. Use
 * more list functions like the .filter() and .map() methods to make this method return each of the 
 * prof - student pairs whose levels add up to 10.
 * DO NOT use for/while loops!
 * 
 * Hint: The .flatten() method may be useful as well. 
 * 
 * Ex:
 * val listOfProfs = listOf(
 * 		Programmer("Anton", 9), 
 * 		Programmer("Tristrum", 9))
 * val listOfStudents = listOf(
 * 		Programmer("Bill", 1), 
 * 		Programmer("Gates", 3))
 * println(meetYourProf(listOfProfs, listOfStudents))
 * 
 * ##########################
 * 
 * [(Programmer(name=Anton, level=9), Programmer(name=Bill, level=1)), 
 * 	(Programmer(name=Tristrum, level=9), Programmer(name=Bill, level=1))]
 */
fun meetYourProf(profs: List<Programmer>, students: List<Programmer>): List<Pair<Programmer, Programmer>> {
	var profList = profs.map { prof ->
		students.map { student ->
			if (prof.level + student.level == 10)
				Pair(prof, student)
			else
				null
		}
	}.flatten()

	var output = profList.filterNotNull()

	return output
//	more of a java structure on this one
//	return profs.map { prof ->
//		students.map { student ->
//			if (prof.level + student.level == 10) Pair(prof, student) else null
//		}
//	}
//		.flatten()
//		.filterNotNull()
}

/*
 * Kotlin handles functions and lambdas easily. Fill in the below function that takes in
 * a list of programmers as well as a function to filter out students, then use a .fold()
 * method to combine the list into a single string that starts with "The passing programmer(s): "
 * then lists each passing student AND his/her level.
 * 
 * Ex:
 * val listOfProfs = listOf(
 * 		Programmer("Anton", 9), 
 * 		Programmer("iOS Lecturer", 2),
 * 		Programmer("Tristrum", 10),)
 * println(specialFilter(listOfProfs, {programmer -> programmer.level > 3}))
 * 
 * ##########################
 * 
 * "The passing programmer(s): Anton9 Tristrum10"
 */
fun specialFilter(students: List<Programmer>, passed: (Programmer) -> (Boolean)): String {
	val filteredStudent = students.filter {passed(it)}
	return filteredStudent.fold("The programmers are: ", {acc, prog -> acc + prog.name + " " + prog.level + " "})
} 


/* Kotlin can extend classes and add functions easily!
 * Watch this clip: https://youtu.be/X1RVYt2QKQE?t=12m20s to see a demo,
 * then fill in this function to return a random number between 0 and the 
 * Int being operated on INCLUSIVE.
 * 
 * Ex:
 * println(5.random())
 * println(5.random())
 * println(5.random())
 * 
 * ##########################
 * 
 * 0
 * 5
 * 4
 */
fun Int.random(): Int {
	return (0..this).random()
}

/*
 * Now that we have a random function, use it to return a random programmer from a
 * list. However, note that since the random() function returns a number that is
 * between 0 and the size of our list INCLUSIVE, there is a chance that the index
 * generated is out of range. If that is the case, return a new Programmer with 
 * name = "Superman" and level = 100 using the .getOrElse() function
 * 
 * Ex:
 * val listOfProgrammers = listOf(
 * 		Programmer("Jim", 1), 
 * 		Programmer("Pam", 3), 
 * 		Programmer("Darryl", 5), 
 * 		Programmer("Dwight", -10), 
 * 		Programmer("Steve Carell", 10))
 * println(getRandomProgrammer(listOfProgrammers))
 * println(getRandomProgrammer(listOfProgrammers))
 * 
 * ##########################
 * 
 * Programmer(name=Darryl, level=5) 
 * Programmer(name=Superman, level=100) 
 */
fun getRandomProgrammer(listOfProgrammers: List<Programmer>): Programmer {
	// to get sizes (size of the list to know the upper limit of the random number)
	val listSize = listOfProgrammers.size
	// use the get or else function on the listofprogrammers passed in
	// looks like
	return listOfProgrammers.getOrElse(listSize.random(), { Programmer("Superman", 100) })
	
//	return Programmer("Superman", 100)
}

/*
 * Additionally, we can use the ? modifier to allow a function to return null. Although
 * not ideal in the functional world, this allows us to easily interact with Java 
 * classes that can potentially be null or return null pointers. This time, if the 
 * index is out of bounds, return null using the .getOrElse() function. 
 * 
 * Ex:
 * val listOfProgrammers = listOf(
 * 		Programmer("Jim", 1), 
 * 		Programmer("Pam", 3), 
 * 		Programmer("Darryl", 5), 
 * 		Programmer("Dwight", -10), 
 * 		Programmer("Steve Carell", 10))
 * println(getRandomProgrammerNull(listOfProgrammers))
 * println(getRandomProgrammerNull(listOfProgrammers))
 * 
 * ##########################
 * 
 * Programmer(name=Darryl, level=5) 
 * null
 */

fun getRandomProgrammerNull(listOfProgrammers: List<Programmer>): Programmer? {
	val listSize = listOfProgrammers.size
	return listOfProgrammers.getOrElse(listSize.random(), { null })
}

/*
* You can add testing to this main method, but please CLEAR it before submitting!
*/
fun main(args: Array<String>) {

}


/*
 * The End! 
 */
