package Gioco;
import java.util.ArrayList;
import java.util.Random;


public class Bot{
    private Mazzo memoria;
    private ArrayList<Carta> mano = new ArrayList<>();
    Random rd = new Random();
    Carta lastEnemy = null;     //quando il nemico gioca per secondo questa carta va settata con la carta giocata da lui;
    Carta lastFirstBot = null;
    private int strategia = 0;
    private int distanza = 0;
    private String nome;
    private int difficolta;

    public Bot(String nome, int difficolta) {
        this.nome = nome;
        this.difficolta = difficolta;
        memoria = new Mazzo();
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    public void aggiornaMemoria(Carta x){       //da implementare nel game loop quando l'avversario gioca per secondo
        this.memoria.getDeck().remove(x);
        lastEnemy = x;
    }

    public void prendiBot(Carta x){
        mano.add(x);
        memoria.getDeck().remove(x);
    }

    public Carta giocaBot(boolean firstMano , PartitaSP p){

        /*
        Appunti
          aggiungi parte fase finale per quando giochi per secondo e per primo
          questa parte deve essere praticamente perfetta serve la supervisione di cammo
        */

        //Dichiarazione base
        Carta x;
        ArrayList<CartaBot> bMano = new ArrayList<>();
        int nBri = 0;
        int nWin = 0;
        boolean conStrate = true;
        int index;
        //creazione array CartaBot
        for (Carta c : mano){
            CartaBot cb = new CartaBot(c.getSeme(), c.getNumero());
            if (c.getSeme() == p.getSemeBriscola()){
                cb.setBriscola(true);
                nBri++;
            }else
                cb.setBriscola(false);
            cb.setLiscio(c.getPunti() == 0 && !cb.isBriscola());
            bMano.add(cb);
            //quante carte possono mangiare la carta considerando lo stesso seme;
            for(Carta b : memoria.getDeck()){
                if(c.getSeme() == b.getSeme())
                    if(p.scontro(c , b) < 0)
                        cb.setnCarPred(cb.getnCarPred() + 1);
            }
        }
        //------------------------------------------------
        //se si e primi di mano
        if(firstMano){
            //se e la primissima mano si cerca di far prendere al avversario
            if(p.getMazzo1().getSize() == 33){
                for(CartaBot cb:bMano){
                    if(cb.getPunti()>=1 && cb.getPunti()<=4 && !cb.isBriscola())      //punticini non di briscola
                        cb.setGiocabilita(100 - cb.getPunti());
                    else if(cb.getPunti() == 0 && !cb.isBriscola())                   //lisci non briscola
                        cb.setGiocabilita(85 - cb.getNumero());
                    else if(cb.getPunti() == 0)                                        //lisci briscola
                        cb.setGiocabilita(70 - cb.getNumero());
                    else if(cb.getPunti()<=4)                                          //punticini briscola
                        cb.setGiocabilita(50 - cb.getPunti());
                    else if(cb.getPunti() >= 10)                                       //carichi non briscola
                        cb.setGiocabilita(30 - cb.getPunti());
                }
            }
            //procedura se ottenere o meno lultima carta
            else if(p.getMazzo1().getDeck().size() == 2) {
                for (CartaBot cb : bMano) {
                    if (p.getMazzo1().getDeck().getFirst().getPunti() >= cb.getPunti() || !cb.isBriscola() && cb.getPunti()<10)
                        if (!cb.isBriscola() && cb.getPunti() < 10)
                            cb.setGiocabilita(100 +cb.getPunti());
                        else
                            cb.setGiocabilita(-cb.getPunti()*2 - cb.getNumero()/2);
                    else
                        cb.setGiocabilita(-40 -cb.getPunti()*2 - cb.getNumero()/2);
                }
            }
            else if(p.getMazzo1().getDeck().isEmpty()){
                if(bMano.size() == 3){
                    //da fare
                }
                else if(bMano.size() == 2){
                    //da fare
                }
            }
            else {
                //strategia
                if (strategia == 0){
                    for (CartaBot cb : bMano) {
                        for (CartaBot cb2 : bMano) {
                            if ((cb.getPunti() >= 2 && cb.getPunti() <= 4) && cb2.isLiscio() && cb.getSeme() == cb2.getSeme()) {
                                strategia = 1;
                                cb2.setGiocabilita(100 + cb.getnCarPred());
                                conStrate = false;
                                break;
                            }
                        }
                    }
                }
                else if(strategia == 1 && distanza <=2){
                    strategia = 0;
                    for (CartaBot cb: bMano) {
                        if (cb.getPunti() >= 2 && cb.getPunti() <= 4 && cb.getSeme() == lastFirstBot.getSeme()) {
                            cb.setGiocabilita(120 - cb.getPunti() + cb.getnCarPred());
                            strategia = 0;
                            distanza = 0;
                            conStrate = false;
                            break;
                        }
                    }
                }
                else{
                    conStrate = true;
                    strategia = 0;
                    distanza = 0;
                }
                //svolgimento classico
                if(conStrate){
                    for (CartaBot cb : bMano) {
                        if (cb.isBriscola()){
                            cb.setGiocabilita(-10);
                            if(nBri == 0)
                                cb.setGiocabilita(-10);
                        }
                        if(cb.getPunti() > 0 && cb.getPunti() <= 4 && !cb.isBriscola())
                            cb.setGiocabilita(100 - cb.getnCarPred());
                        else if (cb.isLiscio())
                            cb.setGiocabilita(70 + cb.getnCarPred());
                        else if (cb.getPunti() > 0 && cb.getPunti() <= 4 && cb.isBriscola())
                            cb.setGiocabilita(40 - cb.getnCarPred());
                        else if (cb.getPunti() >= 10)
                            cb.setGiocabilita(-10 - cb.getnCarPred());
                    }
                }
            }
        }
        //-----------------------------------------------------------------------
        //se si e secondi di mano
        else {
            //rimuove dalla memoria la carta messa dal aversario
            memoria.getDeck().remove(p.getBanco(0));
            //controllo se ci sono carte vincenti
            for (CartaBot cb : bMano) {
                if (p.scontro(p.getBanco(0), mano.get(bMano.indexOf(cb))) < 0) {
                    if (!(cb.isBriscola() && cb.getPunti() >= 10))
                        nWin++;
                    cb.setWin(true);
                }
            }
            //fine paritta
            if(p.getMazzo1().getDeck().size() == 2) {
                //la voglio devo perdere

                //non la voglio devo vincere
            }
            else if(p.getMazzo1().getDeck().isEmpty()){
                if(bMano.size() == 3){
                    //da fare
                }
                else if(bMano.size() == 2){
                    //da fare
                }
            }
            //partita normale
            //se laversario gioca liscio
            else if (p.getBanco(0).getPunti() == 0) {
                for (CartaBot cb : bMano) {
                    if (cb.getPunti() >= 10 && !cb.isBriscola() && cb.isWin()) //carico non di briscola puo prendere
                        cb.setGiocabilita(120 + cb.getPunti());
                    else if (!cb.isWin() && cb.getPunti() < 10)      //si lascia al aversario scegliendo il meno peggio
                        cb.setGiocabilita(100 - cb.getPunti() - cb.getNumero() / 2);
                    else if (cb.isWin())                             //se non si puo lasciare si prendono piu punti possibile
                        if (cb.isBriscola())
                            cb.setGiocabilita(80 - cb.getPunti() - cb.getNumero() / 2);
                        else
                            cb.setGiocabilita(50 + cb.getPunti());
                }
            }
            //se laversario gioca punticini
            else if (p.getBanco(0).getPunti() > 0 && p.getBanco(0).getPunti() <= 4) {
                for (CartaBot cb : bMano) {
                    if (!cb.isBriscola() && cb.isWin() && (p.getBanco(0).getPunti() + cb.getPunti()) >= 6) //non di briscola puo prendere se punti decente
                        cb.setGiocabilita(160 + cb.getPunti() + cb.getnCarPred());
                    else if (!cb.isWin() && cb.getPunti() <= 4)    //si preferisce giocare per secondi
                        cb.setGiocabilita(120 - cb.getPunti() - cb.getNumero() / 2 + cb.getnCarPred());
                    else if (!cb.isBriscola() && cb.isWin() && (p.getBanco(0).getPunti() + cb.getPunti()) < 6)
                        cb.setGiocabilita(80 + cb.getPunti() + cb.getnCarPred());
                    else if (nBri > 1 && cb.isWin() && cb.getPunti() < 10) //prendere con una briscola non carico
                        cb.setGiocabilita(40 + cb.getPunti() - cb.getnCarPred());
                    else if (nWin == 0)       //gli lascia il meno peggio
                        cb.setGiocabilita(-cb.getPunti() - cb.getNumero() / 2);
                }
            }
            //in caso giochi carichi
            else if (p.getBanco(0).getPunti() >= 10) {
                for (CartaBot cb : bMano) {
                    if (cb.isWin()) {     //si sceglie la carta piu conveniente con cui prendere
                        if (!cb.isBriscola())
                            cb.setGiocabilita(100 + cb.getPunti());
                        else
                            cb.setGiocabilita(70 - cb.getnCarPred());
                    }
                    else
                        cb.setGiocabilita(-cb.getnCarPred() - cb.getPunti() - cb.getNumero() / 2);
                }
            }
        }
        //------------------------------------------------
        //Capisce quale carta abbia piu punti scelta e in caso di partita la randomizza
        index = 0;
        int v = bMano.getFirst().getGiocabilita();
        if(bMano.size() > 1) {
            for (int i = 1; i < bMano.size(); i++) {
                if (v == bMano.get(i).getGiocabilita() && rd.nextBoolean() || bMano.get(i).getGiocabilita() > v){
                    index = i;
                    v = bMano.get(i).getGiocabilita();
                }
            }
        }
        //effetua la giocata
        x = mano.get(index);
        distanza++;
        if(firstMano)
            lastFirstBot = x;
        mano.remove(x);
        return x;
    }
}
