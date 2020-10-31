package com.edu.proyecto.models.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonManagedReference;
@Entity

public class Cliente extends Usuario  {

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", email=" + email + ", pedidos=" + pedidos + "]";
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Email
	@NotBlank
	private String email;
	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Pedido> pedidos;
	
	
	
//	@OneToOne()
//	@JoinColumn(name = "user_id")
//	private Usuario usuario;
//	@OneToMany(mappedBy = "cliente")
//	private List<Pedido>pedidos;
	
	
	
	
	
//	public Usuario getUsuario() {
//		return usuario;
//	}
//	public void setUsuario(Usuario usuario) {
//		this.usuario = usuario;
//	}
//	public List<Pedido> getPedidos() {
//		return pedidos;
//	}
//	public void setPedidos(List<Pedido> pedidos) {
//		this.pedidos = pedidos;
//	}

	
	public List<Pedido> getPedidos() {
		return pedidos;
	}
	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	

}
