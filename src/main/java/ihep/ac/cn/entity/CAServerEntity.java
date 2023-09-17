package ihep.ac.cn.entity;

import gov.aps.jca.CAException;
import gov.aps.jca.CAStatus;
import gov.aps.jca.CAStatusException;
import gov.aps.jca.cas.*;
import gov.aps.jca.dbr.DBRType;
import ihep.ac.cn.pv.PV;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class CAServerEntity implements Server {
    protected final Map<String, PV> pvs = new HashMap<>();

    public PV createPV(String name, DBRType type, Object initialValue) {
        PV pv = new PV(name, null, type, initialValue);
        registerPV(name, pv);
        return pv;
    }

    public void pvList() {
        pvs.forEach((k, v) -> {
            DBRType type = v.getType();
            if (type.isINT()) {
                int[] value = (int[]) v.getValue();
                log.info(v.getName() + " " + value[0]);
            } else if (type.isDOUBLE()) {
                double[] value = (double[]) v.getValue();
                log.info(v.getName() + " " + value[0]);
            } else if (type.isSTRING()) {
                String[] value = (String[]) v.getValue();
                log.info(v.getName() + " " + value[0]);
            } else if (type.isSHORT()) {
                short[] value = (short[]) v.getValue();
                log.info(v.getName() + " " + value[0]);
            } else if (type.isFLOAT()) {
                float[] value = (float[]) v.getValue();
                log.info(v.getName() + " " + value[0]);
            }
        });
    }

    public void registerPV(String aliasName, PV pv) {
        synchronized (pvs) {
            pvs.put(aliasName, pv);
        }
    }

    public void registerPV(PV pv) {
        registerPV(pv.getName(), pv);
    }

    public void registerPVs(ArrayList<PV> pvs) {
        pvs.forEach(this::registerPV);
    }

    public ProcessVariable unregisterPV(String aliasName) {
        synchronized (pvs) {
            return pvs.remove(aliasName);
        }
    }

    @Override
    public ProcessVariable processVariableAttach(String aliasName, ProcessVariableEventCallback eventCallback,
                                                 ProcessVariableAttachCallback asyncCompletionCallback)
            throws CAStatusException, IllegalArgumentException, IllegalStateException {
        synchronized (pvs) {
            ProcessVariable pv = pvs.get(aliasName);
            if (pv != null) {
                if (pv.getEventCallback() == null)
                    pv.setEventCallback(eventCallback);
                return pv;
            } else
                throw new CAStatusException(CAStatus.NOSUPPORT, "PV does not exist");
        }
    }

    @Override
    public ProcessVariableExistanceCompletion processVariableExistanceTest(String aliasName, InetSocketAddress clientAddress,
                                                                           ProcessVariableExistanceCallback asyncCompletionCallback)
            throws CAException, IllegalArgumentException, IllegalStateException {
        synchronized (pvs) {
            return pvs.containsKey(aliasName) ?
                    ProcessVariableExistanceCompletion.EXISTS_HERE :
                    ProcessVariableExistanceCompletion.DOES_NOT_EXIST_HERE;
        }
    }
}
