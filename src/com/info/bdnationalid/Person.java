package com.info.bdnationalid;

public class Person 
{
	private String primaryKey;
	private String myName;
	private String IDNO;
	
	public Person(String primaryKey,String myName, String IDNO)
	{
		this.primaryKey = primaryKey;
		this.myName = myName;
		this.IDNO = IDNO;
	}
	
	
	
	public void setPrimaryKey(String primaryKey)
	{
		this.primaryKey = primaryKey;
	}
	public void setName(String myName)
	{
		this.myName = myName;
	}
	public void setIDNO(String IDNO)
	{
		this.IDNO = IDNO;
	}
	
	public String getPrimaryKey()
	{
		return primaryKey;
	}
	
	public String getName()
	{
		return myName;
	}
	
	public String getIDNO()
	{
		return IDNO;
	}
	
	
}
