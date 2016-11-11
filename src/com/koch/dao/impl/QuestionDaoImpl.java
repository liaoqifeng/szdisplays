package com.koch.dao.impl;
import org.springframework.stereotype.Repository;

import com.koch.dao.QuestionDao;
import com.koch.entity.Question;

@Repository("questionDao")
public class QuestionDaoImpl extends BaseDaoImpl<Question> implements QuestionDao{
	
}
