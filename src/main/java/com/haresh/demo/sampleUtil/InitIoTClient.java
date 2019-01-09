package com.haresh.demo.sampleUtil;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.haresh.demo.sampleUtil.SampleUtil.KeyStorePasswordPair;

@Component
public class InitIoTClient {

	@Autowired
	private CommandArguments commandArguments;

	@Value("${aws.iot.clientEndpoint}")
	private String clientEndpoint;

	@Value("${aws.iot.clientId}")
	private String clientId;

	@Value("${aws.iot.certificateName}")
	private String certificateName;

	@Value("${aws.iot.privateKeyFile}")
	private String privateKeyFile;

	@Value("${aws.iot.thingName}")
	private String thingName;

	@PostConstruct
	private void getCertificate() {

		String path = "/aws-cert";// InitIoTClient.class.getResource("/aws-cert").toString();
		commandArguments.put("clientEndpoint", clientEndpoint);
		commandArguments.put("clientId", clientId);
		commandArguments.put("certificateFile", path + "/" + certificateName);
		commandArguments.put("privateKeyFile", path + "/" + privateKeyFile);

	}

	public AWSIotMqttClient initClient() {
		AWSIotMqttClient awsIotClient = null;

		if (!StringUtils.isNotEmpty(thingName)) {
			commandArguments.put("thingName", thingName);
		}

		String clientEndpoint = commandArguments.getNotNull("clientEndpoint", SampleUtil.getConfig("clientEndpoint"));
		String clientId = commandArguments.getNotNull("clientId", SampleUtil.getConfig("clientId"));

		String certificateFile = commandArguments.get("certificateFile", SampleUtil.getConfig("certificateFile"));
		String privateKeyFile = commandArguments.get("privateKeyFile", SampleUtil.getConfig("privateKeyFile"));
		if (awsIotClient == null && certificateFile != null && privateKeyFile != null) {
			String algorithm = commandArguments.get("keyAlgorithm", SampleUtil.getConfig("keyAlgorithm"));
			KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(certificateFile, privateKeyFile, algorithm);

			awsIotClient = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
		}

		if (awsIotClient == null) {
			String awsAccessKeyId = commandArguments.get("awsAccessKeyId", SampleUtil.getConfig("awsAccessKeyId"));
			String awsSecretAccessKey = commandArguments.get("awsSecretAccessKey",
					SampleUtil.getConfig("awsSecretAccessKey"));
			String sessionToken = commandArguments.get("sessionToken", SampleUtil.getConfig("sessionToken"));

			if (awsAccessKeyId != null && awsSecretAccessKey != null) {
				awsIotClient = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey,
						sessionToken);
			}
		}

		if (awsIotClient == null) {
			throw new IllegalArgumentException("Failed to construct client due to missing certificate or credentials.");
		}

		return awsIotClient;
	}
}
