package com.weipai.model;

import java.util.ArrayList;
import java.util.List;

public class ManagerNode {
	private Integer id;

    private Integer powerId;//1 管理员   2经销商    3 零售商

    private String name;

    private String userId;
    
    private String telephone;

	private Integer actualcard;

    private Integer totalcards;

    private Integer managerUpId;

    private boolean status;

    private int inviteCode;
    
    private int rootManager;
    private String weixin;
    private String qq;
    
    private String rebate;
    
    private List<ManagerNode> childagent = new ArrayList<ManagerNode>();
    
    

	public ManagerNode(Manager manager) {
		super();
		this.id = manager.getId();
		this.powerId = manager.getPowerId();
		this.name = manager.getName();
		this.userId = manager.getPassword();
		this.telephone = manager.getTelephone();
		this.actualcard = manager.getActualcard();
		this.totalcards = manager.getTotalcards();
		this.managerUpId = manager.getManagerUpId();
		this.status = manager.getStatus().equals("0");
		this.inviteCode = manager.getInviteCode();
		this.rootManager = manager.getRootManager();
		this.weixin = manager.getWeixin();
		this.qq = manager.getQq();
		this.rebate = manager.getRebate();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPowerId() {
		return powerId;
	}

	public void setPowerId(Integer powerId) {
		this.powerId = powerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Integer getActualcard() {
		return actualcard;
	}

	public void setActualcard(Integer actualcard) {
		this.actualcard = actualcard;
	}

	public Integer getTotalcards() {
		return totalcards;
	}

	public void setTotalcards(Integer totalcards) {
		this.totalcards = totalcards;
	}

	public Integer getManagerUpId() {
		return managerUpId;
	}

	public void setManagerUpId(Integer managerUpId) {
		this.managerUpId = managerUpId;
	}

	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(int inviteCode) {
		this.inviteCode = inviteCode;
	}

	public int getRootManager() {
		return rootManager;
	}

	public void setRootManager(int rootManager) {
		this.rootManager = rootManager;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public List<ManagerNode> getChildagent() {
		return childagent;
	}

	public void setChildagent(List<ManagerNode> childagent) {
		this.childagent = childagent;
	}

	public String getRebate() {
		return rebate;
	}

	public void setRebate(String rebate) {
		this.rebate = rebate;
	}
    
    
    
}
