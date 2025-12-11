
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Main {

    final static Scanner l = new Scanner(System.in);

    public static void main(String[] args) {
        ArrayList<Metal> metais = lerArquivos("metais.txt");
        String[] nomeMetais = new String[metais.size()];
        for (int i = 0; i < metais.size(); i++) {
            nomeMetais[i] = metais.get(i).getNome();
        }
        Object opcao1 = JOptionPane.showInputDialog(null, null, "escolha uma espécie", JOptionPane.QUESTION_MESSAGE, null, nomeMetais, nomeMetais[0]);
        Object opcao2 = JOptionPane.showInputDialog(null, null, "escolha outra espécie", JOptionPane.QUESTION_MESSAGE, null, nomeMetais, nomeMetais[0]);
        Metal m1 = procurarMetal((String) opcao1, metais);
        Metal m2 = procurarMetal((String) opcao2, metais);
        String[] opcoes = {"Aquoso", "Solido"};
        Object estadoM1 = JOptionPane.showInputDialog(null, null, "Qual é o estado do " + m1.getNome(), JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        Object estadoM2 = JOptionPane.showInputDialog(null, null, "Qual é o estado do " + m2.getNome(), JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        m1.setEstado((String) estadoM1);
        m2.setEstado((String) estadoM2);
        if (m1.getEstado().equals(m2.getEstado())) {
            JOptionPane.showMessageDialog(null, "Os estados dos metais NÃO podem ser iguais", "ERRO", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String msg;
        if (m1.getPotencialReducao() == m2.getPotencialReducao()) {
            msg = "as espécies tem o mesmo potencial de redução. A pilha não funciona";
            JOptionPane.showMessageDialog(null, msg, "ERRO", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (m1.getPotencialReducao() > m2.getPotencialReducao()) {
            float potencialPilha = m1.getPotencialReducao() - m2.getPotencialReducao();
            msg = "O metal que reduz é " + m1.getNome() + "\nO metal que oxida é " + m2.getNome() + "\nO potencial da pilha é " + potencialPilha + "\n";
            JOptionPane.showMessageDialog(null, msg, "potencial da pilha", JOptionPane.INFORMATION_MESSAGE);
        } else if (m2.getPotencialReducao() > m1.getPotencialReducao()) {
            float potencialPilha = m2.getPotencialReducao() - m1.getPotencialReducao();
            msg = "O metal que reduz é " + m2.getNome() + "\nO metal que oxida é " + m1.getNome() + "\nO potencial da pilha é " + potencialPilha + "\n";
            JOptionPane.showMessageDialog(null, msg, "potencial da pilha", JOptionPane.INFORMATION_MESSAGE);
        }
        JOptionPane.showMessageDialog(null, formarEquacao(m1, m2), "Equação global", JOptionPane.INFORMATION_MESSAGE);
    }

    public static String formarEquacao(Metal m1, Metal m2) {

        // Definir estados químicos
        String estado1 = m1.getEstado().equalsIgnoreCase("Solido") ? "(s)" : "(aq)";
        String estado1Ion = "(aq)"; // íons sempre aquosos

        String estado2 = m2.getEstado().equalsIgnoreCase("Solido") ? "(s)" : "(aq)";
        String estado2Ion = "(aq)";

        Metal reduz;
        Metal oxida;

        if (m1.getPotencialReducao() > m2.getPotencialReducao()) {
            reduz = m1;
            oxida = m2;
        } else {
            reduz = m2;
            oxida = m1;
        }

        int eRed = reduz.getCarga();
        int eOx = oxida.getCarga();

        int mmc = mmc(eRed, eOx);
        int fatorRed = mmc / eRed;
        int fatorOx = mmc / eOx;

        String reduzMetal = reduz.getEstado().equalsIgnoreCase("Solido") ? "(s)" : "(aq)";
        String reduzIon = "(aq)";
        String oxidaMetal = oxida.getEstado().equalsIgnoreCase("Solido") ? "(s)" : "(aq)";
        String oxidaIon = "(aq)";

        String semiOx
                = fatorOx + oxida.getNome() + oxidaMetal + " → "
                + fatorOx + oxida.getNome() + "^" + oxida.getCarga() + "+" + oxidaIon
                + " + " + (fatorOx * eOx) + " e⁻";

        String semiRed
                = fatorRed + reduz.getNome() + "^" + reduz.getCarga() + "+" + reduzIon
                + " + " + (fatorRed * eRed) + " e⁻ → "
                + fatorRed + reduz.getNome() + reduzMetal;

        String global
                = fatorOx + oxida.getNome() + oxidaMetal + " + "
                + fatorRed + reduz.getNome() + "^" + reduz.getCarga() + "+" + reduzIon
                + " → "
                + fatorOx + oxida.getNome() + "^" + oxida.getCarga() + "+" + oxidaIon
                + " + "
                + fatorRed + reduz.getNome() + reduzMetal;

        return "Oxidação:\n" + semiOx
                + "\n\nRedução:\n" + semiRed
                + "\n\nEquação Global:\n" + global;
    }

    private static int mmc(int a, int b) {
        return a * b / mdc(a, b);
    }

    private static int mdc(int a, int b) {
        while (b != 0) {
            int r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    public static ArrayList<Metal> lerArquivos(String arquivo) {
        ArrayList<Metal> metais = new ArrayList<>();
        try {
            File file = new File(arquivo);
            Scanner input = new Scanner(file);

            while (input.hasNextLine()) {
                String valor = input.nextLine();
                String nome = valor.substring(0, valor.indexOf(" "));
                String carga = valor.substring((valor.indexOf(" ") + 1), valor.lastIndexOf(" "));
                String potencialReducao = valor.substring((valor.lastIndexOf(" ") + 1));
                metais.add(new Metal(nome, Float.parseFloat(potencialReducao), Integer.parseInt(carga)));
            }

            input.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo!!", "ERRO", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return metais;
    }

    public static Metal procurarMetal(String nome, ArrayList<Metal> metais) {
        for (int i = 0; i < metais.size(); i++) {
            if (metais.get(i).getNome().equals(nome)) {
                return metais.get(i);
            }
        }
        return null;
    }
}
