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
        caClientExample();
    }

    public boolean isCaAddressList() {
        return StringUtil.isNullOrEmpty(caAddressList);
    }

    public void caClientExample() {
        String pvName = JAVA_PV_PREFIX + "Integer";
        if (isCaAddressList())
            caAddressList = "";
        Properties properties = PVChannelFactory.setCaAddressList(caAddressList);
        PVChannelFactory pvChannelFactory = new PVChannelFactory(properties);
        Channel<Integer> integerChannel = pvChannelFactory.intChannel(pvName);
        try {
            pvChannelFactory.isConnect(integerChannel);
            log.info("PV Get ==> " + integerChannel.getName() + " " + pvChannelFactory.getValue(integerChannel));
            pvChannelFactory.printChannelInfo(integerChannel);
            integerChannel.close();
            pvChannelFactory.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}