package aplicacaoConsole;

import fachada.*;
import modelo.*;

public class Listar {
    public Listar() {
		try {
			Fachada.inicializar();
//			Fachada.desabilitarEmail(true);

			System.out.println("\n---------listagem de participantes-----");
			for(Participante p : Fachada.listarParticipantes()) 
				System.out.println(p);
			System.out.println("\n---------listagem de convidados-----");
			for(Participante p : Fachada.listarConvidados()) 
				System.out.println(p);
			System.out.println("\n---------listagem de reunioes-----");
			for(Reuniao r : Fachada.listarReunioes()) 
				System.out.println(r);

		} 
		catch (Exception e) {
			System.out.println("---> " + e.getMessage());
		}
			
		Fachada.finalizar();

	}

	public static void main (String[] args) {
		Listar app = new Listar();
	}
}
