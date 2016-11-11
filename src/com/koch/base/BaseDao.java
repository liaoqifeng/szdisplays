package com.koch.base;

import java.util.Map;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class BaseDao extends JdbcDaoSupport{

	/**
	 * ��� �����Ժ���ݿ��ֶ���һ�£���������
	 * @param objForSave ,�̳�Saveable �Ľӿڵ���
	 * @return
	 */
	public Number saveAndReturnKey(Saveable objForSave){
		return saveAndReturnKey(objForSave, objForSave.getTableName(), objForSave.getKeyColumns());
	}
	/**
	 * ���ʵ��,��������
	 * @param objForSave
	 * @param tableName
	 * @param columnAndValue
	 * @param keyColumns
	 * @return
	 */
	public Number saveAndReturnKey(Object objForSave,String tableName,String...keyColumns){
		SimpleJdbcInsert insertActor =   getSimpleJdbcInsert(); 
		insertActor.setTableName(tableName);
		insertActor.usingGeneratedKeyColumns(keyColumns);
		Number newId = insertActor.executeAndReturnKey(new BeanPropertySqlParameterSource(objForSave)) ;
		traceSql(insertActor.getInsertString());
		return newId;
	}
	/**
	 * @param objForSave ������ݿ����
	 * @param tableName
	 * @param keyColumns
	 * @return
	 */
	public void save(Saveable objForSave){
		SimpleJdbcInsert insertActor =   getSimpleJdbcInsert(); 
		insertActor.setTableName(objForSave.getTableName());
		insertActor.execute(new BeanPropertySqlParameterSource(objForSave)) ;
		traceSql(insertActor.getInsertString());
	}
	/**
	 * @param objForSave ������ݿ���࣬ע��
	 * @param tableName
	 * @param keyColumns
	 * @return
	 */
	public void save(Object objForSave,String tableName){
		SimpleJdbcInsert insertActor =   getSimpleJdbcInsert(); 
		insertActor.setTableName(tableName);
		insertActor.execute(new BeanPropertySqlParameterSource(objForSave)) ;
		traceSql(insertActor.getInsertString());
	}
	/**
	 * 
	 * @param objForSave ������ݿ����
	 * @param tableName
	 * @param keyColumns
	 * @return
	 */
	public void save(Object objForSave,String tableName,String...keyColumns){
		SimpleJdbcInsert insertActor =   getSimpleJdbcInsert(); 
		insertActor.setTableName(tableName);
		insertActor.execute(new BeanPropertySqlParameterSource(objForSave)) ;
		traceSql(insertActor.getInsertString());
	}
	/**
	 * 
	 * @param objForSave
	 * @param tableName
	 * @param columnAndValue
	 * @param keyColumns
	 * @return
	 */
	public Number saveAndReturnKey(String tableName,Map<String,Object> columnAndValue,String...keyColumns){
		SimpleJdbcInsert insertActor =   getSimpleJdbcInsert(); 
		insertActor.setTableName(tableName);
		insertActor.usingGeneratedKeyColumns(keyColumns);
		Number newId = insertActor.executeAndReturnKey(columnAndValue) ;
		traceSql(insertActor.getInsertString());
		return newId;
	}
	//----tools
	public SimpleJdbcInsert getSimpleJdbcInsert(){
		return new  SimpleJdbcInsert(getJdbcTemplate());
	}
	public SimpleJdbcInsert getSimpleJdbcInsert(String tableName){
		return new   SimpleJdbcInsert(getJdbcTemplate());
	}
	public void traceSql(String sql){
		System.out.println("JDBC:"+sql);
	}
}
