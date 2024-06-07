package com.madadipouya.springkafkatest.kafka.processor;

import com.madadipouya.springkafkatest.dto.UserPoint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class  PointsAccumulator {
    private Long count;
    private Double sum;

    public PointsAccumulator(Long count, Double sum) {
        this.count = count;
        this.sum = sum;
    }

    public PointsAccumulator add(UserPoint userPoint) {
        count++;
        sum += userPoint.getPoint();
        return this;
    }

    public Double calculateAverage() {
        if (count == 0) {
            return 0.0;
        }
        return sum / count;
    }
}
