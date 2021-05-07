package com.example.demo.services.impl;

import java.math.BigInteger;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.demo.dto.out.Shoe;
import com.example.demo.dto.out.Shoes;
import com.example.demo.dto.out.Stock;
import com.example.demo.dto.out.Stock.State;
import com.example.demo.entities.ShoeEntity;
import com.example.demo.entities.StockEntity;
import com.example.demo.mapper.StockMapper;
import com.example.demo.repositories.ShoeRepository;
import com.example.demo.repositories.StockRepository;
import com.example.demo.services.IStockService;
import com.example.demo.services.exception.QuantityException;

@Service
@PropertySource("classpath:messages.properties")
public class StockServiceImpl implements IStockService {

	private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

	@Value("${message.capacity_max_30}")
	public String messageCapacity;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private ShoeRepository shoeRepository;

	@Autowired
	private StockMapper stockMapper;

	@Value("${message.capacity_full}")
	private String messageCapacityFull;

	@Override
	public Stock getStock() {

		StockEntity stockEntity = stockRepository.getCurrentStockWithShoes();

		return stockMapper.stockEntityToStock(stockEntity);
	}

	@Override
	public Stock updateStock(Stock stock) throws QuantityException {

		BigInteger totalQuantity = calculateQuantityShoes(stock.getShoes());

		if (totalQuantity.intValue() > 30) {

			logger.warn(" [StockServiceImpl.updateStock] " + messageCapacity + totalQuantity.intValue());

			throw new QuantityException(" [StockServiceImpl.updateStock] " + messageCapacity + totalQuantity.intValue(),
					1);
		}

		StockEntity stockSaved = this.stockRepository.save(this.stockMapper.stockToStockEntity(stock));

		return this.stockMapper.stockEntityToStock(stockSaved);
	}

	/**
	 * Méthode permettant de calculer la somme totale des chaussures
	 * 
	 * @param shoes
	 * @return BigInteger
	 */
	private BigInteger calculateQuantityShoes(Shoes shoes) {
		BigInteger somme = BigInteger.valueOf(0);
		for (Shoe shoe : shoes.getShoes()) {
			somme = somme.add(shoe.getQuantity());
		}
		return somme;
	}

	@Override
	public Shoe addShoeToStock(Shoe shoe) throws QuantityException {

		// verifier que le stock n'est pas full
		StockEntity stockEntity = stockRepository.getCurrentStock();
		Stock stock = stockMapper.stockEntityToStock(stockEntity);
		ShoeEntity shoeEntity = new ShoeEntity();
		if (State.FULL.equals(stock.getState())) {

			// si full lever une exception
			logger.warn(" [StockServiceImpl.addShoeToStock] " + messageCapacityFull + stock.getTotalQuantity());
			throw new QuantityException(" [StockServiceImpl.addShoeToStock] " + messageCapacityFull, 3);

		}
		// vérifier si le model existe déjà
		Example<ShoeEntity> example = prepareExampleMatcherShoeToSearch(shoe);

		if (shoeRepository.exists(example)) {
			// recuperer le modèle et le mettre à jour
			shoeEntity = shoeRepository.findOne(example).get();
			shoeEntity.setQuantity(shoeEntity.getQuantity().add(shoe.getQuantity()));
			shoeEntity = updateStockAddNewShoes(stockEntity, shoeEntity);

		} else {

			// sinon ajouter le modele
			shoeEntity = stockMapper.shoeToShoeEntity(shoe);
			shoeEntity.setStock(stockEntity);

			// Mettre à jour le stock en ajoutant
			shoeEntity = updateStockAddNewShoes(stockEntity, shoeEntity);

		}

		return stockMapper.shoeEntityToShoe(shoeEntity);
	}

	/**
	 * Met à jour le stock
	 * 
	 * @param stokEntity
	 * @param currentStock
	 * @param shoeEntity
	 * @throws QuantityException
	 */
	private ShoeEntity updateStockAddNewShoes(StockEntity stockEntity, ShoeEntity shoeEntity) throws QuantityException {

		Integer newStock = Integer.sum(stockEntity.getTotalQuantity(), shoeEntity.getQuantity().intValue());
		if (newStock > 30) {
			logger.warn(" [StockServiceImpl.updateStockAddNewShoes] " + messageCapacity + newStock);
			throw new QuantityException(messageCapacity + newStock, 5);
		}
		stockEntity.setTotalQuantity(newStock);
		stockRepository.save(stockEntity);

		return shoeRepository.save(shoeEntity);
	}

	@Override
	public String removeShoeFromStock(@Valid @NotNull Shoe shoe) throws EntityNotFoundException, QuantityException {

		Example<ShoeEntity> example = prepareExampleMatcherShoeToSearch(shoe);

		// vérifier que le modèle existe
		if (!shoeRepository.exists(example)) {
			// si non retourner exception

			logger.warn(" [StockServiceImpl.removeShoeToStock] Le modele n'existe pas dans le stock ");
			throw new EntityNotFoundException("Le modele n'existe pas dans le stock name: " + shoe.getName() + " size: "
					+ shoe.getSize() + " color: " + shoe.getColor());
		}
		// si oui recupérer le modèle

		ShoeEntity shoeEntity = ((Optional<ShoeEntity>) shoeRepository.findOne(example)).get();

		// vérifier la quantité à enlever si le stock est suffisant

		if (shoe.getQuantity().intValue() > shoeEntity.getQuantity().intValue()) {

			// sinon retourner exception

			logger.warn(" [StockServiceImpl.removeShoeToStock] La quantité est insufisante, quantité présente: "
					+ shoeEntity.getQuantity() + " quantité à retirer: " + shoe.getQuantity());
			throw new QuantityException(
					"[StockServiceImpl.removeShoeToStock] La quantité à retirer, qui vaut: " + shoe.getQuantity()
							+ " est supérieure à la quantité disponible, qui vaut: " + shoeEntity.getQuantity(),
					4);
		}
		// si oui enlever la quantite demandée

		shoeEntity.setQuantity(shoeEntity.getQuantity().subtract(shoe.getQuantity()));

		// mettre à jour le stock

		shoeRepository.save(shoeEntity);
		StockEntity stockEntity = stockRepository.getCurrentStock();
		stockEntity.setTotalQuantity(stockEntity.getTotalQuantity() - shoe.getQuantity().intValue());
		stockRepository.save(stockEntity);

		return "Suppression OK";
	}

	/**
	 * @param shoe
	 * @return Example<ShoeEntity>
	 */
	private Example<ShoeEntity> prepareExampleMatcherShoeToSearch(Shoe shoe) {

		ShoeEntity shoeToSearch = new ShoeEntity();
		shoeToSearch.setSize(shoe.getSize());
		shoeToSearch.setColor(shoe.getColor().toString());

		Example<ShoeEntity> example = Example.of(shoeToSearch);
		return example;
	}

}
