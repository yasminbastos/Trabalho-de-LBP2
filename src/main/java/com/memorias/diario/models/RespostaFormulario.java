package com.memorias.diario.models;

import jakarta.persistence.*;

@Entity
public class RespostaFormulario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    private Integer indicePergunta;

    @Column(length = 1000)
    private String resposta;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Integer getIndicePergunta() { return indicePergunta; }
    public void setIndicePergunta(Integer indicePergunta) { this.indicePergunta = indicePergunta; }

    public String getResposta() { return resposta; }
    public void setResposta(String resposta) { this.resposta = resposta; }
}
