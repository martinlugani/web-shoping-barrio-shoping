package com.edu.proyecto.models.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@PrimaryKeyJoinColumn(name = "userid")
public class Cliente extends Usuario {
//'5', '2020-10-09', 'quinto', '$2a$10$1jkZ/rcR2RgxTY7/n3ZL..FzSLUsT85BxwkIbFUZls6TzsPYFk6CW', '1'
//	$2a$10$1jkZ/rcR2RgxTY7/n3ZL..FzSLUsT85BxwkIbFUZls6TzsPYFk6CW

	@Override
	public String toString() {
		return "Cliente [id=" + ", email=" + email + ", pedidos=" + pedidos + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Email
	@NotBlank
	private String email;
	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Pedido> pedidos;

	public Cliente() {
		// TODO Auto-generated constructor stub
		super();
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
