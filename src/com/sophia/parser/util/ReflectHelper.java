/*
 * Created on 2004-7-9
 *
 *
 * ====================================================================
 *
 */
package com.sophia.parser.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author 2709
 * 2004-7-9
 */
public class ReflectHelper {

	private final static String[] packages = new String[] {
		"java.io",
		"java.lang",
		"java.lang.reflect",
		"java.math",
		"java.net",
		"java.sql",
		"java.text",
		"java.util",
		"java.util.jar",
		"java.util.logging",
		"java.util.prefs",
		"java.util.regex",
		"java.util.zip",
	};

	private final static Class[] primitives = new Class[] {
		boolean.class, byte.class, char.class, double.class, 
		float.class, int.class, long.class, short.class,
	};

	private final static Class[] wraps = new Class[] {
		Boolean.class, Byte.class, Character.class, Double.class,
		Float.class, Integer.class, Long.class, Short.class,
	};

	private final static Class[] classes = new Class[] {
		boolean.class, byte.class, char.class, double.class, 
		float.class, int.class, long.class, short.class,
		Boolean.class, Byte.class, Character.class, Double.class,
		Float.class, Integer.class, Long.class, Short.class,
	};

	/**
	 * 根据数据类型的大小和含义返回2个类型的差异性,越小越相似,-1表示无法转换(转换可能丢失数据)
	 * boolean:     1-bit
	 * byte:        8-bit
	 * char:        16-bit
	 * double:      64-bit
	 * float:       32-bit
	 * int:         32-bit
	 * long:        64-bit
	 * short:       16-bit
	 * double = long > float = int > short = char > byte > boolean ??
	 * 							bo    by    c    d    f    i    l    s    Bo    By    C    D    F    I    L    S
	 * Bo(olean)     0    10    20   70   60   40   50   30    0    10    20   70   60   40   50   30
	 * By(te)       50     0    20   70   60   30   40   10   50     0    20   70   60   30   40   10
	 * C(haracter)  50    40     0   70   60   20   30   10   50    40     0   70   60   20   30   10
	 * D(ouble)     30    -1    -1    0   50   70   60   80   30    -1    -1    0   50   70   60   80
	 * F(loat)      30    -1    -1   10    0   50   20   60   30    -1    -1   10    0   50   20   60
	 * I(nteger)    40    -1    -1   20   30    0   10   50   40    -1    -1   20   30    0   10   50
	 * L(ong)       30    -1    -1   10   20   50    0   60   30    -1    -1   10   20   50    0   60
	 * S(hort)      50    -1    -1   40   30   10   20    0   50    -1    -1   40   30   10   20    0
	 */
	private final static int[][] classDiff = new int[][] {
		new int[]{00, 10, 20, 70, 60, 40, 50, 30, 00, 10, 20, 70, 60, 40, 50, 30},
		new int[]{50, 00, 20, 70, 60, 30, 40, 10, 50, 00, 20, 70, 60, 30, 40, 10},
		new int[]{50, 40, 00, 70, 60, 20, 30, 10, 50, 40, 00, 70, 60, 20, 30, 10},
		new int[]{30, -1, -1, 00, 50, 70, 60, 80, 30, -1, -1, 00, 50, 70, 60, 80},
		new int[]{30, -1, -1, 10, 00, 50, 20, 60, 30, -1, -1, 10, 00, 50, 20, 60},
		new int[]{40, -1, -1, 20, 30, 00, 10, 50, 40, -1, -1, 20, 30, 00, 10, 50},
		new int[]{30, -1, -1, 10, 20, 50, 00, 60, 30, -1, -1, 10, 20, 50, 00, 60},
		new int[]{50, -1, -1, 40, 30, 10, 20, 00, 50, -1, -1, 40, 30, 10, 20, 00},
		new int[]{00, 10, 20, 70, 60, 40, 50, 30, 00, 10, 20, 70, 60, 40, 50, 30},
		new int[]{50, 00, 20, 70, 60, 30, 40, 10, 50, 00, 20, 70, 60, 30, 40, 10},
		new int[]{50, 40, 00, 70, 60, 20, 30, 10, 50, 40, 00, 70, 60, 20, 30, 10},
		new int[]{30, -1, -1, 00, 50, 70, 60, 80, 30, -1, -1, 00, 50, 70, 60, 80},
		new int[]{30, -1, -1, 10, 00, 50, 20, 60, 30, -1, -1, 10, 00, 50, 20, 60},
		new int[]{40, -1, -1, 20, 30, 00, 10, 50, 40, -1, -1, 20, 30, 00, 10, 50},
		new int[]{30, -1, -1, 10, 20, 50, 00, 60, 30, -1, -1, 10, 20, 50, 00, 60},
		new int[]{50, -1, -1, 40, 30, 10, 20, 00, 50, -1, -1, 40, 30, 10, 20, 00}
	};

