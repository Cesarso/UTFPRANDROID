package br.utfpr.cesarsoares.controledevendasdeumconfeiteiroautonomo.utils;

import java.util.Comparator;

/**
 * Esta classe foi implementada para a seguinte
 * Solução: Ordenação Natural (NaturalOrderComparator)
 * A ordenação "natural" simula como se lê os números dentro de um texto.
 * Em vez de olhar para os dígitos '3' e '4' como letras isoladas, a classe NaturalOrderComparator identifica blocos numéricos sequenciais dentro da string,
 * extrai o número completo e o compara de forma matemática (32 contra 4).
 * Com essa lógica implementada e aplicada no COMPARADOR_DESCRICAO da classe Produto, os itens agora seguem a ordem lógica esperada por qualquer usuário:
 * •Item 1
 * •Item 4
 * •Item 5
 * •Item 32
 */
public class NaturalOrderComparator implements Comparator<String> {

    @Override
    public int compare(String s1, String s2) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null) return -1;
        if (s2 == null) return 1;

        int i1 = 0;
        int i2 = 0;

        while (i1 < s1.length() && i2 < s2.length()) {
            char c1 = s1.charAt(i1);
            char c2 = s2.charAt(i2);

            if (Character.isDigit(c1) && Character.isDigit(c2)) {
                String n1 = extractNumber(s1, i1);
                String n2 = extractNumber(s2, i2);

                long val1 = Long.parseLong(n1);
                long val2 = Long.parseLong(n2);

                if (val1 != val2) {
                    return Long.compare(val1, val2);
                }

                i1 += n1.length();
                i2 += n2.length();
            } else {
                if (c1 != c2) {
                    return Character.compare(Character.toLowerCase(c1), Character.toLowerCase(c2));
                }
                i1++;
                i2++;
            }
        }
        return Integer.compare(s1.length(), s2.length());
    }

    private String extractNumber(String s, int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                sb.append(c);
            } else {
                break;
            }
        }
        return sb.toString();
    }
}