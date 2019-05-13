package miracle.field.server.service;

import miracle.field.server.repository.UserRepository;
import miracle.field.server.repository.WordRepository;
import miracle.field.shared.model.Word;
import miracle.field.shared.packet.Packet;
import org.java_websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GameServiceImpl implements GameService {

    private WordRepository wordRepository;
    private StatisticService statisticService;
    private UserRepository userRepository;

    @Autowired
    public GameServiceImpl(WordRepository wordRepository,
                           StatisticService statisticService, UserRepository userRepository) {
        this.wordRepository = wordRepository;
        this.statisticService = statisticService;
        this.userRepository = userRepository;
    }


    @Override
    public void startGame(Map<String, WebSocket> players) {
//      ToDo: something else??
        Word word = wordRepository.getRandomWord();
    }

    @Override
    public void finishedGame() {
    }

    @Override
    public void gameMove(Packet packet) {
    }
}