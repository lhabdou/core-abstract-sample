package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.core.ShoeCore;
import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.out.Shoes;
import com.example.demo.errors.VersionException;
import com.example.demo.facade.ShoeFacade;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(path = "/shoes")
@RequiredArgsConstructor
public class ShoeController {

	@Value("${message.version}")
	private String versionMessage;
	
	private final ShoeFacade shoeFacade;

	@GetMapping(path = "/search")
	@ApiOperation(value = "Return all shoes models specify")
	public ResponseEntity<Shoes> all(ShoeFilter filter, @RequestHeader Integer version) throws VersionException {

		ShoeCore shoeCore = shoeFacade.get(version);
		if (shoeCore == null) {
			throw new VersionException(versionMessage + version , 2);
		}
		return ResponseEntity.ok(shoeCore.search(filter));
	}

}
