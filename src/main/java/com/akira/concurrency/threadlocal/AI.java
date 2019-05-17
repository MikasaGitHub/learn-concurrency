package com.akira.concurrency.threadlocal;

public class AI {
	
	private int id;
	
	private String address;

	private String phone;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "AI [id=" + id + ", address=" + address + ", phone=" + phone + "]";
	}
	
}
