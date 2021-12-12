package dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import modelo.Participante;

public class DAOParticipante extends DAO<Participante> {
    
    public Participante read(Object chave) {
    	try {
			String nome = (String) chave;
			TypedQuery<Participante> q = manager.createQuery(
					"SELECT P FROM Participante P WHERE P.nome =:nome",
					Participante.class);
			q.setParameter("nome", nome);
			return q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
    }

    public List<Participante> consulta(String nomePart, int mes) {
        try {
        	String Consulta = String.format(
        			"SELECT DISTINCT P FROM Participante P JOIN P.reunioes R "
					+ "WHERE SUBSTRING(CAST(R.dataHora  AS TEXT), 6, 2) = '%1$d'"
					+ "AND (R.id IN(SELECT RP.id FROM Participante Pa JOIN "
					+ "Pa.reunioes RP WHERE Pa.nome = '%2$s') "
					+ "OR (R.id IN(SELECT RC.id FROM Convidado C JOIN "
					+ "C.reunioes RC WHERE C.nome = '%2$s'))) AND P.nome <> '%2$s'", mes, nomePart);
        	
			TypedQuery<Participante> q = manager.createQuery(Consulta, Participante.class);

			return q.getResultList();
			
		} catch (NoResultException e) {
			return null;
		}
    }
}
