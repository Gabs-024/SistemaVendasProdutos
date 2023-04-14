package com.gabscrud.projeto_crud.service.impl;

import com.gabscrud.projeto_crud.api.DTO.ItemPedidoDTO;
import com.gabscrud.projeto_crud.api.DTO.PedidosDTO;
import com.gabscrud.projeto_crud.domain.entity.Cliente;
import com.gabscrud.projeto_crud.domain.entity.Item_Pedido;
import com.gabscrud.projeto_crud.domain.entity.Pedido;
import com.gabscrud.projeto_crud.domain.entity.Produto;
import com.gabscrud.projeto_crud.domain.enums.StatusPedido;
import com.gabscrud.projeto_crud.domain.repository.ClientesRepository;
import com.gabscrud.projeto_crud.domain.repository.ItensPedidosRepository;
import com.gabscrud.projeto_crud.domain.repository.PedidosRepository;
import com.gabscrud.projeto_crud.domain.repository.ProdutosRepository;
import com.gabscrud.projeto_crud.exception.PedidoNaoEncontradoException;
import com.gabscrud.projeto_crud.exception.RegraNegocioException;
import com.gabscrud.projeto_crud.service.PedidosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidosService {

    private final PedidosRepository pedidosRepository;
    private final ClientesRepository clientesRepository;
    private final ProdutosRepository produtosRepository;
    private final ItensPedidosRepository itensPedidosRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidosDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido!"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<Item_Pedido> itensPedido = converterItens(pedido,dto.getItens());
        pedidosRepository.save(pedido);
        itensPedidosRepository.saveAll(itensPedido);
        pedido.setItens(itensPedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidosRepository.findByIdFetchItens(id);
    }

    private List<Item_Pedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itens) {
        if(itens.isEmpty()) {
            throw new RegraNegocioException("Não é possível realizar um pedido sem itens.");
        }
        return itens
                .stream()
                .map(dto ->{
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(() -> new RegraNegocioException("Código de produto inválido " + idProduto));

                    Item_Pedido itemPedido = new Item_Pedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
        }).collect(Collectors.toList());
    }
}
