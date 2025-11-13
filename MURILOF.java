import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.*;
public class MURILOF {
    public static void main(String[] args) {
        String[] vet = {"Zn(S)", "Zn(2+)(Aq)", "Cu(S)", "Cu(2+)(Aq)"};
        Object sla = JOptionPane.showInputDialog(null, null, "escolha uma espécie", JOptionPane.QUESTION_MESSAGE, null, vet, vet[0]);
        Object sla1 = JOptionPane.showInputDialog(null, null, "escolha outra espécie", JOptionPane.QUESTION_MESSAGE, null, vet, vet[0]);
        String e1 = (String) sla;
        String e2 = (String) sla1;
        if (e1.substring(0, e1.indexOf("(")).equals(e2.substring(0, e2.indexOf("(")))) {
            JOptionPane.showMessageDialog(null, "as especies não podem ser iguais", "ERRO", JOptionPane.ERROR_MESSAGE);
        } else if(e1.substring(e1.lastIndexOf("(") + 1, e1.lastIndexOf(")")).equals(e2.substring(e2.lastIndexOf("(") + 1, e2.lastIndexOf(")")))) {
            JOptionPane.showMessageDialog(null, "as especies são inválidas", "ERRO", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    public static HashMap<String, Double> lerArquivos(String arquivo) {
        HashMap<String, Double> hash = new HashMap<>();
        try {
            File file = new File(arquivo);
            Scanner input = new Scanner(file);

            while (input.hasNextLine()) {
                String valor = input.nextLine();
                int pos = valor.indexOf(" ");
                hash.put(valor.substring(0, pos), Double.parseDouble(valor.substring(pos + 1, valor.length())));
            }

            input.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo!!", "ERRO", JOptionPane.ERROR_MESSAGE);
        }
        return hash;
    }
}
