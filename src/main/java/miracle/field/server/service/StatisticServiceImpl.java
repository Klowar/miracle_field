package miracle.field.server.service;

import miracle.field.server.repository.StatisticRepository;
import miracle.field.shared.model.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatisticServiceImpl implements StatisticService {

    private StatisticRepository statisticRepository;

    @Autowired
    public StatisticServiceImpl(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @Override
    public void updateUserStatistic(Statistic userStatistic) {
        statisticRepository.save(userStatistic);
    }

    @Override
    public Statistic getUserStatistic(Long id) {
        Optional<Statistic> statisticCandidate = statisticRepository.findById(id);
        if (statisticCandidate.isPresent()) {
            return statisticCandidate.get();
        } else {
            throw new DataIntegrityViolationException("No such statistic");
        }
    }
}
