package ihep.ac.cn.entity;

import org.epics.ca.Channel;
import org.epics.ca.Context;
import org.epics.ca.impl.ProtocolConfiguration;

import java.util.Properties;

public class PVChannelEntity {

    private static final Properties properties = new Properties();

    public static Properties withCaList(String caList) {
        properties.setProperty(ProtocolConfiguration.PropertyNames.EPICS_CA_ADDR_LIST.name(), caList);
        return properties;
    }

    public static Properties withCaListAndPort(String caList, int port) {
        withCaList(caList).setProperty(ProtocolConfiguration.PropertyNames.EPICS_CA_SERVER_PORT.name(), String.valueOf(port));
        return properties;
    }

    public static Properties withCaPort(int port) {
        properties.setProperty(ProtocolConfiguration.PropertyNames.EPICS_CA_SERVER_PORT.name(), String.valueOf(port));
        return properties;
    }

    public static <T> Channel<T> withGenericsChannel(Context context, String prefix, String pvName, Class<T> channelType) {
        return context.createChannel(setPvName(prefix, pvName), channelType);
    }

    public static <T> Channel<T> withGenericsChannel(Context context, String pvName, Class<T> channelType) {
        return context.createChannel(pvName, channelType);
    }

    protected static String setPvName(String prefix, String pvName) {
        return prefix + pvName;
    }

}
