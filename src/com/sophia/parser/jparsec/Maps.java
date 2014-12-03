/*
 * Created on 2005-11-2
 *
 */
package com.sophia.parser.jparsec;

import java.util.ArrayList;
import java.util.List;

import jfun.parsec.Map;
import jfun.parsec.Map2;
import jfun.parsec.Map3;

import com.sophia.parser.Member;
import com.sophia.parser.util.ReflectHelper;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-11-2
 *
 */
class Maps {

	public static final Map id = jfun.parsec.Maps.id();

	public static final Map negate = new Map() {
		public Object map(final Object o) {
			return new Double(-((Number)o).doubleValue());
		}
	};

	public static final Map2 add = new Map2() {
		public Object map(final Object a, final Object b) {
			if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
				final Number n1 = (Number)a;
				final Number n2 = (Number)b;
				return new Double(n1.doubleValue() + n2.doubleValue());
			} else if(String.class.isInstance(a) && String.class.isInstance(b)) {
				final String s1 = (String)a;
				final String s2 = (String)b;
				return s1 + s2;
			} else {
				throw new IllegalArgumentException("wrong!");
			} 
		}
	};

	public static final Map2 sub = new Map2() {
		public Object map(final Object a, final Object b) {
			final Number n1 = (Number) a;
			final Number n2 = (Number) b;
			return new Double(n1.doubleValue() - n2.doubleValue());
		}
	};

	public static final Map2 mul = new Map2() {
		public Object map(final Object a, final Object b) {
			final Number n1 = (Number) a;
			final Number n2 = (Number) b;
			return new Double(n1.doubleValue() * n2.doubleValue());
		}
	};

	public static final Map2 div = new Map2() {
		public Object map(final Object a, final Object b) {
			final Number n1 = (Number) a;
			final Number n2 = (Number) b;
			return new Double(n1.doubleValue() / n2.doubleValue());
		}
	};

	public static final Map2 mod = new Map2() {
		public Object map(final Object a, final Object b) {
			final Number n1 = (Number) a;
			final Number n2 = (Number) b;
			return new Long(n1.longValue() % n2.longValue());
		}
	};

	public static final Map2 power = new Map2() {
		public Object map(final Object a, final Object b) {
			final Number n1 = (Number) a;
			final Number n2 = (Number) b;
			return new Double(Math.pow(n1.doubleValue(), n2.doubleValue()));
		}
	};

	public static final Map2 lt = new Map2() {
		public Object map(final Object a, final Object b) {
			if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
				final Number n1 = (Number)a;
				final Number n2 = (Number)b;
				return new Boolean(n1.doubleValue() < n2.doubleValue());
			} else if(String.class.isInstance(a) && String.class.isInstance(b)) {
				final String s1 = (String)a;
				final String s2 = (String)b;
				return new Boolean(s1 != null && s1.compareTo(s2) < 0);
			} else {
				throw new IllegalArgumentException("wrong!");
			}
		}
	};

	public static final Map2 gt = new Map2() {
		public Object map(final Object a, final Object b) {
			if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
				final Number n1 = (Number)a;
				final Number n2 = (Number)b;
				return new Boolean(n1.doubleValue() > n2.doubleValue());
			} else if(String.class.isInstance(a) && String.class.isInstance(b)) {
				final String s1 = (String)a;
				final String s2 = (String)b;
				return new Boolean(s1 != null && s1.compareTo(s2) > 0);
			} else {
				throw new IllegalArgumentException("wrong!");
			}
		}
	};

	public static final Map2 le = new Map2() {
		public Object map(final Object a, final Object b) {
			if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
				final Number n1 = (Number)a;
				final Number n2 = (Number)b;
				return new Boolean(n1.doubleValue() <= n2.doubleValue());
			} else if(String.class.isInstance(a) && String.class.isInstance(b)) {
				final String s1 = (String)a;
				final String s2 = (String)b;
				return new Boolean(s1 != null && s1.compareTo(s2) <= 0);
			} else if(a == null && b == null) {
				return new Boolean(true);
			} else {
				throw new IllegalArgumentException("wrong!");
			}
		}
	};

	public static final Map2 ge = new Map2() {
		public Object map(final Object a, final Object b) {
			if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
				final Number n1 = (Number)a;
				final Number n2 = (Number)b;
				return new Boolean(n1.doubleValue() >= n2.doubleValue());
			} else if(String.class.isInstance(a) && String.class.isInstance(b)) {
				final String s1 = (String)a;
				final String s2 = (String)b;
				return new Boolean(s1 != null && s1.compareTo(s2) >= 0);
			} else if(a == null && b == null) {
				return new Boolean(true);
			} else {
				throw new IllegalArgumentException("wrong!");
			}
		}
	};

	public static final Map2 eq = new Map2() {
		public Object map(final Object a, final Object b) {
			if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
				final Number n1 = (Number)a;
				final Number n2 = (Number)b;
				return new Boolean(n1.doubleValue() == n2.doubleValue());
			} else if(String.class.isInstance(a) && String.class.isInstance(b)) {
				final String s1 = (String)a;
				final String s2 = (String)b;
				return new Boolean(s1 != null && s1.equals(s2));
			} else if(a == null && b == null) {
				return new Boolean(true);
			} else {
				throw new IllegalArgumentException("wrong!");
			}
		}
	};

	public static final Map2 ne = new Map2() {
		public Object map(final Object a, final Object b) {
			if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
				final Number n1 = (Number)a;
				final Number n2 = (Number)b;
				return new Boolean(n1.doubleValue() != n2.doubleValue());
			} else if(String.class.isInstance(a) && String.class.isInstance(b)) {
				final String s1 = (String)a;
				final String s2 = (String)b;
				return new Boolean(s1 != null && s1.compareTo(s2) != 0);
			} else if(a == null && b == null) {
				return new Boolean(false);
			} else {
				throw new IllegalArgumentException("wrong!");
			}
		}
	};

	public static final Map2 ie = new Map2() {
		public Object map(final Object a, final Object b) {
			if(Number.class.isInstance(a) && Number.class.isInstance(b)) {
				final Number n1 = (Number)a;
				final Number n2 = (Number)b;
				return new Boolean(n1.doubleValue() == n2.doubleValue());
			} else if(String.class.isInstance(a) && String.class.isInstance(b)) {
				final String s1 = (String)a;
				final String s2 = (String)b;
				return new Boolean(s1 != null && s1.equalsIgnoreCase(s2));
			} else if(a == null && b == null) {
				return new Boolean(true);
			} else {
				throw new IllegalArgumentException("wrong!");
			}
		}
	};

	public static final Map2 and = new Map2() {
		public Object map(final Object a, final Object b) {
			final Boolean b1 = (Boolean) a;
			final Boolean b2 = (Boolean) b;
			return new Boolean(b1.booleanValue() && b2.booleanValue());
		}
	};

	public static final Map2 or = new Map2() {
		public Object map(final Object a, final Object b) {
			final Boolean b1 = (Boolean) a;
			final Boolean b2 = (Boolean) b;
			return new Boolean(b1.booleanValue() || b2.booleanValue());
		}
	};

	public static final Map not = new Map() {
		public Object map(final Object o) {
			return new Boolean(!((Boolean)o).booleanValue());
		}
	};

	public static final Map3 list = new Map3() {
		public Object map(final Object a, final Object b, final Object c) {
			List list = new ArrayList();
			if(Object[].class.isInstance(b)) {
				Object[] array = (Object[])b;
				for(int i = 0; i < array.length; i++) {
					list.add(array[i]);
				}
			} else {
				list.add(b);
			}
			return list;
		}
	};

	public static final Map2 call = new Map2() {
		public Object map(final Object a, final Object b) {
			Object r = null;
			try {
				if(Member.class.isInstance(a)) throw new IllegalArgumentException("wrong!");
				if(Member.class.isInstance(b)) {
					Member member = (Member)b;
					if(member.getParams() == null) {
						r = ReflectHelper.get(a, member.getName()); 	//invoke property
					} else {
						r = ReflectHelper.invokeMethod(a, member.getName(), member.getParams());	//invoke method
					}
				} else if(String.class.isInstance(b)) {
					r = ReflectHelper.get(a, (String)b); 	//invoke property
				} else {
					throw new IllegalArgumentException("wrong!");
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			return r;
		}
	};

	public static final Map2 member = new Map2() {
		public Object map(final Object a, final Object b) {
			Member member = new Member();
			try {
				member.setName(a.toString());
				if(Object[].class.isInstance(b)) member.setParams((Object[])b);
				else member.setParams(null);
			} catch(Exception e) {
				e.printStackTrace();
			}
			return member;
		}
	};

}
