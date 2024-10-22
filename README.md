>October 22, 2024\
Scrabble, Version 1.0, *License*: MIT (see bottom of README for full license description)

## Description
- A text-based version of the popular board game Scrabble.
- The game functions by allowing the player to enter the word they'd like to play on the board, where on the board they'd like to play it, and which direction the word should be written. The changes the player requests are then made to the board if they are valid. Every word in every row and column of the board is checked for validity (whether or not they're in the database of words). If the play turns out to be valid, all the point values of all new words created in a player's turn are accredited to that player. Play continues until there are 6 or more consecutive scoreless turns or a player rids of all of their tiles and can't gain anymore.
- The game adheres to a majority of the original Scrabble rules.
- This project is made up of 4 different milestones. Milestone 1, described here, focuses on implementing the logic of the game and 
creating a framework for future milestones.

## Roadmap Ahead
- An attempt at implementing the MVC design pattern in this milestone (Milestone 1) is in hopes for a more straight-forward implementation of a GUI in the next milestone (Milestone 2).
- For Milestone 2, we will turn this text-based version of Scrabble into a GUI that uses Java's awt and swing libraries. Our team hopes to further condense a lot of the current code to make it more efficient and easier to read. A lot of features are semi-implemented in hopes that it will lead to easier implementation in the future.
- Milestone 3 is currently due in roughly a month. Some features due in milestone 3 have some implementation in this version (Milestone 1) in preparation. 

## Dependencies

To execute Scrabble, the following must be installed:
- [x] Oracle OpenJDK version 22 (or higher)

Must include the dictionary text file in the same directory as the JAR file.

## Usage

1. Download the zip file containing the source code and executable file of Scrabble.
2. Extract the zip file containing the source code and executable file of Scrabble.
3. Run the JAR file.

## Known Issues

- The database (of less than 10,000) words is very limited, contains a ton of nouns and names, and even contains some vulgar language (we used the provided website). Not only does this make the game semi-frustrating to play, it goes against the official rules of Scrabble. It is hoped that our team will be able to compile a very large list of commonly used words for the next milestone in hopes for a better user experience. 
- Players currently can't exchange the tiles on their tile holders unless they make a play.
- If a player uses all 7 tiles in their tile holder in one turn, they currently don't get a bonus for doing so. 
- Not all bonus squares are currently placed on the board (only the triple words are), and they currenctly have no influence over the game.
- Only word scores and end-of-game point reductions/additions affect a player's overall score. Player score is often negative because of this.
- Blank tiles are currently not an aspect of the game, therefore there are only 98 tiles in the current version of the game.
- Since it is text-based, tiles are not individually placed. Instead, users have to provide the full word they want to form on the board and where.
- End of game is reached after either 6 scoreless turns (for 2-3 players) or 8 scoreless turns (for 4 players). The official rules state after 6 turns.
- Since we are working with a fixed database of words, implementing certain real-life Scrabble rules would be nonsensical. Therefore, the action of challenging a word someone plays was not implemented.

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
