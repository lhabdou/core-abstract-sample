package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import com.example.demo.dto.out.Shoe;
import com.example.demo.dto.out.Stock;
import com.example.demo.dto.out.Stock.State;
import com.example.demo.entities.ShoeEntity;
import com.example.demo.entities.StockEntity;

/**
 * Mapper permettant de passer des entitées 
 * aux dto et vice et versa avec mapstruct
 * 
 * @author asoilihi
 *
 */
@Mapper(componentModel = "spring")
public interface StockMapper {

	public static final int EMPTY = 0;
	public static final int CAPACITY_MAX = 30;

	/**
	 * Transforme un objet Stock en StockEntity
	 * 
	 * @param stock
	 * @return StockEntity
	 */
	@Mappings({ @Mapping(target = "shoesEntity", source = "stock.shoes.shoes"), })
	StockEntity stockToStockEntity(Stock stock);

	/**
	 * Transforme un objet StockEntity en Stock
	 * 
	 * @param stockEntity
	 * @return Stock
	 */
	@Mappings({ @Mapping(target = "shoes.shoes", source = "stockEntity.shoesEntity"),
			@Mapping(target = "state", source = "stockEntity", qualifiedByName = "stateEnum") })
	Stock stockEntityToStock(StockEntity stockEntity);

	/**
	 * Transforme un objet ShoeEntity en Shoe
	 * 
	 * @param shoeEntity
	 * @return Shoe
	 */
	Shoe shoeEntityToShoe(ShoeEntity shoeEntity);

	/**
	 * Transforme un objet Shoe en ShoeEntity
	 * 
	 * @param shoe
	 * @return ShoeEntity
	 */
	ShoeEntity shoeToShoeEntity(Shoe shoe);

	/**
	 * Retourne l'état du stock en fonction de la quantité
	 * 
	 * @param stockEntity
	 * @return State
	 */
	@Named("stateEnum")
	default State setStateStock(StockEntity stockEntity) {

		State state = switch (stockEntity.getTotalQuantity()) {
		case EMPTY -> State.EMPTY;
		case CAPACITY_MAX -> State.FULL;
		default -> State.SOME;
		};

		return state;

	}

}
