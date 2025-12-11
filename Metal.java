public class Metal {
    private String nome;
    private float potencialReducao;
    private int carga;

    public Metal(String nome, float potencialReducao, int carga) {
        this.nome = nome;
        this.potencialReducao = potencialReducao;
        this.carga = carga;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getPotencialReducao() {
        return potencialReducao;
    }

    public void setPotencialReducao(float potencialReducao) {
        this.potencialReducao = potencialReducao;
    }

    public int getCarga() {
        return carga;
    }

    public void setCarga(int carga) {
        this.carga = carga;
    }
}
