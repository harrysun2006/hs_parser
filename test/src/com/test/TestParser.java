/*
 * Created on 2005-10-18
 *
 */
package com.test;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import java.text.MessageFormat;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.test.predicate.IsEqual;
import com.test.predicate.IsLowercase;
import com.test.predicate.OrPredicate;
import com.test.predicate.Predicate;

import com.sophia.parser.*;
import com.sophia.parser.util.DebugHelper;
import com.sophia.parser.util.ResourceHelper;


/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-10-18
 *
 */
public class TestParser {

	private static Log log = LogFactory.getLog(TestParser.class);
	private final static String baseDir = System.getProperty("user.dir");
	private final static String scriptDir = baseDir + "/bin/resource/scripts/";

	private final static ParseContext context = new ParseContext();

	static {
		context.put("x", new Integer(64));
		context.put("y", new Integer(36));
		context.put("d1", new Double(11.1));
		context.put("d2", new Double(22.2));
		context.put("f1", new Float(33.3f));
		context.put("f2", new Float(44.4f));
		context.put("i1", new Integer(55));
		context.put("i2", new Integer(66));
		context.put("l1", new Long(77));
		context.put("l2", new Long(88));
		context.put("s1", new Short((short)1));
		context.put("s2", new Short((short)2));
		context.put("min", new Integer(0));
		context.put("max", new Integer(100));
		context.put("math", Math.class);
		context.put("product", new Product("PDI13", "13mm冲击钻", Product.Quality.BEST));
		context.put("p1", Product.class);
		context.put("p2", Product.class);
		context.put("p3", Product.class);
		context.put("count", new Integer(20000));
		context.put("log", log);
		context.put("module", "");
		//context.put("true", new Boolean(true));
	}

	private final static String[] exprs1 = new String[] {
		"2 + 3*(5+6)",
		"-5+3*(3-2)",
		"3*(2+8)",
		"-2--6*-2",
		"1.5+2*-3-4*(5-6)+7/(8-9)",
		"1.5+2*-3-4*(5-6)+7/(8-9)-10/(11+12-13)*14-15-16-17+18+19+20",
		"+--3.6+--+6.4",
		"quit"
	};

	private final static String[] exprs2 = new String[] {
		"5.2>3&&1==2",
		"3>=4||!(5>6)",
		"3+2>=4+1*(3.5-2.5)",
		"5/6>7/8||true",
		"1.5+2*-3-4*(5-6)+7/(8-9)-10/(11+12-13)*14-15-16-17+18+19+20==-12.5",
		"x>min&&x<max",
		"1.5+2*-3-4*(5-6)+7/(8-9)-10/(11+12-13)*14-15-16-17+18+19+20",
		"(max-x)/(x-min)",
		"'hello' ~= 'Hello'",
		"'hello'+' world!'",
		"'Hello' < 'hello'",
		"product.code",
		"math.min(4, 12)",
		"math.min(5.5,6.5)-math.max(3.3,4.4)+math.min(math.sqrt(x),math.sqrt(y))",
		"math.min(x, y)",
		"math.min(d1,d2)",
		"math.min(f1,f2)",
		"math.min(i1,i2)",
		"math.min(l1,l2)",
		"math.min(s1,s2)",
		"math.max(d1,f1)+math.max(d1,i1)+math.max(d1,l1)+math.max(d1,s1)",
		"math.max(f1,d1)+math.max(f1,i1)+math.max(f1,l1)+math.max(f1,s1)",
		"math.max(i1,d1)+math.max(i1,f1)+math.max(i1,l1)+math.max(i1,s1)",
		"math.max(l1,d1)+math.max(l1,f1)+math.max(l1,i1)+math.max(l1,s1)",
		"math.max(s1,d1)+math.max(s1,f1)+math.max(s1,i1)+math.max(s1,l1)",
		"math.sqrt(d2)+math.sqrt(f2)+math.sqrt(i2)+math.sqrt(l2)+math.sqrt(s2)",
		"product.code + ': ' + product.name",
		"product.code.length() + product.name.length()",
		"product.getQuality().value.length()",
		"product.getQuality().quality",
		"product.getCode().equals(product.getName())",
		"()",
		"(x+y)",
		"(1+2,true,1==3)",
		"'hello, harry''s cat!'.length()",
		"\"\"\"hello, harry''s cat!\".toUpperCase()",
		"(x-y).compareTo(y)",
		"x.compareTo(x-y)",
		"x%y",
		"[1, x, y, 'hello'.length(), product.getQuality().quality]",
		"{math.min(x, y), product.getQuality().value.length()}",
		"+--x+--y",
		"product.getStatus()==null",
		"null",
		"quit"
	};

