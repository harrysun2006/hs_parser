/*
 * Created on 2005-11-1
 *
 */
package com.sophia.parser.yacc;

import java.util.ArrayList;
import java.util.List;

import com.sophia.parser.Constant;
import com.sophia.parser.Member;
import com.sophia.parser.ParseContext;
import com.sophia.parser.ParseException;
import com.sophia.parser.Parser;
import com.sophia.parser.Variable;
import com.sophia.parser.util.ReflectHelper;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-11-1
 *
 */
class ValueHelper {

	/**
	 * 确定2数算术运算的返回类型
	 * @param n1
	 * @param n2
	 * @return
	 */
	private final static Class getNumberClass(Number n1, Number n2) {
		if(Double.class.isInstance(n1) || Float.class.isInstance(n1) || Double.class.isInstance(n2) || Float.class.isInstance(n2))
			return Double.class;
		else
			return Long.class;
	}

	/**
	 * 确定数的类型
	 * @param n
	 * @return
	 */
	private final static Class getNumberClass(Number n) {
		if(Double.class.isInstance(n) || Float.class.isInstance(n)) {
			return Double.class;
		}	else {
			return Long.class;
		}
	}

	/**
	 * -o
	 * @param o
	 * @return
	 */
	final static Object uminus(Object o) {
		Object r = null;
		if(Number.class.isInstance(o)) {
			Number n = (Number)o;
			final Class clazz = getNumberClass(n);
			if(Double.class.equals(clazz)) r = new Double(-n.doubleValue());
			else r = new Long(-n.longValue());
		} else if(o == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.number.expect");
		}
		return r;
	}

	/**
	 * +o
	 * @param o
	 * @return
	 */
	final static Object uplus(Object o) {
		Object r = null;
		if(Number.class.isInstance(o)) {
			r = o;
		} else if(o == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.number.expect");
		}
		return r;
	}

