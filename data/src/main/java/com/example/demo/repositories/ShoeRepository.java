package com.example.demo.repositories;

import java.math.BigInteger;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.ShoeEntity;

/**
 * Repository lié aux chaussures
 * 
 * @author asoilihi
 *
 */
public interface ShoeRepository extends JpaRepository<ShoeEntity, BigInteger> {

	/**
	 * Méthode permettant de supprimer les paires de chaussures via l'id du stock
	 * 
	 * @param idStock
	 * @return Long
	 */
	@Transactional
	Long deleteByStockId(BigInteger idStock);

}
