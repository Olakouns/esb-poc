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
//        tpoDataRepository.deleteAll();
//        tpoWordOrderRepository.deleteAll();

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
                        .equipment("HLR")
                        .webServiceName("ActivateSubscriber")
                        .template("<activateSubscriberRequest xmlns=\"http://esmt.sn/hlr_api/soam\">\n" +
                                "    <name>${subscriberName}</name>\n" +
                                "    <phoneNumber>${phoneNumber}</phoneNumber>\n" +
                                "    <imsi>${imsi}</imsi>\n" +
                                "    <subscriberType>${subscriberType}</subscriberType>\n" +
                                "</activateSubscriberRequest>")
                        .tpoWorkOrderFailure(List.of(
                                TPOWorkOrder.builder()
                                        .equipment("HLR")
                                        .webServiceName("deactivateSubscriber")
                                        .template("<deactivateSubscriberRequest xmlns=\"http://esmt.sn/hlr_api/soam\" phoneNumber=\"${phoneNumber}\" />")
                                        .build()
                        ))
                        .build(),
                TPOWorkOrder.builder()
                        .equipment("HLR")
                        .webServiceName("ModifyService")
                        .isServiceTemplate(true)
                        .template("<modifyServiceSubscriberRequest xmlns=\"http://esmt.sn/hlr_api/soam\" phoneNumber=\"${phoneNumber}\">\n" +
                                "     <service>\n" +
                                "         <serviceType>${serviceType}</serviceType>\n" +
                                "         <targetNumber>${targetNumber}</targetNumber>\n" +
                                "         <active>${active}</active>\n" +
                                "     </service>\n" +
                                "</modifyServiceSubscriberRequest>")
                        .build(),
                TPOWorkOrder.builder()
                        .equipment("IN")
                        .template("<newConnectionRequest xmlns=\"http://esmt.sn/in_api/soam\">\n" +
                                "\t<name>${subscriberName}</name>\n" +
                                "        <phoneNumber>${phoneNumber}</phoneNumber>\n" +
                                "        <imsi>${imsi}</imsi>\n" +
                                "</newConnectionRequest>")
                        .webServiceName("newConnectionRequest")
                        .tpoWorkOrderFailure(List.of(
                                TPOWorkOrder.builder()
                                        .equipment("IN")
                                        .template("<terminationRequest phoneNumber=\"${phoneNumber}\" xmlns=\"http://esmt.sn/in_api/soam\"/>")
                                        .webServiceName("termination").build()
                        ))
                        .build(),
                TPOWorkOrder.builder()
                        .equipment("IN")
                        .webServiceName("recharging")
                        .template("<rechargingRequest xmlns=\"http://esmt.sn/in_api/soam\" phoneNumber=\"${phoneNumber}\">\n" +
                                "     <dataBalance>${dataBalance}</dataBalance>\n" +
                                "     <callBalance>${callBalance}</callBalance>\n" +
                                "     <smsBalance>${smsBalance}</smsBalance>\n" +
                                "</rechargingRequest>")
                        .build()
        ));
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