	/**
	 * a+b,支持数字和字符串
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object add(Object a, Object b) {
		Object r = null;
		if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
			final Number n1 = (Number)a;
			final Number n2 = (Number)b;
			final Class clazz = getNumberClass(n1, n2);
			if(Double.class.equals(clazz)) r = new Double(n1.doubleValue() + n2.doubleValue());
			else r = new Long(n1.longValue() + n2.longValue());
		} else if(String.class.isInstance(a) && String.class.isInstance(b)) {
			final String s1 = (String)a;
			final String s2 = (String)b;
			r = s1 + s2;
		} else if(a == Constant.PENDING || b == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = String.valueOf(a) + String.valueOf(b);
		} 
		return r;
	}

	/**
	 * a-b
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object sub(Object a, Object b) {
		Object r = null;
		if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
			final Number n1 = (Number) a;
			final Number n2 = (Number) b;
			final Class clazz = getNumberClass(n1, n2);
			if(Double.class.equals(clazz)) r = new Double(n1.doubleValue() - n2.doubleValue());
			else r = new Long(n1.longValue() - n2.longValue());
		} else if(a == Constant.PENDING || b == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.number.expect");
		} 
		return r;
	}

	/**
	 * a*b
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object mul(Object a, Object b) {
		Object r = null;
		if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
			final Number n1 = (Number) a;
			final Number n2 = (Number) b;
			final Class clazz = getNumberClass(n1, n2);
			if(Double.class.equals(clazz)) r = new Double(n1.doubleValue() * n2.doubleValue());
			else r = new Long(n1.longValue() * n2.longValue());
		} else if(a == Constant.PENDING || b == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.number.expect");
		} 
		return r;
	}

	/**
	 * a/b
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object div(Object a, Object b) {
		Object r = null;
		if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
			final Number n1 = (Number) a;
			final Number n2 = (Number) b;
			final Class clazz = getNumberClass(n1, n2);
			//if(Double.class.equals(clazz)) r = new Double(n1.doubleValue() / n2.doubleValue());
			//else r = new Long(n1.longValue() / n2.longValue());
			r = new Double(n1.doubleValue() / n2.doubleValue());
		} else if(a == Constant.PENDING || b == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.number.expect");
		} 
		return r;
	}

	/**
	 * a%b
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object mod(Object a, Object b) {
		Object r = null;
		if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
			final Number n1 = (Number) a;
			final Number n2 = (Number) b;
			final Class clazz = getNumberClass(n1, n2);
			//if(Double.class.equals(clazz)) r = new Double(n1.doubleValue() % n2.doubleValue());
			//else r = new Long(n1.longValue() % n2.longValue());
			r = new Long(n1.longValue() % n2.longValue());
		} else if(a == Constant.PENDING || b == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.number.expect");
		} 
		return r;
	}

	/**
	 * a的b次方
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object power(Object a, Object b) {
		Object r = null;
		if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
			final Number n1 = (Number) a;
			final Number n2 = (Number) b;
			Double d = new Double(Math.pow(n1.doubleValue(), n2.doubleValue()));
			final Class clazz = getNumberClass(n1, n2);
			if(Double.class.equals(clazz)) r = d;
			else r = new Long(d.longValue());
		} else if(a == Constant.PENDING || b == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.number.expect");
		} 
		return r;
	}

	/**
	 * a<b?
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object lt(Object a, Object b) {
		Object r = null;
		if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
			final Number n1 = (Number)a;
			final Number n2 = (Number)b;
			r = new Boolean(n1.doubleValue() < n2.doubleValue());
		} else if(String.class.isInstance(a) && String.class.isInstance(b)) {
			final String s1 = (String)a;
			final String s2 = (String)b;
			r = new Boolean(s1.compareTo(s2) < 0);
		} else if(a == Constant.PENDING || b == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.number.or.string.expect");
		}
		return r;
	}

	/**
	 * a>b?
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object gt(Object a, Object b) {
		Object r = null;
		if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
			final Number n1 = (Number)a;
			final Number n2 = (Number)b;
			r = new Boolean(n1.doubleValue() > n2.doubleValue());
		} else if(String.class.isInstance(a) && String.class.isInstance(b)) {
			final String s1 = (String)a;
			final String s2 = (String)b;
			r = new Boolean(s1.compareTo(s2) > 0);
		} else if(a == Constant.PENDING || b == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.number.or.string.expect");
		}
		return r;
	}

	/**
	 * a<=b?
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object le(Object a, Object b) {
		Object r = null;
		if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
			final Number n1 = (Number)a;
			final Number n2 = (Number)b;
			r = new Boolean(n1.doubleValue() <= n2.doubleValue());
		} else if(String.class.isInstance(a) && String.class.isInstance(b)) {
			final String s1 = (String)a;
			final String s2 = (String)b;
			r = new Boolean(s1.compareTo(s2) <= 0);
		} else if(a == Constant.PENDING || b == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.number.or.string.expect");
		}
		return r;
	}

	/**
	 * a>=b?
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object ge(Object a, Object b) {
		Object r = null;
		if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
			final Number n1 = (Number)a;
			final Number n2 = (Number)b;
			r = new Boolean(n1.doubleValue() >= n2.doubleValue());
		} else if(String.class.isInstance(a) && String.class.isInstance(b)) {
			final String s1 = (String)a;
			final String s2 = (String)b;
			r = new Boolean(s1.compareTo(s2) >= 0);
		} else if(a == Constant.PENDING || b == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.number.or.string.expect");
		}
		return r;
	}

	/**
	 * a==b?
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object eq(Object a, Object b) {
		Object r = null;
		if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
			final Number n1 = (Number)a;
			final Number n2 = (Number)b;
			r = new Boolean(n1.doubleValue() == n2.doubleValue());
		} else if(String.class.isInstance(a) && String.class.isInstance(b)) {
			final String s1 = (String)a;
			final String s2 = (String)b;
			r = new Boolean(s1.equals(s2));
		} else if(a == null && b != null) {
			r = new Boolean(false);
		} else if(a != null && b == null) {
			r = new Boolean(false);
		} else if(a == null && b == null) {
			r = new Boolean(true);
		} else if(a == Constant.PENDING || b == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.number.or.string.expect");
		}
		return r;
	}

	/**
	 * a!=b?
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object ne(Object a, Object b) {
		Object r = null;
		if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
			final Number n1 = (Number)a;
			final Number n2 = (Number)b;
			r = new Boolean(n1.doubleValue() != n2.doubleValue());
		} else if(String.class.isInstance(a) && String.class.isInstance(b)) {
			final String s1 = (String)a;
			final String s2 = (String)b;
			r = new Boolean(s1.compareTo(s2) != 0);
		} else if(a == null && b != null) {
			r = new Boolean(true);
		} else if(a != null && b == null) {
			r = new Boolean(true);
		} else if(a == null && b == null) {
			r = new Boolean(false);
		} else if(a == Constant.PENDING || b == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.number.or.string.expect");
		}
		return r;
	}

	/**
	 * a~=b?(a == b || a.equalsIgnoreCase(b))
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object ie(Object a, Object b) {
		Object r = null;
		if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
			final Number n1 = (Number)a;
			final Number n2 = (Number)b;
			r = new Boolean(n1.doubleValue() == n2.doubleValue());
		} else if(String.class.isInstance(a) && String.class.isInstance(b)) {
			final String s1 = (String)a;
			final String s2 = (String)b;
			r = new Boolean(s1.equalsIgnoreCase(s2));
		} else if(a == Constant.PENDING || b == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.number.or.string.expect");
		}
		return r;
	}

	/**
	 * a&&b
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object and(Object a, Object b) {
		Object r = null;
		if(Boolean.class.isInstance(a) && Boolean.class.isInstance(b)) {
			final Boolean b1 = (Boolean) a;
			final Boolean b2 = (Boolean) b;
			r = new Boolean(b1.booleanValue() && b2.booleanValue());
		} else if(a == Constant.PENDING || b == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.boolean.expect");
		}
		return r;
	}

	/**
	 * a||b
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object or(Object a, Object b) {
		Object r = null;
		if(Boolean.class.isInstance(a) && Boolean.class.isInstance(b)) {
			final Boolean b1 = (Boolean) a;
			final Boolean b2 = (Boolean) b;
			r = new Boolean(b1.booleanValue() || b2.booleanValue());
		} else if(a == Constant.PENDING || b == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.boolean.expect");
		}
		return r;
	}

	/**
	 * !o
	 * @param o
	 * @return
	 */
	final static Object not(Object o) {
		Object r = null;
		if(Boolean.class.isInstance(o)) {
			final Boolean b = (Boolean) o;
			r = new Boolean(!b.booleanValue());
		} else if(o == Constant.PENDING) {
			r = Constant.PENDING;
		} else {
			r = new ParseException("parse.boolean.expect");
		}
		return r;
	}

