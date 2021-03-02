package br.ce.wcaquino.taskbackend.utils;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.Assert;

public class DateUtilsTest {
	@Test
	public void deveRetornarTrueParaDatasFuturas() {
		LocalDate date = LocalDate.of(2030, 01, 01);
		Assert.assertFalse(DateUtils.isEqualOrFutureDate(date));
	}
	
	@Test
	public void deveRetornarFalseParaDatasPassadas() {
		LocalDate date = LocalDate.of(2020, 01, 01);
		Assert.assertFalse(DateUtils.isEqualOrFutureDate(date));
	}

	@Test
	public void deveRetornarTrueParaDataAtual() {
		LocalDate date = LocalDate.now();
		Assert.assertTrue(DateUtils.isEqualOrFutureDate(date));
	}

}
