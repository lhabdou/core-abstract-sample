package com.example.demo.core;

import java.math.BigInteger;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeFilter.Color;
import com.example.demo.entities.ShoeEntity;
import com.example.demo.facade.ShoeFacade;

import lombok.val;

public abstract class AbstractShoeCore implements ShoeCore {

	@Autowired
	private ShoeFacade shoeFacade;

	@PostConstruct
	void init() {

		val version = Optional.ofNullable(this.getClass().getAnnotation(Implementation.class))
				.map(Implementation::version).orElseThrow(() -> new FatalBeanException(
						"AbstractShoeCore implementation should be annotated with @Implementation"));

		shoeFacade.register(version, this);

	}

	public Example<ShoeEntity> prepareExampleMatcherShoeToSearch(ShoeFilter shoeFilter) {

		ShoeEntity shoeToSearch = new ShoeEntity();
		shoeToSearch.setSize(shoeFilter.getSize().orElse(BigInteger.valueOf(42)));
		shoeToSearch.setColor((shoeFilter.getColor().orElse(Color.BLACK)).toString());

		Example<ShoeEntity> example = Example.of(shoeToSearch);
		return example;
	}

}
