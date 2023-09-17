package ihep.ac.cn.factory;

import gov.aps.jca.dbr.*;
import ihep.ac.cn.config.PVJson;
import ihep.ac.cn.entity.CAServerEntity;
import ihep.ac.cn.pv.PV;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PVFactory {

    @Resource
    private CAServerEntity caServer;

    public ArrayList<PV> createPVsByJson(List<PVJson> pvJsons) {
        ArrayList<PV> result = new ArrayList<>();
        pvJsons.forEach(pvJson -> {
            String type = pvJson.getType();
            String name = pvJson.getName();
            switch (type) {
                case "int": {
                    PV pv = caServer.createPV(name, DBR_Int.TYPE, new int[]{0});
                    initPVParams(pv, pvJson);
                    result.add(pv);
                    break;
                }
                case "double": {
                    PV pv = caServer.createPV(name, DBR_Double.TYPE, new double[]{0});
                    initPVParams(pv, pvJson);
                    result.add(pv);
                    break;
                }
                case "short": {
                    PV pv = caServer.createPV(name, DBR_Short.TYPE, new int[]{0});
                    initPVParams(pv, pvJson);
                    result.add(pv);
                    break;
                }
                case "str": {
                    PV pv = caServer.createPV(name, DBR_String.TYPE, new String[]{"0"});
                    initPVParams(pv, pvJson);
                    result.add(pv);
                    break;
                }
                case "float": {
                    PV pv = caServer.createPV(name, DBR_Float.TYPE, new float[]{0});
                    pv.write(new DBR_Float(new float[]{(float) 100.1}), null);
                    initPVParams(pv, pvJson);
                    result.add(pv);
                    break;
                }
                default:
                    try {
                        throw new ClassNotFoundException(name + " not support");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }

        });
        return result;
    }

    protected void initPVParams(PV pv, PVJson pvJson) {
        pv.setLowerAlarmLimit(pvJson.getHigh());
        pv.setLowerWarningLimit(pvJson.getHihi());
        pv.setUpperAlarmLimit(pvJson.getHigh());
        pv.setUpperWarningLimit(pvJson.getHihi());
        pv.setPrecision(pvJson.getPrecision());
        pv.setLowerCtrlLimit(pvJson.getHihi());
        pv.setUpperCtrlLimit(pvJson.getHihi());
        pv.setLowerDispLimit(pvJson.getHigh());
        pv.setUpperDispLimit(pvJson.getHigh());
        pv.setUnits(pvJson.getUnit());
    }

}
