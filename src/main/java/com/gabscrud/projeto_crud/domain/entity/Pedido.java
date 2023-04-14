package com.gabscrud.projeto_crud.domain.entity;

import com.gabscrud.projeto_crud.domain.enums.StatusPedido;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pedido")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "cliente_Id")
    private Cliente cliente;
    @Column(name = "data_pedido")
    private LocalDate dataPedido;
    @Column(name = "total")
    private BigDecimal total;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPedido status;
    @OneToMany(mappedBy = "pedido")
    private List<Item_Pedido> itens;

    public List<Item_Pedido> getItens() {
        if(this.itens == null) {
            this.itens = new ArrayList<>();
        }
        return this.itens;
    }
}