	private final static String[] exprs3 = new String[] {
		"math.min(x, y)",
		"math.min(d1,d2)",
		"math.min(f1,f2)",
		"math.min(i1,i2)",
		"math.min(l1,l2)",
		"math.min(s1,s2)",
		"math.max(d1,f1)+math.max(d1,i1)+math.max(d1,l1)+math.max(d1,s1)",
		"math.max(f1,d1)+math.max(f1,i1)+math.max(f1,l1)+math.max(f1,s1)",
		"math.max(i1,d1)+math.max(i1,f1)+math.max(i1,l1)+math.max(i1,s1)",
		"math.max(l1,d1)+math.max(l1,f1)+math.max(l1,i1)+math.max(l1,s1)",
		"math.max(s1,d1)+math.max(s1,f1)+math.max(s1,i1)+math.max(s1,l1)",
		"math.sqrt(d2)+math.sqrt(f2)+math.sqrt(i2)+math.sqrt(l2)+math.sqrt(s2)",
		"product.code + ': ' + product.name",
		"product.code.length() + product.name.length()",
		"product.getQuality().value.length()",
		"product.getCode().equals(product.getName())",
		"()",
		"(x+y)",
		"(1+2,true,1==3)",
		"'hello, harry''s cat!'.length()",
		"(x-y).compareTo(y)",
		"x.compareTo(x-y)",
		"x%y",
		"[1, x, y, 'hello'.length(), product.getQuality().quality]",
		"{math.min(x, y), product.getQuality().value.length()}",
		"+--x+--y",
		"quit"
	};

	private final static String[] exprs4 = new String[] {
		"2.+3*(5+6)",
		"-.5+3*(3-2)",
		"2.3.4+6-8",
		"x+y",
		"'hello+3\r",
		"'hello+3",
		"p1.status=='selling'&&p1.sellPrice>100",
		"p1.getStatus().length()==2.+3*(5+6)&&p1.sellPrice>100",
		"(p1.status=='selling'||p1.status=='development')&&(p1.sellPrice>p2.sellPrice&&p1.sellPrice<p3.sellPrice)",
		"math.min(4, 12)",
		"product.status==3",
		"p1.status+' status'",
		"p1.status-'ing'",
	};

	private final static String[] scripts = new String[] {
		"s001.yj",
	};

	private final static Object[][] tests = new Object[][] {
		//new Object[]{new com.sophia.parser.yacc.Calculator(), exprs1, "BYACC/J Calculator:"},
		//new Object[]{new com.sophia.parser.jparsec.Calculator(), exprs1, "ParseC Calculator:"},
		//new Object[]{new com.sophia.parser.groovy.Calculator(), exprs1, "Groovy Calculator:"},
		//new Object[]{new com.sophia.parser.yacc.Evaluator(), exprs2, "BYACC/J Evaluator:"},
		//new Object[]{new com.sophia.parser.jparsec.Evaluator(), exprs2, "ParseC Evaluator:"},
		//new Object[]{new com.sophia.parser.groovy.Evaluator(), exprs2, "Groovy Evaluator:"},
		//new Object[]{new com.sophia.parser.yacc.Evaluator2(), exprs2, "BYACC/J Evaluator 2:"},
		//new Object[]{new com.sophia.parser.yacc.Calculator(), exprs4, "BYACC/J Calculator:"},
		//new Object[]{new com.sophia.parser.yacc.Evaluator(), exprs4, "BYACC/J Evaluator:"},
		new Object[]{new com.sophia.parser.yacc.Evaluator2(), exprs4, "BYACC/J Validator 2:"},
	};

	private static void dotests() {
		Parser parser;
		String[] exprs;
		String description;
		for(int i = 0; i < tests.length; i++) {
			try {
				Object[] test = (Object[])tests[i];
				parser = (Parser)test[0];
				exprs = (String[])test[1];
				description = (String)test[2];
				dotest(parser, exprs, description);
			} catch(Exception e) {
				log.error("", e);
			}
		}
	}

