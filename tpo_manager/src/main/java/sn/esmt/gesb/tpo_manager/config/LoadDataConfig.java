package sn.esmt.gesb.tpo_manager.config;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import sn.esmt.gesb.tpo_manager.models.ConstantConfig;
import sn.esmt.gesb.tpo_manager.models.TPOData;
import sn.esmt.gesb.tpo_manager.models.TPOWorkOrder;
import sn.esmt.gesb.tpo_manager.repositories.ConstantConfigRepository;
import sn.esmt.gesb.tpo_manager.repositories.TPODataRepository;
import sn.esmt.gesb.tpo_manager.repositories.TPOWordOrderRepository;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class LoadDataConfig {

    private final TPODataRepository tpoDataRepository;
    private final TPOWordOrderRepository tpoWordOrderRepository;
    private final ConstantConfigRepository constantConfigRepository;

    @PostConstruct
    private void loadData() {
        log.info("Loading data...");
        tpoDataRepository.deleteAll();

        if (tpoDataRepository.count() == 0) {
            createTPOData();
        }

        if (constantConfigRepository.count() == 0) {
            createConstantConfig();
        }
    }

    private void createTPOData() {
        log.info("Creating TPOData...");

        TPOData tpoData = new TPOData();
        tpoData.setTpo("TPO_CREATE_SUBSCRIBER_PRE_PAID");
        tpoData.setDescription("Create a pre paid subscriber");
        tpoData.setVerb("ADD");
        tpoData.setTpoCondition("PRE_PAID");
        tpoData.setPatterns(List.of(
                TPOWorkOrder.builder()
                        .equipment("IN")
                        .template("<newConnectionRequest xmlns=\"http://esmt.sn/in_api/soam\">\n" +
                                "\t<name>${subscriberName}</name>\n" +
                                "        <phoneNumber>${phoneNumber}</phoneNumber>\n" +
                                "        <imsi>${imsi}</imsi>\n" +
                                "</newConnectionRequest>")
                        .webServiceName("newConnectionRequest")
                        .build(),
                TPOWorkOrder.builder()
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

    private void createConstantConfig() {
        log.info("Creating ConstantConfig...");
        ConstantConfig constantConfigIN = ConstantConfig
                .builder()
                .keyName("IN")
                .valueContent("http://localhost:8091/ws")
                .build();

        ConstantConfig constantConfigHLR = ConstantConfig
                .builder()
                .keyName("HLR")
                .valueContent("http://localhost:8092/ws")
                .build();
        constantConfigRepository.saveAll(List.of(constantConfigIN, constantConfigHLR));
    }
}
