package com.bolsaempleo;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class BolsaEmpleo {

    private Session session;

    public BolsaEmpleo(Session session) {
        this.session = session;
    }

    public void agregarAspirante(Aspirante aspirante) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(aspirante);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public List<Integer> listarCedulasAspirantes() {
        String hql = "SELECT cedula FROM Aspirante";
        Query<Integer> consulta = session.createQuery(hql, Integer.class);
        return consulta.list();
    }

    public Aspirante buscarAspirantePorCedula(int cedula) {
        return session.get(Aspirante.class, cedula);
    }

    public List<Aspirante> buscarAspirantePorNombre(String nombre) {
        String hql = "FROM Aspirante WHERE nombre = :nombre";
        Query<Aspirante> consulta = session.createQuery(hql, Aspirante.class);
        consulta.setParameter("nombre", nombre);
        return consulta.list();
    }

    public List<Aspirante> ordenarAspirantesPor(String criterio) {
        String hql = "FROM Aspirante ORDER BY " + criterio;
        Query<Aspirante> consulta = session.createQuery(hql, Aspirante.class);
        return consulta.list();
    }

    public Aspirante obtenerAspiranteMayorExperiencia() {
        String hql = "FROM Aspirante ORDER BY experiencia DESC";
        Query<Aspirante> consulta = session.createQuery(hql, Aspirante.class);
        consulta.setMaxResults(1);
        return consulta.uniqueResult();
    }

    public Aspirante obtenerAspiranteMasJoven() {
        String hql = "FROM Aspirante ORDER BY edad ASC";
        Query<Aspirante> consulta = session.createQuery(hql, Aspirante.class);
        consulta.setMaxResults(1);
        return consulta.uniqueResult();
    }

    public boolean eliminarAspirantePorCedula(int cedula) {
        Aspirante aspirante = buscarAspirantePorCedula(cedula);
        if (aspirante != null) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.delete(aspirante);
                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        } else {
            return false;
        }
    }


    public void eliminarAspirantesConPocaExperiencia(int experienciaMinima) {
        String hql = "DELETE FROM Aspirante WHERE experiencia < :experienciaMinima";
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query<?> consulta = session.createQuery(hql);
            consulta.setParameter("experienciaMinima", experienciaMinima);
            int filasAfectadas = consulta.executeUpdate();
            tx.commit();
            System.out.println("Se han eliminado " + filasAfectadas + " aspirantes con menos de " + experienciaMinima + " años de experiencia.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public double calcularPromedioEdad() {
        String hql = "SELECT AVG(edad) FROM Aspirante";
        Query<Double> consulta = session.createQuery(hql, Double.class);
        Double promedio = consulta.uniqueResult();
        return promedio != null ? promedio:0.0;
}
}