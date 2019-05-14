package miracle.field.server.gameData;

import miracle.field.shared.model.Word;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MiracleFieldInfo extends GameInfo {

    private final int PLAYERS_COUNT;

    private Word word;
    private char[] openLetters;
    private Map<String, Long> playersScore;
    private boolean changeTurn;

    private String winner;
    private String currentPlayer;

    public MiracleFieldInfo(Set<String> playersTokens) {
        PLAYERS_COUNT = playersTokens.size();

        playersScore = new HashMap<>();
        for(String token : playersTokens) {
            playersScore.put(token, 0L);
        }

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

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isChangeTurn() {
        return changeTurn;
    }

    public void setChangeTurn(boolean changeTurn) {
        this.changeTurn = changeTurn;
    }
}
