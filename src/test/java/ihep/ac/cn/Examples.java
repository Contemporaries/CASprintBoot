package ihep.ac.cn;

import ihep.ac.cn.config.PVJson;
import ihep.ac.cn.factory.PVJsonFactory;
import jakarta.annotation.Resource;
import org.epics.ca.impl.ProtocolConfiguration;
import org.junit.jupiter.api.Test;

import java.util.List;

class Examples {

    @Resource
    private PVJsonFactory pvJsonFactory;

    @Test
    void contextLoads() {

    }

    @Test
    void  epics() {
        System.out.println("Test");
        System.out.println(ProtocolConfiguration.PropertyNames.EPICS_CA_ADDR_LIST);
    }

    @Test
    void printDBJson() {
        List<PVJson> pvJsons = pvJsonFactory.pvJsonToPV();
        pvJsons.forEach(System.out::println);
    }

}
