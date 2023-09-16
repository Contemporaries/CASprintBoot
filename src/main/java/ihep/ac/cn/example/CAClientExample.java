package ihep.ac.cn.example;


import ihep.ac.cn.entity.PVChannelEntity;
import io.netty.util.internal.StringUtil;
import org.epics.ca.Channel;
import org.epics.ca.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;


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
                System.out.println(connectState);
                doubleChannel.close();
                context.close();
            }
            System.out.println(doubleChannel.getAsync().get());
            doubleChannel.getProperties().forEach((k, v) -> System.out.println(k + "  " + v));
            doubleChannel.close();
            context.close();
        } catch (Exception e) {
            e.printStackTrace();
            context.close();
        }
    }

}