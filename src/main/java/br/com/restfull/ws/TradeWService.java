package br.com.restfull.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import br.com.restfull.manager.TradeManager;
import br.com.restfull.model.Trade;


/**Class to receive the trade to check
 * @param json trade
 * @return String
 * @author Reinaldo Holtz
 * @version 1.00
 */
@Path("/json/service")
public class TradeWService {

	@GET
	@Path("/get/{jtrade}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response readTrade(@PathParam("jtrade") String jtrade) {
		String result = "";
		Gson gson = new Gson();
		try{
			Trade trade = gson.fromJson(jtrade, Trade.class);
			TradeManager tradeManager = new TradeManager();
			result = tradeManager.tradeCheck(trade);
		}catch(Exception ex){
			result = "9999";
		}	
		return Response.status(201).entity(result).build();	
	}
	
}