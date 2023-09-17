package ihep.ac.cn.example;


import ihep.ac.cn.entity.PVChannelEntity;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.epics.ca.Channel;
import org.epics.ca.Context;
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
        if (StringUtil.isNullOrEmpty(caAddressList)) {
            caAddressList = "";
        }
        Properties withCaList = PVChannelEntity.withCaList(caAddressList);
        Context context = new Context(withCaList);
        try {
            Channel<Double> doubleChannel = PVChannelEntity
                    .withGenericsChannel(context, JAVA_PV_PREFIX, "Double", Double.class);
            String connectState = doubleChannel.connectAsync().get().getConnectionState().name();
            if (!connectState.equals("CONNECTED")) {
                log.info("PV CA Client connectivity state: " + connectState);
                doubleChannel.close();
                context.close();
            }
            log.info("PV Get ==> " + doubleChannel.getName() + " " + doubleChannel.getAsync().get());
            doubleChannel.getProperties().forEach((k, v) -> log.info(k + ": " + v));
            doubleChannel.close();
            context.close();
        } catch (Exception e) {
            e.printStackTrace();
            context.close();
        }
    }

}