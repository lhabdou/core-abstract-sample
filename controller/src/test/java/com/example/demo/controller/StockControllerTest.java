package com.example.demo.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.dto.in.ShoeFilter.Color;
import com.example.demo.dto.out.Shoe;
import com.example.demo.dto.out.Shoes;
import com.example.demo.dto.out.Stock;
import com.example.demo.dto.out.Stock.State;
import com.example.demo.services.IStockService;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest
public class StockControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@InjectMocks
	private StockController stockController;

	@MockBean
	private IStockService stockService;

	private Stock stock;

	private Shoe shoe;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		this.mockMvc = MockMvcBuilders.standaloneSetup(stockController).build();

		stock = Stock.builder().shoes(Shoes.builder().shoes(Collections.emptyList()).build()).state(State.EMPTY)
				.totalQuantity(BigInteger.ZERO).build();

		shoe = Shoe.builder().name("model1").size(BigInteger.valueOf(42)).color(Color.BLACK).quantity(BigInteger.TEN)
				.build();
	}

	@Test
	public void getStockTest() throws Exception {

		Mockito.when(stockService.getStock()).thenReturn(stock);

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/shoes/stock").contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.state", is(this.stock.getState().toString())))
				.andExpect(jsonPath("$.shoes.shoes", is(this.stock.getShoes().getShoes())))
				.andExpect(jsonPath("$.totalQuantity", is(this.stock.getTotalQuantity().intValue())))
				.andExpect(jsonPath("$.creationDate", is(this.stock.getCreationDate())));

	}

	@Test
	public void updateStockTest() throws Exception {

		Mockito.when(this.stockService.updateStock(this.stock)).thenReturn(stock);
		this.mockMvc
				.perform(MockMvcRequestBuilders.patch("/shoes/stock").contentType(MediaType.APPLICATION_JSON)
						.content(convertObjectToJson(this.stock)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.state", is(this.stock.getState().toString())))
				.andExpect(jsonPath("$.shoes.shoes", is(this.stock.getShoes().getShoes())))
				.andExpect(jsonPath("$.totalQuantity", is(this.stock.getTotalQuantity().intValue())))
				.andExpect(jsonPath("$.creationDate", is(this.stock.getCreationDate())));

	}

	@Test
	public void addShoeToStockOkTest() throws Exception {

		Mockito.when(this.stockService.addShoeToStock((Shoe) Mockito.any())).thenReturn(this.shoe);
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/shoes/stock/shoe").contentType(MediaType.APPLICATION_JSON)
						.content(convertObjectToJson(this.shoe)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name", is(this.shoe.getName())))
				.andExpect(jsonPath("$.size", is(this.shoe.getSize().intValue())))
				.andExpect(jsonPath("$.quantity", is(this.shoe.getQuantity().intValue())))
				.andExpect(jsonPath("$.color", is(this.shoe.getColor().toString())));

	}

	@Test
	public void removeShoeFromStockOkTest() throws Exception {

		Mockito.when(this.stockService.removeShoeFromStock((Shoe) Mockito.any())).thenReturn("Suppression OK");
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/shoes/stock/shoe").contentType(MediaType.APPLICATION_JSON)
						.content(convertObjectToJson(this.shoe)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

	}

	@Test
	public void getShoeToStockOKTest() throws Exception {

		Mockito.when(stockService.getShoeFromStock(Mockito.any())).thenReturn(this.shoe);

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/shoes/stock/shoe").contentType(MediaType.APPLICATION_JSON)
						.content(convertObjectToJson(this.shoe)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name", is(this.shoe.getName())))
				.andExpect(jsonPath("$.size", is(this.shoe.getSize().intValue())))
				.andExpect(jsonPath("$.quantity", is(this.shoe.getQuantity().intValue())))
				.andExpect(jsonPath("$.color", is(this.shoe.getColor().toString())));
	}

	/**
	 * Méthode pour convertir le flux JSON en Bytes
	 * 
	 * @param object
	 * @return byte[]
	 * @throws IOException
	 */
	private byte[] convertObjectToJson(Object object) throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		mapper.setSerializationInclusion(Include.NON_NULL);

		return mapper.writeValueAsBytes(object);
	}
}
