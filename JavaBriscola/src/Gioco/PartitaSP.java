package Gioco;

public class PartitaSP extends Partita{
    public PartitaSP() {
        super(0);
    }

    @Override
    public void setupGameMode() {
        getSquadres().get(0).aggiungiNuovoGiocatore(nomiGiocatori[0], this);
        getSquadres().get(1).aggiungiNuovoBot(this,0);

        //DISTRIBUZIONE PRIME 3 CARTE
        distribuisci();
        
        Giocatore player = (Giocatore) getSquadres().getFirst().getGiocatores().getFirst();
        player.mostraFrame(-1,0);
    }
}
