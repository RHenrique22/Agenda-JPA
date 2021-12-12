package modelo;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class Convidado extends Participante {
    private String empresa;

    public Convidado() {}
    
    public Convidado (String nome, String email, String empresa) {
        super(nome, email);
        this.empresa = empresa;
    }

    public String getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        String output;
        output = super.toString() + String.format("%nEmpresa: %s", this.getEmpresa());

        return output;
    }
}
