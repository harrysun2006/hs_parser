/*
 * Created on 2005-11-9
 *
 */
package com.sophia.parser.yacc;

import java.lang.reflect.Method;
import java.util.List;

import com.sophia.parser.Constant;
import com.sophia.parser.Member;
import com.sophia.parser.ParseContext;
import com.sophia.parser.util.ReflectHelper;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-11-9
 *
 */
class ClassHelper {

	/**
	 * 确定2数算术运算的返回类型
	 * @param n1
	 * @param n2
	 * @return
	 */
	private final static Class getNumberClass(Class n1, Class n2) {
		if(Number.class.isAssignableFrom(n1) && Number.class.isAssignableFrom(n2)) {
			if(Double.class.isAssignableFrom(n1) || Float.class.isAssignableFrom(n1) || Double.class.isAssignableFrom(n2) || Float.class.isAssignableFrom(n2))
				return Double.class;
			else
				return Long.class;
		} else {
			return Constant.ERROR;
		}
	}

	/**
	 * 确定数的类型
	 * @param n
	 * @return
	 */
	private final static Class getNumberClass(Class n) {
		if(Double.class.isInstance(n) || Float.class.isInstance(n)) {
			return Double.class;
		}	else if(Number.class.isInstance(n)){
			return Long.class;
		} else {
			return Constant.ERROR;
		}
	}

	/**
	 * -a
	 * @param a
	 * @return
	 */
	final static Class uminus(Class a) {
		return (a != null && Number.class.isAssignableFrom(a)) ? a : Constant.ERROR;
	}

	/**
	 * +a
	 * @param a
	 * @return
	 */
	final static Class uplus(Class a) {
		return (a != null && Number.class.isAssignableFrom(a)) ? a : Constant.ERROR;
	}

