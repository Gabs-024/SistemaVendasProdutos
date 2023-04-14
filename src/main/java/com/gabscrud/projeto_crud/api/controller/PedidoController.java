package com.gabscrud.projeto_crud.api.controller;

import com.gabscrud.projeto_crud.api.DTO.InformacaoItemPedidoDTO;
import com.gabscrud.projeto_crud.api.DTO.InformacoesPedidoDTO;
import com.gabscrud.projeto_crud.api.DTO.PedidosDTO;
import com.gabscrud.projeto_crud.domain.entity.Item_Pedido;
import com.gabscrud.projeto_crud.domain.entity.Pedido;
import com.gabscrud.projeto_crud.service.PedidosService;
import com.gabscrud.projeto_crud.service.impl.PedidoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@RestController
@RequestMapping("/api/pedidos")
@Api("Api Pedido")
public class PedidoController {
    private PedidosService pedidosService;

    public PedidoController(PedidoServiceImpl pedidosService) {
        this.pedidosService = pedidosService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Salva um novo pedido")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pedido criado com sucesso!"),
            @ApiResponse(code = 404, message = "Cliente não encontrado")
    })
    public Integer save(@RequestBody @Valid PedidosDTO dto) {
        Pedido pedido = pedidosService.salvar(dto);
        return pedido.getId();
    }

    @GetMapping("{id}")
    @ApiOperation("Obtem detalhes de um pedido")
    public InformacoesPedidoDTO getById(@PathVariable Integer id) {
        return pedidosService
                .obterPedidoCompleto(id)
                .map(p -> converter(p))
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Pedido não encontrado!"));
    }

    private InformacoesPedidoDTO converter (Pedido pedido) {
        return InformacoesPedidoDTO
                .builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy)")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .itens(converter(pedido.getItens()))
                .build();
    }

    private List<InformacaoItemPedidoDTO> converter(List<Item_Pedido> itens) {
        if(CollectionUtils.isEmpty(itens)){
            return Collections.emptyList();
        }
        return (List<InformacaoItemPedidoDTO>) itens.stream().map (
                item -> InformacaoItemPedidoDTO
                        .builder()
                        .descricaoProduto((item.getProduto().getDescricao()))
                        .precoUnitario(item.getProduto().getPreco())
                        .quantidade(item.getQuantidade())
                        .build()
        ).collect(Collectors.toList());
    }
}
