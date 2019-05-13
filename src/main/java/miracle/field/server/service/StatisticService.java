package miracle.field.server.service;

import miracle.field.shared.model.Statistic;
import miracle.field.shared.model.User;

public interface StatisticService {
    void updateUserStatistic(Statistic statistic);
    Statistic getUserStatistic(Long id);
}
