package com.bmj.bean;

import com.bmj.tree.bean.CommonContactTreeNodeId;
import com.bmj.tree.bean.CommonContactTreeNodeLabel;
import com.bmj.tree.bean.CommonContactTreeNodePid;

public class CommonContactBean
{
	@CommonContactTreeNodeId
	private int id;
	@CommonContactTreeNodePid
	private int pId;
	@CommonContactTreeNodeLabel
	private String label;

	public CommonContactBean()
	{
	}

	public CommonContactBean(int id, int pId, String label)
	{
		this.id = id;
		this.pId = pId;
		this.label = label;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getpId()
	{
		return pId;
	}

	public void setpId(int pId)
	{
		this.pId = pId;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

}
