package com.skens.tsms.external_channel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FormUpdateRequest {

	@NotBlank(message = "제목을 입력하세요.")
	@Size(max = 200)
	private String title;

	@NotBlank(message = "내용을 입력하세요.")
	@Size(max = 4000)
	private String content;

	/** Full form payload as JSON string (all other fields). */
	private String payloadJson;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPayloadJson() {
		return payloadJson;
	}

	public void setPayloadJson(String payloadJson) {
		this.payloadJson = payloadJson;
	}
}