	private static void dotest(Parser parser, String[] exprs, String description) {
		String s;
		Object r;
		Collector collector;
		Translator translator;
		ErrorReporter reporter;
		Traversal traversal = new Traversal();
		System.out.println(description);
		Date b = new Date();
		for(int i = 0; i < exprs.length; i++) {
			s = exprs[i];
			try {
				if (i != 6 || s.startsWith("#")) continue;
				if (s.equalsIgnoreCase("quit") || s.equalsIgnoreCase("exit")) break;
				//collector = new Collector();
				//translator = new Translator();
				//reporter = new ErrorReporter();
				//parser.addVisitor(collector);
				//parser.addVisitor(translator);
				//parser.addVisitor(reporter);
				parser.addVisitor(traversal);
				r = parser.evaluate(s, context);
				System.out.println(i + ". " + s + " = " + DebugHelper.debug(r));
				//debug(collector.getResult());
				//System.err.println("===>" + translator.getResult());
			} catch (Exception e) {
				log.error(i + ". " + s, e);
			}
		}
		Date e = new Date();
		System.out.println("use " + (e.getTime() - b.getTime()) + " ms.\n");
	}

	// 测试表达式翻译
	private static void testTranslate() {
		try {
			Product p1 = new Product("PDIA001", "冲击钻A001", Product.Quality.GOOD, "selling", 88.0, 128.5, 1000, new Integer(1850));
			Product p2 = new Product("PDIA002", "冲击钻A002", Product.Quality.BEST, "concept", 78.3, 99.7, 550, new Integer(1300));
			Customer c = new Customer("B&Q", "百安居", "blank");
			String s = "pa1.getStatus().length()==2.+3*(5+6)&&pa1.getStatus().substring(0, pa2.status.length() - 4)=='sell'&&pa2.status=='develop'&&pa1.buyPrice<pa2.buyPrice&&pa1.name!='PDI<pa2pa1'&&c.status=='approved'";
			ParseContext context = new ParseContext();
			context.put("pa1", Product.class);
			context.put("pa2", p2);
			context.put("c", Customer.class);
			Map map = new Hashtable();
			map.put("pa1", "产品1");
			map.put("pa2", "产品2");
			map.put("c", "客户");
			map.put(Product.class, "产品");
			map.put("name", "名称");
			map.put("com.test.Product.status", "状态1");
			map.put("com.test.Customer.status", "状态2");
			map.put("getStatus", "状态1");
			map.put("length", "长度");
			map.put("substring", "子串");
			map.put("buyPrice", "买入价");
			map.put("sellPrice", "卖出价");
			map.put("count", "数量");
			map.put("&&", " 并且 ");
			map.put("==", " 等于 ");
			map.put("!=", " 不等于 ");
			map.put("<", " 小于 ");
			String r = translate(s, context, map);
			System.out.println("expression: " + s);
			System.err.println("translate to: " + r);
		} catch(Exception e) {
			log.error("", e);
		}
	}

	private static String translate(String s, ParseContext context, Map map) throws Exception {
		Translator translator = new Translator(context, map);
		Parser validator = new com.sophia.parser.yacc.Evaluator2();
		validator.addVisitor(translator);
		validator.evaluate(s, context);
		Object r = translator.getResult();
		if(r == null) r = s;
		return r.toString();
	}

	private static void debug(Object o) {
		if(List.class.isInstance(o)) {
			List l = (List)o;
			Token t;
			for(int i = 0; i < l.size(); i++) {
				t = (Token)l.get(i);
				//if(t.isAtomic())
				System.err.println(l.get(i));
			}
		}
	}

	/**
	 * yacc script
	 * @throws Exception
	 */
	private static void testYaccScript() throws Exception {
		Parser shell = new com.sophia.parser.yacc.Shell();
		String s;
		Object r;
		context.put("module", "yacc");
		System.out.println("Yacc Script:");
		Date b = new Date();
		for(int i = 0; i < scripts.length; i++) {
			try {
				s = read(scripts[i]);
				System.out.println("read script " + i + ":");
				System.out.println(s);
				r = shell.evaluate(s, context);
				System.out.println("return: " + DebugHelper.debug(r));
			} catch (Exception e) {
				log.error("script " + i + " execute failed!", e);
			}
		}
		Date e = new Date();
		System.out.println("use " + (e.getTime() - b.getTime()) + " ms.\n");
	}

