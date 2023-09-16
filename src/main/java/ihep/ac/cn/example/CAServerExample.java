package ihep.ac.cn.example;


import com.cosylab.epics.caj.cas.CAJServerContext;
import gov.aps.jca.CAException;
import ihep.ac.cn.config.PVJson;
import ihep.ac.cn.entity.CAServerEntity;
import ihep.ac.cn.factory.PVFactory;
import ihep.ac.cn.factory.PVJsonFactory;
import ihep.ac.cn.pv.PV;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

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
            context.printInfo();
            ArrayList<PV> pvs = pvFactory.createPVsByJson(pvJsonFactory.pvJsonToPV());
            System.out.println("pvs size: " + pvs.size());
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
}