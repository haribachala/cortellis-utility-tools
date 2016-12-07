package com.clarivate.cortellis.alerts.controller;

import com.clarivate.cortellis.alerts.exceptions.MessageProducerException;
import com.clarivate.cortellis.alerts.message.MessageSender;
import com.clarivate.cortellis.commons.utils.MailUtility;
import com.clarivate.cortellis.commons.utils.SystemHostName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jms.JMSException;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Controller
public class SedaAlertsController {

    private static final Logger logger = LoggerFactory.getLogger(SedaAlertsController.class);
    Properties brokerURLS=null;

    @RequestMapping("/sendAlerts")
    public String triggerAlerts(
            @RequestParam("env") String env,
            @RequestParam(value = "alertIds", required = false) String alertIds,
            @RequestParam("logicalDate") String logicalDate,
            @RequestParam(value = "chkAllAlerts", required = false) boolean isAllAlerts, Model model) throws JMSException, MessageProducerException {

        String sedaTopic = "DO_ALERT_TOPIC";
        String ids;

        try {
            SystemHostName systemHostName = new SystemHostName();
            if (env.equalsIgnoreCase("prod-edc") || env.equalsIgnoreCase("prod-eagan")) {
                MailUtility mailUtility = new MailUtility();
                mailUtility.sendMail("haribachala@gmail.com", "hariprasad.bachala@tr.com", "Seda alerts triggered manually on " + env + "", "Alerts Processed by :" + systemHostName.getHostName() + "  User :" + System.getProperty("user.name"));
            }

            logger.info("Alerts Process by :" + systemHostName.getHostName() + "  User :" + System.getProperty("user.name"));

            ids = prepareAlertIds(alertIds, isAllAlerts);
        //    List<String> envParameters = getEnvParameters(env, ids, logicalDate);
            loadActiveMqBrokerURLs();
            String sedaJmsServerURL = brokerURLS.getProperty(env);
            String message =createMessage(ids, logicalDate);
           // String sedaJmsServerURL = envParameters.get(0);
           // String message = envParameters.get(1);
            if(sedaJmsServerURL== null || message==null){
                throw new MessageProducerException("Selected Environment ActiveMq Broker URL Not Found.");
            }

            logger.info("sending message to url: " + sedaJmsServerURL + " topic: " + sedaTopic + " msg: \n" + message);

            MessageSender sendmsg = new MessageSender(sedaJmsServerURL, sedaTopic, message);
            sendmsg.send();

            model.addAttribute("message", "A message has been sent to trigger the alert(s)");
            logger.info("Done!");

            return "response";
        } catch (JMSException e) {
            logger.error(e.getMessage());
            model.addAttribute("message", "Exception : " + e.getMessage());
            return "exception";
        } catch (Exception e) {
            logger.error(e.getMessage());
            model.addAttribute("message", "Exception : " + e.getMessage());
            return "exception";

        }


    }

    private List<String> getEnvParameters(String env, String alertIds, String logicalDate) {

        List<String> envParamsList = new ArrayList<>();
        String sedaJmsServerURL = "";
        String message;

        switch (env) {
            case "local":
                sedaJmsServerURL = "tcp://localhost:61616";
                message = createMessage(alertIds, logicalDate);
                break;
            case "dev-dtc":
                sedaJmsServerURL = "tcp://middleware-b-srv-dev-dtc-1.int.thomsonreuters.com:61616";
                message = createMessage(alertIds, logicalDate);
                break;
            case "build-dtc":
                sedaJmsServerURL = "tcp://middleware-b-srv-build-dtc-1.int.thomsonreuters.com:61616";
                message = createMessage(alertIds, logicalDate);
                break;
            case "qa-eagan":
                sedaJmsServerURL = "tcp://middleware-b-srv-qa-eagan-1.int.thomsonreuters.com:61616";
                message = createMessage(alertIds, logicalDate);
                break;
            case "perf-eagan":
                sedaJmsServerURL = "tcp://middleware-b-srv-perf-eagan-1.int.thomsonreuters.com:61616";
                message = createMessage(alertIds, logicalDate);
                break;
            case "prod-eagan":
                //sedaJmsServerURL = "tcp://middleware-b-srv-prod-eagan-1.int.thomsonreuters.com:61616";
                message = createMessage(alertIds, logicalDate);
                break;
            case "prod-edc":
                //sedaJmsServerURL = "tcp://middleware-b-srv-prod-edc-1.int.thomsonreuters.com:61616";
                message = createMessage(alertIds, logicalDate);
                break;
            default:
                sedaJmsServerURL = null;
                message = null;
        }
        envParamsList.add(0, sedaJmsServerURL);
        envParamsList.add(1, message);
        return envParamsList;

    }

    private String createMessage(String Ids, String logicalDate) {
        LocalDateTime currentTime = LocalDateTime.now();

        return "<com.thomsonreuters.dataorchestration.jms.message.system.SystemMessage>\n" +
                "<ids>" + Ids + "</ids>\n" +
                "<type>SYSTEM</type>\n" +
                "<timeStamp>" + currentTime + "</timeStamp>\n" +
                "<logicalDate>" + logicalDate + "</logicalDate>\n" +
                "<systemStatus>UPDATE_SUCCESSFUL</systemStatus>\n" +
                "</com.thomsonreuters.dataorchestration.jms.message.system.SystemMessage>";
    }

    private String prepareAlertIds(String alertIds, boolean isAllAlerts) {
        if (isAllAlerts)
            return "";
        else
            return alertIds.trim();
    }

    @RequestMapping("/getAlertSedaEnvironments")
    @ResponseBody
    public String getAlertSedaEnvironments(HttpServletResponse httpServletResponse) throws  Exception{
        File envFile=null ;
        BufferedReader br =null;
        StringBuilder stringBuilder = null;
        String currentLine;
        try {
            envFile = new File("conf/alertsSedaEnvs.txt");
            httpServletResponse.setContentType("text/plain");
            br = new BufferedReader(new FileReader(envFile));
            stringBuilder =new StringBuilder();
            while ((currentLine=br.readLine()) != null) {
                   stringBuilder.append(currentLine).append("\n");

            }
        } catch (Exception e) {
            logger.info("Alerts Seda Environment Config File Not Found: " + e.getMessage());
            throw  new MessageProducerException("Alerts Seda Environment Config File Not Found: " + e.getMessage());
        }finally {
            if(br!=null)
                br.close();

        }
        return stringBuilder.toString();

    }

    private void loadActiveMqBrokerURLs() throws  Exception{
        brokerURLS = new Properties();
        FileInputStream fileInputStream =null;
        File activeMqPropertiesFile ;
        try {
            activeMqPropertiesFile = new File("conf/activeMqBrokerConfig.properties");
            fileInputStream =new FileInputStream(activeMqPropertiesFile);
            brokerURLS.load(fileInputStream);
          } catch (Exception e) {
            logger.info("Environment Config File Not Found: " + e.getMessage());
            brokerURLS=null;
            throw  new MessageProducerException("activemqBrokerConfig File Not Found: " + e.getMessage());
        }finally {
            if(fileInputStream!=null)
                fileInputStream.close();
        }


    }

}
