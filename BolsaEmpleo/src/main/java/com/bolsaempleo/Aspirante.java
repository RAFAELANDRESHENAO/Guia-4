package com.bolsaempleo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "aspirantes")  // Nombre de la tabla en la base de datos
public class Aspirante {

    @Id  // Indica que 'cedula' es la clave primaria
    @Column(name = "cedula")  // Nombre de la columna en la base de datos
    private int cedula;

    @Column(name = "nombre")  // Nombre de la columna en la base de datos
    private String nombre;

    @Column(name = "edad")  // Nombre de la columna en la base de datos
    private int edad;

    @Column(name = "experiencia")  // Nombre de la columna en la base de datos
    private int experiencia;

    @Column(name = "profesion")  // Nombre de la columna en la base de datos
    private String profesion;

    @Column(name = "telefono")  // Nombre de la columna en la base de datos
    private String telefono;

    // Constructor por defecto
    public Aspirante() {
    }

    // Constructor con parámetros
    public Aspirante(int cedula, String nombre, int edad, int experiencia, String profesion, String telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.edad = edad;
        this.experiencia = experiencia;
        this.profesion = profesion;
        this.telefono = telefono;
    }

    // Getters
    public int getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public String getProfesion() {
        return profesion;
    }

    public String getTelefono() {
        return telefono;
    }
}
