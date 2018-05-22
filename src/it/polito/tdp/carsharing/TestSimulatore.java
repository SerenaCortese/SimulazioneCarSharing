package it.polito.tdp.carsharing;

public class TestSimulatore {

	public static void main(String[] args) {
		Simulatore sim = new Simulatore() ;
		
		sim.setNC(15);
		sim.setT_IN(5);
		
		sim.init(8*60);
		
		sim.run();
		
		System.out.format("Arrivati %d clienti, insoddisfatti %d\n", sim.getClientiArrivati(), sim.getClientiInsoddisfatti()) ;		
		

	}

}
