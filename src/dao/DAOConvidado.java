package dao;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import modelo.Convidado;

public class DAOConvidado extends DAO<Convidado> {
    
    public Convidado read(Object chave) {
    	try {
    		String nomeConvidado = (String) chave;
    		TypedQuery<Convidado> q = manager.createQuery(
        		"SELECT c FROM Convidado c WHERE c.nome =:nome",
        		Convidado.class);
    		q.setParameter("nome", nomeConvidado);
    		return q.getSingleResult();
    	}
    	catch (NoResultException e) {
			return null;
		}
        
    }
}
