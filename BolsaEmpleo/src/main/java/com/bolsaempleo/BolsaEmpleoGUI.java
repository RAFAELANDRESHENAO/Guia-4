package com.bolsaempleo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class BolsaEmpleoGUI extends JFrame {
    private BolsaEmpleo bolsaEmpleo;
    private JTextArea outputArea;
    private JButton refreshButton; // Botón para refrescar

    public BolsaEmpleoGUI(BolsaEmpleo bolsaEmpleo) {
        this.bolsaEmpleo = bolsaEmpleo;
        initComponents();
    }
    private void initComponents() {
        setTitle("Bolsa de Empleo");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Título
        JLabel tituloLabel = crearTituloLabel("/imagenes/titulo.png");
        add(tituloLabel, BorderLayout.NORTH);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(crearPanelBotones(), BorderLayout.NORTH);
        mainPanel.add(crearMessagePanel(), BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JLabel crearTituloLabel(String ruta) {
        JLabel label = new JLabel();
        try {
            ImageIcon icono = new ImageIcon(Objects.requireNonNull(getClass().getResource(ruta)));
            Image imagen = icono.getImage().getScaledInstance(800, 400, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(imagen));
        } catch (Exception e) {
        }
        return label;
    }

    private JPanel crearPanelBotones() {
        String[] textosBotones = {"Agregar Aspirante", "Listar Cédulas", "Buscar por Cédula",
                "Buscar por Nombre", "Ordenar Aspirantes",
                "Mayor Experiencia", "Aspirante más Joven",
                "Contratar Aspirante", "Eliminar Falta de experiencia",
                "Promedio de Edad"};
        String[] rutasIconos = {"/imagenes/agregar.png", "/imagenes/listar.png",
                "/imagenes/buscar.png", "/imagenes/buscar_nombre.png",
                "/imagenes/ordenar.png", "/imagenes/mayor_experiencia.png",
                "/imagenes/mas_joven.png", "/imagenes/contratar.png",
                "/imagenes/eliminar.png", "/imagenes/promedio_edad.png"};

        JPanel buttonPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        for (int i = 0; i < textosBotones.length; i++) {
            JButton boton = crearBotonConIcono(rutasIconos[i], textosBotones[i]);
            int finalI = i;
            boton.addActionListener(e -> ejecutarAccion(finalI));
            buttonPanel.add(boton);
        }
        return buttonPanel;
    }

    private JPanel crearMessagePanel() {
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        refreshButton = new JButton("Refrescar");
        refreshButton.addActionListener(e -> refreshMessages());

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(scrollPane, BorderLayout.CENTER);
        messagePanel.add(refreshButton, BorderLayout.SOUTH);
        return messagePanel;
    }

    private JButton crearBotonConIcono(String rutaIcono, String textoBoton) {
        JButton boton = new JButton(textoBoton);
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource(rutaIcono));
            Image imagen = icono.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            boton.setIcon(new ImageIcon(imagen));
        } catch (Exception e) {
        }
        return boton;
    }

    private void ejecutarAccion(int index) {
        switch (index) {
            case 0 -> agregarAspirante();
            case 1 -> listarCedulas();
            case 2 -> buscarPorCedula();
            case 3 -> buscarPorNombre();
            case 4 -> ordenarAspirantes();
            case 5 -> aspiranteMayorExperiencia();
            case 6 -> aspiranteMasJoven();
            case 7 -> contratarAspirante();
            case 8 -> eliminarAspirantesConPocaExperiencia();
            case 9 -> calcularPromedioEdad();
        }
    }

    private void mostrarMensaje(String mensaje) {
        outputArea.append(mensaje + "\n\n");
    }

    private void refreshMessages() {
        outputArea.setText(""); // Limpiar el área de texto
        mostrarMensaje("Mensajes refrescados."); // Mensaje de confirmación (opcional)
    }

    private void agregarAspirante() {
        JTextField[] fields = {
                new JTextField(), new JTextField(), new JTextField(),
                new JTextField(), new JTextField(), new JTextField()
        };
        String[] labels = { "Cédula:", "Nombre:", "Edad:", "Experiencia:", "Profesión:", "Teléfono:" };
        Object[] message = new Object[labels.length * 2];

        for (int i = 0; i < labels.length; i++) {
            message[i * 2] = labels[i];
            message[i * 2 + 1] = fields[i];
        }

        if (JOptionPane.showConfirmDialog(null, message, "Agregar Aspirante", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                // Extraer y validar valores
                int cedula = Integer.parseInt(fields[0].getText());
                String nombre = fields[1].getText();
                int edad = Integer.parseInt(fields[2].getText());
                int experiencia = Integer.parseInt(fields[3].getText());
                String profesion = fields[4].getText();
                String telefono = fields[5].getText();

                // Validar campos no vacíos
                for (JTextField field : fields) {
                    if (field.getText().isEmpty()) {
                        throw new IllegalArgumentException("Todos los campos son obligatorios.");
                    }
                }

                // Validar edades y experiencia
                if (edad <= 0 || experiencia < 0) {
                    throw new IllegalArgumentException("La edad debe ser mayor a 0 y la experiencia no puede ser negativa.");
                }

                // Validar el formato del teléfono
                if (!telefono.matches("\\d{10}")) {
                    throw new IllegalArgumentException("El teléfono debe tener 10 dígitos.");
                }

                // Agregar aspirante
                bolsaEmpleo.agregarAspirante(new Aspirante(cedula, nombre, edad, experiencia, profesion, telefono));
                JOptionPane.showMessageDialog(null, "Aspirante agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                mostrarMensaje("Ingrese valores numéricos válidos.");
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException ex) {
                mostrarMensaje(ex.getMessage());
            } catch (Exception ex) {
                mostrarMensaje("Error al agregar aspirante: " + ex.getMessage());
            }
        }
    }


    private void listarCedulas() {
        List<Integer> cedulas = bolsaEmpleo.listarCedulasAspirantes();
        mostrarMensaje("Cédulas de aspirantes:");
        for (int cedula : cedulas) {
            mostrarMensaje(String.valueOf(cedula));
        }
    }

    private void buscarPorCedula() {
        String cedulaStr = JOptionPane.showInputDialog("Ingrese la cédula del aspirante a buscar:");
        try {
            int cedula = Integer.parseInt(cedulaStr);
            Aspirante aspirante = bolsaEmpleo.buscarAspirantePorCedula(cedula);
            if (aspirante != null) {
                mostrarMensaje("Aspirante encontrado:\n\n" +
                        "Cédula: " + aspirante.getCedula() + "\n" +
                        "Nombre: " + aspirante.getNombre() + "\n" +
                        "Edad: " + aspirante.getEdad() + "\n" +
                        "Experiencia: " + aspirante.getExperiencia() + "\n" +
                        "Profesión: " + aspirante.getProfesion() + "\n" +
                        "Teléfono: " + aspirante.getTelefono());
            } else {
                mostrarMensaje("No se encontró un aspirante con esa cédula.");
            }
        } catch (NumberFormatException ex) {
            mostrarMensaje("Por favor, ingrese un número de cédula válido.");
        }
    }


    private void buscarPorNombre() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del aspirante a buscar:");
        List<Aspirante> aspirantes = bolsaEmpleo.buscarAspirantePorNombre(nombre);
        if (aspirantes.isEmpty()) {
            mostrarMensaje("No se encontraron aspirantes con ese nombre.");
        } else {
            mostrarMensaje("Aspirantes encontrados:");
            for (Aspirante a : aspirantes) {
                mostrarMensaje("Nombre: " + a.getNombre() + ", Cédula: " + a.getCedula());
            }
        }
    }

    private void ordenarAspirantes() {
        String[] opciones = {"edad", "experiencia", "profesion"};
        String criterio = (String) JOptionPane.showInputDialog(null, "Seleccione el criterio de ordenación:",
                "Ordenar Aspirantes", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (criterio != null) {
            List<Aspirante> aspirantesOrdenados = bolsaEmpleo.ordenarAspirantesPor(criterio);
            mostrarMensaje("Aspirantes ordenados por " + criterio + ":");
            for (Aspirante a : aspirantesOrdenados) {
                mostrarMensaje(a.getNombre() + ", " + criterio + ": " +
                        (criterio.equals("edad") ? a.getEdad() :
                                criterio.equals("experiencia") ? a.getExperiencia() : a.getProfesion()));
            }
        }
    }

    private void aspiranteMayorExperiencia() {
        Aspirante mayorExperiencia = bolsaEmpleo.obtenerAspiranteMayorExperiencia();
        if (mayorExperiencia != null) {
            mostrarMensaje("El aspirante con mayor experiencia es: " + mayorExperiencia.getNombre() +
                    " con " + mayorExperiencia.getExperiencia() + " años de experiencia.");
        } else {
            mostrarMensaje("No hay aspirantes registrados.");
        }
    }

    private void aspiranteMasJoven() {
        Aspirante masJoven = bolsaEmpleo.obtenerAspiranteMasJoven();
        if (masJoven != null) {
            mostrarMensaje("El aspirante más joven es: " + masJoven.getNombre() +
                    " con " + masJoven.getEdad() + " años.");
        } else {
            mostrarMensaje("No hay aspirantes registrados.");
        }
    }

    private void contratarAspirante() {
        String cedulaStr = JOptionPane.showInputDialog("Ingrese la cédula del aspirante a contratar:");
        try {
            int cedula = Integer.parseInt(cedulaStr);
            Aspirante aspirante = bolsaEmpleo.buscarAspirantePorCedula(cedula);

            if (aspirante != null) {
                Object[] opciones = {"Sí", "No"}; // Cambiar textos de los botones
                int option = JOptionPane.showOptionDialog(null,
                        "¿Desea contratar a " + aspirante.getNombre() + "?\n\nCédula: " + aspirante.getCedula(),
                        "Contratar Aspirante", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, opciones, opciones[0]);

                if (option == JOptionPane.YES_OPTION) {
                    bolsaEmpleo.eliminarAspirantePorCedula(cedula);
                    mostrarMensaje("Aspirante " + aspirante.getNombre() + " contratado y eliminado de la lista.");
                } else {
                    mostrarMensaje("Contratación cancelada para " + aspirante.getNombre() + ".");
                }
            } else {
                mostrarMensaje("No se encontró un aspirante con esa cédula.");
            }
        } catch (NumberFormatException ex) {
            mostrarMensaje("Por favor, ingrese un número de cédula válido.");
        }
    }

    private void eliminarAspirantesConPocaExperiencia() {
        String experienciaStr = JOptionPane.showInputDialog("Ingrese los años de experiencia mínima:");
        try {
            int experienciaMinima = Integer.parseInt(experienciaStr);
            bolsaEmpleo.eliminarAspirantesConPocaExperiencia(experienciaMinima);
            mostrarMensaje("Se han eliminado aspirantes con menos de " + experienciaMinima + " años de experiencia.");
        } catch (NumberFormatException ex) {
            mostrarMensaje("Por favor, ingrese un número válido.");
        }
    }

    private void calcularPromedioEdad() {
        double promedioEdad = bolsaEmpleo.calcularPromedioEdad();
        mostrarMensaje("El promedio de edad de los aspirantes es: " + promedioEdad);
    }

    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Aspirante.class).buildSessionFactory();
        Session session = sessionFactory.openSession();

        SwingUtilities.invokeLater(() -> {
            BolsaEmpleo bolsaEmpleo = new BolsaEmpleo(session);
            BolsaEmpleoGUI gui = new BolsaEmpleoGUI(bolsaEmpleo);
            gui.setVisible(true);
        });
    }
}