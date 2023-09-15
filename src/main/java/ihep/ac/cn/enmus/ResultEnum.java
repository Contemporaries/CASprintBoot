package ihep.ac.cn.enmus;

public enum ResultEnum {
    SUCCESS((short)2000, "Success"),
    EXCEPTION((short)3000, "Exception"),
    ERROR((short)5000, "Error");

    private final short value;
    private final String reasonPhrase;

    ResultEnum(short value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public short value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    @Override
    public String toString() {
        return "ResultEnum{" +
                "value=" + value +
                ", reasonPhrase='" + reasonPhrase + '\'' +
                '}';
    }
}
