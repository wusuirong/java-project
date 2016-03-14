package com.irm.utils.codegen.templates.ifc.tools;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.irm.utils.codegen.templates.GeneralGenerator;
import com.irm.utils.codegen.utils.Comment;
import com.irm.utils.codegen.utils.InformationContainer;
import com.irm.utils.codegen.utils.MessageContainer;
import com.irm.utils.codegen.utils.PropertiesLoader;

@Comment(
		"*****************************************"
		+ "\r\n任务：IntegrationRouterClassGenerator"
		+ "\r\n用途：生成接口路由类实现"
		+ "\r\n用途：当IRMS调用其他系统服务时，必须经过这层路由到正确的适配器上"
		+ "\r\n*****************************************")
public class IntegrationRouterClassGenerator extends GeneralGenerator {
	@SuppressWarnings("unused")
	private static final transient Log log = LogFactory.getLog(IntegrationRouterClassGenerator.class);

	@Override
	public void execute(InformationContainer infoContainer, MessageContainer msgContainer) throws Exception {
		Map<String, String> infos = infoContainer.getInformations();
		String serviceInterfaceClassName = infos.get(Const.JAVA_SERVICE_CLASSNAME_WITHOUT_SYSTEM_CODE);
		String serviceInterfaceMethodName = infos.get(Const.JAVA_SERVICE_METHODNAME);
		String remoteSystemName = infos.get(Const.REMOTE_SYSTEM_CODE_LOWERCASE);
		String serviceDescription = infos.get(Const.SERVICE_DESCRIPTION);
		String methodDescription = infos.get(Const.METHOD_DESCRIPTION);
		String irmsProvideService = infos.get(Const.IRMS_PROVIDE_SERVICE);
		String serviceEnName = infos.get(Const.INTERFACE_METHOD_ENUM_CONST);
		
		String serviceClientEnName = infos.get(Const.SERVICE_CLIENT_SYSTEM_CODE_UPPERCASE);
		String serviceServerEnName = infos.get(Const.SERVICE_SERVER_SYSTEM_CODE_UPPERCASE);
		String integrationFunctionNameInT3Doc = infos.get(Const.INTERFACE_METHOD_EN_CODE_IN_T3DOC);
		String integrationProtocolType = infos.get(Const.INTERFACE_PROTOCOL_TYPE);
		String deployProvince = infos.get(Const.INTERFACE_FUNCTION_DEPLOY_PROVINCE);
		
		if ("Y".equals(irmsProvideService)) {
			serviceInterfaceClassName = "Irms" + serviceInterfaceClassName;
		} else {
			serviceInterfaceClassName = remoteSystemName + serviceInterfaceClassName;
		}
		
		if ("N".equalsIgnoreCase(irmsProvideService)) {
			//integration端配置
			String integrationProjectSrcPath = PropertiesLoader.get(PropertyName.INTEGRATION_PROJECT_SRC_PATH);
			String integrationInterfacePackagePath = PropertiesLoader.get(PropertyName.INTEGRATION_PACKAGE_PATH);
			integrationInterfacePackagePath = integrationInterfacePackagePath + "." + remoteSystemName + ".service";
			
			String classUpperName = serviceInterfaceClassName.substring(0,1).toUpperCase() + serviceInterfaceClassName.substring(1);
			String integrationRouteClassFullName = integrationInterfacePackagePath + ".impl.Integration" + classUpperName + "Impl";

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("serviceInterfaceClassName", serviceInterfaceClassName);
			map.put("serviceInterfaceMethodName", serviceInterfaceMethodName);
			map.put("remoteSystemName", remoteSystemName);
			map.put("serviceDescription", serviceDescription);
			map.put("irmsProvideService", irmsProvideService);
			map.put("methodDescription", methodDescription);
			map.put("serviceClientEnName", serviceClientEnName);
			map.put("serviceServerEnName", serviceServerEnName);
			map.put("integrationFunctionNameInT3Doc", integrationFunctionNameInT3Doc);
			map.put("integrationProtocolType", integrationProtocolType);
			map.put("serviceEnName", serviceEnName);
			map.put("deployProvince", deployProvince);
			
			String integrationRouteClassFullPath = integrationProjectSrcPath + integrationRouteClassFullName.replace(".", File.separator) + ".java";
//			createOrUpdateFile(integrationRouteClassFullPath, "template1", "template2", "}", map, msgContainer);
			createFile(integrationRouteClassFullPath, "template1", map, msgContainer);
		} else {
			System.out.println("此接口调用方向为其他系统使用IRMS，不需要生成接口路由类");
		}
	}

	@Override
	public void inputInformation(InformationContainer infoContainer) {
		inputInfo("业务接口是否是IRMS提供服务(Y/N):", infoContainer, Const.IRMS_PROVIDE_SERVICE, new String[]{"Y","N"});

		inputInfoWithEmptyCheck("输入业务接口类名称(ex:TencentQqMessageService):", infoContainer, Const.JAVA_SERVICE_CLASSNAME_WITHOUT_SYSTEM_CODE);
		
		inputInfoWithMultiLines("输入业务接口描述,以END$结束(ex:这是代码生成器的样例,一个qq消息发送服务END$):", infoContainer, Const.SERVICE_DESCRIPTION, "END$");
		
		inputInfoWithEmptyCheck("输入接口调用方的英文大写编号，供模拟界面显示(ex:IRMS):", infoContainer, Const.SERVICE_CLIENT_SYSTEM_CODE_UPPERCASE);
		inputInfoWithEmptyCheck("输入接口提供方的英文大写编号，供模拟界面显示(ex:EOMS):", infoContainer, Const.SERVICE_SERVER_SYSTEM_CODE_UPPERCASE);
		inputInfoWithEmptyCheck("输入T3需求文档中定义的接口方法英文名:", infoContainer, Const.INTERFACE_METHOD_EN_CODE_IN_T3DOC);
		
		inputInfo("输入接口使用的传输协议:", infoContainer, Const.INTERFACE_PROTOCOL_TYPE, new String[]{"WEBSERVICE_AXIS14","WEBSERVICE_CXF","MQ_WEBSPHERE"});
		
		inputInfo("接口发布省份拼音(ex:zhejiang):", infoContainer, Const.INTERFACE_FUNCTION_DEPLOY_PROVINCE, new String[]{"general","zhejiang","shanghai","jiangxi","gansu","shanxi"});
	}
}
