package com.gabscrud.projeto_crud.domain.repository;

import com.gabscrud.projeto_crud.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutosRepository extends JpaRepository <Produto, Integer> {
}
