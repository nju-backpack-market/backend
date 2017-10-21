package cn.sansotta.market.domain.entity;

import java.io.Serializable;

/**
 * Created by Hiki on 2017/10/21.
 */

public class ProductEntity {

	private long id;

	private String name;

	private double price;

	private String description;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString(){
		return getId() + "\t" + getName() + "\t" + getPrice() + "\t" + getDescription();
	}
}
