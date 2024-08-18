# Padlock

Welcome to stage 3 of the interview. At this stage, you're going to write a software to crack a padlock.

The padlock, of course, is not real, it's only a metaphor for a physical padlock, so there is no legal issues.

The padlock has several features:

1. The keys/buttons on the numpad of the padlock are used exactly once. 
   This means for a 4 button padlock, code like 1234, 1324 are valid, 
   but code like 1123 and 122 are invalid.
2. The padlock is modified for you so you can operate it from Java.
   The hardware part need to do some heavy work to figure out the input buffer memory.
   So every time you write a digit of the input buffer, it takes a long time to do (almost 1 second).
3. The test process is fast. There is no heavy work occurs when asking the padlock if the input is correct.
4. If the input passcode is rejected (aka wrong code), the input buffer will stay untouched.
   So if 1234 is the wrong one, after test it's still 1234 in the memory.
5. The correct passcode is chosen randomly.


## Your task

1. Implement a cracking software that will work with any number of buttons (there is at least 1 button).
2. Try to make the process as fast as possible.
3. Currently, the padlock is a simple Java object, but in the future we may want to connect it
   using TCP socket, RESTful API, a cli program, or something else. Please use your software
   design techniques to make your software be easier to add those features.

Your code should be placed in the root project, where the `padlock-impl` subproject is the actual
implementation of the padlock.
Please leave it untouched when you submit your work.

You're encouraged to iterate through your solutions, just like how we solve the real world hard problems.
Start with a simple but slow one, then figure out how to make it faster, bit by bit.
By the end of the day, you have to choose one solution to submit even if you have multiple solutions available.
Just like we need to choose one implementation for the production environment.

## Criteria

The algorithm used to crack the padlock is not that important.
You have to write a working solution that will crack the padlock no matter how long it take.
The faster the better, but a slow one is also acceptable.

Proper software design techniques are also evaluated from the code.
You shouldn't slap everything into one giant Java file.
You should design the proper architectures for your code.

Last but not least, readability and maintainability are also important.
Take this project as a show off to your designing and coding skills.

## Build

This repo uses Gradle to build and test.
There is a unit test boilerplate and a gradle task configured to
automatically test and evaluate the code when you push your commits.

The `SolutionTestBase` is an abstract class for other tests.
It tests the correctness of your solution and don't care about the run time.
See `SolutionTest` for how to use that.

The `PerformanceAnalyze` is not a unit test, but it do analyze roughly how
fast your solution is. You need to fill in the `solve` method before you run it.

Use `./gradlew test` to run all unit test configured in the project,
and use `./gradlew runPerformanceTest` to get an analysis.

> Note: You don't have to have a local gradle installation.
> The `gradlew` script will download one for you.
> Just install a valid jdk (version >= 8) and very thing should be fine.

## Still have unclear problems?

Feel free to contact Jeffrey Freeman (jeffrey.freeman@cleverthis.com).