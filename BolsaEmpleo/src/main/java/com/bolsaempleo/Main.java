package com.bolsaempleo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Configuración de Hibernate y creación de BolsaEmpleo
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Aspirante.class).buildSessionFactory();
        Session session = sessionFactory.openSession();
        BolsaEmpleo bolsaEmpleo = new BolsaEmpleo(session);

        // Prueba de guardar un aspirante
        guardarAspirantePrueba(session);

        // Crear y mostrar la GUI
        SwingUtilities.invokeLater(() -> new BolsaEmpleoGUI(bolsaEmpleo).setVisible(true));

        // Cerrar la sesión y la fábrica al finalizar
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            session.close();
            sessionFactory.close();
        }));
    }

    private static void guardarAspirantePrueba(Session session) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Aspirante aspirante = new Aspirante(123456, "Juan Pérez", 30, 5, "Ingeniero", "123456789");
            session.save(aspirante);
            tx.commit();
            System.out.println("Aspirante guardado exitosamente: " + aspirante.getNombre());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Error al guardar el aspirante: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
