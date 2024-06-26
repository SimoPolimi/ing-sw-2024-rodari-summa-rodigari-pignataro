package it.polimi.ingsw.gc42.controller;

/**
 * Enum representing the various statuses of the game and the Players
 */
public enum GameStatus {
    // TODO: check
    /**
    * Waiting for the Players to join the Game
    */
    WAITING_FOR_PLAYERS,
    /**
     * Player is not in the game
     */
    NOT_IN_GAME,
    /**
     * Player is connecting to the game
     */
    CONNECTING,
    /**
     * Players are waiting for the host to start the Game
     */
    WAITING_FOR_SERVER,
    /**
     * Player is ready to start the Game
     */
    READY,
    /**
     * Player is ready to choose his token
     */
    READY_TO_CHOOSE_TOKEN,
    /**
     * Player is ready to choose his secret objective
     */
    READY_TO_CHOOSE_SECRET_OBJECTIVE,
    /**
     * Player is ready to choose his starter card
     */
    READY_TO_CHOOSE_STARTER_CARD,
    /**
     * Player is ready to draw his starting hand
     */
    READY_TO_DRAW_STARTING_HAND,
    /**
     * Player is ready to play the Game
     */
    READY_TO_PLAY,
    /**
     * Player is playing
     */
    PLAYING,
    /**
     * It's Player's turn
     */
    MY_TURN,
    /**
     * It's not Player's turn
     */
    NOT_MY_TURN,
    /**
     * It's the semi last turn
     */
    SEMI_LAST_TURN,
    /**
     * It's the last turn
     */
    LAST_TURN,
    /**
     * Calculating points of every Player
     */
    COUNTING_POINTS,
    /**
     * Game is ended
     */
    END_GAME
}
