package ihep.ac.cn.factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ihep.ac.cn.config.PVJson;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class PVJsonFactory {

    @Resource
    private ResourceLoader resourceLoader;

    @Value("${pv.db}")
    private String dbPath;


    public List<PVJson> pvJsonToPV() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<PVJson> pvJsons = null;
        try {
            String path = resourceLoader.getResource("classpath:" + dbPath).getFile().getAbsolutePath();
            pvJsons = objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pvJsons;
    }

}