	/**
	 * e1, e2, ... ..., en
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object exprs(Object a, Object b) {
		List r = null;
		if(List.class.isInstance(a)) {
			r = (List)a;
		} else {
			r = new ArrayList();
		} 
		r.add(b);
		return r;
	}

	/**
	 * 数组(e1, e2, ... ..., en) or [e1, e2, ... ..., en]
	 * @param o
	 * @return
	 */
	final static Object array(Object o) {
		Object[] r = null;
		if(List.class.isInstance(o)) {
			List list = (List)o;
			r = list.toArray();
		} else {
			r = new Object[0];
		}
		return r;
	}

	/**
	 * 列表{e1, e2, ... ..., en}
	 * @param o
	 * @return
	 */
	final static Object list(Object o) {
		Object r = null;
		if(List.class.isInstance(o)) r = (List)o;
		else r = new ArrayList();
		return r;
	}

	/**
	 * 成员: 方法或属性
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object member(Object a, Object b) {
		Object r = null;
		Member member = new Member();
		r = member;
		if(a == null || a == Constant.PENDING) r = new ParseException("parse.name.expect");
		else member.setName(a.toString());
		if(b == null) member.setParams(null);
		else if(Object[].class.isInstance(b)) member.setParams((Object[])b);
		else if(List.class.isInstance(b)) member.setParams(((List)b).toArray());
		else r = new ParseException("parse.null.or.params.expect");
		Object[] objs = member.getParams();
		if(objs != null) {
			Class[] classes = new Class[objs.length];
			for(int i = 0; i < objs.length; i++) classes[i] = objs[i].getClass();
			member.setParamTypes(classes);
		}
		return r;
	}

	/**
	 * 调用成员: 求方法或属性值
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object call(Object a, Object b) {
		if(a != null) return call(a.getClass(), a, b);
		else return call(null, a, b);
	}

	/**
	 * 调用成员: 求方法或属性值
	 * @param c
	 * @param a
	 * @param b
	 * @return
	 */
	final static Object call(Class clazz, Object a, Object b) {
		Object r = null;
		Object v = a;
		if(a == Constant.PENDING) {
			if(clazz == null) r = new ParseException("parse.object.expect");
			else v = clazz;	// 求静态方法或属性值
		}
		if(a == null) {
			r = new ParseException("parse.object.expect");
		} else {
			try {
				if(Member.class.isInstance(b)) {
					Member member = (Member)b;
					if(member.getParams() == null) {
						r = ReflectHelper.get(v, member.getName()); 	//invoke property
					} else if(Constant.hasPending(member.getParams())) {
						r = Constant.PENDING;
					} else {
						r = ReflectHelper.invokeMethod(v, member.getName(), member.getParams());	//invoke method
					}
				} else if(String.class.isInstance(b)) {
					r = ReflectHelper.get(v, b.toString()); 	//invoke property
				} else {
					r = new ParseException("parse.member.expect");
				}
			} catch(Exception e) {
				if(a == Constant.PENDING) r = Constant.PENDING;
				else r = new ParseException("parse.call.error", e);
			}
		}
		return r;
	}

