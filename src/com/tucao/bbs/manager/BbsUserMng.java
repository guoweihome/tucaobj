package com.tucao.bbs.manager;

import java.util.List;

import com.tucao.bbs.entity.BbsUser;
import com.tucao.bbs.entity.BbsUserExt;
import com.tucao.common.email.EmailSender;
import com.tucao.common.email.MessageTemplate;
import com.tucao.common.page.Pagination;

public interface BbsUserMng {
	public Pagination getPage(String username, String email, Integer groupId,
			Boolean disabled, Boolean admin,Long pointMin,Long pointMax,Long prestigeMin,Long prestigeMax,Integer orderBy, int pageNo, int pageSize);

	public List<BbsUser> getAdminList(Integer siteId, Boolean allChannel,
			Boolean disabled, Integer rank);

	public BbsUser findById(Integer id);

	public BbsUser findByUsername(String username);
	
	public BbsUser registerMember(String username, String email,
			String password, String ip, Integer groupId, BbsUserExt userExt,String openId,String weiboId);
	
	
	public BbsUser registerMember(String username, String email,
			String password, String ip, Integer groupId, BbsUserExt userExt, Boolean activation , EmailSender sender, MessageTemplate msgTpl,String openId,String weiboId) ;

	public void updateLoginInfo(Integer userId, String ip);

	public void updateUploadSize(Integer userId, Integer size);

	public void updatePwdEmail(Integer id, String password, String email);

	public boolean isPasswordValid(Integer id, String password);

	public BbsUser saveAdmin(String username, String email, String password,
			String ip, boolean viewOnly, boolean selfAdmin, int rank,
			Integer groupId, Integer[] roleIds, Integer[] channelIds,
			Integer[] siteIds, Byte[] steps, Boolean[] allChannels,
			BbsUserExt userExt);

	public BbsUser updateAdmin(BbsUser bean, BbsUserExt ext, String password,
			Integer groupId, Integer[] roleIds, Integer[] channelIds,
			Integer[] siteIds, Byte[] steps, Boolean[] allChannels);

	public BbsUser updateMember(Integer id, String email, String password,
			Boolean isDisabled, String signed, String avatar, BbsUserExt ext,
			Integer groupId);

	public BbsUser deleteById(Integer id);
	
	public int getLongUserId(String sequchar);

	public BbsUser[] deleteByIds(Integer[] ids);

	public boolean usernameNotExist(String username);

	public boolean emailNotExist(String email);
	
	public List<BbsUser> getSuggestMember(String username, Integer count);
	
	public List<BbsUser> getHotMember(Integer count);
	
	public void updatePoint(Integer userId,Integer point,  Integer prestige,String mid,int num,int operator);
}