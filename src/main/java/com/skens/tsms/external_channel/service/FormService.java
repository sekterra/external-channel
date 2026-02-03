package com.skens.tsms.external_channel.service;

import com.skens.tsms.external_channel.domain.Form;
import com.skens.tsms.external_channel.dto.FormCreateRequest;
import com.skens.tsms.external_channel.dto.FormUpdateRequest;
import com.skens.tsms.external_channel.mapper.FormMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FormService {

	private final FormMapper formMapper;

	public FormService(FormMapper formMapper) {
		this.formMapper = formMapper;
	}

	@Transactional
	public Form create(FormCreateRequest request) {
		Form entity = new Form();
		entity.setTitle(request.getTitle().trim());
		entity.setContent(request.getContent().trim());
		entity.setPayloadJson(request.getPayloadJson());
		entity.setUseYn("Y");
		LocalDateTime now = LocalDateTime.now();
		entity.setCreatedAt(now);
		entity.setUpdatedAt(now);
		formMapper.insert(entity);
		return formMapper.selectById(entity.getId());
	}

	public Form getById(Long id) {
		return formMapper.selectById(id);
	}

	public List<Form> list() {
		return formMapper.selectList();
	}

	@Transactional
	public Form update(Long id, FormUpdateRequest request) {
		Form existing = formMapper.selectById(id);
		if (existing == null) {
			return null;
		}
		existing.setTitle(request.getTitle().trim());
		existing.setContent(request.getContent().trim());
		existing.setPayloadJson(request.getPayloadJson());
		existing.setUpdatedAt(LocalDateTime.now());
		formMapper.update(existing);
		return formMapper.selectById(id);
	}

	@Transactional
	public boolean logicalDelete(Long id) {
		Form existing = formMapper.selectById(id);
		if (existing == null) {
			return false;
		}
		return formMapper.logicalDelete(id, LocalDateTime.now()) > 0;
	}
}
