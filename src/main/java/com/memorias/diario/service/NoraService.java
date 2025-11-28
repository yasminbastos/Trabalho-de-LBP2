package com.memorias.diario.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NoraService {

    public List<String> gerarSugestoes(String humor, Map<Integer, List<String>> preferencias) {

        List<String> sugestoes = new ArrayList<>();

        // ===============================
        // SUGESTÕES BASEADAS NO HUMOR
        // ===============================

        switch (humor) {

            case "Triste":
                sugestoes.add("Nora: Sei que hoje não está fácil. Vá com calma 💙");
                sugestoes.add("Que tal dar uma caminhada enquanto escuta música?");
                break;

            case "Estressado":
                sugestoes.add("Nora: Parece que as coisas estão pesadas hoje 😤");
                sugestoes.add("Que tal fazer uma pausa ou se afastar um pouco do que te estressa?");
                break;

            case "Bravo":
                sugestoes.add("Nora: Parece que as coisas estão pesadas hoje 😤");
                sugestoes.add("Que tal fazer uma pausa ou se afastar um pouco do que te estressa?");
                break;

            case "Feliz":
                sugestoes.add("Nora: Que alegria ver você assim! Espalhe essa energia ✨");
                break;

            case "Calmo":
                sugestoes.add("Nora: Que bom sentir essa tranquilidade 🌿");
                sugestoes.add("Aproveite esse momento para cuidar de você.");
                break;

            case "Desapontado":
                sugestoes.add("Nora: Nem sempre as coisas saem como esperamos 😔");
                sugestoes.add("Talvez seja um bom momento para descansar e reavaliar com carinho.");
                break;

            case "Preocupado":
                sugestoes.add("Nora: Muitas coisas na cabeça podem cansar bastante 🧠");
                sugestoes.add("Você pode tentar escrever o que está te preocupando.");
                break;

            case "Assustado":
                sugestoes.add("Nora: Está tudo bem sentir medo às vezes 🫂");
                sugestoes.add("Respire fundo. Você não está sozinho(a).");
                break;

            case "Frustrado":
                sugestoes.add("Nora: Frustrações machucam, eu sei 😣");
                sugestoes.add("Talvez descarregar isso em algo físico ou criativo ajude.");
                break;

            case "Enjoado":
                sugestoes.add("Nora: Parece aquele cansaço de tudo, né?");
                sugestoes.add("Que tal mudar um pouco o ambiente ou a rotina hoje?");
                break;

            case "Pensativo":
                sugestoes.add("Nora: Pensamentos profundos costumam aparecer nesses momentos 🤔");
                sugestoes.add("Refletir é importante, mas lembre de descansar a mente também.");
                break;

            case "Animado":
                sugestoes.add("Nora: Essa energia está contagiante! 🚀");
                sugestoes.add("Aproveite para fazer algo que você vinha adiando.");
                break;

            case "Envergonhado":
                sugestoes.add("Nora: Todos nós passamos por momentos assim 😅");
                sugestoes.add("Seja gentil com você, isso não define quem você é.");
                break;

            case "Tedioso":
                sugestoes.add("Nora: O tédio pode ser a porta para algo novo 🎈");
                sugestoes.add("Talvez experimentar algo diferente hoje te anime.");
                break;

            default:
                sugestoes.add("Nora: Obrigada por compartilhar como você está se sentindo 💭");
                break;
        }

        return sugestoes;
    }
}
