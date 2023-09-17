package ihep.ac.cn.factory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.epics.ca.Channel;
import org.epics.ca.Context;
import org.epics.ca.impl.ProtocolConfiguration;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Component
@NoArgsConstructor
public class PVChannelFactory {


    @Getter
    private final Properties properties = new Properties();
    @Setter
    @Getter
    private Context context;

    @Setter
    @Getter
    private String pvName;

    public PVChannelFactory(String pvName) {
        this.pvName = pvName;
        this.context = new Context(properties);
    }

    public PVChannelFactory setProperties(String caPort, String caAddressList) {
        properties.setProperty(ProtocolConfiguration.PropertyNames.EPICS_CA_REPEATER_PORT.name(), caPort);
        properties.setProperty(ProtocolConfiguration.PropertyNames.EPICS_CA_ADDR_LIST.name(), caAddressList);
        return this;
    }

    public PVChannelFactory setCaPort(String caPort) {
        properties.setProperty(ProtocolConfiguration.PropertyNames.EPICS_CA_REPEATER_PORT.name(), caPort);
        return this;
    }

    public PVChannelFactory setCaAddressList(String caAddressList) {
        properties.setProperty(ProtocolConfiguration.PropertyNames.EPICS_CA_ADDR_LIST.name(), caAddressList);
        return this;
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

    public void close() {
        context.close();
    }

}
