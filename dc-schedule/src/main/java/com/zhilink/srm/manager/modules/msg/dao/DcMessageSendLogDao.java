/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.msg.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.msg.entity.DcMessageSendLog;

/**
 * 信息发送详情DAO接口
 * @author chrisye
 * @version 2018-02-06
 */
@MyBatisDao
public interface DcMessageSendLogDao extends CrudDao<DcMessageSendLog> {
	
}