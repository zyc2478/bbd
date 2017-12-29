package com.autobid.util;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.autobid.entity.Employeer;


public class MybatisTest {
	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;
	static {
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����
	 */
	public static void findEmployeerById(int id) {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			Employeer employeer = (Employeer) session.selectOne(
					"com.mucfc.model.EmployeerMapper.findEmployeerByID", 1);
			if (employeer == null)
				System.out.println("null");
			else
				System.out.println(employeer);
		} finally {
			session.close();
		}
	}
	/**
	 * ����
	 */
	public static void addEmployeer(Employeer employeer){
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			 //����ֵ�Ǽ�¼����  
            int resultCount = session.insert("com.mucfc.model.EmployeerMapper.addEmployeer", employeer );  
            System.out.printf("��ǰ�����employeer_id :%d    ��ǰ�������ݿ�������:%d " , employeer.getEmployeer_id() ,resultCount);  //��ȡ��������id  
            System.out.println("");
            session.commit() ;  		
		} finally {
			session.close();
		}
		
	}
	/**
	 * ɾ��
	 * 
	 */
	public static void deleteEmployeer(int id){
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			 //����ֵ�Ǽ�¼����  
			 int resultCount=session.delete("com.mucfc.model.EmployeerMapper.deleteEmployeer",id); 
			  System.out.println("��ǰɾ�����ݿ�������: "+resultCount);  //��ȡ��������id  
            session.commit() ;  		
		} finally {
			session.close();
		}
	}
	/**
	 * ����
	 */
	public static void updateEmployeer(Employeer employeer){
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession(); 
            session.update("com.mucfc.model.EmployeerMapper.updateEmployeer",employeer);    
            session.commit() ;  		
		} finally {
			session.close();
		}
		
	}

	public static void main(String[] args) {
		
		
		
		

		
		Employeer employeer1=new Employeer();
		employeer1.setEmployeer_name("����");
		employeer1.setEmployeer_age(23);
		employeer1.setEmployeer_department("��Ʒһ��");
		employeer1.setEmployeer_worktype("��������ʦ");
		
		Employeer employeer2=new Employeer();
		employeer2.setEmployeer_name("����");
		employeer2.setEmployeer_age(30);
		employeer2.setEmployeer_department("��Ʒ����");
		employeer2.setEmployeer_worktype("���Թ���ʦ");
		
		Employeer employeer3=new Employeer();
		employeer3.setEmployeer_name("С��");
		employeer3.setEmployeer_age(22);
		employeer3.setEmployeer_department("��Ʒ����");
		employeer3.setEmployeer_worktype("���ݷ���ʦ");
		
		
		Employeer employeer4=new Employeer();
		employeer4.setEmployeer_name("����");
		employeer4.setEmployeer_age(22);
		employeer4.setEmployeer_department("�ƻᲿ");
		employeer4.setEmployeer_worktype("������Ա");
	
		//����
     	addEmployeer(employeer1);
		addEmployeer(employeer2);
		addEmployeer(employeer3);
		addEmployeer(employeer4);
		//����
		findEmployeerById(1);
		//ɾ��
		deleteEmployeer(1);
		//����
		employeer2.setEmployeer_id(2);
		employeer2.setEmployeer_age(21);
		employeer2.setEmployeer_department("��Ʒ����");
		updateEmployeer(employeer2);

	}

}
