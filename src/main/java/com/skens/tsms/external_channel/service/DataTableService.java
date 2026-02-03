package com.skens.tsms.external_channel.service;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DataTableService {

	// STUB: Returns sample table data
	public List<Map<String, Object>> getSampleData() {
		return List.of(
			Map.of("no", 1, "applicationDate", "2025-01-15", "applicationType", "신규", "status", "완료", "processDate", "2025-01-16"),
			Map.of("no", 2, "applicationDate", "2025-01-16", "applicationType", "변경", "status", "처리중", "processDate", "-"),
			Map.of("no", 3, "applicationDate", "2025-01-17", "applicationType", "신규", "status", "대기", "processDate", "-"),
			Map.of("no", 4, "applicationDate", "2025-01-18", "applicationType", "해지", "status", "완료", "processDate", "2025-01-19"),
			Map.of("no", 5, "applicationDate", "2025-01-19", "applicationType", "변경", "status", "완료", "processDate", "2025-01-20")
		);
	}
}
