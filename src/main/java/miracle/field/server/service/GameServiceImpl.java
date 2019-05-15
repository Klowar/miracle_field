package miracle.field.server.service;

import com.sun.xml.fastinfoset.util.CharArray;
import com.sun.xml.fastinfoset.util.CharArrayArray;
import miracle.field.server.gameData.MiracleFieldInfo;
import miracle.field.server.repository.UserRepository;
import miracle.field.server.repository.WordRepository;
import miracle.field.shared.model.Statistic;
import miracle.field.shared.model.Word;
import org.hibernate.type.CharacterType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class GameServiceImpl implements GameService<MiracleFieldInfo> {

    private WordRepository wordRepository;
    private StatisticService statisticService;
    private UserService userService;

    @Autowired
    public GameServiceImpl(WordRepository wordRepository,
                           StatisticService statisticService, UserService userService) {
        this.wordRepository = wordRepository;
        this.statisticService = statisticService;
        this.userService = userService;
    }


    @Override
    public void startGame(Collection<String> players,
                          MiracleFieldInfo gameInfo) {
        gameInfo.setCurrentPlayer(players.iterator().next());
        Word word = wordRepository.getRandomWord();
        gameInfo.setWord(word);
    }

    @Override
    public void finishedGame(MiracleFieldInfo gameInfo) {
        Long id = userService.getAuthorizedUser(gameInfo.getWinner()).getId();
        Statistic statistic = statisticService.getUserStatistic(id);
        statistic.setScore(
                statistic.getScore() + gameInfo.getPlayersScore().get(gameInfo.getWinner())
        );
        statisticService.updateUserStatistic(
                statistic
        );
    }

    @Override
    public void gameMove(MiracleFieldInfo gameInfo, String player, String data) {
        String currentPlayer = gameInfo.getCurrentPlayer();
        if (!currentPlayer.equals(player))
            return;

        Set<Character> word = gameInfo.getOpenLetters();

        if (!word.contains(data) && gameInfo.getWord().contains(data)) {
            gameInfo.setChangeTurn(false);
            stringToSet(data, gameInfo.getOpenLetters());
            gameInfo.updatePlayerScore(currentPlayer, gameInfo.getChangeTurnScore());
        }

        if (stringToSet(gameInfo.getWord(), null).equals(word) ||
            Arrays.equals(gameInfo.getWord().toCharArray(), data.toCharArray())) {
            gameInfo.setWinner(player);
        }

    }

    private Set<Character> stringToSet(String word, @Nullable Set<Character> resultSet) {
        if(resultSet == null) {
            resultSet = new HashSet<>();
        }

        for(int i = 0; i < word.length(); i ++) {
            resultSet.add(word.charAt(i));
        }
        return resultSet;
    }

}