package com.koch.dao.impl;

import org.springframework.stereotype.Repository;

import com.koch.dao.ReceiverDao;
import com.koch.entity.Receiver;

@Repository("receiverDao")
public class ReceiverDaoImpl extends BaseDaoImpl<Receiver> implements ReceiverDao{
}
