package com.gabscrud.projeto_crud.service;

import com.gabscrud.projeto_crud.api.DTO.PedidosDTO;
import com.gabscrud.projeto_crud.domain.entity.Pedido;
import com.gabscrud.projeto_crud.domain.enums.StatusPedido;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PedidosService {
    @Transactional
    Pedido salvar(PedidosDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

}
