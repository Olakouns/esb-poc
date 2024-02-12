package sn.esmt.gesb.tpo_manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sn.esmt.gesb.tpo_manager.builder.SoapRequestBuilder;

@SpringBootTest
class TpoManagerApplicationTests {

    @Test
    void contextLoads() {
        String soapRequest = "<esbRootActionRequest xmlns=\"http://esmt.sn/gesb/soam\" requestId=\"1235478\">\n" +
                "            <esbContent verb=\"ADD\">\n" +
                "                <esbParameter name=\"ola\" newValue=\"HI\"/>\n" +
                "            </esbContent>\n" +
                "        </esbRootActionRequest>";
        System.out.println(SoapRequestBuilder.buildSoapRequest(soapRequest));
    }

}
