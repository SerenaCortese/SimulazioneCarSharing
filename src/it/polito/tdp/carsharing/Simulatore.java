package it.polito.tdp.carsharing;

import java.util.PriorityQueue;

public class Simulatore {
	
	/*
	 * Enumerazione per i tipi di eventi gestiti dal simulatore.
	 * ENUMERAZIONE: raccolta di costanti sotto un nome unico che racchiude il Tipo delle costanti
	 * Sono costanti di valore nullo ma di tipo Evento, d'ora in poi ogni variabile che definiremo EventType
	 * potrà assumere solo questi due valori:CUSTOMER_IN, CAR_RETURNED.
	 */
	enum EventType {
		CUSTOMER_IN, // arriva un nuovo cliente
		CAR_RETURNED // viene restituita un'auto
	}
	
	//classe accessibile solo da Simulatore
	class Event implements Comparable<Event> {
		
		private int minuti ; // minuti a partire dall'inizio della simulazione
		//per soluzione completa potrei usare un LocalDateTime se simulazione su più giorni
		//o LocalTime se simulazione dura solo un giorno.
		
		private EventType tipo ;
		
		//altre info potrebbero essere info sul cliente o sulla specifica macchina
		
		public Event(int minuti, EventType tipo) {
			super();
			this.minuti = minuti;
			this.tipo = tipo;
		}
		public int getMinuti() {
			return minuti;
		}
		public EventType getTipo() {
			return tipo;
		}
		
		//NO setter=> oggetto immutabile e la priority queue lo inserisce nel posto più adatto 
		//se lo settassi di nuovo dopo inserimento in p.q., la p.q. non se ne accorgerebbe e la coda non sarebbe nel giusto ordine
		
		@Override
		public int compareTo(Event other) {
			//ordinamento crescente: rende minori tempi precedenti,maggiori quelli successivi
			return this.minuti - other.minuti ;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + minuti;
			result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Event other = (Event) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (minuti != other.minuti)
				return false;
			if (tipo != other.tipo)
				return false;
			return true;
		}
		private Simulatore getOuterType() {
			return Simulatore.this;
		}
		@Override
		public String toString() {
			return "Event [minuti=" + minuti + ", tipo=" + tipo + "]";
		}
		
		
		
	}

	// Coda degli eventi
	private PriorityQueue<Event>queue = new PriorityQueue<>() ;
	
	// Parametri di simulazione // Impostati all'inizio // Costanti durante la simulazione
	private int NC = 20 ; // numero di auto disponibili
	private int T_IN = 10 ; // periodo di arrivo di nuovi clienti (10 min)
	private int T_TRAVEL_BASE = 60 ;  // durata minima del viaggio
	private int T_TRAVEL_DURATA = 3 ; // quanti periodi di durata BASE può durare un viaggio
	
	// Modello del mondo // Stato del sistema // Evolvono in continuazione
	private int disponibili ; // numero di auto disponibili 
	
	// Valori da calcolare // Output
	private int clientiArrivati ; // clienti arrivati al noleggio
	private int clientiInsoddisfatti ; // numero di clienti insoddisfatti
	
	
	/**
	 * Crea la coda iniziale di eventi ed inizializza correttamente tutte le variabili di simulazione
	 * @param durataMax durata complessiva della simulazione, in minuti
	 */
	public void init(int durataMax) {
		
		// inizializza la coda degli eventi
		queue.clear();
		int time = 0 ;
		while (time <= durataMax) {
			Event e = new Event(time, EventType.CUSTOMER_IN) ;
			//se volessi più variabilità nell'arrivo clienti, time+(int)Math.random()*minutiScelti ad esempio
			queue.add(e) ;
			time = time + T_IN ;//ogni 10 minuti arriva un cliente=>aggiungo eventi
		}
		
		// inizializzo le variabili di simulazione
		disponibili = NC ;
		clientiArrivati = 0 ;
		clientiInsoddisfatti = 0 ;
		
	}
	
	public void run() {
		
		Event e ;
		while((e = queue.poll()) != null) {//estrae primo elemento in coda e lo elimina da coda
			processEvent(e) ;
		}
	}
	
	
	private void processEvent(Event e) {
		System.out.println(e);
		
		switch(e.getTipo()) {
			case CUSTOMER_IN:
				clientiArrivati++ ;
				if(disponibili>0) {
					// cliente soddisfatto:gli diamo auto e prevediamo quando la riporterà
					disponibili-- ;
					int durata = T_TRAVEL_BASE * (1+(int)(Math.random()*T_TRAVEL_DURATA)) ;
					//Math.random()=ho numero reale tra [0,1);*3=num reale tra [0,3); (int) = numero intero[0,3);
					//+1={1,2,3}; *60minuti=>le 3 possibili durate di noleggio quindi rientrerà in quel tempo e schedulo suo rientro
					Event rientro = new Event(e.getMinuti()+durata, EventType.CAR_RETURNED) ;
					queue.add(rientro) ;
				} else {
					// cliente insoddisfatto
					clientiInsoddisfatti++ ;
				}
				
				break;
				
			case CAR_RETURNED:
				disponibili++ ;
				break;
		}
		
	}

	public int getNC() {
		return NC;
	}
	public void setNC(int nC) {
		NC = nC;
	}
	public int getT_IN() {
		return T_IN;
	}
	public void setT_IN(int t_IN) {
		T_IN = t_IN;
	}
	public int getT_TRAVEL_BASE() {
		return T_TRAVEL_BASE;
	}
	public void setT_TRAVEL_BASE(int t_TRAVEL_BASE) {
		T_TRAVEL_BASE = t_TRAVEL_BASE;
	}
	public int getT_TRAVEL_DURATA() {
		return T_TRAVEL_DURATA;
	}
	public void setT_TRAVEL_DURATA(int t_TRAVEL_DURATA) {
		T_TRAVEL_DURATA = t_TRAVEL_DURATA;
	}
	public int getClientiArrivati() {
		return clientiArrivati;
	}
	public int getClientiInsoddisfatti() {
		return clientiInsoddisfatti;
	}
	
	
	
}
