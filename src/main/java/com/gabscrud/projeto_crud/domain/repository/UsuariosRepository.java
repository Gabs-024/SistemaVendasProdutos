package com.gabscrud.projeto_crud.domain.repository;

import com.gabscrud.projeto_crud.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByLogin(String login);

}
