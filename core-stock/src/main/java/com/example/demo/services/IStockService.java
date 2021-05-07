package com.example.demo.services;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.example.demo.dto.out.Shoe;
import com.example.demo.dto.out.Stock;
import com.example.demo.services.exception.QuantityException;

public interface IStockService {

	/**
	 * Méthode permettant de retourner le stock et son état
	 * 
	 * @return Stock
	 */
	Stock getStock();

	/**
	 * Méthode permettant de mettre à jour le stock
	 * 
	 * @param stock
	 * @return Stock
	 * @throws QuantityException
	 */
	Stock updateStock(Stock stock) throws QuantityException;

	/**
	 * Méthode permettant de rajouter une paire de chaussure au stock
	 * 
	 * @param shoe
	 * @return
	 * @throws QuantityException
	 */
	Shoe addShoeToStock(Shoe shoe) throws QuantityException;

	/**
	 * Méthode permettant de retirer du stock une paire de chaussure
	 * 
	 * @param shoe
	 * @return
	 * @throws EntityNotFoundException
	 * @throws QuantityException 
	 */
	String removeShoeFromStock(Shoe shoe) throws EntityNotFoundException, QuantityException;

	/**
	 * Méthode permettant de retourner un modèle de chaussure
	 * présent dans le stock
	 * 
	 * @param shoe
	 * @return
	 */
	Shoe getShoeFromStock(@Valid Shoe shoe);

}
