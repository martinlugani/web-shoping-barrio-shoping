package com.edu.proyecto.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "pedidos_items")
public class ItemPedido implements Serializable {

	@Override
	public String toString() {
		return "ItemPedido [id=" + id + ", cantidad=" + cantidad + ", producto=" + producto + ", subtotal=" + subtotal
				+ "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6572438395455130456L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer cantidad;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "procducto_id")
	private Producto producto;
	private Double subtotal;

	@PrePersist
	private void prePersist() {

		subtotal = calculoSubtotal();

	}

	public Double calculoSubtotal() {
		// TODO Auto-generated method stub
		subtotal = cantidad * producto.getPrecio();
		return subtotal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

}
