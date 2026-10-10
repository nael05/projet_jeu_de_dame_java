# International Checkers Game Project

**Individual School Project**
This project was completed individually as part of my computer science studies at Ynov Campus.

## Overview
This project is a complete implementation of the international checkers game on a 10x10 board, developed in Java. The application features a graphical interface created with Swing, allowing two players to compete locally. The game engine follows official rules, notably mandatory capturing and specific movements for kings (dames).

## Main Features
- 10x10 board compliant with international standards.
- Turn-based management between Black and White players.
- Pawn movement rules: forward diagonal for simple moves.
- King movement rules: movement across multiple squares in all directions.
- Mandatory capture system.
- Multi-jump (rafales) handling: ability to chain several captures with the same piece.
- Automatic promotion of a pawn to a king when it reaches the opponent's last row.
- Move history displayed in real-time in the interface.
- Automatic detection of the end of the game when a player has no pieces or valid moves left.

## Project Architecture
The project is structured in an object-oriented manner with the following classes:

### Logic and Engine (Model)
- **Jeu.java**: Main control class linking the board and the interface.
- **Plateau.java**: Manages the business logic, move validation, captures, and turn changes.
- **Case.java**: Represents a board cell and the associated piece.

### Entities (Data)
- **Piece.java**: Abstract class defining the basic structure of a piece.
- **Pion.java**: Represents a standard pawn.
- **Dame.java**: Represents a king.
- **Joueur.java**: Stores the name and color of the participants.

### Graphical Interface (View)
- **FenetreJeu.java**: Handles the visual display of the checkerboard and user interactions.
- **Main.java**: Entry point to launch the application.

## Implemented Rules
1. **Simple move**: A pawn moves one square diagonally forward.
2. **Capture (Jump)**: A pawn can capture an opponent's piece by jumping over it to an empty square, forward or backward.
3. **Capture priority**: If a capture is possible, it is mandatory.
4. **Kings**: They move any distance diagonally as long as the path is clear.
5. **Promotion**: A pawn becomes a king when it reaches the opponent's back rank (row 0 for White, row 9 for Black).

## Installation and Launch
To compile and run the project, use the following commands in your terminal:

1. Compilation:
   ```bash
   javac *.java
   ```
2. Launch:
   ```bash
   java Main
   ```

## Usage
- **Selection**: Click on a piece of your color to select it (a red border appears).
- **Movement**: Click on the destination square to validate the move.
- **Multi-jump**: If another capture is possible after a jump, the piece remains selected to continue the sequence.
- **History**: Check the side text area to see the list of played moves.
