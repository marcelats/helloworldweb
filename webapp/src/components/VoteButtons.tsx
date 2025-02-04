import { useState } from 'react';

// Definir os tipos das props
interface VoteButtonsProps {
  projectId: string;
  onVote: (projectId: string, voteType: 'positive' | 'negative') => void;
}

const VoteButtons: React.FC<VoteButtonsProps> = ({ projectId, onVote }) => {
  // Tipando o estado como 'positive', 'negative', ou null
  const [voted, setVoted] = useState<'positive' | 'negative' | null>(null);

  const handleVote = (type: 'positive' | 'negative') => {
    setVoted(type);  // Atualiza o estado com o voto
    onVote(projectId, type);  // Chama a função de callback passando o ID do projeto e o tipo do voto
  };

  return (
    <div className="text-center mt-6">
      <h3 className="text-xl mb-4">Vote no Projeto de Lei</h3>
      <button
        onClick={() => handleVote('positive')}
        className={`px-6 py-3 text-white font-semibold rounded-md mr-4 ${
          voted === 'positive' ? 'bg-green-500' : 'bg-gray-500 hover:bg-green-400'
        }`}
      >
        Positivo
      </button>
      <button
        onClick={() => handleVote('negative')}
        className={`px-6 py-3 text-white font-semibold rounded-md ${
          voted === 'negative' ? 'bg-red-500' : 'bg-gray-500 hover:bg-red-400'
        }`}
      >
        Negativo
      </button>
      {voted && <p className="mt-4 text-lg">Você votou: {voted === 'positive' ? 'Positivo' : 'Negativo'}</p>}
    </div>
  );
};

export default VoteButtons;
