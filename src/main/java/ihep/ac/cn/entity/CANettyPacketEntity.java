package ihep.ac.cn.entity;

import lombok.Getter;
import lombok.Setter;

public class CANettyPacketEntity {
    @Getter
    private final short packet_0_default = 0xAA;
    @Getter
    private final short packet_1_default = 0xCC;

    @Setter
    @Getter
    private short packet_2_cmd;

    @Getter
    private final short packet_3_default = 0x00;

    @Getter
    @Setter
    private short packet_4_data;

    @Getter
    @Setter
    private short packet_5_xor;

    @Getter
    private final short packet_6_default = 0x0D;
    @Getter
    private final short packet_7_default = 0x0A;
}
