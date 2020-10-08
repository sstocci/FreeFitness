# FreeFitness
Free Fitness is an app I am developing in Android Studio using Java. The idea of the app is to create a completely free app for the user to track, and view trends in their workouts. Before using the app the user must create a login using Email and Password so I can keep track of my users. I use Google Firebase Authentication to make sure the user provides a validate login and password. Once the user logins in the first time they will automatically login everytime after they run the app until they logout. Free Fitness has 4 main features that I have implemented: Home, Log Workout, View Trends, and Calculate BMI. It stores the users workouts in an SQLite database on the mobile phone.

To see the java code used for this: freeFitness -> App -> src -> main -> java -> com -> sstinc -> freefitness
## The 4 Features 

### Home Page
The home page shows all the icons and explains the functionality of each one. This will be modified in the future to show relevant articles about fitness such as good new exercises, healthy foods, tips for losing/gaining weight, and more.

### Log Workout
Log Workout takes an exercise name and the number of sets (how many times they will repeat this exercise) and creates a list with as many list elements as the sets. Each list item will show the exercise name, the set number, weight, and reps. Weight and reps will be filled in by the user as they complete their workout. This allows for the user to easily fill in their results as they workout and doesn't interfere with their workout. After they complete the number of sets they choice the user will have two choices: Go on to the next exercise or finish workout. If they pick next exercise, it takes the current workout exercise and logs it to the database, this data will be used for View Trends. If they select finish workout, a dialog will have them select what muscle group they completed in the workout, this data will be used in View Calendar.

### View Trends
This page allows the user to see their progress as they continue to use the app. The user will select an exercise they have done before and then the app will display a line chart showing their max weight they did for the excerise as the Y- axis and the date completed as the X- axis.



### Calculate BMI
This pages allows the user to check their BMI by entering their height and weight. The app then displays the BMI value along with the range it falls into.


[![Run on Repl.it](https://repl.it/badge/github/sstocci/FreeFitness)](https://repl.it/github/sstocci/FreeFitness)