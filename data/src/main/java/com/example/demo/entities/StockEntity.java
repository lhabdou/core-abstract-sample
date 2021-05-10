package com.example.demo.entities;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * L'entitée représentant un un stock à une date donnée
 * 
 * @author asoilihi
 *
 */
@Getter
@Setter
@Entity
@ToString
@Table(name = "stock")
public class StockEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "stock")
	@OrderBy
	private Set<ShoeEntity> shoesEntity;

	private Integer totalQuantity;

	@Column(name = "creation_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate creationDate;

}
