package miracle.field.server.repository;

import miracle.field.shared.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM words ORDER BY random() LIMIT 1")
    Word getRandomWord();

}