	/**
	 * groovy script
	 * @throws Exception
	 */
	private static void testGroovyScript() throws Exception {
		context.put("module", "groovy");
		Parser shell = new com.sophia.parser.groovy.Shell();
		String s;
		Object r;
		System.out.println("Groovy Script:");
		Date b = new Date();
		for(int i = 0; i < scripts.length; i++) {
			try {
				s = read(scripts[i]);
				System.out.println("read script " + i + ":");
				System.out.println(s);
				r = shell.evaluate(s, context);
				System.out.println("return: " + DebugHelper.debug(r));
			} catch (Exception e) {
				log.error("script " + i + " execute failed!", e);
			}
		}
		Date e = new Date();
		System.out.println("use " + (e.getTime() - b.getTime()) + " ms.\n");
	}

	private static String read(String name) throws Exception {
		StringBuffer sb = new StringBuffer();
		File f = new File(scriptDir + name);
		Reader r = new FileReader(f);
		char[] buf = new char[1024];
		int len = 0;
		while((len = r.read(buf)) > 0) {
			sb.append(buf, 0, len);
		}
		return sb.toString();
	}

	private static void testPredicate() {
		Predicate p = new OrPredicate(new IsLowercase(), new IsEqual("Hello"));
		String s = "hello";
		System.out.println("p(" + s + ") = " + p.is(s, null));
	}

	private static void testParsec() {
		Parser evaluator = new com.sophia.parser.yacc.Evaluator();
		String s;
		Object r;
		Date b = new Date();
		for(int i = 0; i < exprs3.length; i++) {
			s = exprs3[i];
			try {
				if (s.startsWith("#")) continue;
				if (s.equalsIgnoreCase("quit") || s.equalsIgnoreCase("exit")) break;
				r = evaluator.evaluate(s, context);
				System.out.println(s + " = " + DebugHelper.debug(r));
			} catch (Exception e) {
				log.error(s + " eval FAILED!", e);
			}
		}
		Date e = new Date();
		System.out.println("use " + (e.getTime() - b.getTime()) + " ms.\n");
	}

	private static void testRegex() {
		String regex;
		String s;
		regex = "('|\")([^\"]*)(\\1)";
		//regex = "<(.*)>([^<>]*)<(/\\1)>";
		//s = "'hello, harry''s cat!'";
		s = "\"hello, harry's cat!\"";
		//s = "<h1>hello</h1>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		if(m.find()) {
			System.out.println("open: " + m.group(1));
			System.out.println("quoted: " + m.group(2));
			System.out.println("close: " + m.group(3));
		} else {
			System.err.println("NOT match!");
		}
	}

	public static void main(String[] args) throws Exception {
		dotests();
		testTranslate();
		//testYaccScript();
		//testGroovyScript();
		//testParsec();
		//testPredicate();
		//testRegex();
	}

	private static void test1() 
	{
		boolean a1 = true;
		byte a2 = 1;
		char a3 = '2';
		double a4 = 33333333333.33;
		float a5 = 44444444444.44f;
		int a6 = 555555555;
		long a7 = 66666666666l;
		short a8 = 8888;
		Boolean A1 = new Boolean(false);
		Byte A2 = new Byte(a2);
		Character A3 = new Character(a3);
		Double A4 = new Double(a4);
		Float A5 = new Float(a5);
		Integer A6 = new Integer(a6);
		Long A7 = new Long(a7);
		Short A8 = new Short(a8);

		System.out.println(short.class.isAssignableFrom(int.class));
		System.out.println(int.class.isAssignableFrom(short.class));
		System.out.println(Integer.class.isAssignableFrom(int.class));
		System.out.println(int.class.isAssignableFrom(Integer.class));
		System.out.println(A4.floatValue() + ", " + A4.intValue() + ", " + A4.longValue() + ", " + A4.shortValue());
		System.out.println(A5.doubleValue() + ", " + A5.intValue() + ", " + A5.longValue() + ", " + A5.shortValue());
		System.out.println(A6.floatValue() + ", " + A6.doubleValue() + ", " + A6.longValue() + ", " + A6.shortValue());
		System.out.println(A7.floatValue() + ", " + A7.doubleValue() + ", " + A7.intValue() + ", " + A7.shortValue());
		System.out.println(A8.floatValue() + ", " + A8.doubleValue() + ", " + A8.intValue() + ", " + A8.longValue());
		A1 = new Boolean(A4.longValue() > 0);
		A2 = new Byte(A4.byteValue());
		A3 = new Character((char)A4.intValue());
		System.out.println(A1.booleanValue() + ", " + A2.byteValue() + ", " + A3.charValue());
		//System.out.println(getClassDiff(Character.class, double.class));
	}

	private static void test2() {
		Package[] packages = Package.getPackages();
		for(int i = 0; i < packages.length; i++) {
			System.err.println(packages[i].getName());
		}
	}

	private static void test3() {
		int x = 36;
		int y = 37;
		boolean b = x++ >= y;
		System.out.println("x = " + x + ", b = " + b);
	}

	private static void test4() {
		Object a = Product.class;
		Object b = new Product();
		System.out.println(a.getClass());
		System.out.println(b.getClass());
		System.err.println(Class.class.isInstance(a));
		System.err.println(Class.class.isInstance(b));
	}

	private static void test5() {
		String[] names = {"a1", "X2", "2man", "_hello", "wood*"};
		String x;
		for(int i = 0; i < names.length; i++) {
			x = (ParseContext.isValidName(names[i])) ? "" : "NOT ";
			System.out.println(names[i] + " is a " + x + "valid name!");
		}
	}
}

