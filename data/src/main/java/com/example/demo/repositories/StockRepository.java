package com.example.demo.repositories;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.StockEntity;

/**
 * Repository lié au stock
 * 
 * @author asoilihi
 *
 */
public interface StockRepository extends JpaRepository<StockEntity, BigInteger> {

	/**
	 * Méthode permettant de retourner le stock le plus récent sans la liste des
	 * chaussures
	 * 
	 * @return StockEntity
	 */
	@Query("SELECT s FROM StockEntity s " + "WHERE s.creationDate = (select MAX(creationDate) FROM StockEntity) ")
	StockEntity getCurrentStock();

	/**
	 * Méthode permettant de retourner le stock le plus récent avec la liste des
	 * chaussures
	 * 
	 * @return StockEntity
	 */
	@Query("SELECT s FROM StockEntity s " + "INNER JOIN s.shoesEntity shoes "
			+ "WHERE s.creationDate = (select MAX(creationDate) FROM StockEntity) order by shoes.id")
	StockEntity getCurrentStockWithShoes();

}
