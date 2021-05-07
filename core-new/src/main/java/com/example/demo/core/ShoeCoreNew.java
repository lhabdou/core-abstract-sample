package com.example.demo.core;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeFilter.Color;
import com.example.demo.dto.out.Shoe;
import com.example.demo.dto.out.Shoes;
import com.example.demo.entities.ShoeEntity;
import com.example.demo.mapper.StockMapper;
import com.example.demo.repositories.ShoeRepository;

@Implementation(version = 2)
public class ShoeCoreNew extends AbstractShoeCore {

	@Autowired
	private ShoeRepository shoeRepository;

	@Autowired
	private StockMapper stockMapper;

	@Override
	public Shoes search(final ShoeFilter filter) {
		List<Shoe> listShoes = new ArrayList<Shoe>();
		listShoes.add(Shoe.builder().name("New shoe").color(filter.getColor().orElse(Color.BLACK))
				.size(filter.getSize().orElse(BigInteger.TWO)).build());

		List<ShoeEntity> listEntityShoes = shoeRepository.findAll(prepareExampleMatcherShoeToSearch(filter));

		listEntityShoes.forEach(shoeEntity -> listShoes.add(stockMapper.shoeEntityToShoe(shoeEntity)));

		return Shoes.builder().shoes(listShoes).build();
	}

}
