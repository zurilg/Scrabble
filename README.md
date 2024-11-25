>November 24, 2024\
Scrabble, Version 2.0, *License*: MIT (see bottom of README for full license description)

## Description
- A GUI-based version of the popular board game Scrabble.
- User may choose how many players they wish to play with. Players can be human or AI.
- The game functions by allowing the player to place a word on the board using the randomly assigned tiles at their disposal. Once the player makes the move, the inner game logic ensures that it is valid. The game decides the validity of the move by checking whether or not the placed word is in the database of valid words and by checking if the placed word is connected to existing tiles on the board and is not isolated. If valid, the points gained from that move are added to the player's score. If the move is not valid, the placed tiles are returned to the player, and they must make another move. Players also have the option to skip their turn if they choose to. 
- The AI players follow the simple strategy of playing the highest scoring move. It does this by reading the entire dictionary and checking the entire board for valid moves. Once it finds the valid moves the controller will sort the moves based on their score and play the one with the highest score.
- The game adheres to a majority of the original Scrabble rules.
- This project is made up of 4 different milestones. Milestone 3, described here, focuses on implementing AI players, blank tiles, and premium squares. 
  
## Roadmap Ahead
- Milestone 3 uses much of the logic from the previous milestone, including the MVC design pattern in its implementation. 
- For milestone 3, we implemented blank tiles, premium squares and "AI" players. 
- AI players will be managed through an "AI Player" class, a subclass of the player class. The game will maintain a list of the players playing and iterate through it to determine whose turn it is. If the player is an "AI" player, the AI class will handle the logistics of the "AI" making a move, including placing tiles on the board.
- Premium squares were implemented using an XML file that indicates the coordinates of each premium square and the bonuses they contain.
- Blank tiles prompt the user to specify which letter the blank tile represents, then validate the turn using the chosen letter.
- Milestone 4 will require us to customize our boards using XML or JSON. XML is already used in milestone 3 to create the premium squares on the board so custom boards should be straightforward to implement.
- Milestone 4 will also require us to save and load the state of the board using serialization along with a multi-level undo option for players. 
  
## Dependencies

To execute Scrabble, the following must be installed:
- [x] Oracle OpenJDK version 22 (or higher)

Must include the dictionary text file in the same directory as the JAR file.

## Usage
1. Download the zip file containing the source code and executable file of Scrabble.
2. Extract the zip file containing the source code and executable file of Scrabble.
3. Run the JAR file.

## Known Issues

- The database of available words is very limited and contains a ton of nouns and names. This can potentially lead to frustrating gameplay experiences. It is hoped that our team will be able to compile a larger database consisting of commonly used words for the next milestone for a better user experience.
- Due to the nature of Scrabble and the limited dictionary, the game may occasionally reach a deadlock state. This occurs when no valid moves are possible with the remaining tiles, making it impossible to complete the game. This can happen early on due to how AI and human players make their moves.
- Currently players cannot redraw the tiles in their tile holder. 
- This may be a cosmetic issue to some people, but we find the game is as playable without them. But the game does not currently have coordinates for the rows and columns. Although, the bonus square colors already help guide a player.
- Another cosmetic issue is that the end of game stats messages are not lined up perfectly.
## Credits

- Mohammad Ahmadi (101267874)
- Zuri Lane-Griffore (101241678)
- Abdul Aziz Al-Sibakhi (101246056)
- Redah Eliwa (101273466)

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
