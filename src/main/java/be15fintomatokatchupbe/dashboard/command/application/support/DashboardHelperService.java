package be15fintomatokatchupbe.dashboard.command.application.support;

import be15fintomatokatchupbe.dashboard.command.domain.aggregate.InfluencerTraffic;
import be15fintomatokatchupbe.dashboard.command.domain.aggregate.Traffic;
import be15fintomatokatchupbe.dashboard.command.domain.repository.InfluencerTrafficRepository;
import be15fintomatokatchupbe.dashboard.command.domain.repository.TrafficRepository;
import be15fintomatokatchupbe.relation.domain.PipelineInfluencerClientManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardHelperService {
    private final InfluencerTrafficRepository influencerTrafficRepository;
    private final TrafficRepository trafficRepository;

    @Transactional
    public void deleteInfluencerTrafficByPicmId(List<PipelineInfluencerClientManager> picmList){
        if (picmList == null || picmList.isEmpty()) return;

        influencerTrafficRepository.deleteAllByPipelineInfluencerClientManagerIn(picmList);
    }

    @Transactional
    public void assignRandomTrafficToPICM(List<PipelineInfluencerClientManager> picmList) {
        List<Traffic> trafficList = trafficRepository.findAllById(List.of(1L, 2L, 3L, 4L));
        Map<Long, Traffic> trafficMap = new HashMap<>();
        for (Traffic t : trafficList) {
            trafficMap.put(t.getTrafficId(), t);
        }

        List<InfluencerTraffic> trafficToSave = new ArrayList<>();

        for (PipelineInfluencerClientManager picm : picmList) {
            List<Double> percentages = generateRandomPercentages(4);

            for (int i = 0; i < 4; i++) {
                InfluencerTraffic traffic = InfluencerTraffic.builder()
                        .pipelineInfluencerClientManager(picm)
                        .traffic(trafficMap.get((long) (i + 1))) // traffic_id 1~4
                        .percentage(percentages.get(i))
                        .build();
                trafficToSave.add(traffic);
            }
        }

        influencerTrafficRepository.saveAll(trafficToSave);
    }

    private List<Double> generateRandomPercentages(int size) {
        Random random = new Random();
        List<Double> raw = new ArrayList<>();
        double sum = 0;

        for (int i = 0; i < size; i++) {
            double val = random.nextDouble(); // 0.0 ~ 1.0
            raw.add(val);
            sum += val;
        }

        List<Double> percentages = new ArrayList<>();
        double total = 0;
        for (int i = 0; i < size - 1; i++) {
            double pct = Math.round(raw.get(i) / sum * 1000.0) / 10.0; // 소수점 1자리
            percentages.add(pct);
            total += pct;
        }
        // 마지막 값은 100에서 나머지를 보정
        percentages.add(Math.round((100.0 - total) * 10.0) / 10.0);

        return percentages;
    }
}
