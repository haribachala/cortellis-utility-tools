package com.clarivate.cortellis.record.services.controller;

import com.clarivate.cortellis.alerts.exceptions.MessageProducerException;
import com.clarivate.cortellis.commons.utils.MailUtility;
import com.clarivate.cortellis.commons.utils.SystemHostName;
import com.clarivate.cortellis.commons.utils.URLGeneratorUtility;
import com.clarivate.cortellis.record.services.constants.RecordServiceConstants;
import com.clarivate.cortellis.record.services.exceptions.FileNotFoundException;
import com.clarivate.cortellis.record.services.exceptions.PathNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

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
    RestTemplate restTemplate =null;
    RecordServiceConstants recordServiceConstants=null;
    URLGeneratorUtility urlGeneratorUtility =null;
    Properties rsHostPorts =null;
    Properties dataStagingHostNames =null;

    @RequestMapping(value="/recordServiceAction", produces={"application/json"})
    public String recordServiceAction(
            @RequestParam String componentName,
            @RequestParam String task,
            @RequestParam String recordServiceEnv,
            @RequestParam(value = "snapshotTime" , required =false) String snapshotTime,
            Model model
            ){

        try {
            SystemHostName systemHostName = new SystemHostName();
           if (recordServiceEnv.contains("Prod")) {
                MailUtility mailUtility = new MailUtility();
                mailUtility.sendMail("haribachala@gmail.com", "hariprasad.bachala@tr.com",  task.toUpperCase()+ " task triggered manually on " + recordServiceEnv + "", task+ " task Processed by :" + systemHostName.getHostName() + "  User :" + System.getProperty("user.name"));
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
                model.addAttribute("exception", "requested task URI path Not Found");
                throw new PathNotFoundException("requested task URI path Not Found");
            }

            urlGeneratorUtility = new URLGeneratorUtility();
            URI postRequestURL =urlGeneratorUtility.buildRequestPostURL(recordServiceConstants);
            restTemplate = new RestTemplate();
             //HttpStatus status= restTemplate.postForObject(postRequestURL, null, HttpStatus.class);
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange(postRequestURL.toString(), HttpMethod.POST, entity, String.class);
           // model.addAttribute("message", "HTTP Status Code: " +result.getStatusCode()+ " message: "    + task.toUpperCase() +" request sent to server successfully\n"
          // + "Response: " +result.getBody());
            model.addAttribute("message", result.getBody());
            logger.info("Done!");
        }catch (Exception e){
            logger.error(e.getMessage());
            model.addAttribute("message", "Exception : " + e.getMessage());
            return "exception";
        }

        return "response";

    }
    @RequestMapping("/getRSEnvironments")
    @ResponseBody
    public String getRSEnvironments(HttpServletResponse httpServletResponse, String task) throws  Exception{
        File rsEnvFile=null ;
        BufferedReader br =null;
        StringBuilder rsHostBuilder = null;
        String currentLine;
        try {
            // get ports

            getRecordServerPorts();
            if(!task.equalsIgnoreCase("extract")) {
                rsEnvFile = new File("rs-MW-Envs.txt");
                httpServletResponse.setContentType("text/plain");
                br = new BufferedReader(new FileReader(rsEnvFile));
                rsHostBuilder = new StringBuilder();
                while ((currentLine = br.readLine()) != null) {
                    rsHostBuilder.append(currentLine).append("\n");

                }
            }else {
                rsEnvFile = new File("rs-data-staging-Envs.txt");
                httpServletResponse.setContentType("text/plain");
                br = new BufferedReader(new FileReader(rsEnvFile));
                rsHostBuilder = new StringBuilder();
                while ((currentLine = br.readLine()) != null) {
                    rsHostBuilder.append(currentLine).append("\n");

                }
            }
        } catch (Exception e) {
            logger.info("rs Config File Not Found: " + e.getMessage());
            throw  new FileNotFoundException("rs Config File Not Found: " + e.getMessage());
        }finally {
            if(br!=null)
                br.close();

        }
        return rsHostBuilder.toString();

    }

    public void getRecordServerPorts() throws  Exception{
        rsHostPorts = new Properties();
        FileInputStream fileInputStream =null;
        File rsHostPortsFile ;
        try {
            rsHostPortsFile = new File("recordServerHostPorts.properties");
            fileInputStream =new FileInputStream(rsHostPortsFile);
            rsHostPorts.load(fileInputStream);
        } catch (Exception e) {
            logger.info("Environment Config File Not Found: " + e.getMessage());
            rsHostPorts=null;
            throw  new MessageProducerException("recordServerHostPorts File Not Found: " + e.getMessage());
        }finally {
            if(fileInputStream!=null)
                fileInputStream.close();
        }



    }

   /* private void loadMWHostNames() throws  Exception{
        mwHostNames = new Properties();
        FileInputStream fileInputStream =null;
        File mwHostNamesPropertiesFile ;
        try {
            mwHostNamesPropertiesFile = new File("rs-MW-Envs.txt");
            fileInputStream =new FileInputStream(mwHostNamesPropertiesFile);
            mwHostNames.load(fileInputStream);
        } catch (Exception e) {
            logger.info("rs Config File Not Found: " + e.getMessage());
            mwHostNames=null;
            throw  new FileNotFoundException("rs Config File Not Found: " + e.getMessage());
        }finally {
            if(fileInputStream!=null)
                fileInputStream.close();
        }


    }
    private void loadDataStagingHostNames() throws  Exception{
        dataStagingHostNames = new Properties();
        FileInputStream fileInputStream =null;
        File dsHostNamesPropertiesFile ;
        try {
            dsHostNamesPropertiesFile = new File("rs-data-staging-Envs.txt");
            fileInputStream =new FileInputStream(dsHostNamesPropertiesFile);
            dataStagingHostNames.load(fileInputStream);
        } catch (Exception e) {
            logger.info("rs Config File Not Found: " + e.getMessage());
            mwHostNames=null;
            throw  new FileNotFoundException("rs Config File Not Found: " + e.getMessage());
        }finally {
            if(fileInputStream!=null)
                fileInputStream.close();
        }


    }*/
}

