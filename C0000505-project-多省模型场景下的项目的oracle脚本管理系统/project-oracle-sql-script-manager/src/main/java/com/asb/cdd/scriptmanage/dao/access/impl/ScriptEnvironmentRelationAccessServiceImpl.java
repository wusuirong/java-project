package com.asb.cdd.scriptmanage.dao.access.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asb.cdd.scriptmanage.dao.access.ScriptEnvironmentRelationAccessService;
import com.asb.cdd.scriptmanage.dao.access.model.ScriptDestinationRelation;
import com.asb.cdd.scriptmanage.dao.access.model.ScriptEnvironmentRelation;
import com.irm.system.access.common.service.impl.AbstractNamespaceAccessServiceImpl;
import com.irm.system.authorization.vo.UserContext;

public class ScriptEnvironmentRelationAccessServiceImpl 
extends AbstractNamespaceAccessServiceImpl<ScriptEnvironmentRelation> 
implements ScriptEnvironmentRelationAccessService {

	private static final String FIND_BY_SCRIPT_ID = "findByScriptId";
	
	private static final String FIND_UNSUCCESS_RELATION_WITH_SCRIPT_IDS = "findUnSuccessRelationWithScriptIds";
	
	public List<ScriptEnvironmentRelation> findByScriptId(long scriptId, UserContext userContext) {
		Map<String,Object> parameters=new HashMap<String,Object>();
		parameters.put("scriptId", scriptId);		
		List<ScriptEnvironmentRelation> results = this.getIbatisDataAccessObject().find(this.getNamespace(), FIND_BY_SCRIPT_ID, parameters);
		return results;
	}

	public List<ScriptEnvironmentRelation> findUnSuccessRelationWithScriptIds(long[] scriptIds, UserContext userContext) {
		Map<String,Object> parameters=new HashMap<String,Object>();
		parameters.put("scriptIds", scriptIds);		
		List<ScriptEnvironmentRelation> results = this.getIbatisDataAccessObject().find(this.getNamespace(), FIND_UNSUCCESS_RELATION_WITH_SCRIPT_IDS, parameters);
		return results;
	}

}
