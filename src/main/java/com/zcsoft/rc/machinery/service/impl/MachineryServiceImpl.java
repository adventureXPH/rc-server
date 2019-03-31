package com.zcsoft.rc.machinery.service.impl;


import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import com.zcsoft.rc.api.machinery.entity.MachineryListRsp;
import com.zcsoft.rc.api.machinery.entity.MachineryRsp;
import com.zcsoft.rc.machinery.dao.MachineryDAO;
import com.zcsoft.rc.machinery.model.entity.Machinery;
import com.zcsoft.rc.machinery.service.MachineryService;
import com.zcsoft.rc.user.model.entity.User;
import com.zcsoft.rc.user.model.entity.UserFollow;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MachineryServiceImpl extends BaseServiceImpl<Machinery, java.lang.String> implements MachineryService {
	
	private MachineryDAO machineryDAO;

	@Resource
	public void setMachineryDAO(MachineryDAO machineryDAO) {
		super.setBaseDAO(machineryDAO);
		this.machineryDAO = machineryDAO;
	}

	@Override
	public MachineryListRsp followList(User user) {
		List<Machinery> machineryList = machineryDAO.queryUserFollowList(user.getId(), UserFollow.FOLLOW_TYPE_MACHINERY);

		MachineryListRsp rsp = new MachineryListRsp();

		if(machineryList == null) {
			return rsp;
		}

		List<MachineryRsp> machineryRspList = new ArrayList<>(machineryList.size());
		machineryList.forEach(machinery -> {
			MachineryRsp machineryRsp = new MachineryRsp();
			BeanUtils.copyProperties(machinery, machineryRsp);

			machineryRspList.add(machineryRsp);
		});

		rsp.setMachineryRspList(machineryRspList);

		return rsp;
	}
}
