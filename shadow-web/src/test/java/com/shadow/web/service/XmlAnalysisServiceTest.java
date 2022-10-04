package com.shadow.web.service;

import com.cislc.shadow.utils.enums.Encryption;
import com.cislc.shadow.utils.enums.Protocol;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XmlAnalysisServiceTest {

    @Autowired
    private XmlAnalysisService service;

    @Test
    public void analyseXml() {
        File xmlFile = new File("src/main/resources/xmlData/SduCabinet.xml");
        File xsdFile = new File("src/main/resources/xmlData/model.xsd");
        service.analyseXml(xmlFile, xsdFile, Protocol.MQTT, Encryption.NONE);
    }

}