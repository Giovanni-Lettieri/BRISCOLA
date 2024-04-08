package Gioco;

import java.util.ArrayList;

public class Giocatore {
    private String nome;
    private ArrayList<Carta> mano = new ArrayList<>();

    public Giocatore(String nome) {
        this.nome = nome;
        mano.clear();
    }

    public void prendi(Carta x)
    {
        mano.add(x);
    }

    public Carta getCarta(int index)
    {
        return mano.get(index);
    }

    public Carta giocaCarta(int index)
    {
        Carta x = mano.get(index);
        mano.remove(index);
        return x;
    }



    /*
    @Override
    public String toString()
    {
        String s = "-----MANO DI: " + nome +"----\n";
        for(Carta x: mano)
        {
            s+=x.getNome() + "\n";
        }
        s+="------------------------\n";
        return s;
    }
    */

}
