package com.skens.tsms.external_channel.controller.api;

import com.skens.tsms.external_channel.domain.Form;
import com.skens.tsms.external_channel.dto.FormCreateRequest;
import com.skens.tsms.external_channel.dto.FormUpdateRequest;
import com.skens.tsms.external_channel.service.FormService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forms")
public class FormController {

	private final FormService formService;

	public FormController(FormService formService) {
		this.formService = formService;
	}

	@PostMapping
	public ResponseEntity<Form> createForm(@Valid @RequestBody FormCreateRequest request) {
		Form created = formService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Form> getFormById(@PathVariable Long id) {
		Form form = formService.getById(id);
		return form != null ? ResponseEntity.ok(form) : ResponseEntity.notFound().build();
	}

	@GetMapping
	public ResponseEntity<List<Form>> listForms() {
		return ResponseEntity.ok(formService.list());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Form> updateForm(@PathVariable Long id, @Valid @RequestBody FormUpdateRequest request) {
		Form updated = formService.update(id, request);
		return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteForm(@PathVariable Long id) {
		boolean deleted = formService.logicalDelete(id);
		return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}
}
