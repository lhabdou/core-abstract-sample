package com.example.demo.entities;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * L'entitée ShoeEntity représentant une paire de chaussure
 * 
 * @author asoilihi
 *
 */
@EqualsAndHashCode
@Entity
@Table(name = "shoe")
@ToString
@Getter
@Setter
public class ShoeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "name")
	private String name;

	@Column(name = "size")
	private BigInteger size;

	@Column(name = "color")
	private String color;

	@Column(name = "quantity")
	private BigInteger quantity;

	@ManyToOne
	@JoinColumn(name = "id_stock", referencedColumnName = "id")
	private StockEntity stock;

}