	/**
	 * a+b,支持数字和字符串
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class add(Class a, Class b) {
		Class r;
		if(a == null || b == null) {
			r = Constant.ERROR;
		} else if(Number.class.isAssignableFrom(a) && Number.class.isAssignableFrom(b)) {
			r = getNumberClass(a, b);
		} else if(String.class.isAssignableFrom(a) && String.class.isAssignableFrom(b)) {
			r = String.class;
		} else {
			r = String.class;
		} 
		return r;
	}

	/**
	 * a-b
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class sub(Class a, Class b) {
		Class r;
		if(a == null || b == null) {
			r = Constant.ERROR;
		} else if(Number.class.isAssignableFrom(a) && Number.class.isAssignableFrom(b)) {
			r = getNumberClass(a, b);
		} else {
			r = Constant.ERROR;
		} 
		return r;
	}

	/**
	 * a*b
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class mul(Class a, Class b) {
		Class r;
		if(a == null || b == null) {
			r = Constant.ERROR;
		} else if(Number.class.isAssignableFrom(a) && Number.class.isAssignableFrom(b)) {
			r = getNumberClass(a, b);
		} else {
			r = Constant.ERROR;
		} 
		return r;
	}

	/**
	 * a/b
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class div(Class a, Class b) {
		Class r;
		if(a == null || b == null) {
			r = Constant.ERROR;
		} else if(Number.class.isAssignableFrom(a) && Number.class.isAssignableFrom(b)) {
			r = getNumberClass(a, b);
		} else {
			r = Constant.ERROR;
		} 
		return r;
	}

	/**
	 * a%b
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class mod(Class a, Class b) {
		Class r;
		if(a == null || b == null) {
			r = Constant.ERROR;
		} else if(Number.class.isAssignableFrom(a) && Number.class.isAssignableFrom(b)) {
			r = Long.class;
		} else {
			r = Constant.ERROR;
		} 
		return r;
	}

	/**
	 * a的b次方
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class power(Class a, Class b) {
		Class r;
		if(a == null || b == null) {
			r = Constant.ERROR;
		} else if(Number.class.isAssignableFrom(a) && Number.class.isAssignableFrom(b)) {
			r = getNumberClass(a, b);
		} else {
			r = Constant.ERROR;
		} 
		return r;
	}

	/**
	 * a<b?
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class lt(Class a, Class b) {
		Class r;
		if(a == null || b == null) {
			r = Constant.ERROR;
		} else if(Number.class.isAssignableFrom(a) && Number.class.isAssignableFrom(b)) {
			r = Boolean.class;
		} else if(String.class.isAssignableFrom(a) && String.class.isAssignableFrom(b)) {
			r = Boolean.class;
		} else {
			r = Constant.ERROR;
		}
		return r;
	}

	/**
	 * a>b?
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class gt(Class a, Class b) {
		Class r;
		if(a == null || b == null) {
			r = Constant.ERROR;
		} else if(Number.class.isAssignableFrom(a) && Number.class.isAssignableFrom(b)) {
			r = Boolean.class;
		} else if(String.class.isAssignableFrom(a) && String.class.isAssignableFrom(b)) {
			r = Boolean.class;
		} else {
			r = Constant.ERROR;
		}
		return r;
	}

	/**
	 * a<=b?
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class le(Class a, Class b) {
		Class r;
		if(a == null || b == null) {
			r = Constant.ERROR;
		} else if(Number.class.isAssignableFrom(a) && Number.class.isAssignableFrom(b)) {
			r = Boolean.class;
		} else if(String.class.isAssignableFrom(a) && String.class.isAssignableFrom(b)) {
			r = Boolean.class;
		} else {
			r = Constant.ERROR;
		}
		return r;
	}

	/**
	 * a>=b?
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class ge(Class a, Class b) {
		Class r;
		if(a == null || b == null) {
			r = Constant.ERROR;
		} else if(Number.class.isAssignableFrom(a) && Number.class.isAssignableFrom(b)) {
			r = Boolean.class;
		} else if(String.class.isAssignableFrom(a) && String.class.isAssignableFrom(b)) {
			r = Boolean.class;
		} else {
			r = Constant.ERROR;
		}
		return r;
	}

	/**
	 * a==b?
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class eq(Class a, Class b) {
		Class r;
		if(a == null || b == null) {
			r = Boolean.class;
		} else if(Number.class.isAssignableFrom(a) && Number.class.isAssignableFrom(b)) {
			r = Boolean.class;
		} else if(String.class.isAssignableFrom(a) && String.class.isAssignableFrom(b)) {
			r = Boolean.class;
		} else {
			r = Constant.ERROR;
		}
		return r;
	}

	/**
	 * a!=b?
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class ne(Class a, Class b) {
		Class r;
		if(a == null || b == null) {
			r = Boolean.class;
		} else if(Number.class.isAssignableFrom(a) && Number.class.isAssignableFrom(b)) {
			r = Boolean.class;
		} else if(String.class.isAssignableFrom(a) && String.class.isAssignableFrom(b)) {
			r = Boolean.class;
		} else {
			r = Constant.ERROR;
		}
		return r;
	}

	/**
	 * a~=b?(a == b || a.equalsIgnoreCase(b))
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class ie(Class a, Class b) {
		Class r;
		if(a == null || b == null) {
			r = Constant.ERROR;
		} else if(Number.class.isAssignableFrom(a) && Number.class.isAssignableFrom(b)) {
			r = Boolean.class;
		} else if(String.class.isAssignableFrom(a) && String.class.isAssignableFrom(b)) {
			r = Boolean.class;
		} else {
			r = Constant.ERROR;
		}
		return r;
	}

	/**
	 * a&&b
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class and(Class a, Class b) {
		Class r;
		if(a == null || b == null) {
			r = Constant.ERROR;
		} else if(Boolean.class.isAssignableFrom(a) && Boolean.class.isAssignableFrom(b)) {
			r = Boolean.class;
		} else {
			r = Constant.ERROR;
		}
		return r;
	}

	/**
	 * a||b
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class or(Class a, Class b) {
		Class r;
		if(a == null || b == null) {
			r = Constant.ERROR;
		} else if(Boolean.class.isAssignableFrom(a) && Boolean.class.isAssignableFrom(b)) {
			r = Boolean.class;
		} else {
			r = Constant.ERROR;
		}
		return r;
	}

	/**
	 * !a
	 * @param a
	 * @return
	 */
	final static Class not(Class a) {
		return (a != null && Boolean.class.isAssignableFrom(a)) ? Boolean.class : Constant.ERROR;
	}

