package com.ricoh.yunsang.Data;

import java.io.Serializable;

public class Word implements Serializable{

	private static final long serialVersionUID = 1L;
	private String word;
	private String explanation_1;
	private String explanation_2;
	private String explanation_3;

	/**
	 * @param word
	 * @param explanation_1
	 * @param explanation_2
	 * @param explanation_3
	 */
	public Word(String word, String explanation_1, String explanation_2,
			String explanation_3) {
		super();
		this.word = word;
		this.explanation_1 = explanation_1;
		this.explanation_2 = explanation_2;
		this.explanation_3 = explanation_3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Word [word=" + word + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}

	public Word() {
		// TODO Auto-generated constructor stub
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getExplanation_1() {
		return explanation_1;
	}

	public void setExplanation_1(String explanation_1) {
		this.explanation_1 = explanation_1;
	}

	public String getExplanation_2() {
		return explanation_2;
	}

	public void setExplanation_2(String explanation_2) {
		this.explanation_2 = explanation_2;
	}

	public String getExplanation_3() {
		return explanation_3;
	}

	public void setExplanation_3(String explanation_3) {
		this.explanation_3 = explanation_3;
	}

}
