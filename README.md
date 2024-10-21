# Title

Scrabble *License*: MIT (see bottom of README for full license description)\

# October 22, 2024

# IntelliJ IDEA Community Edition 2023.3.2


__________________________________________________________________________________________________________________________

# Description

Created a text-based version of the board game Scrabble. Game works by scanning user inputs for name, move, word, direction, 
coordinates for where to place tiles on the board. 

The game is played with 2-4 players.

The implementation of the logic ensures that the game adheres to the original rules of Scrabble, checking legality of placed tiles 
and updating the score after every turn.

This project is made up of 4 different milestones. Milestone 1, described here, focuses on implementing the logic of the game and 
creating a framework for future milestones.

__________________________________________________________________________________________________________________________

# Dependencies

java.util.ArrayList

java.util.HashSet

java.io.*

java.util.Scanner

Java 

IntelliJ IDEA Community Edition 2023.3.2

__________________________________________________________________________________________________________________________

# Installation

#Download IntelliJ IDEA Community Edition 2023.3.2 to your computer

#Download The latest version of Java

#Check the presence of the different Java libraries on your computer using the terminal. Install it if needed.

__________________________________________________________________________________________________________________________

# Usage

Download and decompress zip file containing source and executable file of Scrabble. Run the JAR file.

##Player setup:

Enter the amount of players you wish to play with. Assign each player a name. The order of turns is decided at the beginning of the game. 

Each player is assigned a tile, the player whose tile letter is closest to A goes first.

7 random tiles are given to each player from the tile bag. 

##Gameplay:

Each player has three options each turn, play their turn, skip their turn, or quit the game. The game ends when a player quits. Once the player places their tiles, the legality of the move is checked, then the board is updated and the next player goes. The score is 
updated after each turn.

If the move is illegal a message is displayed and the player is forced to restart their turn.

##End of game: 

The game is played until either a player quits, or the bag is empty and all the tiles are placed. The player with the most points 
accumalted is the winner.

__________________________________________________________________________________________________________________________

# Credits

## Mohammad Ahmadi (10167874)

## Zuri Lane-Griffore (101241678) 

## Abdul Aziz Al-Sibakhi (101246056)

## Redah Eliwa (101273466)
__________________________________________________________________________________________________________________________

# License

#MIT License

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
copies of the Software, a
