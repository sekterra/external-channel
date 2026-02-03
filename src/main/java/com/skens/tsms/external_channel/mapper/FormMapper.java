package com.skens.tsms.external_channel.mapper;

import com.skens.tsms.external_channel.domain.Form;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FormMapper {

	int insert(Form entity);

	Form selectById(@Param("id") Long id);

	List<Form> selectList();

	int update(Form entity);

	int logicalDelete(@Param("id") Long id, @Param("updatedAt") java.time.LocalDateTime updatedAt);
}
