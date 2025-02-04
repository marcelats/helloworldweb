import type { Metadata } from "next";
import Link from "next/link";
import "./globals.css";

export const metadata: Metadata = {
  title: "WebApp Votação",
  description: "ICMC/USP Projeto de Disciplina",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="pt-BR">
      <body className="bg-gray-100 text-gray-800">
        <header className="bg-gradient-to-r from-blue-600 to-indigo-600 text-white shadow-lg">
          <div className="container mx-auto flex items-center justify-between p-6">
            {/* Logo e Nome do Webapp */}
            <Link href="/" className="text-2xl font-bold tracking-wider hover:text-indigo-200">
              Webapp de Votação
            </Link>
            
            {/* Navegação */}
            <nav className="flex space-x-6">
              <Link href="/" className="hover:underline hover:text-indigo-200 transition duration-200">
                Home
              </Link>
              <Link href="/projects" className="hover:underline hover:text-indigo-200 transition duration-200">
                Projetos de Lei
              </Link>
              <Link href="/about" className="hover:underline hover:text-indigo-200 transition duration-200">
                Sobre
              </Link>
            </nav>
          </div>
        </header>

        <main className="container mx-auto p-8">{children}</main>

        <footer className="bg-gray-800 text-white p-4 text-center">
          <p>© 2024 Webapp de Votação</p>
        </footer>
      </body>
    </html>
  );

}
