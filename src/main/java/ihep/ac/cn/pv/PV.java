package ihep.ac.cn.pv;

import com.cosylab.epics.caj.cas.handlers.AbstractCASResponseHandler;
import gov.aps.jca.CAException;
import gov.aps.jca.CAStatus;
import gov.aps.jca.Monitor;
import gov.aps.jca.cas.ProcessVariable;
import gov.aps.jca.cas.ProcessVariableEventCallback;
import gov.aps.jca.cas.ProcessVariableReadCallback;
import gov.aps.jca.cas.ProcessVariableWriteCallback;
import gov.aps.jca.dbr.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PV extends ProcessVariable {

    protected DBRType type;
    protected Object value;
    protected int count;
    protected TimeStamp timestamp;
    protected String units = GR.EMPTYUNIT;
    protected Number upperDispLimit = GR.ZEROD;
    protected Number lowerDispLimit = GR.ZEROD;
    protected Number upperAlarmLimit = GR.ZEROD;
    protected Number upperWarningLimit = GR.ZEROD;
    protected Number lowerWarningLimit = GR.ZEROD;
    protected Number lowerAlarmLimit = GR.ZEROD;
    protected Number upperCtrlLimit = GR.ZEROD;
    protected Number lowerCtrlLimit = GR.ZEROD;
    protected short precision = -1;
    protected String[] enumLabels = null;

    public PV(String name, ProcessVariableEventCallback eventCallback,
              DBRType type, Object initialValue) {
        super(name, eventCallback);

        if (!initialValue.getClass().isArray())
            throw new IllegalArgumentException("array expected as initialValue");

        this.type = type;
        this.value = initialValue;
        this.count = java.lang.reflect.Array.getLength(this.value);
        this.timestamp = new TimeStamp();
    }

    public Object getValue() {
        return value;
    }

    public DBRType getType() {
        return type;
    }

    public int getDimensionSize(int dimension) {
        if (dimension == 0)
            return count;
        else
            return 0;
    }

    public Number getLowerAlarmLimit() {
        return lowerAlarmLimit;
    }

    public void setLowerAlarmLimit(Number lowerAlarmLimit) {
        this.lowerAlarmLimit = lowerAlarmLimit;
    }

    public Number getLowerCtrlLimit() {
        return lowerCtrlLimit;
    }

    public void setLowerCtrlLimit(Number lowerCtrlLimit) {
        this.lowerCtrlLimit = lowerCtrlLimit;
    }

    public Number getLowerDispLimit() {
        return lowerDispLimit;
    }

    public void setLowerDispLimit(Number lowerDispLimit) {
        this.lowerDispLimit = lowerDispLimit;
    }

    public Number getLowerWarningLimit() {
        return lowerWarningLimit;
    }

    public void setLowerWarningLimit(Number lowerWarningLimit) {
        this.lowerWarningLimit = lowerWarningLimit;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Number getUpperAlarmLimit() {
        return upperAlarmLimit;
    }

    public void setUpperAlarmLimit(Number upperAlarmLimit) {
        this.upperAlarmLimit = upperAlarmLimit;
    }

    public Number getUpperCtrlLimit() {
        return upperCtrlLimit;
    }

    public void setUpperCtrlLimit(Number upperCtrlLimit) {
        this.upperCtrlLimit = upperCtrlLimit;
    }

    public Number getUpperDispLimit() {
        return upperDispLimit;
    }

    public void setUpperDispLimit(Number upperDispLimit) {
        this.upperDispLimit = upperDispLimit;
    }

    public Number getUpperWarningLimit() {
        return upperWarningLimit;
    }

    public void setUpperWarningLimit(Number upperWarningLimit) {
        this.upperWarningLimit = upperWarningLimit;
    }

    public short getPrecision() {
        return precision;
    }

    public void setPrecision(short precision) {
        this.precision = precision;
    }

    public String[] getEnumLabels() {
        return enumLabels;
    }

    public void setEnumLabels(String[] labels) {
        this.enumLabels = labels;
    }

    public synchronized CAStatus read(DBR value, ProcessVariableReadCallback asyncReadCallback) throws CAException {

        // fill
        fillInDBR(value);

        // given DBR is always at least TIME
        ((TIME) value).setTimeStamp(timestamp);

        int minCount = Math.min(count, value.getCount());
        System.arraycopy(this.value, 0, value.getValue(), 0, minCount);
        return CAStatus.NORMAL;
    }

    public void fillInDBR(DBR value) {
        if (value.isGR()) {
            // fill GR
            GR gr = (GR) value;
            gr.setUnits(getUnits());
            gr.setUpperDispLimit(getUpperDispLimit());
            gr.setLowerDispLimit(getLowerDispLimit());
            gr.setUpperAlarmLimit(getUpperAlarmLimit());
            gr.setUpperWarningLimit(getUpperWarningLimit());
            gr.setLowerWarningLimit(getLowerWarningLimit());
            gr.setLowerAlarmLimit(getLowerAlarmLimit());
        }

        if (value.isCTRL()) {
            // fill-up GR to CTRL
            CTRL ctrl = (CTRL) value;
            ctrl.setUpperCtrlLimit(getUpperCtrlLimit());
            ctrl.setLowerCtrlLimit(getLowerCtrlLimit());
        }

        if (value.isPRECSION()) {
            // fill PRECISION
            PRECISION precision = (PRECISION) value;
            precision.setPrecision(getPrecision());
        }

        if (value.isLABELS()) {
            // fill LABELS
            LABELS labels = (LABELS) value;
            labels.setLabels(getEnumLabels());
        }
    }

    public synchronized CAStatus write(DBR value, ProcessVariableWriteCallback asyncWriteCallback) {
        this.value = value.getValue();
        this.count = java.lang.reflect.Array.getLength(this.value);
        this.timestamp = new TimeStamp();
        if (this.type.isSTRING())
            log.info("PV Put ==> " + this.name + " " + isString(value));
        log.info("PV Put ==> " + this.name + " " + isNumber(value));
        // notify
        if (interest) {
            DBR monitorDBR = AbstractCASResponseHandler.createDBRforReading(this);
            fillInDBR(monitorDBR);
            ((TIME) monitorDBR).setTimeStamp(timestamp);
            System.arraycopy(this.value, 0, monitorDBR.getValue(), 0, count);
            eventCallback.postEvent(Monitor.VALUE | Monitor.LOG, monitorDBR);
        }

        return CAStatus.NORMAL;
    }

    public Number isNumber(DBR value) {
        if (value.isDOUBLE()) {
            double[] doubleValue = (double[]) value.getValue();
            return doubleValue[0];
        } else if (value.isSHORT()) {
            short[] shortValue = (short[]) value.getValue();
            return shortValue[0];
        } else if (value.isINT()) {
            int[] intValue = (int[]) value.getValue();
            return intValue[0];
        } else if (value.isFLOAT()) {
            float[] floatValue = (float[]) value.getValue();
            return floatValue[0];
        } else {
            log.info(this.name + " of type not number");
            return null;
        }
    }

    public String isString(DBR value) {
        String[] strValue = (String[]) value.getValue();
        return strValue[0];
    }

}
