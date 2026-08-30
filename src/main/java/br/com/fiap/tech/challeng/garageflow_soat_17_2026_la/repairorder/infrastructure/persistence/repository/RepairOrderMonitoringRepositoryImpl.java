package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.AverageWorkshopServiceExecutionTime;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderMonitoringRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.WorkshopServiceStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.document.AverageWorkshopServiceExecutionTimeDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

@Repository
@RequiredArgsConstructor
public class RepairOrderMonitoringRepositoryImpl
        implements RepairOrderMonitoringRepository {

    private static final String COLLECTION = "repair_orders";

    private final MongoTemplate mongoTemplate;

    @Override
    public List<AverageWorkshopServiceExecutionTime>
    findAverageWorkshopServiceExecutionTime() {

        Criteria criteria = new Criteria()
                .andOperator(
                        Criteria.where("workshopServices.status")
                                .is(WorkshopServiceStatus.FINISHED),
                        Criteria.where("workshopServices.durationInMinutes")
                                .ne(null)
                                .gt(0)
                );

        GroupOperation groupOperation =
                group("workshopServices.workshopServiceId")
                        .first("workshopServices.description")
                        .as("description")
                        .count()
                        .as("completedServices")
                        .avg("workshopServices.durationInMinutes")
                        .as("averageDurationInMinutes")
                        .min("workshopServices.durationInMinutes")
                        .as("minimumDurationInMinutes")
                        .max("workshopServices.durationInMinutes")
                        .as("maximumDurationInMinutes");

        ProjectionOperation projection =
                project()
                        .and("_id")
                        .as("workshopServiceId")
                        .and("description")
                        .as("description")
                        .and("completedServices")
                        .as("completedServices")
                        .and("averageDurationInMinutes")
                        .as("averageDurationInMinutes")
                        .and("minimumDurationInMinutes")
                        .as("minimumDurationInMinutes")
                        .and("maximumDurationInMinutes")
                        .as("maximumDurationInMinutes");

        Aggregation aggregation =
                newAggregation(
                        unwind("workshopServices"),
                        match(criteria),
                        groupOperation,
                        projection,
                        sort(Sort.Direction.ASC, "description")
                );

        AggregationResults<AverageWorkshopServiceExecutionTimeDocument> results =
                mongoTemplate.aggregate(
                        aggregation,
                        COLLECTION,
                        AverageWorkshopServiceExecutionTimeDocument.class
                );

        return results.getMappedResults()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private AverageWorkshopServiceExecutionTime toDomain(
            AverageWorkshopServiceExecutionTimeDocument document) {

        return AverageWorkshopServiceExecutionTime.builder()
                .workshopServiceId(
                        document.getWorkshopServiceId()
                )
                .description(
                        document.getDescription()
                )
                .completedServices(
                        document.getCompletedServices()
                )
                .averageDurationInMinutes(
                        BigDecimal.valueOf(
                                document.getAverageDurationInMinutes()
                        ).setScale(
                                2,
                                RoundingMode.HALF_UP
                        )
                )
                .minimumDurationInMinutes(
                        document.getMinimumDurationInMinutes()
                )
                .maximumDurationInMinutes(
                        document.getMaximumDurationInMinutes()
                )
                .build();
    }
}