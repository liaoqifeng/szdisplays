package com.koch.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

public class OrderListComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		if (o1 == null) {
			return 1;
		}
		if (o2 == null) {
			return -1;
		}
		Class cls1 = o1.getClass();
		Class cls2 = o2.getClass();
		try {
			Method met1 = cls1.getMethod("getOrderList");
			Method met2 = cls2.getMethod("getOrderList");
			if(met1.invoke(o1, null)==null)
				return 1;
			if(met2.invoke(o2, null)==null)
				return -1;
			Integer order1 = Integer.valueOf(met1.invoke(o1, null).toString());
			Integer order2 = Integer.valueOf(met2.invoke(o2, null).toString());
			if(order1 > order2)
				return 1;
			if(order1 <= order2)
				return -1;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	
	
}
