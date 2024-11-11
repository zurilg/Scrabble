>November 10, 2024\
Scrabble, Version 2.0, *License*: MIT (see bottom of README for full license description)

## Description
- A GUI-based version of the popular board game Scrabble.
- The game functions by allowing the player to place a word on the board using the randomly assigned tiles at their disposal. Once the player makes the move, the inner game logic ensures that it is valid. The game decides the validity of the move by checking whether or not the placed word is in the database of valid words and by checking if the placed word is connected to existing tiles on the board and is not isolated. If valid, the points gained from that move are added to the player's score. If the move is not valid, the placed tiles are returned to the player, and they must make another move. Players also have the option to skip their turn if they choose to. 
- The game adheres to the original Scrabble rules.
- This project is made up of 4 different milestones. Milestone 2, described here, focuses on implementing the GUI features of the game and expanding upon the previosly established framework to support future milestones 
## Roadmap Ahead
- Milestone 2 uses much of the logic from the previous milestone, including the MVC design pattern in its implementation. This allowed for a rather straightforward implementation of the GUI components.
- For milestone 3, we must implement blank tiles and "AI" players. AI players will be managed through an "AI Player" class. The game will maintain a list of the players playing and iterate through it to determine whose turn it is. If the player is an "AI" player, the AI class will handle the logistics of the "AI" making a move, including placing tiles on the board.
- With milestone 3 due in approximately two weeks, we decided to implement premium squares in milestone 2 to reduce the workload in the future.
- Milestone 4 will require us to customize our boards using XML or JSON. XML is already used in milestone 2 to create the premium squares on the board.
  
## Dependencies

To execute Scrabble, the following must be installed:
- [x] Oracle OpenJDK version 22 (or higher)

Must include the dictionary text file in the same directory as the JAR file.

## Usage
1. Download the zip file containing the source code and executable file of Scrabble.
2. Extract the zip file containing the source code and executable file of Scrabble.
3. Run the JAR file.

## Known Issues

- The database of available words is very limited, and contains a ton of nouns and names. This can potentially lead to frustrating gameplay experiences. It is hoped that our team will be able to compile a larger database consisting of commonly used words for the next milestone for a better user experience.
- The current code requires more refactoring to enhance readability and reduce complexity.

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
