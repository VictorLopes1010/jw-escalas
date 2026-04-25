package com.tj.designacoes.mapper;

import com.tj.designacoes.dto.UsuarioDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioCriacao", ignore = true)
    @Mapping(target = "usuarioAlteracao", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAlteracao", ignore = true)
    @Mapping(target = "senha", ignore = true)
    com.tj.designacoes.entity.Usuario toEntity(UsuarioDTO dto);

    UsuarioDTO toDTO(com.tj.designacoes.entity.Usuario entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "usuarioCriacao", ignore = true)
    @Mapping(target = "usuarioAlteracao", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAlteracao", ignore = true)
    @Mapping(target = "senha", ignore = true)
    void updateEntityFromDto(UsuarioDTO dto, @MappingTarget com.tj.designacoes.entity.Usuario entity);
}