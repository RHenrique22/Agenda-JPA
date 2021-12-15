package modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class Participante {
	@Id
    private String nome;
	
    private String email;
    
    @ManyToMany(mappedBy = "participantes", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Reuniao> reunioes = new ArrayList<>();

    public Participante() {}
    
    public Participante (String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Reuniao> getReunioes() {
        return this.reunioes;
    }

    public void adicionar(Reuniao r) {
        this.reunioes.add(r);
    }

    public void remover(Reuniao r) {
        this.reunioes.remove(r);
    }

    public int TotalDeReunioes() {
        return this.reunioes.size();
    }

    @Override
    public String toString() {
        String output;
        output = String.format("%nNome: %s%nE-mail: %s%nReuniões: ", this.getNome(), this.getEmail());

        if (reunioes.isEmpty()) {
            output += "Sem reuniões";
        }
        else {
            for (Reuniao r : reunioes) {
                output += String.format("%s ", r.getId());
            }
        }

        return output;
    }

}
