# MineSweeper23-7-25

Minesweeper(CODED ON WINDOWS SYSTEM)
Minesweeper 
Choice of maven plugins:
Decision to use Lombok for the extra annotation features
1)Update java version in project structure. (I am using java 17) if using other java versions please do step 2 as well
   <img width="940" height="499" alt="image" src="https://github.com/user-attachments/assets/a5e161bc-03c9-4db0-ac93-a68039af9beb" />
<img width="940" height="513" alt="image" src="https://github.com/user-attachments/assets/8116d3a5-be2d-4122-a725-36c43ad98d82" />

2.	If using other java versions example 1.8 please change pom.xml <Java.version></Java.version> below to the specific java version
 
3.	important to download lombok plugin from marketplace. Steps below in screenshot provided  
4.	enable Lombok annotation processing in intellij
 
 
 
5.	After downloading lombok plugin and setup is done, Restart Intellij, Clear cache if needed and reload maven project(circle Icon highlighted in screenshot).  After restarting run "mvn clean install" in terminal
 
 
6.	Right click run as file MineGameTest to run the Junit test cases
7.	Right click run as file MineSweeper to run program
 
 


APPROACH

First There are 2 boards one is the player board and one is the answer board. 
1)Initialise the player board to all “-“
2)Intialise the answer board by randomly placing mines according to input
3) Compute the hints for each cell not occupied by a mine . This is a number to determine how many mines are in its adjacent cell and update the answer board.
4) Start the game and get input from user
5)When revealing a cell that is 0 on the answer board recursively open the rest of the surrounding cells and update the player board.
6)Use a visited set to keep track of all the visited cells
7)When visited all available cells end the game, or when located and opened a cell with a mine immediately end the game
