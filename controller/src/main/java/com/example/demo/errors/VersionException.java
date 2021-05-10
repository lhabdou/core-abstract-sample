package com.example.demo.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Exception générée lorsque l'utilisateur saisie un numéro de 
 * version non existante (hors 1 ou 2)
 * 
 * @author asoilihi
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class VersionException extends Exception {

	private static final long serialVersionUID = 4688654117397550L;

	private String message;

	private Integer code;

}
