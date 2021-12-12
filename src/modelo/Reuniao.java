package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

@Entity
public class Reuniao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dataHora;
	
	@Transient
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    private String assunto;
    
    @ManyToMany
    private List<Participante> participantes = new ArrayList<>();

    public Reuniao() {}
    
    public Reuniao (String dataHora, String assunto) {
        this.dataHora = LocalDateTime.parse(dataHora, formatter);
        this.assunto = assunto;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDatahora() {
        return this.dataHora;
    }

    public void setDatahora(LocalDateTime datahora) {
        this.dataHora = datahora;
    }

    public String getAssunto() {
        return this.assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public List<Participante> getParticipantes() {
        return this.participantes;
    }

    public void adicionar(Participante p) {
        this.participantes.add(p);
    }

    public void remover(Participante p) {
        this.participantes.remove(p);
    }

    public Participante localizarParticipante(String nome) {
        for (Participante participante : participantes) {
            if (participante.getNome().equals(nome)) {
                return participante;
            }
        }
        return null;
    }

    public int TotalDeParticipantes() {
        return this.participantes.size();
    }

    @Override
    public String toString() {
        String output = "";
        output += String.format("%nId: %d%nData e hora: %s%nAssunto: %s%nParticipantes: ",
            this.getId(), this.getDatahora().format(formatter), this.getAssunto());

        for (Participante p : participantes) {
            output += String.format("%s ", p.getNome());
        }

        return output;
    }

}
