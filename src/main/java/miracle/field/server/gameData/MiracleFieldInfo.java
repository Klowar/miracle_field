package miracle.field.server.gameData;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import miracle.field.shared.model.Word;

import java.util.*;

@Data
public class MiracleFieldInfo extends GameInfo {

    @JsonCreator
    public MiracleFieldInfo(
            @JsonProperty("openLetters") Set<Character> openLetters,
            @JsonProperty("changeTurnScore") int changeTurnScore,
            @JsonProperty("word") Word word
            ){
        this.openLetters = openLetters;
        this.changeTurnScore = changeTurnScore;
        this.word = word;
    }

    @JsonIgnore
    private Queue<Long> turnScore;

    private Word word;

    private Set<Character> openLetters;

    @JsonIgnore
    private Map<String, Long> playersScore;

    @JsonIgnore
    private boolean changeTurn;

    @JsonIgnore
    private String winner;

    @JsonIgnore
    private String currentPlayer;

    private int changeTurnScore;

    public MiracleFieldInfo(Set<String> playersTokens) {
        final int MAX_TURN_AMOUNT = 32;
        playersScore = new HashMap<>();
        turnScore = new PriorityQueue<>();

        for(String token : playersTokens)
            playersScore.put(token, 0L);
        for (int i = 0; i < MAX_TURN_AMOUNT; i++)
            turnScore.add((long) ((Math.random() * 12) + 1));

        this.winner = null;
        this.changeTurn = true;
    }

    public Word getWord() {
        return word;
    }

    @JsonIgnore
    public String getWordDescription() {
        return word.getDescription();
    }

    public Set<Character> getOpenLetters() {
        return openLetters;
    }

    public Map<String, Long> getPlayersScore() {
        return playersScore;
    }

    public String getWinner() {
        return winner;
    }


    public void setWord(Word word) {
        openLetters = new HashSet<>();
        this.word = word;
    }

    public void addLetter(Character s) {
        openLetters.add(s);
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


    public int getTurnScore() {
        return changeTurnScore;
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