	/**
	 * e1, e2, ... ..., en
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class exprs(Class a, Class b) {
		return List.class;
	}

	/**
	 * 数组(e1, e2, ... ..., en) or [e1, e2, ... ..., en]
	 * @param a
	 * @return
	 */
	final static Class array(Class a) {
		return Object[].class;
	}

	/**
	 * 列表{e1, e2, ... ..., en}
	 * @param a
	 * @return
	 */
	final static Class list(Class a) {
		return List.class;
	}

	/**
	 * 成员: 方法或属性
	 * @param a
	 * @param b
	 * @return
	 */
	final static Class member(Class a, Class b) {
		return Member.class;
	}

	/**
	 * 调用成员: 取方法或属性值的类型
	 * @param a expr's class
	 * @param b token(value is member)
	 * @return
	 */
	final static Class call(Class a, Object b) {
		Class r;
		if(a == null) r = Constant.ERROR;
		else {
			try {
				if(Member.class.isInstance(b)) {
					Member member = (Member)b;
					if(member.getParamTypes() == null) {
						r = ReflectHelper.getClazz(a, member.getName()); 	//invoke property
					} else {
						Method m = ReflectHelper.getMethod(a, member.getName(), member.getParamTypes());	//invoke method
						r = m.getReturnType();
					}
				} else if(String.class.isInstance(b)) {
					r = ReflectHelper.getClazz(a, b.toString()); 	//invoke property
				} else {
					r = Constant.ERROR;
				}
			} catch(Exception e) {
				r = Constant.ERROR;
			}
		}
		r = ReflectHelper.getWrappedClass(r);
		return r;
	}

	/**
	 * 构造子: new Object(p1, p2);
	 * @param o
	 * @return
	 */
	final static Class construct(Object o) {
		Class r;
		try {
			if(Member.class.isInstance(o)) {
				Member member = (Member)o;
				r = ReflectHelper.findClass(member.getName());	//find the class by name
			} else {
				r = Constant.ERROR;
			}
		} catch(Exception e) {
			r = Constant.ERROR;
		}
		return r;
	}

	/**
	 * Boolean(true, false), null(null)或根据上下文取对象的类型
	 * @param o
	 * @param context
	 * @return
	 */
	final static Class name(Object o, ParseContext context) {
		Class r;
		if(String.class.isInstance(o)) {
			String name = (String)o;
			if("true".equalsIgnoreCase(name)) {
				r = Boolean.class;
			} else if("false".equalsIgnoreCase(name)) {
				r = Boolean.class;
			} else if("null".equalsIgnoreCase(name)) {
				r = null;
			} else if(context != null) {
				Object value = context.get(name);
				if(value == null) r = String.class;
				else if(Class.class.isInstance(value)) r = (Class)value;
				else r = value.getClass();
			} else {
				r = String.class;
			}
		} else {
			r = Constant.ERROR;
		}
		return r;
	}

	/**
	 * 赋值
	 * Date b = new Date();
	 * d = new Double(9.9);
	 * int i = 3;
	 * @param a the class name
	 * @param b the object value
	 */
	final static Class set(Object a, Object b) {
		Class r = Constant.ERROR;
		try {
			if(a != null) r = ReflectHelper.findClass(a.toString());
		} catch(Exception e) {
		}
		if(r == null && b != null) r = b.getClass();
		return r;
	}
}
