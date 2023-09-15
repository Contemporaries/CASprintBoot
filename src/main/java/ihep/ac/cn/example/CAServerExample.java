package ihep.ac.cn.example;


import com.cosylab.epics.caj.cas.CAJServerContext;
import gov.aps.jca.CAException;
import ihep.ac.cn.entity.CAServerEntity;
import ihep.ac.cn.factory.PVFactory;
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
            HashMap<String, Object> pvList = new HashMap<>();
            pvList.put("PV:STRING", "str value");
            pvList.put("PV:DOUBLE", 1.01);
            pvList.put("PV:FLOAT", (float) 1.21);
            pvList.put("PV:SHORT", (short) 45);
            pvList.put("PV:INT", 824);
            ArrayList<PV> pvs = pvFactory.createPVs(pvList);
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