'use client';

import IProject from "@/types/project";
import Link from "next/link";
import useSWR from "swr";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:3010';

const fetcher = (url: string) => fetch(url).then((res) => res.json());

export default function ProjectsPage() {

  const { data: projects, error } = useSWR<IProject[]>(`${API_BASE_URL}/project`, fetcher);

  if (error) return <div>Falha ao carregar os projetos.</div>;
  if (!projects) return <div>Carregando...</div>;
  
  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-2">Projetos de Lei para Votação</h1>
      <p className="text-gray-700 mb-6">
        Veja abaixo todos os projetos de lei disponíveis para voto.
      </p>
      <div className="space-y-4">
        {projects.map((project) => (
          <div
            key={project.id}
            className="w-full bg-white p-6 border border-gray-300 rounded-lg shadow-sm hover:shadow-md transition duration-300"
          >
            <h2 className="text-2xl font-semibold text-gray-800 mb-2">{project.name}</h2>
            <p className="text-gray-600 mb-4">{project.description}</p>
            <Link href={`/projects/${project.id}`}>
              <button className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700 transition">
                Ver Detalhes
              </button>
            </Link>
          </div>
        ))}
      </div>
    </div>
  );
  }
  