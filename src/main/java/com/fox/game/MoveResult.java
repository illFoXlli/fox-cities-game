package com.fox.game;

public record MoveResult(
        MoveStatus status,
        String message,
        String userCity,
        String computerCity,
        boolean gameOver
) {
}
