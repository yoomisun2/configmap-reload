package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.DoneableConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;

@SpringBootApplication
public class ConfigmapReloadApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigmapReloadApplication.class, args);
	}
	
}

@RestController
class TestController {
	
	@Value("${spring.cloud.kubernetes.config.namespace}")
	private String namespaceName;
	
	@Value("${spring.cloud.kubernetes.config.name}")
	private String configMapName;
	
	@Autowired
	private KubernetesClient client;
	
	@Autowired
	private ManagementProperties properties;
	
	@GetMapping("/")
	public List<Api> get() {
		return properties.getApis();
	}
	
	@GetMapping("/replace")
	public void replace() throws JsonProcessingException {
		//create yaml data
		List<Api> list = new ArrayList<Api>();
		Api a1 = new Api("/api/estimate/.*", null, null);
		list.add(a1);
		
		String value = createDataContent(list);
		
		//edit configMap data
		Resource<ConfigMap, DoneableConfigMap> configMapResource = client.configMaps().inNamespace(namespaceName).withName(configMapName);
		ConfigMap configMap = configMapResource.edit().addToData("application.yaml", value).done();
		configMap = configMapResource.replace(configMap);
		System.out.println(configMap);
	}

	@GetMapping("/createOrReplace")
	public void createOrReplace() throws JsonProcessingException {
		List<Api> list = new ArrayList<Api>();
		Api a1 = new Api("/api/estimate/.*", null, null);
		list.add(a1);
		
		String value = createDataContent(list);
		
		ConfigMap configMap = new ConfigMapBuilder().withNewMetadata().withName(configMapName).endMetadata()
				.addToData("application.yaml", value)
				.build();
				
		Resource<ConfigMap, DoneableConfigMap> configMapResource = client.configMaps().inNamespace(namespaceName).withName(configMapName);
		configMap = configMapResource.createOrReplace(configMap);
		System.out.println(configMap);
	}
	
	private String createDataContent(List<Api> list) throws JsonProcessingException {
		Map<String, Object> apis = new HashMap<String, Object>();
		apis.put("apis", list);
		
		Map<String, Object> zmanagement = new HashMap<String, Object>();
		zmanagement.put("management", apis);
		
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsString(zmanagement);
	}
}
