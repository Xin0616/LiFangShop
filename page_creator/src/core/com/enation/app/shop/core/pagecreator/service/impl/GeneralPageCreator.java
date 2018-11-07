/**
 * 
 */
package com.enation.app.shop.core.pagecreator.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.io.output.FileWriterWithEncoding;

import com.enation.eop.processor.facade.FacadePageParser;

/**
 * 静态页面生成器
 * @author kingapex
 * @version v1.3,2016年6月30日  by Sylow
 * @since v6.1
 */
public class GeneralPageCreator  extends FacadePageParser {
	private String pagePath;
	
	public GeneralPageCreator(String _pagePath){
		this.pagePath=_pagePath;
	}
	
	

	@Override
	protected Writer getWriter() throws IOException{
		
		// 获取文件的目录
		File file = new File(pagePath);
		file = new File(file.getParent());
		
		// 如果文件夹不存在则创建
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
		//解决生成乱码的问题
		return new FileWriterWithEncoding(pagePath, "UTF-8"); //改用apache common中的writer
	}
	

	@Override
	protected void outError(Exception e) {
		e.printStackTrace();
	}
	
	public String getPagePath() {
		return pagePath;
	}

	public void setPagePath(String pagePath) {
		this.pagePath = pagePath;
	}
	
	
}
