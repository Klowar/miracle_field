package miracle.field.server.service;

import miracle.field.server.repository.WordRepository;
import miracle.field.shared.model.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {

    private WordRepository wordRepository;

    @Autowired
    public RoomServiceImpl(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Override
    public Word generateWord() {
        Word generatedWord = wordRepository.getRandomWord();
        return generatedWord;
    }
}
