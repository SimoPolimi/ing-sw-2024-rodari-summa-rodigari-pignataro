package it.polimi.ingsw.gc42.network.messages;

public enum MessageType {
    START_GAME,
    SET_PLAYER_STATUS,
    NEW_GAME,
    GRAB_CARD,
    PLAY_CARD,
    KICK_PLAYER,
    NEXT_TURN,
    ADD_PLAYER,
    DRAW_CARD,
    SET_NAME,
    SET_PLAYER_SECRET_OBJECTIVE,
    SET_PLAYER_STARTER_CARD,
    SET_PLAYER_TOKEN,
    FLIP_CARD,
    FLIP_STARTER_CARD,
    SET_CURRENT_STATUS,
    SEND_MESSAGE,

    GET_AVAILABLE_GAMES,
    GET_DECK,
    GET_PLAYER_TURN,
    GET_NUMBER_OF_PLAYERS,
    GET_SLOT_CARD,
    GET_SECRET_OBJECTIVE,
    GET_TEMPORARY_OBJECTIVE_CARDS,
    GET_TEMPORARY_STARTER_CARD,
    GET_PLAYER_STATUS,
    GET_PLAYERS_INFO,
    GET_PLAYERS_HAND_SIZE,
    IS_PLAYER_FIRST,
    GET_AVAILABLE_PLACEMENT,
    CAN_CARD_BE_PLAYED,
    GET_PLAYER_TOKEN,
    GET_PLAYERS_LAST_PLAYED_CARD,
    GET_PLAYERS_HAND_CARD,
    GET_PLAYERS_PLAY_FIELD,
    GET_INDEX_OF_PLAYER,
    GET_FULL_CHAT,
    CHECK_NICKNAME,
    BLOCK_NICKNAME,

    SHOW_SECRET_OBJECTIVES_SELECTION_DIALOG,
    SHOW_STARTER_CARD_SELECTION_DIALOG,
    SHOW_TOKEN_SELECTION_DIALOG,
    ASK_TO_DRAW_OR_GRAB,

    // Notify,
    NOTIFY_GAME_IS_STARTING,
    NOTIFY_DECK_CHANGED,
    NOTIFY_SLOT_CARD_CHANGED,
    NOTIFY_NUMBER_OF_PLAYERS_CHANGED,
    NOTIFY_PLAYERS_TOKEN_CHANGED,
    NOTIFY_PLAYERS_PLAY_AREA_CHANGED,
    NOTIFY_PLAYERS_HAND_CHANGED,
    NOTIFY_HAND_CARD_WAS_FLIPPED,
    NOTIFY_PLAYERS_OBJECTIVE_CHANGED,
    NOTIFY_COMMON_OBJECTIVES_CHANGED,
    NOTIFY_TURN_CHANGED,
    NOTIFY_PLAYERS_POINTS_CHANGED,
    NOTIFY_NEW_MESSAGE,
    GET_READY,

    NOTIFY_LAST_TURN,
    NOTIFY_END_GAME,


    ADD_VIEW,

    CLIENT_STATE,

    DISCONNECT_PLAYER,
    REJOIN_GAME

}
