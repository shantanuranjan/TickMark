package com.example.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	private long uid;
	private String firstName;
	private String lastName;
	private String emailId;
	private String password;
	private int usertype;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getUsertype() {
		return usertype;
	}

	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeLong(uid);
		out.writeString(firstName);
		out.writeString(lastName);
		out.writeString(emailId);
		out.writeString(password);
		out.writeInt(usertype);

	}

	public User(Parcel p) {
		uid= p.readLong();
		firstName = p.readString();
		lastName = p.readString();
		emailId = p.readString();
		password = p.readString();
		usertype = p.readInt();
	}
	
	public User(long uid, String fname, String lname, String email, String pswd, int utype){
		this.uid = uid;
		this.firstName = fname;
		this.lastName = lname;
		this.emailId = email;
		this.password = pswd;
		this.usertype = utype;
	}

}
