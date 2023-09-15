package ihep.ac.cn.example;

import ihep.ac.cn.handler.CADecodeHandler;
import ihep.ac.cn.handler.CAEncodeHandler;
import ihep.ac.cn.handler.CAHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class NettyClientExample {

    private static final EventLoopGroup clientGroup = new NioEventLoopGroup();

    private static boolean initFlag;

    private static Channel channel;

    public void doConnect(Bootstrap bootstrap, EventLoopGroup eventLoopGroup) {
        ChannelFuture cf = null;
        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .remoteAddress("", 8898)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new CADecodeHandler())
                                    .addLast(new CAEncodeHandler())
                                    .addLast(CAHandler.getInstance());

                        }
                    });
            cf = bootstrap.connect().addListener((ChannelFuture futureListener) -> {
                if (!futureListener.isSuccess()) {
                    final EventLoop eventLoop = futureListener.channel().eventLoop();
                    eventLoop.schedule(() -> doConnect(new Bootstrap(), eventLoop), 10, TimeUnit.SECONDS);
                    initFlag = false;
                } else {
                    initFlag = true;
                }
                if (initFlag) {
                    channel = futureListener.channel();
                }
            });

            cf.channel().closeFuture().sync();
        } catch (Exception ignored) {
            System.out.println("error");
        }
    }

}
