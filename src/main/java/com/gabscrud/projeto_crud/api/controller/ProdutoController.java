package com.gabscrud.projeto_crud.api.controller;

import com.gabscrud.projeto_crud.domain.entity.Produto;
import com.gabscrud.projeto_crud.domain.repository.ProdutosRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private ProdutosRepository repository;

    public ProdutoController(ProdutosRepository repository) {
        this.repository = repository;
    }

    @GetMapping("{id}")
    @ApiOperation("Obtem detalhes de um produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto encontrado"),
            @ApiResponse(code = 404, message = "Produto não encontrado")
    })
    public Produto getProdutoById(@PathVariable Integer id) {
        return repository
                .findById(id)
                .orElseThrow( () ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Produto não encontrado"));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Salva um novo produto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Produto criado com sucesso"),
            @ApiResponse(code = 404, message = "Produto não encontrado")
    })
    public Produto save (@RequestBody @Valid Produto produto) {
        return repository.save(produto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Apaga o produto do banco de dados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto encontrado"),
            @ApiResponse(code = 404, message = "Produto não encontrado")
    })
    public void delete(@PathVariable Integer id) {
        repository.findById(id).map( produto -> {
            repository.delete(produto);
            return produto;
        }).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto não encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Atualiza os detalhes do produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto atualizado com sucesso!"),
            @ApiResponse(code = 404, message = "Produto não encontrado")
    })
    public void update(@PathVariable Integer id,
                       @RequestBody @Valid Produto produto) {
        repository.findById(id).map(produtoExistente -> {
            produto.setId(produtoExistente.getId());
            repository.save(produto);
            return produtoExistente;
        }).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto não encontrado"));
    }

    @GetMapping
    @ApiOperation("Lista todos os produtos cadastrados")
    public List<Produto> find (Produto filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro, matcher);
        return repository.findAll(example);
    }
}
