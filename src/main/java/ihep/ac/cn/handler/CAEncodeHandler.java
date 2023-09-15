package ihep.ac.cn.handler;

import ihep.ac.cn.entity.CANettyPacketEntity;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CAEncodeHandler extends MessageToByteEncoder<CANettyPacketEntity> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, CANettyPacketEntity lvpPacket, ByteBuf byteBuf) throws Exception {

    }
}
