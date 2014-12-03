/*
 * Created on 2005-10-24
 *
 */
package com.test;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-10-24
 *
 */
class Product {

	private String code;
	private String name;
	private Quality quality;
	private String status;
	private double buyPrice;
	private double sellPrice;
	private int count;
	private Integer weight;

	public Product() {
		this.code = "";
		this.name = "";
		this.quality = Quality.NONE;
	}

	public Product(String code, String name, Quality quality) {
		this.code = code;
		this.name = name;
		this.quality = quality;
	}

	public Product(String code, String name, Quality quality, String status, double buyPrice, double sellPrice, int count, Integer weight) {
		this.code = code;
		this.name = name;
		this.quality = quality;
		this.status = status;
		this.buyPrice = buyPrice;
		this.sellPrice = sellPrice;
		this.count = count;
		this.weight = weight;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String string) {
		code = string;
	}

	public String getName() {
		return name;
	}

	public void setName(String string) {
		name = string;
	}

	public Quality getQuality() {
		return quality;
	}

	public void setQuality(Quality quality) {
		this.quality = quality;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String string) {
		status = string;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(double d) {
		buyPrice = d;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double d) {
		sellPrice = d;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int i) {
		count = i;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer i) {
		weight = i;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(code).append(": ");
		sb.append(name).append(" ");
		sb.append(status).append(" ");
		sb.append(buyPrice).append(" ");
		sb.append(sellPrice).append(" ");
		return sb.toString();
	}

	public static final class Quality {
		public final static Quality NONE = new Quality("none");
		public final static Quality BEST = new Quality("best");
		public final static Quality GOOD = new Quality("good");
		public final static Quality PASS = new Quality("pass");
		public final static Quality BAD = new Quality("bad");

		private String quality;

		private Quality(String quality) {
			this.quality = quality;
		}

		public String getValue() {
			return this.quality;
		}

		public void setValue(String value) {
			Quality q = valueOf(value);
			this.quality = q.toString();
		}

		public Quality valueOf(String value) {
			Quality q;
			if(value == null) q = NONE;
			else if(value.equalsIgnoreCase("best")) q = BEST;
			else if(value.equalsIgnoreCase("good")) q = GOOD;
			else if(value.equalsIgnoreCase("pass")) q = PASS;
			else if(value.equalsIgnoreCase("bad")) q = BAD;
			else q = NONE;
			return q;
		}

		public String toString() {
			return this.quality;
		}

	}

}