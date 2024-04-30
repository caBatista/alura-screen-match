package br.com.alura.screenmatch.model;

public enum Genre {
	ACTION,
	ROMANCE,
	COMEDY,
	DRAMA,
	CRIME,
	ANIMATION,
	BIOGRAPHY;

	public static Genre fromString(String text) {
		text = text.toUpperCase();
		
		for (Genre genre : Genre.values()) {
			if (genre.toString().equals(text)) {
				return genre;
			}
		}
		throw new IllegalArgumentException("There was no genre defined this way: " + text);
	}
}
