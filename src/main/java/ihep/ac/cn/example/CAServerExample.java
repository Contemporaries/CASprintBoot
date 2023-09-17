package ihep.ac.cn.example;


import com.cosylab.epics.caj.cas.CAJServerContext;
import gov.aps.jca.CAException;
import ihep.ac.cn.entity.CAServerEntity;
import ihep.ac.cn.factory.PVFactory;
import ihep.ac.cn.factory.PVJsonFactory;
import ihep.ac.cn.pv.PV;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component
public class CAServerExample {

    @Resource
    private CAServerEntity caServer;

    @Resource
    private PVFactory pvFactory;

    @Resource
    private PVJsonFactory pvJsonFactory;

    @Value("${pv.tcpPort}")
    private int tcpPort;

    @Value("${pv.udpPort}")
    private int udpPort;

    public void start() {
        CAJServerContext context = new CAJServerContext();
        try {
            context.setTcpServerPort(tcpPort);
            context.setUdpServerPort(udpPort);
            context.initialize(caServer);
            printInfo(context);
            ArrayList<PV> pvs = pvFactory.createPVsByJson(pvJsonFactory.pvJsonToPV());
            log.info("PV Count: " + pvs.size());
            caServer.registerPVs(pvs);
            caServer.pvList();
            context.run(0);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                context.destroy();
            } catch (CAException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void printInfo(CAJServerContext context) {
        log.info("VERSION: " + context.getVersion().getVersionString());
        log.info("TCP PORT: " + context.getTcpServerPort());
        log.info("UDP PORT: " + context.getUdpServerPort());
        log.info("BEACON PORT: " + context.getBeaconPort());
        log.info("BEACON PERIOD: " + context.getBeaconPeriod());
        log.info("IGNORE ADDRESS LIST: " + context.getIgnoreAddressList());
        log.info("BEACON ADDRESS LIST: " + context.getBeaconAddressList());
        log.info("BEACON AUTO ADDRESS LIST: " + context.isAutoBeaconAddressList());
        log.info("MAX ARRAY BYTES: " + context.getMaxArrayBytes());
        log.info("STATE: " + (context.isInitialized() ? "INITIALIZED" : "NO_INITIALIZED"));
    }

}