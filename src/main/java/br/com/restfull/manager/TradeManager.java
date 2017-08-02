package br.com.restfull.manager;

import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import br.com.restfull.dao.CounterpartyDAO;
import br.com.restfull.model.Counterparty;
import br.com.restfull.model.Trade;

/**Class to check all types of trade
 * @author Reinaldo Holtz
 * @version 1.00
 */
public class TradeManager {

	/**
     * Method to check the trade.
     * @author Reinaldo Holtz
     * @param  Trade
     * @return String
     */	
	public String tradeCheck(Trade trade){		
		String result = "";
      
		result = this.checkAllTypes(trade);		
		// SPOT, FORWARD: validate the value date against the product type
		// *** I did not understand what is being asked.
		
		if (trade.getType() != null && trade.getType().toUpperCase().contains("VANILLAOPTION")){
			result = result + this.checkOptionsType(trade);
		}
		
		return result;
	}
	
	/**
     * Method to check all type trade:
     * Value date cannot be before trade date
     * Value date cannot fall on weekend or non-working day for currency
     * If the counterparty is one of the supported ones
     * Validate currencies if they are valid ISO codes (ISO 4217)
     * @author Reinaldo Holtz
     * @param  Trade
     * @return String
     */	
	public String checkAllTypes(Trade trade){
		String result = "";
		
		if (trade.getValueDate() != null && trade.getValueDate().before(trade.getTradeDate())){
			result = result + "1001";
		}				
		
		if (trade.getValueDate() != null  && checkWorkingDay(trade.getValueDate())){
			result = result + "1002";
		}			
		
		if (!checkCounterparty(trade.getCustomer())){
			result = result + "1003";
		}
		
		if (trade.getCcyPair() != null && !checkCurrency(trade.getCcyPair())){
			result = result + "1004";
		}
		return result;
	}
	
	/**
     * Method to check Option type trade:
     * The style can be either American or European.
     * American option style will have in addition the excerciseStartDate, which has to be after the trade date but before the expiry date.
     * Expiry date and premium date shall be before delivery date.
     * @author Reinaldo Holtz
     * @param  Trade
     * @return String
     */	
	public String checkOptionsType(Trade trade){
		String result = "";
	
		if (trade.getStyle() != null && !checkStyle(trade.getStyle())){
			result = result + "1005";
		}
					
		if (trade.getStyle() != null &&  trade.getStyle().equals("AMERICAN")){
			if (!(trade.getExcerciseStartDate().after(trade.getTradeDate()) && trade.getExcerciseStartDate().before(trade.getExpiryDate()))){
				result = result + "1006";
			}		
		}
	
		if (!(trade.getExpiryDate().before(trade.getDeliveryDate()) && trade.getPremiumDate().before(trade.getDeliveryDate()))){
			result = result + "1007";
		}
		return result;
	}
	
	/**
     * Method to check if the counterparty is one of the supported ones
     * @author Reinaldo Holtz
     * @param  nameCounterParty
     * @return String
     */	
	public boolean checkCounterparty(String nameCounterParty){
		boolean found = false;
		CounterpartyDAO counterpartyDAO = new CounterpartyDAO();
		List<Counterparty> counterParties = counterpartyDAO.getCounterparties();
		
		for (int i=0;i<counterParties.size();i++){
			Counterparty counterparty = new Counterparty();
			counterparty = counterParties.get(i);
			if (counterparty.getName().equals(nameCounterParty)){
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	/**
     * Method to check American option style will have in addition the excerciseStartDate, 
     * which has to be after the trade date but before the expiry date.
     * @author Reinaldo Holtz
     * @param  style
     * @return String
     */	
	public boolean checkStyle(String style){
		if (style.equals("EUROPEAN") || style.equals("AMERICAN")){
			return true;
		}
		return false;
	}
	
	/**
     * Method to check Working Day
     * @author Reinaldo Holtz
     * @param  date
     * @return boolean
     */	
	public boolean checkWorkingDay (Date date)
	{
		Calendar data = Calendar.getInstance();
	    data.setTime(date);
	    int dayOfWeek = data.get(Calendar.DAY_OF_WEEK);
	    
	    if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY)
	    {	           
	        return true;
	    }	   
	    return false;
   }

	/**
     * Method to check Currency
     * @author Reinaldo Holtz
     * @param  currency
     * @return boolean
     */	
    public boolean checkCurrency(String currency){
	   String curr1 = "";
	   String curr2 = "";
	   Currency curr;		   
	   boolean result = false;
	   
	   if (currency.length() > 3){
		   curr1 = currency.substring(0,3);
		   curr2 = currency.substring(3,6);
		   try{
			   curr = Currency.getInstance(curr1);
			   if (curr.equals(curr1)){
				   result = true;
			   }
		   }catch(Exception ex){
			   return false;   
		   }
		   
		   try{
			   curr = Currency.getInstance(curr2);
			   if (curr.equals(curr2)){
				   result = true;
			   }
		   }catch(Exception ex){
			   return false;   
		   }
	   }else{
		   try{
			   curr = Currency.getInstance(currency);
			   if (curr.equals(currency)){
				   result = true;
			   }
		   }catch(Exception ex){
			   return false;   
		   }
	   }
	   
	   return result;
    }
}
