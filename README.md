# MineSweeper23-7-25

Minesweeper(CODED ON WINDOWS SYSTEM)

Minesweeper 

Choice of maven plugins:

Decision to use Lombok for the extra annotation features

1)Update java version in project structure. (I am using java 17) if using other java versions please do step 2 as well

   <img width="940" height="499" alt="image" src="https://github.com/user-attachments/assets/a5e161bc-03c9-4db0-ac93-a68039af9beb" />
<img width="940" height="513" alt="image" src="https://github.com/user-attachments/assets/8116d3a5-be2d-4122-a725-36c43ad98d82" />

2.	If using other java versions example 1.8 please change pom.xml <Java.version></Java.version> below to the specific java version
3.	
 <img width="940" height="507" alt="image" src="https://github.com/user-attachments/assets/40969d22-6eaf-46f4-860a-da5dbda1386f" />

4.	important to download lombok plugin from marketplace. Steps below in screenshot provided  
5.	enable Lombok annotation processing in intellij
   
 <img width="940" height="505" alt="image" src="https://github.com/user-attachments/assets/c3a9df4b-11e8-4be0-aa02-68776660eff9" />
<img width="940" height="522" alt="image" src="https://github.com/user-attachments/assets/0b4f4af2-da6c-4e2d-a4cb-cdbed7ea3c35" />
<img width="940" height="526" alt="image" src="https://github.com/user-attachments/assets/1ac25a9c-6935-4e43-b89e-d3c2867b06aa" />

 
 
6.	After downloading lombok plugin and setup is done, Restart Intellij, Clear cache if needed and reload maven project(circle Icon highlighted in screenshot).  After restarting run "mvn clean install" in terminal
   
 <img width="940" height="528" alt="image" src="https://github.com/user-attachments/assets/c18bfcbe-99ba-45df-949a-4aa42bd62420" />
<img width="940" height="471" alt="image" src="https://github.com/user-attachments/assets/626be522-27d4-4b13-af41-379031b1a4c7" />

 
7.	Right click run as file MineGameTest to run the Junit test cases
8.	Right click run as file MineSweeper to run program
   
 <img width="940" height="515" alt="image" src="https://github.com/user-attachments/assets/0e86cd52-6f90-41ba-ba3f-379d573d6e58" />
<img width="940" height="522" alt="image" src="https://github.com/user-attachments/assets/7f6cf0b6-b2fb-4584-87f3-3403fa1b2f3b" />

 


APPROACH


First There are 2 boards one is the player board and one is the answer board.


1.Initialise the player board to all "-" (This is the board used for the player)

2.Intialise the answer board by randomly placing mines according to input

3. Compute the hints for each cell not occupied by a mine . This is a number to determine how many mines are in its adjacent cell and update    the answer board. (This is the board used to refer to for the mine locations)
   
4. Start the game and get input from user
   
5. When revealing a cell on player board that is 0 on the answer board recursively open the rest of the surrounding cells and update the player board.
   
6. Use a visited set to keep track of all the visited cells, this is to prevent visiting an already visited cell
   
7. When visited all available cells end the game, or when located and opened a cell with a mine immediately end the game
