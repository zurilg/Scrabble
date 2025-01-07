>January 06, 2024\
Scrabble, Version 4.0, *License*: MIT (see bottom of README for full license description)

## Disclaimer
This project is an educational, non-commercial implementation inspired by the popular (and fantastically fun) board game Scrabble. It was created solely as a class assignment to demonstrate the process of software development and the implementation of popular design patterns. The game name "Scrabble" and associated trademarks are owned by Hasbro (in the U.S. and Canada) and Mattel (outside the U.S. and Canada). This project is not affiliated with, endorsed by, or connected to the official Scrabble brand in any way. All rights to the original game and its intellectual property remain with their respective owners.


## Description
- A GUI-based version of the popular board game Scrabble.
- The user is prompted to start a new game. If user has a previous game saved, they have the option to load that game and continue playing it in its most recent state.
- If they wish to load a saved game, they are first prompted to enter the file name of the saved game. The file must exist in order to load the saved game.
- If there are no saved games then the user must start a new game to begin playing.
- After choosing to start a new game, the user must choose the board they wish to play with from the three options available:
  1. Classic
  2. New Wave
  3. No bonuses
- Upon choosing the board, teh user may choose how many players they wish to play with. Players can be human or AI.
- The game functions by allowing the player to place a word on the board using the randomly assigned tiles at their disposal. Once the player makes the move, the inner game logic ensures that it is valid. The game decides the validity of the move by checking whether or not the placed word is in the database of valid words and by checking if the placed word is connected to existing tiles on the board and is not isolated. If valid, the points gained from that move are added to the player's score. If the move is not valid, the placed tiles are returned to the player, and they must make another move. Players also have the option to skip their turn if they choose to. 
- The AI players follow the simple strategy of playing the highest scoring move. It does this by reading the entire dictionary and checking the entire board for valid moves. Once it finds the valid moves the controller will sort the moves based on their score and play the one with the highest score.
-Throughout the game, users have the option to undo a round of moves or redo a round after undoing. This allows players to revert their actions if they make a mistake or change their minds about a move. They can do this by simply opening the "Turns" menu and clicking either the "Undo" menu option or the "Redo" menu option.
- Players can also save their current game during the game, or before exiting. If they wish to do so during the game they must open the "Game" menu and click the "Save" menu option, where they are prompted to enter the name they wish to save the game as. Players must enter a valid name format.
- When exiting the game the players are asked if they wish to save the game or not.
- The "Game" menu also has an "End Game" menu option that terminates the game and displays the stats of all the player.
- Once the game has ended, a message appears displaying the player game stats and the winner.
- The game adheres to a majority of the original Scrabble rules.
- This project is made up of 4 different milestones.
  
## Roadmap Ahead
- Milestone 4 uses much of the logic from the previous milestone, including the MVC design pattern in its implementation. 
- For milestone 4, the program maintains the player stats in the Player Class throughout the game, when a game is concluded and its file is deleted, the end game stats of the player are displayed.
- Looking forward, each team member will most likely use the code for this game as inspiration for future projects, such as university assignments or personal projects.
  
## Dependencies
To execute Scrabble, the following must be installed:
- [x] Oracle OpenJDK version 22 (or higher)

Must include the GameAssets folder in the same directory as the JAR file.

## Usage
1. Download the Scrabble JAR file and GameAssets folder and be sure they're contained within the same directory.
2. Use appropriate shell (command prompt or terminal) and navigate to corresponding directory.
3. Run the JAR file using the following command: **java -jar Scrabble.jar**
4. Enjoy playing!

## Known Issues
- Currently players cannot replace the tiles in their tile holder.
- Small behaviorual bug: There is a small chance of a correct move not working, player must play the move again for it work.
- The database of available words is very limited and contains a ton of nouns and names. This can potentially lead to frustrating gameplay experiences. It is hoped that our team will be able to compile a larger database consisting of commonly used words for the next milestone for a better user experience.
- Due to the nature of Scrabble and the limited dictionary, the game may occasionally reach a deadlock state. This occurs when no valid moves are possible with the remaining tiles, making it impossible to complete the game. This can happen early on due to how AI and human players make their moves. 
- This may be a cosmetic issue to some people, but we find the game is as playable without them. But the game does not currently have coordinates for the rows and columns. Although, the bonus square colors already help guide a player.
  
## Credits
- Zuri Lane-Griffore
- Mohammad Ahmadi
- Abdul Aziz Al-Sibakhi
- Redah Eliwa

## License

MIT License

Copyright (c) [2024] [Group 35]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
