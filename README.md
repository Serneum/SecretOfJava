Secret of Java is an endless dungeon crawler that was written for a Game Programming class many years ago. Doors (openings in the wall)
are randomly generated and can appear in the middle of any of the four walls. The number of enemies, as well as the number of boxes you can push,
are also randomly generated. Every once in a while, the Mana Beast will appear on the far right side of the map and, when defeated, will reveal a
staircase to the next floor.

Sprites were taken from various sprite sheets on the internet. Some come from Sword of Mana, while others come from open source games.

Controls
--------
- WASD movement
- 1, 2, and 3 are spells. Click to use after pressing any of the buttons
- 4 and 5 use abilities in front of the player
- P to pause/unpause the game
- Esc to quit the game


Build
-----
You can build the jar by running
```
gradle jar
```

You can run the game by running
```
java -jar Secret-Of-Java.jar
```

Gradle places the jar in build/libs/
