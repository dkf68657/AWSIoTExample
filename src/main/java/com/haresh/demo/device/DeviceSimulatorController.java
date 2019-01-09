package com.haresh.demo.device;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import com.haresh.demo.device.ShadowThing.Document;
import com.haresh.demo.sampleUtil.Message;

@Controller
@RequestMapping("/device")
public class DeviceSimulatorController {

	@Autowired
	private DeviceService  deviceService;
	
	@SendTo("/topic/bulbstatus")
	public Message bulbStatus(Message message) throws Exception {
		return message;
	}
	
	@GetMapping("")
    public String clustering(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
			HttpServletRequest request, Model model) {
		System.out.println("DeviceSimulatorController.clustering()");
		try {
			deviceService.getDeviceStatus();
		} catch (InterruptedException | AWSIotException e) {
			System.out.println("Not able to capture device status.");
			e.printStackTrace();
		}
		
        return "device";
    }
	
	@RequestMapping(value = "/getLastStatus", produces=MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public Document getLastStatus(HttpServletRequest request) {
		Document document = null;
		try {
			document =  deviceService.getDeviceLastStatus();
		} catch (InterruptedException | AWSIotException | IOException | AWSIotTimeoutException e) {
			System.out.println("Not able to capture device last status.");
			e.printStackTrace();
		}
        return document;
    }
	
	@RequestMapping(value = "/sessionid", produces=MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public Message sessionid(HttpServletRequest request) {
        return new Message(request.getSession().getId());
    }
	
	
	@RequestMapping(value = "/redBulbStatus", produces=MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public String redBulbStatus(@RequestParam("status") boolean status,
    		HttpServletRequest request, Model model) {
		
		try {
			deviceService.sentTelemetryMessage(BulbType.RED, status);
		} catch (InterruptedException | AWSIotException | AWSIotTimeoutException | IOException e) {
			e.printStackTrace();
			System.out.println("Error Occured While Sending Message");
		}
        return "device";
    }
	
	@RequestMapping(value = "/greenBulbStatus", produces=MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public String greenBulbStatus(@RequestParam("status") boolean status,
    		HttpServletRequest request, Model model) {
		
		try {
			deviceService.sentTelemetryMessage(BulbType.GREEN, status);
		} catch (InterruptedException | AWSIotException | AWSIotTimeoutException | IOException e) {
			System.out.println("Error Occured While Sending Message");
			e.printStackTrace();
		}
		
		return "device";
    }
	
	@RequestMapping(value = "/blueBulbStatus", produces=MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public String blueBulbStatus(@RequestParam("status") boolean status,
    		HttpServletRequest request, Model model) {
		
		try {
			deviceService.sentTelemetryMessage(BulbType.BLUE, status);
		} catch (InterruptedException | AWSIotException | AWSIotTimeoutException | IOException e) {
			System.out.println("Error Occured While Sending Message");
			e.printStackTrace();
		}
		return "device";
    }
	
	@RequestMapping(value = "/updateDesiredValueOfBulb", produces=MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public String blueBulbStatus(@RequestParam("bulb") String bulbType,
    		@RequestParam("status") boolean status,
    		HttpServletRequest request, Model model) {
		
		try {
			deviceService.updateDeviceDesiredStatus(BulbType.valueOf(bulbType.toUpperCase()), status);
		} catch (InterruptedException | AWSIotException | AWSIotTimeoutException | IOException e) {
			System.out.println("Error Occured While Sending Message");
		}
		return "device";
    }
	
	
}
