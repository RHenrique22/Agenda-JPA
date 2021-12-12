package dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import modelo.Reuniao;

public class DAOReuniao extends DAO<Reuniao> {
    public Reuniao read(Object chave) {
        try {
            int id = (int) chave;
            TypedQuery<Reuniao> q = manager.createQuery(
                "SELECT R FROM Reuniao R WHERE R.id =:id",
                Reuniao.class
            );
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Reuniao> consulta() {
        try {
            TypedQuery<Reuniao> q = manager.createQuery(
                "SELECT DISTINCT R FROM Convidado C JOIN C.reunioes R ORDER BY R.id",
                Reuniao.class
            );
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
