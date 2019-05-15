package miracle.field.server.gameData;

import miracle.field.shared.model.Word;

import java.util.*;

public class MiracleFieldInfo extends GameInfo {

    private Queue<Long> turnScore;
    private Word word;
    private char[] openLetters;
    private Map<String, Long> playersScore;
    private boolean changeTurn;

    private String winner;
    private String currentPlayer;

    public MiracleFieldInfo(Set<String> playersTokens) {
        final int MAX_TURN_AMOUNT = 32;
        playersScore = new HashMap<>();
        turnScore = new PriorityQueue<>();

        for(String token : playersTokens)
            playersScore.put(token, 0L);
        for (int i = 0; i < MAX_TURN_AMOUNT; i++)
            turnScore.add((long) ((Math.random() * 16) + 1));

        this.winner = null;
        this.changeTurn = true;
    }

    public String getWord() {
        return word.getWord();
    }

    public String getWordDescription() {
        return word.getDescription();
    }

    public char[] getOpenLetters() {
        return openLetters;
    }

    public Map<String, Long> getPlayersScore() {
        return playersScore;
    }

    public String getWinner() {
        return winner;
    }


    public void setWord(Word word) {
        openLetters = new char[word.getWord().length()];
        this.word = word;
    }

    public void setOpenLetters(char[] openLetters) {
        this.openLetters = openLetters;
    }

    public void setPlayersScore(Map<String, Long> playersScore) {
        this.playersScore = playersScore;
    }

    public void updatePlayerScore(String token, Long score) {
        playersScore.put(token,
                playersScore.get(token) + score
        );
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Long getTurnScore() {
        return turnScore.peek();
    }

    public Long getChangeTurnScore() {
        return turnScore.poll();
    }

    public boolean isChangeTurn() {
        return changeTurn;
    }

    public void setChangeTurn(boolean changeTurn) {
        this.changeTurn = changeTurn;
    }
}
