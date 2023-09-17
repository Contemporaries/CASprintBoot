package ihep.ac.cn.example;


import ihep.ac.cn.handler.CADecodeHandler;
import ihep.ac.cn.handler.CAEncodeHandler;
import ihep.ac.cn.handler.CAHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class NettyServerExample {

    @Value("${netty.port}")
    private int port;

    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            Bootstrap boot = new Bootstrap();
            boot.group(bossGroup)
                    .option(ChannelOption.SO_BACKLOG, 128);
            serverBootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new CADecodeHandler())
                                    .addLast(new CAEncodeHandler())
                                    .addLast(new IdleStateHandler(10, 0, 0))
                                    .addLast(CAHandler.getInstance());
                        }
                    });

            try {
                ChannelFuture cf = serverBootstrap.bind(port).sync();
                log.info("Netty server started on " + port);
                cf.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
