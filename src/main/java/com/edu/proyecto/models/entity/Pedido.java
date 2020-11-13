package com.edu.proyecto.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {

	
	@Override
	public String toString() {
		return "Pedido [id=" + id + ", createAt=" + createAt + ", entrega=" + entrega + ", cliente=" + cliente
				+ ", items=" + items + ", comercio=" + comercio + ", estado=" + estado + ", total=" + total + "]";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Temporal(TemporalType.DATE)
	@Column(name = "create_at")
	private Date createAt;
	@Temporal(TemporalType.TIMESTAMP)
	private Date entrega;
	@ManyToOne(fetch = FetchType.EAGER)
	private Cliente cliente;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "pedidos_id")
	private List<ItemPedido> items;
	@ManyToOne(fetch = FetchType.EAGER)
	private Comercio comercio;
	private String estado;
	
	private Double total;
	@PrePersist
	public void prePersist() {
		createAt = new Date();
		estado = "Solicitado";
		calculaTotal();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public Date getEntrega() {
		return entrega;
	}
	public void setEntrega(Date entrega) {
		this.entrega = entrega;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public List<ItemPedido> getItems() {
		return items;
	}
	public void setItems(List<ItemPedido> items) {
		this.items = items;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Comercio getComercio() {
		return comercio;
	}
	public void setComercio(Comercio comercio) {
		this.comercio = comercio;
	}
	public void calculaTotal() {
		total=(double) 0;
		for (ItemPedido itemPedido : items) {
			if (itemPedido.getSubtotal()==null) {
				itemPedido.calculoSubtotal();
			}
			
			total+=itemPedido.getSubtotal();
		}
	}
	public void addItem(ItemPedido item) {
		if (items==null) {
			items= new ArrayList<ItemPedido>();
		}
		items.add(item);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
