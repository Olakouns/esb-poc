package sn.esmt.gesb.tpo_manager.config;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import sn.esmt.gesb.tpo_manager.models.TPOData;
import sn.esmt.gesb.tpo_manager.models.TPOWordOrder;
import sn.esmt.gesb.tpo_manager.repositories.TPODataRepository;
import sn.esmt.gesb.tpo_manager.repositories.TPOWordOrderRepository;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class LoadDataConfig {

    private final TPODataRepository tpoDataRepository;
    private final TPOWordOrderRepository tpoWordOrderRepository;

    @PostConstruct
    private void loadData() {
        log.info("Loading data...");
        if (tpoDataRepository.count() == 0) {
            createTPOData();
        }
        if (tpoWordOrderRepository.count() == 0) {
            createTPOWordOrder();
        }
    }

    private void createTPOData() {
        log.info("Creating TPOData...");
        TPOData tpoOnFailure = new TPOData();
        tpoOnFailure.setTpo("TPO_CREATE_SUBSCRIBER_PRE_PAID_FAILURE");
        tpoOnFailure.setDescription("Delete a post paid subscriber");
        tpoOnFailure.setVerb("DELETE");
        tpoOnFailure.setPatterns(List.of(TPOWordOrder.builder()
                .equipment("HLR")
                .template("<subscriber><msisdn>${imsi}</msisdn></subscriber>")
                .webServiceName("DeleteSubscriber")
                .build()));


        TPOData tpoData = new TPOData();
        tpoData.setTpo("TPO_CREATE_SUBSCRIBER_POST_PAID");
        tpoData.setDescription("Create a post paid subscriber");
        tpoData.setVerb("ADD");
        tpoData.setTpoCondition("POST_PAID");
        tpoData.setTpoDataOnFailure(tpoOnFailure);
        tpoData.setPatterns(List.of(
                TPOWordOrder.builder()
                        .equipment("HLR")
                        .template("<subscriber>\n" +
                                "\t<name>${subscriberName}</name>\n" +
                                "\t<phone>${phoneNumber}</phone>\n" +
                                "\t<type>${subscriberType}</phone>\n" +
                                "\t<imsi>${imsi}</imsi>\n" +
                                "</subscriber>")
                        .webServiceName("ActivateSubscriber")
                        .build(),
                TPOWordOrder.builder()
                        .equipment("HLR")
                        .template("<serviceRoot phone=\"${phoneNumber}\" imsi=\"${imsi}\">\n" +
                                "\t\t<service>\n" +
                                "\t\t\t\t<name>${serviceName}</name>\n" +
                                "\t\t\t\t<activate>${activate}</activate>\n" +
                                "\t\t\t\t<target>${targetNumber}</target>\n" +
                                "\t\t</service>\n" +
                                "</serviceRoot>\n")
                        .webServiceName("ModifyService")
                        .build()));
        tpoDataRepository.save(tpoData);
    }

    private void createTPOWordOrder() {
        log.info("Creating TPOWordOrder...");
    }
}
