// home_manha.js
document.addEventListener('DOMContentLoaded', function() {
  const body = document.body;

  // Personalizar saudação baseada no horário
  const saudacao = document.getElementById('saudacao');
  const hora = new Date().getHours();
  let periodo = 'manhã';

  if (hora >= 12 && hora < 18) {
      periodo = 'tarde';
  } else if (hora >= 18 || hora < 5) {
      periodo = 'noite';
  }

  saudacao.textContent = `Boa ${periodo}!`;

  // Adicionar efeito de digitação à saudação
  const textoOriginal = saudacao.textContent;
  saudacao.textContent = '';
  let i = 0;

  function typeWriter() {
      if (i < textoOriginal.length) {
          saudacao.textContent += textoOriginal.charAt(i);
          i++;
          setTimeout(typeWriter, 50);
      }
  }

  // Iniciar efeito de digitação após um breve delay
  setTimeout(typeWriter, 500);
});

// Adicionando interatividade aos cards
document.addEventListener('DOMContentLoaded', function() {
  const cards = document.querySelectorAll('.card');

  cards.forEach(card => {
      // Efeito de clique
      card.addEventListener('click', function() {
          this.style.transform = 'scale(0.98)';
          setTimeout(() => {
              this.style.transform = '';
          }, 150);

          // Simulação de redirecionamento
          const title = this.querySelector('.card-title').textContent;
          console.log(`Navegando para: ${title}`);
      });

      // Efeito de foco para acessibilidade
      card.addEventListener('keypress', function(e) {
          if (e.key === 'Enter') {
              this.click();
          }
      });
  });
});