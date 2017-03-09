package com.neo.springmvc.handlers;

import com.neo.db.JdbcUpdateDaoImpl;
import com.neo.db.JdbcUserDaoImpl;
import com.neo.db.versionMessage;
import com.neo.utils.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.annotation.Resource;

import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;

@Controller
public class HelloWorld {

	@Autowired
	private HttpServletRequest request;
    @Resource
    private JdbcUpdateDaoImpl jdbcUpdateDaoImpl;
    @Resource
    private JdbcUserDaoImpl jdbcUserDaoImpl;
	@RequestMapping(value = "/upload",method = RequestMethod.POST)
	public ModelAndView  hello(@RequestParam("file") MultipartFile file,@RequestParam(value = "notes",required = false) String notes) {
		String resultMessage = new String("upload success");
		if (!file.isEmpty()) {
			try {
				String rootPath = request.getSession().getServletContext().getRealPath("/");
				System.out.println(rootPath);
				
				int index = rootPath.substring(0, rootPath.length() - 2).lastIndexOf(File.separator);
				String relativePath = rootPath.substring(index + 1) + "package" + File.separator + file.getOriginalFilename();   //svae the relative path in the site
				String absolutePath = rootPath +  "package" + File.separator + file.getOriginalFilename();  //save path use absulute path
				
				File fPath = new File(absolutePath);
				if(!fPath.exists())
					fileHandler.createFile(absolutePath);
				file.transferTo(fPath);
				
				String version = null,MD5 = null;
				try{
					version = fileHandler.getVersionFromGzFile(absolutePath);
					MD5 =  md5.getFileMD5String(fPath);
				}catch(Exception ex){
					ex.printStackTrace();
					resultMessage = "upload failed:not in jzip form or cannot get verions message";
					fPath.delete();
				}
				if(version == null || MD5 == null){
					resultMessage = "upload failed:not in jzip form or cannot get version message";
					fPath.delete();
				}
				else{
					long size = fPath.length();
					System.out.println(" add version:" + version +"  filepath:" + relativePath + "  md5:" + MD5 + "  size:" + size + "  notes:" + notes );
					int ret = jdbcUpdateDaoImpl.addVersion(version, relativePath, MD5,size,notes);
					if(-1 == ret){
						resultMessage = "upload failed:add version message to database failed.";
						fPath.delete();
					}
					else if(2 == ret)
						resultMessage = "this version is already exists,update success";
				}
			} catch (Exception e) {
				resultMessage = "upload failed:  ";
				e.printStackTrace();
			}
		}else
			resultMessage = "upload failed: file cannot be empty.";
		ModelAndView mav = new ModelAndView("uploadResult");
		mav.getModel().put("resultMessage",resultMessage);
		return mav;
	}

	@RequestMapping("uploadSuccess")
	public ModelAndView uploadSuccess() {
		ModelAndView mav = new ModelAndView("uploadSuccess");
		return mav;
	}
	@RequestMapping("list")
	public void list(HttpServletResponse res){	
		int errCode = 0;
		String errMsg = " ";
		List<versionMessage> list = jdbcUpdateDaoImpl.getAllVersion();
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json; charset=utf-8");
		JSONArray jsonArray = new JSONArray();
		for(versionMessage version : list ){
			JSONObject obj = new JSONObject();
			obj.put("verid", version.getVersinId());
			obj.put("verstr",version.getVersion());
			obj.put("md5sum",version.getMD5());
			obj.put("download_url",version.getUrl());
			obj.put("filesize",version.getFileSize());
			obj.put("time",version.getUploadTime());
			obj.put("notes", version.getNotes());
			jsonArray.put(obj);
		}
		String toClient = "{\"errCode\":" + errCode + ",\"errMsg\":" +"\"" + errMsg + "\"" +  ",\"version\":" + jsonArray.toString() + "}";
		PrintWriter out = null;
		try {
			res.setStatus(200);
		    out = res.getWriter();
		    out.write(toClient);
		} catch (IOException e) {
			res.setStatus(404);
		    e.printStackTrace();
		} finally {
		    if (out != null) {
		        out.close();
		    }
		}
	}
	
/*	@RequestMapping("download")
	public void download(@RequestParam("version") String version,HttpServletResponse res){
		System.out.println("client try to download version: " +  version);
		String path = jdbcUpdateDaoImpl.getPath(version);
		if(path != null){
			 String rootPath = request.getSession().getServletContext().getRealPath("/");
			 String fileName = path.substring(path.lastIndexOf("/")+1);
			 res.setHeader("content-disposition", "attachment;filename="+fileName);
			 res.setStatus(200);
			 try{
				 InputStream in = new FileInputStream(path);
				 int len = 0;
				 byte[] buffer = new byte[1024];
				 OutputStream out = res.getOutputStream();
				 while ((len = in.read(buffer)) > 0) {
					  out.write(buffer,0,len);
				  }
				 in.close();
			 }catch(Exception e){
				 e.printStackTrace();
			 }
		}else{
			System.out.println("version not found");
			res.setStatus(404);
		}
	}*/
	@RequestMapping("resetPassword")
	public ModelAndView resetPassword(@RequestParam("mailTime") String mailTime)
	{
		long now = System.currentTimeMillis()/1000;
		if(now - Integer.valueOf(mailTime) > 300){
			String resultMessage = "this url is expired";
			ModelAndView mav = new ModelAndView("uploadResult");
			mav.getModel().put("resultMessage", resultMessage);
			return mav;
		}else{	
			ModelAndView mav = new ModelAndView("resetPassword");
			return mav;
		}
	}
	
	@RequestMapping("resetPasswordHandler")
	public ModelAndView resetPasswordHandler(@RequestParam("userName") String userName,@RequestParam("newPassword") String password){
		String encodePassword  = new String();
		try{
			encodePassword = DESedeUtils.encrypt(password);
			System.out.println(encodePassword);		
		}catch(Exception e){
			e.printStackTrace();
		}
		jdbcUserDaoImpl.resetPassword(userName, encodePassword);
		ModelAndView mav = new ModelAndView("resetPassword");
		return mav;
	}
}