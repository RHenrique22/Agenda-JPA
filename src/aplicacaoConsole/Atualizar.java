package aplicacaoConsole;

import fachada.Fachada;
import modelo.*;

public class Atualizar {
    public Atualizar() {
		try {
			Fachada.inicializar();
//			Fachada.desabilitarEmail(true);
			Fachada.adicionarParticipanteReuniao("jose", 1);
			Fachada.removerParticipanteReuniao("joao", 1);
			Fachada.apagarParticipante("ana");  //cancela 3 e 4
			Fachada.cancelarReuniao(5);
		} 
		catch (Exception e) {
			System.out.println("---> " + e.getMessage());
		}

		Fachada.finalizar();
	}

	public static void main (String[] args) {
		Atualizar app = new Atualizar();
	}
}
