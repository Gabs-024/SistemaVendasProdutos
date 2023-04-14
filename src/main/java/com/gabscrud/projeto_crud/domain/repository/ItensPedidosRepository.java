package com.gabscrud.projeto_crud.domain.repository;

import com.gabscrud.projeto_crud.domain.entity.Item_Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItensPedidosRepository extends JpaRepository<Item_Pedido, Integer> {
}
