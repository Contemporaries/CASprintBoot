package ihep.ac.cn.factory;

import gov.aps.jca.dbr.*;
import ihep.ac.cn.entity.CAServerEntity;
import ihep.ac.cn.pv.PV;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class PVFactory {

    @Resource
    private CAServerEntity caServer;

    public ArrayList<PV> createPVs(HashMap<String, Object> list) {
        ArrayList<PV> result = new ArrayList<>();
        list.forEach((k, v) -> {
            String name = v.getClass().getName();
            switch (name) {
                case "java.lang.Integer":
                    int intValue = (int) v;
                    result.add(caServer.createPV(k, DBR_Int.TYPE, new int[]{intValue}));
                    break;
                case "java.lang.String":
                    String strValue = (String) v;
                    result.add(caServer.createPV(k, DBR_String.TYPE, new String[]{strValue}));
                    break;
                case "java.lang.Double":
                    double douValue = (double) v;
                    result.add(caServer.createPV(k, DBR_Double.TYPE, new double[]{douValue}));
                    break;
                case "java.lang.Float":
                    float floatValue = (float) v;
                    result.add(caServer.createPV(k, DBR_Float.TYPE, new float[]{floatValue}));
                    break;
                case "java.lang.Short":
                    short shortValue = (short) v;
                    result.add(caServer.createPV(k, DBR_Short.TYPE, new short[]{shortValue}));
                    break;
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

}
