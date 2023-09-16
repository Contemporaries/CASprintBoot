package ihep.ac.cn.component;

import ihep.ac.cn.config.PVJson;
import ihep.ac.cn.example.CAClientExample;
import ihep.ac.cn.example.CAServerExample;
import ihep.ac.cn.example.NettyServerExample;
import ihep.ac.cn.factory.PVJsonFactory;
import jakarta.annotation.Resource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

@Component
public class MultipleAPPStarter implements ApplicationRunner {

    @Resource
    private CAServerExample caServer;

    @Resource
    private NettyServerExample lvpNettyServer;

    @Resource
    private CAClientExample caClient;

    @Resource
    private PVJsonFactory pvJsonFactory;


    @Override
    public void run(ApplicationArguments args) {
//        Executors.newSingleThreadExecutor().execute(() -> lvpNettyServer.start());
        Executors.newSingleThreadExecutor().execute(() -> caServer.start());
        Executors.newSingleThreadExecutor().execute(() -> caClient.start());
    }
}
