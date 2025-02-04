'use client';

import VoteButtons from '@/components/VoteButtons';
import IProject from '@/types/project';
import IVoteMessage from '@/types/vote';
import { useParams } from 'next/navigation';
import useSWR from 'swr';

// Função de fetcher usada pelo SWR para buscar dados
const fetcher = (url: string) => fetch(url).then((res) => res.json());

const API_REQUISICAO_PROJETOS = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:3010';
const API_POSTAGEM_VOTOS = process.env.NGINX_PUBLIC_API_BASE_URL || 'http://localhost:80';

export default function ProjectDetailPage() {

  const { id } = useParams(); // Obtém o ID do projeto a partir da URL
  
  const  handleVote = async (projectId: string, voteType: string) => {
    console.log(`Voto no projeto ${projectId}: ${voteType}`);
    // Enviar o voto para a API
    const date = new Date(Date.now());
    const voteDate = date.toISOString();
    const data: IVoteMessage = {
      projectId,
      voteType,
      voteDate
    };
    const response = await fetch( 
      `${API_POSTAGEM_VOTOS}/kafka-producer/vote`, { 
      method: 'POST', 
      body: JSON.stringify(data),
      headers: {
        'Content-Type': 'application/json'
      }
    }
    );

    if (!response.ok) {
      throw new Error('Erro ao enviar o voto!');
    }

    return response.json();    
  };

  // ToDo: Refatorar a chamada do SWR quando a API estiver funcionando
  const { data: project, error } = useSWR<IProject>(id ? `${API_REQUISICAO_PROJETOS}/project/${id}` : null, fetcher);

  if (error) return <div>Falha ao carregar os detalhes do projeto.</div>;
  if (!project) return <div>Carregando...</div>;

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-4">{project.name}</h1>
      <p className="text-lg text-gray-700 mb-4">{project.description}</p>
      <p className="text-md text-gray-600 mb-4">Provisório: {project.provisional ? 'Sim' : 'Não'}</p>
      <div className="bg-gray-100 p-4 rounded-lg shadow-sm">
        <h4 className="text-lg font-semibold">Detalhes adicionais:</h4>
        <p>Última vez editado: {project.last_edited_at}</p>
      </div>
      <VoteButtons projectId={id as string} onVote={handleVote} />
    </div>
  );
}
