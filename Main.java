import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Main {

    final static Scanner l = new Scanner(System.in);

    public static void main(String[] args) {

        // Inicialização das espécies
        ArrayList<Metal> metais = lerArquivos("metais.txt");

        // Preparar lista de nomes para escolher
        String[] nomeMetais = new String[metais.size()];
        for (int i = 0; i < metais.size(); i++) {
            nomeMetais[i] = metais.get(i).getNome();
        }

        // Usuário escolhe quem vai REDUZIR (tem que ter maior potencial de redução)
        Object opcaoReducao = JOptionPane.showInputDialog(null, null, "metal que vai reduzir",
                JOptionPane.QUESTION_MESSAGE, null, nomeMetais, nomeMetais[0]);

        // Usuário escolhe quem vai OXIDAR (tem que ter menor potencial de redução)
        Object opcaoOxidacao = JOptionPane.showInputDialog(null, null, "metal que vai oxidar",
                JOptionPane.QUESTION_MESSAGE, null, nomeMetais, nomeMetais[0]);

        Metal reduz = procurarMetal((String) opcaoReducao, metais);
        Metal oxida = procurarMetal((String) opcaoOxidacao, metais);

        // Estados automáticos (não pergunta, baseando na carga)
        reduz.setEstado(definirEstado(reduz));
        oxida.setEstado(definirEstado(oxida));

        // Validar se a combinação funciona
        if (reduz.getPotencialReducao() <= oxida.getPotencialReducao()) {
            JOptionPane.showMessageDialog(null,
                    "Erro: A pilha não funciona.",
                    "ERRO", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calcular potencial da pilha
        float potencial = reduz.getPotencialReducao() - oxida.getPotencialReducao();
        String potencialStr = String.format("%.2f", potencial);

        String msg = "O metal que reduz é " + reduz.getNome() +
                "\nO metal que oxida é " + oxida.getNome() +
                "\nO potencial da pilha é " + potencialStr + " V";

        JOptionPane.showMessageDialog(null, msg, "Potencial da pilha", JOptionPane.INFORMATION_MESSAGE);

        // Mostrar equação global
        JOptionPane.showMessageDialog(null, formarEquacao(reduz, oxida), "Equação global", JOptionPane.INFORMATION_MESSAGE);
    }

    // Define automaticamente o estado do metal
    public static String definirEstado(Metal m) {
        return m.getCarga() == 0 ? "Solido" : "Aquoso";
    }

    public static String formarEquacao(Metal reduz, Metal oxida) {
        String ionEstado = "(aq)";

        // Pegando as cargas para balanceamento
        int cargaRed = reduz.getCarga();
        int cargaOx = oxida.getCarga();

        // Se a carga for zero, substitui por 1 para evitar divisão por zero
        if (cargaRed == 0) cargaRed = 1;
        if (cargaOx == 0) cargaOx = 1;

        int mmc = mmc(cargaRed, cargaOx);
        int fatorRed = mmc / cargaRed;
        int fatorOx = mmc / cargaOx;

        String metalSolido = "(s)";
        String estadoInicialOx = "(s)";

        String semiOx =
            fatorOx + oxida.getNome() + estadoInicialOx + " → "
            + fatorOx + oxida.getNome() + "^" + cargaOx + "+" + ionEstado
            + " + " + (fatorOx * cargaOx) + " e⁻";

        String semiRed =
            fatorRed + reduz.getNome() + "^" + cargaRed + "+" + ionEstado
            + " + " + (fatorRed * cargaRed) + " e⁻ → "
            + fatorRed + reduz.getNome() + metalSolido;

        String global =
            fatorOx + oxida.getNome() + estadoInicialOx + " + "
            + fatorRed + reduz.getNome() + "^" + cargaRed + "+" + ionEstado
            + " → "
            + fatorOx + oxida.getNome() + "^" + cargaOx + "+" + ionEstado
            + " + "
            + fatorRed + reduz.getNome() + metalSolido;

        return "Oxidação:\n" + semiOx +
               "\n\nRedução:\n" + semiRed +
               "\n\nEquação Global:\n" + global;
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

                metais.add(new Metal(nome,
                        Float.parseFloat(potencialReducao),
                        Integer.parseInt(carga)));
            }

            input.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo!",
                    "ERRO", JOptionPane.ERROR_MESSAGE);
        }
        return metais;
    }

    public static Metal procurarMetal(String nome, ArrayList<Metal> metais) {
        for (Metal m : metais) {
            if (m.getNome().equals(nome)) {
                return m;
            }
        }
        return null;
    }
}
