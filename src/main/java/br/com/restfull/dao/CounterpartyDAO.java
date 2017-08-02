package br.com.restfull.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.restfull.model.Counterparty;

public class CounterpartyDAO {

	public List<Counterparty> getCounterparties(){
		List<Counterparty> counterParties = new ArrayList();
		Counterparty counterparty = new Counterparty();
		
		counterparty.setName("PLUTO1");
		counterParties.add(counterparty);
		
		counterparty = new Counterparty();
		counterparty.setName("PLUTO2");
		counterParties.add(counterparty);
		return counterParties;
	}
}
