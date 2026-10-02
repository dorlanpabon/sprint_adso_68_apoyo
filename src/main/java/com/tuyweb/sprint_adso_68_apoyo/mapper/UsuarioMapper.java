package com.tuyweb.sprint_adso_68_apoyo.mapper;

import com.tuyweb.sprint_adso_68_apoyo.dto.RegistroDTO;
import com.tuyweb.sprint_adso_68_apoyo.dto.UsuarioDTO;
import com.tuyweb.sprint_adso_68_apoyo.dto.UsuarioFormDTO;
import com.tuyweb.sprint_adso_68_apoyo.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO toDTO(Usuario usuario);

    UsuarioFormDTO toFormDTO(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "activo", constant = "true")
    Usuario fromRegistro(RegistroDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    Usuario fromForm(UsuarioFormDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEntityFromForm(UsuarioFormDTO dto, @MappingTarget Usuario usuario);
}