	/**
	 * 查找clazz在数组classes中的位置
	 * @param clazz
	 * @param classes
	 * @return
	 */
	private static int getClassIndex(Class clazz, Class[] classes) {
		for(int i = 0; i < classes.length; i++) {
			if(clazz.equals(classes[i])) return i;
		}
		return -1;
	}

	/**
	 * 查找数据类型差异表, 返回2个数据类型的差异指数
	 * @param one
	 * @param other
	 * @return
	 */
	private static int getClassDiff(Class one, Class other) {
		int diff = -1;
		if(Number.class.isAssignableFrom(one) && other.equals(Object.class)) diff = 100;
		else if(other.isAssignableFrom(one)) diff = 0;
		else {
			int i = getClassIndex(one, classes);
			int j = getClassIndex(other, classes);
			if(i < 0 || j < 0) return -1;
			else return classDiff[i][j];
		}
		return diff;
	}

	/**
	 * 返回2组数据类型的差异指数之和
	 * @param one
	 * @param other
	 * @return
	 */
	private static int getClassDiff(Class[] one, Class[] other) {
		int i = one.length;
		int j = other.length;
		int t = 0;
		int d = 0;
		if(i == j) {
			for(int k = 0; k < i; k++) {
				d = getClassDiff(one[k], other[k]);
				if(d < 0) return d;
				else t += d;
			}
		} else {
			return -1;
		}
		return t;
	}

	/**
	 * 将参数数组转换为制定类型的对象数组
	 * @param originals
	 * @param types
	 * @return
	 */
	private static Object[] getApplicableArguments(Object[] originals, Class[] types) {
		int count = (originals == null) ? 0 : originals.length;
		Object[] result = new Object[count];
		Class type;
		if(originals != null) {
			for(int i = 0; i < count; i++) {
				if(types == null || i >= types.length) type = Object.class;
				else type = types[i];
				try {
					result[i] = convert(originals[i], type);
				} catch(Exception e) {
					result[i] = originals[i];
				}
			}
		}
		return result;
	}

