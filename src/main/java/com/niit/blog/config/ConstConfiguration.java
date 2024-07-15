package com.niit.blog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * properties文件中的自定义配置项
 * 目前仅有上传路径一项
 * 配置项前缀为"const"
 */
@Component
@ConfigurationProperties(prefix = "const")
public class ConstConfiguration {
	private String uploadPath;  // 对应配置文件中的配置项 upload-path
	private String email;

	public String getUploadPath() {
		return uploadPath;
	}
	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
