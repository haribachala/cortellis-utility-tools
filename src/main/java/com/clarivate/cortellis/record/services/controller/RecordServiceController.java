package com.clarivate.cortellis.record.services.controller;

import com.clarivate.cortellis.alerts.exceptions.MessageProducerException;
import com.clarivate.cortellis.commons.utils.MailUtility;
import com.clarivate.cortellis.commons.utils.SystemHostName;
import com.clarivate.cortellis.commons.utils.URLGeneratorUtility;
import com.clarivate.cortellis.record.services.constants.RecordServiceConstants;
import com.clarivate.cortellis.record.services.exceptions.FileNotFoundException;
import com.clarivate.cortellis.record.services.exceptions.PathNotFoundException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.URI;
import java.util.Arrays;
import java.util.Properties;

@Controller
public class RecordServiceController {

    private static final Logger logger = LoggerFactory.getLogger(RecordServiceController.class);
    private RestTemplate restTemplate =null;
    private RecordServiceConstants recordServiceConstants=null;
    private URLGeneratorUtility urlGeneratorUtility =null;
    private Properties rsHostPorts =null;
    Properties dataStagingHostNames =null;
    @ResponseBody
    @RequestMapping(value="/recordServiceAction", produces= MediaType.TEXT_HTML_VALUE)
    public String recordServiceAction(
            @RequestParam String componentName,
            @RequestParam String task,
            @RequestParam String recordServiceEnv,
            @RequestParam(value = "snapshotTime" , required =false) String snapshotTime,
         HttpServletRequest httpServletRequest, HttpServletResponse response
            ){
        String responseText;
        ResponseEntity<String> result= null;
        HttpStatus httpStatusCode;
        try {
            SystemHostName systemHostName = new SystemHostName();
           if (recordServiceEnv.contains("prod")) {
                MailUtility mailUtility = new MailUtility();
                mailUtility.sendMail("cortellis.utility.tool@gmail.com", "hariprasad.bachala@tr.com","cortellis.utility.tool@gmail.com",  task.toUpperCase()+ " task triggered manually on " + recordServiceEnv + "", task+ " task Processed by :" + systemHostName.getHostName() + "  User :" + System.getProperty("user.name"));
           }
            logger.info("Record Service" +task.toUpperCase()+  " Process by :" + systemHostName.getHostName() + "  User :" + System.getProperty("user.name"));
            recordServiceConstants = new RecordServiceConstants();
            recordServiceConstants.setDataSet(componentName);
            recordServiceConstants.setHost(recordServiceEnv);
            recordServiceConstants.setTask(task);
            recordServiceConstants.setSnapShotTime(snapshotTime);
            recordServiceConstants.setProtocol("http");
            recordServiceConstants.setPort(rsHostPorts.getProperty(componentName));
            if(task.equalsIgnoreCase("health")){
                //Admin port is server port +1
                int adminPort = Integer.valueOf(recordServiceConstants.getPort());
                recordServiceConstants.setPort(String.valueOf(adminPort +1));
            }
            recordServiceConstants.setRecordExtractPath("/recordextractor/extract/");
            recordServiceConstants.setRecordLoaderPath("/recordserver/loader/load/");
            recordServiceConstants.setRecordPublishPath("/recordserver/loader/publish/");
            recordServiceConstants.setRecordHealthPath("/admin/health");
            if(task.equalsIgnoreCase("extract")){
                recordServiceConstants.setUrlPath(recordServiceConstants.getRecordExtractPath()+recordServiceConstants.getDataSet());
            }else if(task.equalsIgnoreCase("load")){
                recordServiceConstants.setUrlPath(recordServiceConstants.getRecordLoaderPath()+recordServiceConstants.getDataSet() + "/" +recordServiceConstants.getSnapShotTime());
            }else if(task.equalsIgnoreCase("publish")){
                recordServiceConstants.setUrlPath(recordServiceConstants.getRecordPublishPath()+recordServiceConstants.getDataSet() + "/" +recordServiceConstants.getSnapShotTime());
            }else if(task.equalsIgnoreCase("health")){
                recordServiceConstants.setUrlPath(recordServiceConstants.getRecordHealthPath());
            }else {
                throw new PathNotFoundException("requested task URI path Not Found");
            }
            urlGeneratorUtility = new URLGeneratorUtility();
            URI postRequestURL =urlGeneratorUtility.buildRequestPostURL(recordServiceConstants);
            restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            //headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            result = restTemplate.exchange(postRequestURL.toString(), HttpMethod.POST, entity, String.class);

            if(task.equalsIgnoreCase("health")) {
                 responseText=  result.getBody();
             //  String[] json = recordServiceEnv.split("},");
                JSONObject embeddedCassandraHealth=  new JSONObject(responseText).getJSONObject("embeddedCassandraHealth");
                JSONObject recordServerHealth=  new JSONObject(responseText).getJSONObject("recordServerHealth");
                String  dataStatus = new JSONObject(responseText).get("status").toString();
                String  storageType=embeddedCassandraHealth.get("Storage Type:").toString();
                String  cassandraStatus =embeddedCassandraHealth.get("status").toString();
                String  latestSnapshot=embeddedCassandraHealth.get("latestSnapshot").toString();
                String  availableSnapshots=embeddedCassandraHealth.get("availableSnapshots").toString();
                String  recordCount=embeddedCassandraHealth.get("recordCount").toString();
                String  activeMq=recordServerHealth.get("ActiveMq").toString();
                StringBuilder responseBuilder =new StringBuilder();
                responseBuilder.append(dataStatus).append("#")
                               .append(storageType).append("#")
                               .append(cassandraStatus).append("#")
                               .append(latestSnapshot).append("#")
                               .append(availableSnapshots) .append("#")
                               .append(recordCount).append("#")
                               .append(activeMq);
                httpStatusCode = result.getStatusCode();
                if(httpStatusCode.equals(HttpStatus.OK)) response.setStatus(200);
                response.setStatus(HttpStatus.OK.value());
                return  responseBuilder.toString();
            }else {
                response.setStatus(200);
                responseText=  " Request sent to server successfully";
            }
             logger.info("Task Completed!");
        }catch (Exception e){
            logger.error(e.getMessage());
             response.setStatus(400);
             if(null!=e.getMessage()){
                responseText = e.getMessage();}
             else{
                responseText = e.getCause().toString();
            }
        }
            return  responseText;

    }
    @RequestMapping("/getRSEnvironments")
    @ResponseBody
    public String getRSEnvironments(HttpServletResponse httpServletResponse, String task) throws  Exception{
        File rsEnvFile ;
        BufferedReader br =null;
        StringBuilder rsHostBuilder;
        String currentLine;
        try {
            // get ports
            getRecordServerPorts();
            if(!task.equalsIgnoreCase("extract")) {
                rsEnvFile = new File("conf/rs-MW-Envs.txt");
                httpServletResponse.setContentType("text/plain");
                br = new BufferedReader(new FileReader(rsEnvFile));
                rsHostBuilder = new StringBuilder();
                while ((currentLine = br.readLine()) != null) {
                    rsHostBuilder.append(currentLine).append("\n");
             }
            }else {
                rsEnvFile = new File("conf/rs-data-staging-Envs.txt");
                httpServletResponse.setContentType("text/plain");
                br = new BufferedReader(new FileReader(rsEnvFile));
                rsHostBuilder = new StringBuilder();
                while ((currentLine = br.readLine()) != null) {
                    rsHostBuilder.append(currentLine).append("\n");
                }
            }
        } catch (Exception e) {
            logger.info("Record Service Hosts Config File Not Found: " + e.getMessage());
            throw  new FileNotFoundException("Record Service Hosts Config File Not Found: " + e.getMessage());
        }finally {
            if(br!=null)
                br.close();

        }
        return rsHostBuilder.toString();

    }

    private  void getRecordServerPorts() throws  Exception{
        rsHostPorts = new Properties();
        FileInputStream fileInputStream =null;
        File rsHostPortsFile ;
        try {
            rsHostPortsFile = new File("conf/recordServerHostPorts.properties");
            fileInputStream =new FileInputStream(rsHostPortsFile);
            rsHostPorts.load(fileInputStream);
        } catch (Exception e) {
            logger.info("Record Server Host Ports Config File Not Found: " + e.getMessage());
            rsHostPorts=null;
            throw  new MessageProducerException("Record Server Host Ports Config File Not Found: " + e.getMessage());
        }finally {
            if(fileInputStream!=null)
                fileInputStream.close();
        }



    }


}