	/**
	 * 将字符串转化为制定类型的对象
	 * @param value
	 * @param type
	 * @return
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	private static Object convertString(String value, Class type)
		throws NumberFormatException, ParseException
	{
		Object result = value;
		if(int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type)) {
			result = Integer.valueOf(value);
		} else if(boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
			result = Boolean.valueOf(value);
		} else if(float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)) {
			result = Float.valueOf(value);
		} else if(double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)) {
			result = Double.valueOf(value);
		} else if(long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type)) {
			result = Long.valueOf(value);
		} else if(char.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type)) {
			if(value.length() > 0) result = new Character(value.charAt(0));
		} else if(byte.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type)) {
			result = Byte.valueOf(value);
		} else if(short.class.isAssignableFrom(type) || Short.class.isAssignableFrom(type)) {
			result = Short.valueOf(value);
		} else if(BigDecimal.class.isAssignableFrom(type)) {
			result = new BigDecimal(Double.parseDouble(value));
		} else if(BigInteger.class.isAssignableFrom(type)) {
			result = new BigInteger(value);
		} else if(type.isArray()) {
			String[] values = value.split(",|;");
			Object[] objects = new Object[values.length];
			for(int i = 0; i < values.length; i++) {
				objects[i] = convert(values[i], type.getComponentType());
			}
			result = objects;
		} else if(Collection.class.isAssignableFrom(type)) {
			String[] values = value.split(",|;");
			Collection collection = new ArrayList();
			for(int i = 0; i < values.length; i++) {
				collection.add(values[i]);
			}
			Collection t = (Collection) result;
			t.addAll(collection);
		} else if(Map.class.isAssignableFrom(type)) {
			String[] values = value.split(",|;");
			Map map = new Hashtable();
			for(int i = 0; i < values.length; i++) {
			}
			Map t = (Map) result;
			t.putAll(map);
		} else if(Calendar.class.isAssignableFrom(type)) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(df.parse(value));
			result = calendar;
		} else if(Date.class.isAssignableFrom(type)) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			result = df.parse(value);
		}
		return result;
	}

	/**
	 * 将数字转化为制定类型的对象
	 * @param value
	 * @param type
	 * @return
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	private static Object convertNumber(Number value, Class type)
		throws NumberFormatException, ParseException
	{
		Object result = value;
		if(int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type)) {
			result = new Integer(value.intValue());
		} else if(boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
			result = new Boolean(value.longValue() != 0);
		} else if(float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)) {
			result = new Float(value.floatValue());
		} else if(double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)) {
			result = new Double(value.doubleValue());
		} else if(long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type)) {
			result = new Long(value.longValue());
		} else if(char.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type)) {
			result = new Character((char)value.intValue());
		} else if(byte.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type)) {
			result = new Byte(value.byteValue());
		} else if(short.class.isAssignableFrom(type) || Short.class.isAssignableFrom(type)) {
			result = new Short(value.shortValue());
		} else if(BigDecimal.class.isAssignableFrom(type)) {
			result = new BigDecimal(value.doubleValue());
		} else if(BigInteger.class.isAssignableFrom(type)) {
			result = new BigInteger(value.toString());
		}
		return result;
	}

	/**
	 * 将对象转化为BigDecimal类型
	 * @param value
	 * @return
	 */
	private static BigDecimal convertToBigDecimal(Object value) {
		double d;
		BigDecimal r = null;
		try {
			if(Double.class.isInstance(value)) {
				d = ((Double)value).doubleValue();
			} else {
				d = Double.parseDouble(String.valueOf(value));
			}
			r = new BigDecimal(d);
		} catch(NumberFormatException nfe) {
		}
		return r;
	}

	/**
	 * 将对象转化为BigInteger类型
	 * @param value
	 * @return
	 */
	private static BigInteger convertToBigInteger(Object value) {
		BigInteger r = null;
		try {
			r = new BigInteger(value.toString());
		} catch(NumberFormatException nfe) {
		}
		return r;
	}

	/**
	 * 根据制定类型转化对象
	 * @param source
	 * @param type
	 * @return
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	public static Object convert(Object source, Class type) 
		throws NumberFormatException, ParseException
	{
		Object result = source;
		if(String.class.isInstance(source)) {
			String value = (String) source;
			result = convertString(value, type);
		} else if(Number.class.isInstance(source)) {
			Number value = (Number) source;
			result = convertNumber(value, type);
		} else {
			if(type.equals(BigDecimal.class)) {
				result = convertToBigDecimal(source);
			} else if(type.equals(BigInteger.class)) {
				result = convertToBigInteger(source);
			}
		}
		return result;
	}

	/**
	 * 返回类中对应参数类型的构造子
	 * @param clazz
	 * @param argTypes
	 * @return
	 */
	public static Constructor getConstructor(Class clazz, Class[] argTypes)	{
		if(null == clazz) throw new NullPointerException();
		Constructor c1, c2 = null;
		int argLen = (argTypes == null) ? 0 : argTypes.length;
		try {
			c1 = clazz.getConstructor(argTypes);
		} catch(Exception e) {
			c1 = null;
		}
		if(c1 != null) return c1;
		Constructor[] ctors = null;
		Class superClazz = null;
		int b = -1, c, d;
		while(clazz != superClazz) {
			ctors = clazz.getConstructors();
			d = 0;
			c = -1;
			for(int i = 0; i < ctors.length; i++) {
				Class[] paramTypes = ctors[i].getParameterTypes();
				if(paramTypes.length != argLen) continue; 
				d = getClassDiff(argTypes, paramTypes);
				if(d > 0) {
					if(c < 0 || c > d) {
						c = d;
						c2 = ctors[i];
					}
				} else if(d == 0) {
					c = d;
					c2 = ctors[i];
					break;
				}
			}
			if(c > 0) {
				if(b < 0 || b > c) {
					b = c;
					c1 = c2;
				} 
			} else if(c == 0) {
				b = c;
				c1 = c2;
				break;
			}
			clazz = clazz.getSuperclass();
			if(clazz != null) superClazz = clazz.getSuperclass();
			else superClazz = clazz;
		}
		return c1;
	}