class Traversal implements TokenVisitor {

	public void visit(Token token) {
		//if(!token.isAtomic())return;
		System.err.println(token);
	}

	public Object getResult() {
		return null;
	}
}

class Translator implements TokenVisitor, ExprVisitor {

	private ParseContext context;
	private Map map;
	private Map tokens;
	private Map peers;

	public Translator(ParseContext context, Map map) {
		this.context = context;
		this.map = map;
		this.tokens = new LinkedHashMap();
		this.peers = new Hashtable();
	}

	public void visit(Token token) {
		//System.err.println(token);
		if(!token.isAtomic())return;
		tokens.put(new Integer(token.getColumn()), token);
	}

	public void visit(Token a, Token b, Token c) {
		if(Member.class.isInstance(c.getValue()) && ".".equals(b.getValue())) peers.put(c, a);
	}

	public Object getResult() {
		StringBuffer sb = new StringBuffer();
		Token t1, t2;
		String text;
		Class clazz;
		Object value;
		Object name;
		Member member;
		for(Iterator it = tokens.values().iterator(); it.hasNext(); ) {
			t1 = (Token)it.next();
			text = t1.getText().trim();
			name = null;
			if(Member.class.isInstance(t1.getValue())) {
				member = (Member)t1.getValue();
				t2 = (Token)peers.get(t1);
				clazz = t2.getClazz();
				name = map.get(clazz.getName() + "." + member.getName());
				if(name == null) name = map.get(member.getName());
			}
			clazz = t1.getClazz();
			value = context.get(text); 
			if(name == null) name = map.get(text);
			if(name == null) name = map.get(clazz);
			if(name == null) name = map.get(clazz.getName());
			if(name == null) name = t1.getText();
			sb.append(name);
		}
		return sb.toString();
	}
}

class ErrorReporter implements TokenVisitor {

	private final static ResourceBundle rb1 = ResourceHelper.getBundle("resource/langs/parse");
	private static ResourceBundle rb2;

	static {
		try {
			rb2 = ResourceBundle.getBundle("parse");
		} catch(Exception e) {
			rb2 = null;
		}
	}

	private static String getResource(String s, ResourceBundle[] rbs) {
		String r = s;
		int count = (rbs == null) ? 0 : rbs.length;
		for(int i = 0; i < count; i++) {
			if(rbs[i] == null) continue;
			try {
				r = rbs[i].getString(s);
				break;
			} catch(Exception e) {
			}
		}
		return r;
	}

	private static String format(String s) {
		return format(s, null);
	}

	private static String format(String s, Object[] args) {
		String p = getResource(s, new ResourceBundle[]{rb1, rb2});
		try {
			if(args == null) args = new Object[0];
			p = MessageFormat.format(p, args);
		} catch(Exception e) {
			p = s;
		}
		return p;
	}

	public ErrorReporter() {
	}

	public void visit(Token token) {
		ParseException pe = null;
		Object[] info = new Object[]{new Integer(token.getLine()), new Integer(token.getColumn() + 1)};
		String message;
		if(token.getClazz() == null) {
			message = format("parse.class.error", info);
			pe = new ParseException(message);
		}
		if(ParseException.class.isInstance(token.getValue())) {
			pe = (ParseException)token.getValue();
			message = format(pe.getMessage(), info);
			pe = new ParseException(message, pe.getCause());
		}
		if(pe != null) throw pe;
	}

	public Object getResult() {
		return null;
	}
}