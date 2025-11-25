package com.memorias.diario.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class RegistroDia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    private LocalDate data;

    private String humor;

    @Column(length = 2000)
    private String anotacoes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getHumor() { return humor; }
    public void setHumor(String humor) { this.humor = humor; }

    public String getAnotacoes() { return anotacoes; }
    public void setAnotacoes(String anotacoes) { this.anotacoes = anotacoes; }
}
