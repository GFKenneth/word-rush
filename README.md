# Word Rush: Timed Word Game in Java

A timed word game with a Swing GUI, built from scratch in Java. The game logic runs on **custom data structures** instead of the standard library: a hash map, a hash set, and a trie.

## Features

- Timed gameplay: find as many valid words as you can before the clock runs out.
- Custom `MiHashMap` and `MiHashSet`, implemented from scratch (no `HashMap` or `HashSet`).
- Trie (prefix tree) for fast word validation and prefix lookups.
- Dictionary loading, random letter sequence generation, and word validation.
- Game statistics that persist between sessions.
- Graphical interface built with Java Swing, with a setup screen and a game screen.

## Project Structure

```
.
├── README.md
└── src/
    ├── palabrascontrarreloj/   # Game logic and data structures
    │   ├── MiHashMap.java
    │   ├── MiHashSet.java
    │   ├── Trie.java
    │   ├── NodoTrie.java
    │   ├── CargadorDiccionario.java
    │   ├── GeneradorSecuencia.java
    │   ├── Validador.java
    │   ├── Partida.java
    │   ├── Estadisticas.java
    │   ├── PersistenciaEstadisticas.java
    │   └── Main.java
    └── gui/                    # Swing interface
        ├── MainGUI.java
        ├── VentanaPrincipal.java
        ├── PanelSetup.java
        ├── PanelJuego.java
        └── Colores.java
```

| Component | Description |
|---|---|
| `MiHashMap`, `MiHashSet` | Hash-based map and set written from scratch |
| `Trie`, `NodoTrie` | Prefix tree used to store the dictionary and validate words |
| `CargadorDiccionario` | Loads the word list into the trie |
| `GeneradorSecuencia` | Generates the letter sequences for each round |
| `Validador` | Checks whether a submitted word is valid |
| `Partida` | Manages the state and timing of a game |
| `Estadisticas`, `PersistenciaEstadisticas` | Tracks and saves player statistics |
| `gui/*` | Swing windows and panels |


## Build and Run

From the project root:

```bash
javac -d bin src/gui/*.java src/palabrascontrarreloj/*.java
java -cp bin gui.MainGUI
```
> **Tiling window managers (i3, sway, Hyprland, etc.:** if the window appears blank, run with `_JAVA_AWT_WM_NONREPARENTING=1 java -cp bin gui.MainGUI`.


To run the console version instead:

```bash
java -cp bin palabrascontrarreloj.Main
```

## Technical Notes

- Word validation and prefix checks run in O(L), where L is the length of the word, thanks to the trie.


Kenneth Galván Flores