# TicTacToe

A console-based Tic-Tac-Toe game, where players take turns marking spaces in a 3x3 grid. The first to align three of their marks horizontally, vertically, or diagonally wins the round.

## How to Play

```text
start <player1> <player2>
exit
```

**player**
- `user` — human player
- `bot` — bot, random difficulty
- `bot:<difficulty>` — bot with set difficulty

**difficulty**: `easy` | `medium` | `hard`

### Examples

```text
start user user
start bot:hard user
start user bot
start bot:easy bot:medium
```

