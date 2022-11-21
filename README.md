# **ECM2414 CA** Multi-Threaded Card Game
 (710026259) & (710039259)

## **Running The Game**
1. Extract the zip file
2. You should see a file named *cards.jar*, a folder called packs and all the relevant testing files and directories.
3. To run the jar, open a terminal instance (or cmd) and navigate to the directory the zip file was unzipped into.
4. Type into the terminal *java -jar cards.jar*
    - This will start the game.
    - It will ask for the number of players.
    - Then the location of a pack to play with, a pack file has been uploaded with packs for 2 to 100 players, but please specify the correct location of a pack you want to run the game with.
    - We recommend having the pack file in the same directory as the cards.jar file and then when prompted typing in the name of the pack file.  (e.g. "4.txt")


### **Running The Tests**
We used junit 5 to write our tests, so this framework is required to run the tests.

1. In the extracted zip file you will find a folder containing all the relevant project files required to open the project in IntelliJ IDEA.
2. Open the project in IntelliJ IDEA and you should have run configuration available for  testing. This can be found in the top right hand corner. (You can choose between Game and test, Game will run the Game and test will run the tests).
3. Run the tests!