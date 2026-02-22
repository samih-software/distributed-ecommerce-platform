package com.example.fastdelivery.sheduler;

import com.example.fastdelivery.sheduler.DeliveryAssignmentService;
import com.example.fastdelivery.entities.DeliveryPendingEntity;
import com.example.fastdelivery.repositories.PendingDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class NightlyAssignmentJob {

    private final PendingDeliveryRepository pendingRepo;
    private final DeliveryAssignmentService assignmentService;

    @Scheduled(cron = "0 0 0 * * *")
    public void assignDeliveries() {

        LocalDate targetDate = LocalDate.now().plusDays(1);

        List<DeliveryPendingEntity> pending =
                pendingRepo.findByKeyScheduledDate(targetDate);

        assignmentService.assign(pending, targetDate);
    }
}
