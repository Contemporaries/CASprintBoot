package ihep.ac.cn.factory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.epics.ca.Channel;
import org.epics.ca.Context;
import org.epics.ca.impl.ProtocolConfiguration;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Slf4j
public class PVChannelFactory {


    @Setter
    @Getter
    private Context context;

    @Setter
    @Getter
    private String pvName;

    public PVChannelFactory(String pvName, Properties properties) {
        this.pvName = pvName;
        this.context = new Context(properties);
    }

    public static Properties setProperties(String caPort, String caAddressList) {
        Properties properties = new Properties();
        properties.setProperty(ProtocolConfiguration.PropertyNames.EPICS_CA_REPEATER_PORT.name(), caPort);
        properties.setProperty(ProtocolConfiguration.PropertyNames.EPICS_CA_ADDR_LIST.name(), caAddressList);
        return properties;
    }

    public static Properties setCaPort(String caPort) {
        Properties properties = new Properties();
        properties.setProperty(ProtocolConfiguration.PropertyNames.EPICS_CA_REPEATER_PORT.name(), caPort);
        return properties;
    }

    public static Properties setCaAddressList(String caAddressList) {
        Properties properties = new Properties();
        properties.setProperty(ProtocolConfiguration.PropertyNames.EPICS_CA_ADDR_LIST.name(), caAddressList);
        return properties;
    }

    public Channel<Integer> intChannel() {
        return context.createChannel(pvName, Integer.class);
    }

    public Channel<Double> doubleChannel() {
        return context.createChannel(pvName, Double.class);
    }

    public Channel<Float> floatChannel() {
        return context.createChannel(pvName, Float.class);
    }

    public Channel<String> stringChannel() {
        return context.createChannel(pvName, String.class);
    }

    public Channel<Short> shortChannel() {
        return context.createChannel(pvName, Short.class);
    }

    public Channel<Byte> byteChannel() {
        return context.createChannel(pvName, Byte.class);
    }

    public <T> T getValue(Channel<T> channelType) throws ExecutionException, InterruptedException {
        return channelType.getAsync().get();
    }

    public <T> void isConnect(Channel<T> channel) throws ExecutionException, InterruptedException {
        String connectState = channel.connectAsync().get().getConnectionState().name();
        log.info("PV CA Client connectivity state: " + connectState);
        boolean connected = connectState.equals("CONNECTED");
        if (!connected)
            channel.close();
    }

    public void close() {
        context.close();
    }

    public <T> void printChannelInfo(Channel<T> channel) {
        channel.getProperties().forEach((k, v) -> log.info(k + ": " + v));
    }

}
