package ihep.ac.cn.example;


import ihep.ac.cn.factory.PVChannelFactory;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.epics.ca.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;


@Slf4j
@Component
public class CAClientExample {

    private static final String JAVA_PV_PREFIX = "JAVA:PV:";

    @Value("${pv.caAddressList}")
    private String caAddressList;

    public void start() {
        clientExample();
    }

    public boolean isCaAddressList() {
        return StringUtil.isNullOrEmpty(caAddressList);
    }

    public void clientExample() {
        String pvName = JAVA_PV_PREFIX + "Integer";
        if (isCaAddressList())
            caAddressList = "";
        Properties properties = PVChannelFactory.setCaAddressList(caAddressList);
        PVChannelFactory intChannel = new PVChannelFactory(pvName, properties);
        Channel<Integer> integerChannel = intChannel.intChannel();
        try {
            intChannel.isConnect(integerChannel);
            log.info("PV Get ==> " + integerChannel.getName() + " " + intChannel.getValue(integerChannel));
            intChannel.printChannelInfo(integerChannel);
            integerChannel.close();
            intChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}