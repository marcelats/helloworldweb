// src/app/about/page.tsx
export default function AboutPage() {
    return (
      <div className="flex flex-col items-center p-8">
        <h2 className="text-3xl font-bold mb-4">Sobre o Webapp de Votação</h2>
        <p className="text-gray-700 mb-6 text-center max-w-2xl">
          Este projeto foi desenvolvido para facilitar a participação pública na votação de
          projetos de lei. Aqui, você pode explorar e votar em projetos que afetam nossa
          sociedade.
        </p>
        <h3 className="text-2xl font-semibold mb-2">Desenvolvedores</h3>
        <p className="text-gray-600 text-center">
          Criado por João, Luciano e Marcela.
        </p>
      </div>
    );
  }
  