	/**
	 * 构造子: new Object(p1, p2);
	 * @param o
	 * @return
	 */
	final static Object construct(Object o) {
		Object r = o;
		try {
			if(Member.class.isInstance(o)) {
				Member member = (Member)o;
				if(Constant.isPending(member.getParams())) r = Constant.PENDING;
				else r = ReflectHelper.invokeConstructor(member.getName(), member.getParams());	//invoke constructor
			} else {
				r = new ParseException("parse.member.expect");
			}
		} catch(Exception e) {
			r = new ParseException("parse.construct.error", e);
		}
		return r;
	}

	/**
	 * true, false, null或根据上下文取对象
	 * @param o
	 * @param context
	 * @return
	 */
	final static Object name(Object o, ParseContext context, Parser parser) {
		Object r;
		if(String.class.isInstance(o)) {
			String name = (String)o;
			if(context != null && parser.isReserved(name) && context.containsKey(name)) {
				r = new ParseException("parse.reserved.error");
			} else if("true".equalsIgnoreCase(name)) {
				r = new Boolean(true);
			} else if("false".equalsIgnoreCase(name)) {
				r = new Boolean(false);
			} else if("null".equalsIgnoreCase(name)) {
				r = null;
			} else if(context != null) {
				r = context.get(name);
				if(r == null) r = name;
			} else {
				r = name;
			}
		} else {
			r = new ParseException("parse.name.expect");
		}
		return r;
	}

	/**
	 * 赋值
	 * Date b = new Date();
	 * d = new Double(9.9);
	 * int i = 3;
	 * @param a the class name
	 * @param b the object name
	 * @param c the object value
	 */
	final static Object set(Object a, Object b, Object c, ParseContext context) {
		Class clazz = null;
		String name = b.toString();
		Variable variable;
		try {
			if(a != null) clazz = ReflectHelper.findClass(a.toString());
		} catch(Exception e) {
			clazz = null;
		}
		variable = new Variable(name, c, clazz);
		context.put(name, variable);
		return c;
	}
}
