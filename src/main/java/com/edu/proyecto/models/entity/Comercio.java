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

@Entity(name = "comercios")
@PrimaryKeyJoinColumn(name = "userId")
public class Comercio extends Usuario {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	
//	private Long id;
	@Email
	@NotBlank
	private String email;
	@NotBlank
	private String razonSocial;
	@NotBlank
	private String direccion;
	@NotBlank
	private String descripcion;
	@OneToMany(mappedBy = "comercio", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Pedido> pedidos;

	private String image;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@OneToMany(mappedBy = "comercio", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Producto> productos;

	@Override
	public String toString() {
		return "Comercio [id=" + ", email=" + email + ", razonSocial=" + razonSocial + ", productos=" + productos
				+ "]".concat("username= ".concat(getNicname()));
	}

//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
