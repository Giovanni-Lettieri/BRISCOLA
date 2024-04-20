package Gioco;

public abstract class Entita {
    
    public abstract void prendiCarta(Carta x);
    
    public abstract Carta giocaCarta();

    public abstract void assegnaTurno();

    public abstract void finalizaTurno();
    
    public String nome;
    public Partita partita;
    public Squadra squadra;

    public Entita(String nome, Squadra squadra, Partita partita) {
        this.nome = nome;
        this.partita = partita;
        this.squadra = squadra;
    }

    public String getNome() {
        return nome;
    }

    public Partita getPartita() {
        return partita;
    }

    public Squadra getSquadra() {
        return squadra;
    }
}
