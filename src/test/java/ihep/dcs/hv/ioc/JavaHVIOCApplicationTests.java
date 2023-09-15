package ihep.dcs.hv.ioc;

import org.epics.ca.impl.ProtocolConfiguration;
import org.junit.jupiter.api.Test;

class JavaHVIOCApplicationTests {

    @Test
    void contextLoads() {

    }

    @Test
    void  epics() {
        System.out.println("Test");
        System.out.println(ProtocolConfiguration.PropertyNames.EPICS_CA_ADDR_LIST);
    }

}