	/**
	 * 调用类名对应类的构造子
	 * @param name
	 * @param argValues
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object invokeConstructor(String name, Object[] argValues)
		throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		return invokeConstructor(findClass(name), null, argValues);
	}

	/**
	 * 调用类的构造子
	 * @param clazz
	 * @param argValues
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object invokeConstructor(Class clazz, Object[] argValues)
		throws InstantiationException, IllegalAccessException, InvocationTargetException
	{
		return invokeConstructor(clazz, null, argValues);
	}

	/**
	 * 按指定的参数类型调用类名对应类的构造子
	 * @param name
	 * @param argTypes
	 * @param argValues
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object invokeConstructor(String name, Class[] argTypes, Object[] argValues)
		throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		return invokeConstructor(findClass(name), argTypes, argValues);
	}

	/**
	 * 按指定的参数类型调用类的构造子
	 * @param clazz
	 * @param argTypes
	 * @param argValues
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object invokeConstructor(Class clazz, Class[] argTypes, Object[] argValues)
		throws InstantiationException, IllegalAccessException, InvocationTargetException
	{
		int length = (argValues == null) ? 0 : argValues.length;
		Class[] types = argTypes;
		if(types == null || types.length < length) {
			types = new Class[length];
			for(int i = 0; i < length; i++)
				types[i] = argValues[i].getClass();
		}
		Constructor ctor = getConstructor(clazz, types);
		return ctor.newInstance(argValues);
	}

	/**
	 * 返回对象中名字为name的方法
	 * @param anObject
	 * @param name
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Method getMethod(Object anObject, String name)
		throws NoSuchMethodException, SecurityException
	{
		return getMethod(anObject, name, null);
	}

	/**
	 * 返回对象中名字为name的方法
	 * @param anObject
	 * @param name
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Method getMethod(Object anObject, String name, Class[] argTypes)
		throws NoSuchMethodException, SecurityException
	{
		Class clazz = null;
		if(Class.class.isInstance(anObject)) clazz = (Class)anObject;
		else if(anObject != null) clazz = anObject.getClass();
		else clazz = null;
		return getMethod(clazz, name, argTypes);
	}

	/**
	 * 返回类中名字为name的方法
	 * @param clazz
	 * @param name
	 * @param argTypes
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Method getMethod(Class clazz, String name, Class[] argTypes)
		throws NoSuchMethodException, SecurityException
	{
		if(null == clazz || null == name) throw new NullPointerException();
		Method m1, m2 = null;
		int argLen = (argTypes == null) ? 0 : argTypes.length;
		try {
			m1 = clazz.getMethod(name, argTypes);
		} catch(Exception e) {
			m1 = null;
		}
		if(m1 != null) return m1;
		Method[] methods = null;
		Class superClazz = null;
		int b = -1, c, d;
		while(clazz != superClazz) {
			methods = clazz.getDeclaredMethods();
			d = 0;
			c = -1;
			for(int i = 0; i < methods.length; i++) {
				if(!name.equalsIgnoreCase(methods[i].getName())) continue;
				Class[] paramTypes = methods[i].getParameterTypes();
				if(paramTypes.length != argLen) continue; 
				d = getClassDiff(argTypes, paramTypes);
				if(d > 0) {
					if(c < 0 || c > d) {
						c = d;
						m2 = methods[i];
					}
				} else if(d == 0) {
					c = d;
					m2 = methods[i];
					break;
				}
			}
			if(c > 0) {
				if(b < 0 || b > c) {
					b = c;
					m1 = m2;
				} 
			} else if(c == 0) {
				b = c;
				m1 = m2;
				break;
			}
			clazz = clazz.getSuperclass();
			if(clazz != null) superClazz = clazz.getSuperclass();
			else superClazz = clazz;
		}
		return m1;
	}

	/**
	 * 返回对象中名字为name的方法
	 * @param anObject
	 * @param name
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object invokeMethod(Object anObject, String name)
		throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		return invokeMethod(anObject, name, null, null);
	}

	/**
	 * 调用对象的名字为name的方法
	 * @param anObject
	 * @param name
	 * @param argValues
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object invokeMethod(Object anObject, String name, Object[] argValues)
		throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		return invokeMethod(anObject, name, null, argValues);
	}

	/**
	 * 按指定的参数类型调用对象的名字为name的方法
	 * @param anObject
	 * @param name
	 * @param argTypes
	 * @param argValues
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object invokeMethod(Object anObject, String name, Class[] argTypes, Object[] argValues)
		throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		Method method = null;
		Object result = null;
		Class aClass = null;
		if(Class.class.isInstance(anObject))aClass = (Class)anObject;
		else if(anObject != null) aClass = anObject.getClass();
		else aClass = null; 
		if(argTypes == null || ( argValues != null && argTypes.length != argValues.length)){
			int parameterNumber;
			if(argValues == null) parameterNumber = 0;
			else parameterNumber = argValues.length;
			Class[] parameterTypes;
			if(parameterNumber <= 0) parameterTypes = null;
			else parameterTypes = new Class[parameterNumber];
			for(int i = 0; i < parameterNumber; i++) {
				parameterTypes[i] = argValues[i].getClass();
			}
			method = getMethod(aClass, name, parameterTypes);
		} else {
			method = getMethod(aClass, name, argTypes);
		}
		if(method == null) {
			throw new NoSuchMethodException();
		} else {
			method.setAccessible(true);
			Object[] arguments = getApplicableArguments(argValues, method.getParameterTypes());
			result = method.invoke(anObject, arguments);
		}
		return result;
	}

	/**
	 * 返回对象的属性类型
	 * @param anObject
	 * @param property
	 * @return
	 */
	public static Class getClazz(Object anObject, String property)
		throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		GetClassModule[] modules = new GetClassModule[]{
			new GetClassModule1(),
			new GetClassModule2(),
			new GetClassModule3(),
			new GetClassModule4(),
		};
		Class result = null;
		boolean found = false;
		for(int i = 0; i < modules.length; i++) {
			try {
				result = modules[i].getClazz(anObject, property);
				found = true;
				break;
			} catch(Exception e) {
			}
		}
		if(!found) throw new NoSuchMethodException("no such property: " + property + "!");
		return result;
	}

	/**
	 * 返回对象的类型,如果是原始类型则返回对应的包装类型
	 * @param anObject
	 * @return
	 */
	public static Class getWrappedClass(Object anObject)
	{
		Class clazz;
		if(Class.class.isInstance(anObject)) clazz = (Class)anObject;
		else if(anObject == null) clazz = null;
		else clazz = anObject.getClass();
		if(clazz != null) {
			if(clazz.isPrimitive()) {
				int i = indexOf(primitives, clazz);
				if(i >= 0 && i < wraps.length) clazz = wraps[i];
			} 
		}
		return clazz;
	}

	private static int indexOf(Object[] objs, Object o)
	{
		if(objs != null) {
			for(int i = 0; i < objs.length; i++) {
				if(objs[i] == o) return i;
			}
		}
		return -1;
	}

	/**
	 * 返回对象的属性值
	 * @param anObject
	 * @param property
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object get(Object anObject, String property)
		throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		GetModule[] modules = new GetModule[]{
			new GetModule1(),
			new GetModule2(),
			new GetModule3(),
			new GetModule4(),
		};
		Object result = null;
		boolean found = false;
		for(int i = 0; i < modules.length; i++) {
			try {
				result = modules[i].get(anObject, property);
				found = true;
				break;
			} catch(Exception e) {
			}
		}
		if(!found) throw new NoSuchMethodException("no such property: " + property + "!");
		return result;
	}

	/**
	 * 设置对象的属性值
	 * @param anObject
	 * @param property
	 * @param argValues
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object set(Object anObject, String property, Object[] argValues)
		throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException 
	{
		SetModule[] modules = new SetModule[]{
			new SetModule1(),
			new SetModule2(),
			new SetModule3(),
		};
		Object result = null;
		boolean found = false;
		for(int i = 0; i < modules.length; i++) {
			try {
				result = modules[i].set(anObject, property, argValues);
				found = true;
				break;
			} catch(Exception e) {
			}
		}
		if(!found) throw new NoSuchMethodException("no such property: " + property + "!");
		return result;
	}

	/**
	 * 返回对象中名字为name的域
	 * @param anObject
	 * @param name
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static Field getField(Object anObject, String name)
		throws NoSuchFieldException, SecurityException
	{
		return getField(anObject, name, null);
	}

	/**
	 * 返回对象中名字为name的域
	 * @param anObject
	 * @param name
	 * @param type
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static Field getField(Object anObject, String name, Class type)
		throws NoSuchFieldException, SecurityException
	{
		Class clazz = null;
		if(Class.class.isInstance(anObject)) clazz = (Class)anObject;
		else if(anObject != null) clazz = anObject.getClass();
		else clazz = null;
		return getField(clazz, name, type);
	}

	/**
	 * 返回类中名字为name的域
	 * @param clazz
	 * @param name
	 * @param type
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static Field getField(Class clazz, String name, Class type)
		throws NoSuchFieldException, SecurityException
	{
		if(null == clazz || null == name) throw new NullPointerException();
		Field field = null;
		try {
			field = clazz.getField(name);
		} catch(Exception e) {
			field = null;
		}
		Field[] fields = null;
		Class superClazz = null;
		while(field == null && clazz != superClazz) {
			fields = clazz.getDeclaredFields();
			for(int i = 0; i < fields.length; i++) {
				if(fields[i].getName().equals(name)
					&& (type == null
						|| (type != null
							&& type.isAssignableFrom(fields[i].getType())
				))) {
					field = fields[i];
					break;
				}
			}
			clazz = clazz.getSuperclass();
			superClazz = clazz.getSuperclass();
		}
		return field;
	}

	/**
	 * 返回对象的名字为name的域值
	 * @param anObject
	 * @param name
	 * @return
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 */
	public static Object getFieldValue(Object anObject, String name)
		throws NoSuchFieldException, IllegalAccessException, SecurityException
	{
		return getFieldValue(anObject, name, null);
	}

	/**
	 * 返回对象的名字为name的域值
	 * @param anObject
	 * @param name
	 * @param type
	 * @return
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 */
	public static Object getFieldValue(Object anObject, String name, Class type)
		throws NoSuchFieldException, IllegalAccessException, SecurityException
	{
		Field field = null;
		Class aClass = null;
		if(Class.class.isInstance(anObject))aClass = (Class) anObject;
		else if(anObject != null) aClass = anObject.getClass();
		else aClass = null; 
		field = getField(aClass, name, type);
		if(field != null) {
			field.setAccessible(true);
			return field.get(anObject);
		} 
		return null;
	}

	/**
	 * 设置对象的名字为name的域值
	 * @param anObject
	 * @param name
	 * @param value
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws ParseException
	 */
	public static void setFieldValue(Object anObject, String name, Object value)
		throws NoSuchFieldException, IllegalAccessException, SecurityException, ParseException
	{
		Field field = null;
		Class aClass = null;
		if(Class.class.isInstance(anObject))aClass = (Class) anObject;
		else if(anObject != null) aClass = anObject.getClass();
		else aClass = null; 
		field = getField(aClass, name, value.getClass());
		if(field != null) {
			field.setAccessible(true);
			value = convert(value, field.getType());
			field.set(anObject, value);
		} 
	}

	/**
	 * 在运行环境中根据名字查找类
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class findClass(String name) throws ClassNotFoundException {
		return findClass(name, null);
	}

	/**
	 * 在运行环境中根据名字查找类
	 * @param name
	 * @param packages
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class findClass(String name, String[] packages) throws ClassNotFoundException {
		FindClassModule[] modules = new FindClassModule[]{
			new FindClassModule1(),
			new FindClassModule2(),
			new FindClassModule3(),
			new FindClassModule4(),
		};
		Class clazz = null;
		boolean found = false;
		for(int i = 0; i < modules.length; i++) {
			try {
				clazz = modules[i].findClass(name, packages);
				found = true;
				break;
			} catch(ClassNotFoundException cnfe) {
			}
		}
		if(!found) throw new ClassNotFoundException("class " + name + " NOT found!");
		return clazz;
	}

	private ReflectHelper() {
	}

	/**
	 * 求属性值模块接口
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	public static interface GetModule {
		public Object get(Object anObject, String property) throws Exception;
	}

	/**
	 * 对xyz属性取object.getXyz()返回值
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	private static class GetModule1 implements GetModule {
		public Object get(Object anObject, String property) throws Exception {
			char ch = Character.toUpperCase(property.charAt(0));
			StringBuffer s = new StringBuffer();
			s.append("get");
			s.append(ch);
			s.append(property.substring(1));
			return invokeMethod(anObject, s.toString());
		}
	}

	/**
	 * 对xyz属性取object.getxyz()返回值
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	private static class GetModule2 implements GetModule {
		public Object get(Object anObject, String property) throws Exception {
			return invokeMethod(anObject, "get" + property);
		}
	}

	/**
	 * 对xyz属性取object.xyz域值
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	private static class GetModule3 implements GetModule {
		public Object get(Object anObject, String property) throws Exception {
			return getFieldValue(anObject, property);
		}
	}

	/**
	 * 数组类型"length"属性的特殊处理
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	private static class GetModule4 implements GetModule {
		public Object get(Object anObject, String property) throws Exception {
			Class clazz = anObject.getClass();
			Object r = null;
			if(clazz.isArray() && property.equals("length")) {
				Class compClazz = clazz.getComponentType();
				if(compClazz.isPrimitive()) {
					if(compClazz.equals(boolean.class)) {
						boolean[] array = (boolean[])anObject;
						r = new Integer(array.length);
					} else if(compClazz.equals(byte.class)) {
						byte[] array = (byte[])anObject;
						r = new Integer(array.length);
					} else if(compClazz.equals(char.class)) {
						char[] array = (char[])anObject;
						r = new Integer(array.length);
					} else if(compClazz.equals(double.class)) {
						double[] array = (double[])anObject;
						r = new Integer(array.length);
					} else if(compClazz.equals(float.class)) {
						float[] array = (float[])anObject;
						r = new Integer(array.length);
					} else if(compClazz.equals(int.class)) {
						int[] array = (int[])anObject;
						r = new Integer(array.length);
					} else if(compClazz.equals(long.class)) {
						long[] array = (long[])anObject;
						r = new Integer(array.length);
					} else if(compClazz.equals(short.class)) {
						short[] array = (short[])anObject;
						r = new Integer(array.length);
					} 
				} else {
					Object[] array = (Object[])anObject;
					r = new Integer(array.length);
				}
			} else {
				throw new Exception("object is not an array and property is not length.");
			}
			return r;
		}
	}

	/**
	 * 设置属性值模块接口
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	public static interface SetModule {
		public Object set(Object anObject, String property, Object[] argValues) throws Exception;
	}

	/**
	 * 调用object.setXyz(...)来设置xyz属性
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	private static class SetModule1 implements SetModule {
		public Object set(Object anObject, String property, Object[] argValues) throws Exception {
			char ch = Character.toUpperCase(property.charAt(0));
			StringBuffer s = new StringBuffer();
			s.append("set");
			s.append(ch);
			s.append(property.substring(1));
			return invokeMethod(anObject, s.toString(), argValues);
		}
	}

	/**
	 * 调用object.setxyz(...)来设置xyz属性
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	private static class SetModule2 implements SetModule {
		public Object set(Object anObject, String property, Object[] argValues) throws Exception {
			return invokeMethod(anObject, "set" + property, argValues);
		}
	}

	/**
	 * 对域object.xyz赋值来设置xyz属性
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	private static class SetModule3 implements SetModule {
		public Object set(Object anObject, String property, Object[] argValues) throws Exception {
			setFieldValue(anObject, property, argValues[0]);
			return null;
		}
	}

	/**
	 * 在运行环境中根据名字查找类的模块接口
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	public static interface FindClassModule {
		public Class findClass(String name, String[] packages) throws ClassNotFoundException;
	}

	/**
	 * 使用当前线程的类装载器查找
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	private static class FindClassModule1 implements FindClassModule {
		public Class findClass(String name, String[] packages) throws ClassNotFoundException {
			return Thread.currentThread().getContextClassLoader().loadClass(name);
		}
	}

	/**
	 * 使用Class类的静态方法forName查找
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	private static class FindClassModule2 implements FindClassModule {
		public Class findClass(String name, String[] packages) throws ClassNotFoundException {
			return Class.forName(name);
		}
	}

	/**
	 * 在传入的包名参数中查找
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	private static class FindClassModule3 implements FindClassModule {
		public Class findClass(String name, String[] packages) throws ClassNotFoundException {
			Class clazz = null;
			if(packages != null) {
				for(int i = 0; i < packages.length; i++) {
					try {
						clazz = Class.forName(packages[i] + "." + name);
						break;
					} catch(ClassNotFoundException cnfe) {
					}
				}
			}
			if(clazz == null) throw new ClassNotFoundException();
			return clazz;
		}
	}

	/**
	 * 在常用J2EE包中查找
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	private static class FindClassModule4 implements FindClassModule {
		public Class findClass(String name, String[] packages) throws ClassNotFoundException {
			Class clazz = null;
			String[] _packages = ReflectHelper.packages;
			for(int i = 0; i < _packages.length; i++) {
				try {
					clazz = Class.forName(_packages[i] + "." + name);
					break;
				} catch(ClassNotFoundException cnfe) {
				}
			}
			if(clazz == null) throw new ClassNotFoundException();
			return clazz;
		}
	}

	/**
	 * 求对象属性类型的模块接口
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	public static interface GetClassModule {
		public Class getClazz(Object anObject, String property) throws Exception;
	}

	/**
	 * 对xyz属性取object.getXyz()方法的返回类型
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	private static class GetClassModule1 implements GetClassModule {
		public Class getClazz(Object anObject, String property) throws Exception {
			char ch = Character.toUpperCase(property.charAt(0));
			StringBuffer s = new StringBuffer();
			s.append("get");
			s.append(ch);
			s.append(property.substring(1));
			Method m = getMethod(anObject, s.toString());
			return m.getReturnType();
		}
	}

	/**
	 * 对xyz属性取object.getxyz()方法的返回类型
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	private static class GetClassModule2 implements GetClassModule {
		public Class getClazz(Object anObject, String property) throws Exception {
			Method m = getMethod(anObject, "get" + property);
			return m.getReturnType();
		}
	}

	/**
	 * 对xyz属性取object.xyz域的类型
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	private static class GetClassModule3 implements GetClassModule {
		public Class getClazz(Object anObject, String property) throws Exception {
			Field f = getField(anObject, property);
			return f.getType();
		}
	}

	/**
	 * 数组类型"length"属性的特殊处理
	 * @author harry.sun
	 * mail: hysun@thorn-bird.com
	 * 2005-11-10
	 *
	 */
	private static class GetClassModule4 implements GetClassModule {
		public Class getClazz(Object anObject, String property) throws Exception {
			Class clazz;
			if(Class.class.isInstance(anObject)) clazz = (Class)anObject;
			else if(anObject != null) clazz = anObject.getClass();
			else clazz = null;
			if(clazz != null && clazz.isArray()) return Integer.class;
			else return null;
		}
	}

}
