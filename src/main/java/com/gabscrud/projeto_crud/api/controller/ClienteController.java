package com.gabscrud.projeto_crud.api.controller;

import com.gabscrud.projeto_crud.domain.entity.Cliente;
import com.gabscrud.projeto_crud.domain.repository.ClientesRepository;
import io.swagger.annotations.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/clientes")
@Api("Api Clientes")
public class ClienteController {

    private ClientesRepository clientesRepository;

    public ClienteController(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    @GetMapping("{id}")
    @ApiOperation("Obtem detalhes de um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado"),
            @ApiResponse(code = 404, message = "Cliente não encontrado")
    })
    public Cliente getClienteById( @PathVariable @ApiParam("ID do cliente") Integer id) {
        return clientesRepository
                .findById(id)
                .orElseThrow( () ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Cliente não encontrado"));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Salva um novo cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente salvo com sucesso"),
            @ApiResponse(code = 404, message = "Cliente não encontrado")
    })
    public Cliente save (@RequestBody @Valid Cliente cliente) {
        return clientesRepository.save(cliente);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Apaga o cliente do banco de dados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente excluído com sucesso"),
            @ApiResponse(code = 404, message = "Cliente não encontrado")
    })
    public void delete(@PathVariable Integer id) {
        clientesRepository.findById(id).map( cliente -> {
            clientesRepository.delete(cliente);
            return cliente;
        }).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Atualiza os detalhes do cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Cliente não encontrado")
    })
    public void update(@PathVariable Integer id,
                       @RequestBody @Valid Cliente cliente) {
        clientesRepository.findById(id).map(clienteExistente -> {
            cliente.setId(clienteExistente.getId());
            clientesRepository.save(cliente);
            return clienteExistente;
        }).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"));
    }

    @GetMapping
    @ApiOperation("Lista todos os clientes cadastrados")
    public List<Cliente> find (Cliente filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro, matcher);
        return clientesRepository.findAll(example);
    }
}
