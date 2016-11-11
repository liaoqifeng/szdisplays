package com.koch.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.koch.dao.QuestionDao;
import com.koch.service.QuestionService;
import com.koch.entity.Question;

@Service
public class QuestionServiceImpl extends BaseServiceImpl<Question> implements QuestionService{
	@Resource
	private QuestionDao questionDao;
	@Resource
	public void setBaseDao(QuestionDao questionDao) {
		super.setBaseDao(questionDao);
	}
}
