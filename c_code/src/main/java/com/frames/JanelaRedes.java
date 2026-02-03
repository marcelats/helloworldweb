package com.frames;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import com.gerador.Gerador;
import com.gerador.GeradorParSMPL;
import com.gerador.GeradorSIMPACK;
import com.gerador.GeradorSIMPACK2;
import com.gerador.GeradorSMPL;
import com.gerador.GeradorSMPLX;
import com.graph.Arc;
import com.graph.Chegada;
import com.graph.Graph;
import com.graph.Node;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import io.javalin.Javalin;
import io.javalin.http.UploadedFile;
import java.io.*;

import java.nio.charset.StandardCharsets;

/**
 * Classe que extende Janela e implementa as funcionalidades para modelagem
 * baseada na teoria de redes de filas
 * @author Andr    Felipe Rodrigues
 *
 */
public class JanelaRedes {
	
	/* tipoNo de qualquer centro de servi   o     o mesmo valor - tipoNo serve para o modulo avaliador */
	
	public JanelaRedes()
	{
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
 		Javalin app = Javalin.create().start(8002);

		app.post("/process", ctx -> {

            String lang = ctx.formParam("lang");
            if (lang == null) {
                ctx.status(400).result("Lang not received");
                return;
            }

            UploadedFile arquivo = ctx.uploadedFile("file");
            if (arquivo == null) {
                ctx.status(400).result("File not received");
                return;
            }

            String entradaTexto;
            try (InputStream is = arquivo.content()) {
                entradaTexto = new String(is.readAllBytes());
            }
			Graph graph = new Graph();
			String filename = null;
			try{		
	
		        int nChegadas = -1;
				int[] nClientes, cs;
				float[] tChegada;
				String nomeGrafo = "", tExec = "",numCiclos = "", tamBatch = "", maxEntidades = "", 
				tipoModelo = "", warmUp = "", tWarmup = "";
		        try (BufferedReader br = new BufferedReader(new StringReader(entradaTexto))) {
		            String linha;
					int count = 0;
		            while ((linha = br.readLine()) != null) {
		                linha = linha.trim();
						if (linha.startsWith("digraph")){
							Pattern pattern = Pattern.compile("\\bdigraph\\s+(\\w+)\\s*\\{");
			                Matcher matcher = pattern.matcher(linha);
			                if (matcher.find()) {
			                    nomeGrafo = matcher.group(1); 
			                    graph.setNomeModelo(nomeGrafo);
			                }
						}
										
		                if (linha.startsWith("comment=")) {
		                    Matcher matcher = Pattern.compile("comment=\"([^\"]+)\"").matcher(linha);
		                    if (matcher.find()) {
		                        String[] partes = matcher.group(1).trim().split(" ");
		                        if (partes.length >= 8) {
		                            tExec = partes[0];
									numCiclos = partes[1];
									tamBatch = partes[2];
									maxEntidades = partes[3];
									tipoModelo = partes[4];
									warmUp = partes[5];
									tWarmup = partes[6]; 
		                        }
		                	}
							graph.setTempoExecucao(tExec);
							graph.setNumeroCiclos(numCiclos);
							graph.setTamanhoBatch(tamBatch);
							graph.setNumeroMaximoEntidades(maxEntidades);
							graph.setTipoModelo(tipoModelo);
							graph.setWarmUp(warmUp);
							graph.setTamWarmUp(tWarmup);
		            	}
						
		                else if (linha.matches("\\d+\\s*\\[.*comment=.*\\]")) {
							int indexEspaco = linha.indexOf(" ");
							String nodeIdStr = linha.substring(0, indexEspaco).trim();
							int nodeId = Integer.parseInt(nodeIdStr);
							String comment = null;
							Pattern pattern = Pattern.compile("comment=\\s*(\"[^\"]*\"|[^\\s\\]]+)");
							Matcher matcher = pattern.matcher(linha);
		
							if (matcher.find()) {
								comment = matcher.group(1);
								if (comment.startsWith("\"") && comment.endsWith("\"")) {
									comment = comment.substring(1, comment.length() - 1); 
								}
							}
							String[] partes = comment.trim().split("\\s+"); 
							int tipo = -1;
							if (partes.length == 1) {tipo = Integer.parseInt(partes[0]);}
							int distChegada = -1;
							int distServico = -1;
							String mediaChegada = "";
							String mediaServico = "";
							String numberOfServers = "";
							Boolean filaVazia = false;
							Boolean maxMin = false;
							String sequencia = "";
							String sequenciaFonte = "";
							String desvioPadrao = "";
							String desvioPadraoFonte = "";
							Boolean isPrimRec = false;
					
							if (partes.length >= 5) {
								tipo = Integer.parseInt(partes[0]);
								if(!("None".equals(partes[1]))) distChegada = Integer.parseInt(partes[1]);
								System.out.println("distChegada do no " + nodeId + ": " + distChegada);
								distServico = Integer.parseInt(partes[2]);
								System.out.println("distServico do no " + nodeId + ": " + distServico);
								if(!("None".equals(partes[3]))) mediaChegada = partes[3];
								mediaServico = partes[4];
								numberOfServers = partes[5];
								filaVazia = Boolean.parseBoolean(partes[6]);
								maxMin = Boolean.parseBoolean(partes[7]);
								sequenciaFonte = partes[8];
								sequencia = partes[9];
								desvioPadraoFonte = partes[10];
								desvioPadrao = partes[11];
								isPrimRec = Boolean.parseBoolean(partes[12]);
							}
							if(tipo == 1 || tipo == 3){
								Node node = new Node(0,0,null,tipo,nodeId,"");
								node.setImage(new ImageIcon("/home/marcela/Downloads/asdadesktop/imgIcones/R1x1.gif"));
								graph.addNode(node);
							}
							else 
							{
								Node node = new Node(0, 0, null, tipo, nodeId, "CS");
								switch(distServico)
								{	case 0:
										node.setDistribuicaoServico("normal");
										System.out.println("distServico " + distServico + " do no " + nodeId + " eh normal, node.getDistribuicaoServico: " + node.getDistribuicaoServico());
										break;
									case 1:
										node.setDistribuicaoServico("expntl");
										System.out.println("distServico " + distServico + " do no " + nodeId + " eh expntl, node.getDistribuicaoServico: " + node.getDistribuicaoServico());
										break;
									case 2:
										node.setDistribuicaoServico("uniform");
										System.out.println("distServico " + distServico + " do no " + nodeId + " eh uniform, node.getDistribuicaoServico: " + node.getDistribuicaoServico());
										break;
									case 3:
										node.setDistribuicaoServico("hyperx");
										System.out.println("distServico " + distServico + " do no " + nodeId + " eh hyperx, node.getDistribuicaoServico: " + node.getDistribuicaoServico());
										break;
									case 4:
										node.setDistribuicaoServico("erlang");
										System.out.println("distServico " + distServico + " do no " + nodeId + " eh erlang, node.getDistribuicaoServico: " + node.getDistribuicaoServico());
										break;
									default:
										node.setDistribuicaoServico("expntl");
										System.out.println("distServico " + distServico + " do no " + nodeId + " eh nenhuma das anteriores, node.getDistribuicaoServico: " + node.getDistribuicaoServico());
								}
								switch(distChegada)
								{	case 0:
										node.setDistribuicaoChegada("normal");
										System.out.println("distChegada " + distChegada + " do no " + nodeId + " eh normal, node.getDistribuicaoChegada: " + node.getDistribuicaoChegada());
										break;
									case 1:
										node.setDistribuicaoChegada("expntl");
										System.out.println("distChegada " + distChegada + " do no " + nodeId + " eh expntl, node.getDistribuicaoChegada: " + node.getDistribuicaoChegada());
										break;
									case 2:
										node.setDistribuicaoChegada("uniform");
										System.out.println("distChegada " + distChegada + " do no " + nodeId + " eh uniform, node.getDistribuicaoChegada: " + node.getDistribuicaoChegada());
										break;
									case 3:
										node.setDistribuicaoChegada("hyperx");
										System.out.println("distChegada " + distChegada + " do no " + nodeId + " eh hyperx, node.getDistribuicaoChegada: " + node.getDistribuicaoChegada());
										break;
									case 4:
										node.setDistribuicaoChegada("erlang");
										System.out.println("distChegada " + distChegada + " do no " + nodeId + " eh erlang, node.getDistribuicaoChegada: " + node.getDistribuicaoChegada());
										break;
									default:
										node.setDistribuicaoChegada("expntl");
										System.out.println("distChegada " + distChegada + " do no " + nodeId + " eh nenhuma das anteriores, node.getDistribuicaoChegada: " + node.getDistribuicaoChegada());
								}
						
								node.setMediaFonte(mediaChegada);
								node.setMedia(mediaServico);
								node.setImage(new ImageIcon("/home/marcela/Downloads/asdadesktop/imgIcones/R1x1.gif"));
								graph.addNode(node);
								node.setNumServidores(numberOfServers);
								node.setEstatisticaFilaVazia(filaVazia);
								node.setComprimentoMaxMin(maxMin);
								node.setSequencia(sequencia);
								node.setMediaFonte(mediaChegada);
								node.setSequenciaFonte(sequenciaFonte);
								node.setDesvioPadrao(desvioPadrao);
								node.setDesvioPadraoFonte(desvioPadraoFonte);
								node.setNomeCentroServico("CS" + String.valueOf(count++));
								node.setPrimRec(isPrimRec);
								System.out.println("distServico do no " + nodeId + " eh " + graph.getNode(nodeId).getDistribuicaoServico() + " e distChegada eh " + graph.getNode(nodeId).getDistribuicaoChegada());
							}
						}
		                else if (linha.contains("->")) {
					        Pattern pattern = Pattern.compile("(\\d+)\\s*->\\s*(\\d+)\\s*\\[comment=(\\d+)\\]");
					        Matcher matcher = pattern.matcher(linha);
		
				        if (matcher.find()) {
				            int origem = Integer.parseInt(matcher.group(1));     
				            int destino = Integer.parseInt(matcher.group(2));    
				            String probabilidade = matcher.group(3); 
							graph.getNode(origem).addArc(graph.getNode(destino), destino, graph.getNode(destino).getTipoNo(), "");
							Vector arcs = graph.getNode(origem).getArcs();
							Arc arc = (Arc) arcs.get(arcs.size() - 1); 
							graph.getNode(origem).setProb(true);
							arc.setProbabilidade(probabilidade);
				        }
		    
					} else if (linha.contains("Arrivals")){
						String[] partes = linha.trim().split("\\s+");
						nChegadas = Integer.parseInt(partes[1]);
						System.out.println(nChegadas + " chegadas");
						nClientes = new int[nChegadas];
						tChegada = new float[nChegadas];
						cs = new int[nChegadas];
						for(int i = 0; i < nChegadas; i++)
						{
							nClientes[i] = Integer.parseInt(partes[2+i*3]);
							tChegada[i] = Float.parseFloat(partes[3+i*3]);
							cs[i] = Integer.parseInt(partes[4+i*3]);
							graph.addChegada(new Chegada(nClientes[i], tChegada[i], cs[i]));
							System.out.println("Chegada " + i + ": " + nClientes[i] +" " + tChegada[i] + " " +cs[i]+"\n");
						}
					}
		        }   
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			System.out.println("Texec: "+graph.getTempoExecucao());
			System.out.println("nome modelo: "+graph.getNomeModelo());
			System.out.println("n ciclos: "+graph.getNumeroCiclos());
			System.out.println("n max entidades: "+graph.getNumeroMaximoEntidades());
			System.out.println("tam batch: "+graph.getTamanhoBatch());
			System.out.println("bool warmup: "+graph.getWarmUp());
			System.out.println("warmup time: "+graph.getTamWarmUp());
			System.out.println("modelo aberto, fechado ou misto: "+graph.getTipoModelo());
			System.out.println("n chegadas :"+graph.getChegadaSize());
			for (int i = 0; i <graph.getChegadaSize(); i++) {	
				System.out.println("num clientes do "+i+": "+graph.getChegada(i).getNumeroClientes());
				System.out.println("tempo chegada do "+i+": "+graph.getChegada(i).getTempoChegada());
				System.out.println("indice do "+i+": "+graph.getChegada(i).getNodeIndex());
			}
			System.out.println("num nos: "+graph.getSize());
			for (int i = 0; i <graph.getSize(); i++) {	
				System.out.println(graph.getNode(i));
				System.out.println(i+" tipo no: "+graph.getTipoNo(i));
				System.out.println(i+" ID no: "+graph.getIdNo(i));
				System.out.println(i+" num arcos: "+graph.getNode(i).getSize());
				System.out.println(i+" nome centro serviço: "+graph.getNode(i).getNomeCentroServico());
				System.out.println(i+" distribuicao chegada: "+graph.getNode(i).getDistribuicaoChegada());
				System.out.println(i+" distribuicao servico: "+graph.getNode(i).getDistribuicaoServico());
				System.out.println(i+" bool tempo resposta: "+graph.getNode(i).getTempoResposta());
				System.out.println(i+" bool throughput: "+graph.getNode(i).getThroughput());
				System.out.println(i+" bool tamanho fila: "+graph.getNode(i).getTamanhoFila());
				System.out.println(i+" bool estatistica fila vazia: "+graph.getNode(i).getEstatisticaFilaVazia());
				System.out.println(i+" bool comp max min: "+graph.getNode(i).getComprimentoMaxMin());
				System.out.println(i+" num filas: "+graph.getNode(i).getNumFilas());
				System.out.println(i+" num servidores: "+graph.getNode(i).getNumServidores());
				System.out.println(i+" media: "+graph.getNode(i).getMedia());
				System.out.println(i+" num voltas: "+graph.getNode(i).getNumVoltas());
				System.out.println(i+" sequencia: "+graph.getNode(i).getSequencia());
				System.out.println(i+" media fonte: "+graph.getNode(i).getMediaFonte());
				System.out.println(i+" sequencia fonte: "+graph.getNode(i).getSequenciaFonte());
				System.out.println(i+" num arestas: "+graph.getNode(i).getChega());
				System.out.println(i+" desvio padrao: "+graph.getNode(i).getDesvioPadrao());
				System.out.println(i+" ponto mais provavel: "+graph.getNode(i).getPontoMaisProvavel());
				System.out.println(i+" desvio padrao fonte: "+graph.getNode(i).getDesvioPadraoFonte());
				System.out.println(i+" ponto mais provavel fonte: "+graph.getNode(i).getPontoMaisProvavelFonte());
				System.out.println(i+" tid: "+graph.getNode(i).getTid());
		
				for (int j = 0; j <graph.getNode(i).getSize(); j++) {
					System.out.println(i+" "+j+ " id no destino: "+graph.getNode(i).getArc(j).getIdNoArc());
					System.out.println(i+" "+j+ " probabilidade: "+graph.getNode(i).getArc(j).getProbabilidade());}
				}
	    	} catch (Exception e){	
				e.printStackTrace();
				throw e;
			} 
			Gerador gerador;
			if (lang.equals("C SMPL"))
			{
				gerador = new GeradorSMPL(graph);
				gerador.criaArquivo();
				gerador.leGabarito("/com/gabaritos/GABARITO.DAT");											
			}
			else if (lang.equals("C ParSMPL"))
			{
				gerador = new GeradorParSMPL(graph);
				gerador.criaArquivo();
				gerador.leGabarito(null); 
			}
			else if ( lang.equals("C SMPLX"))
			{
				gerador = new GeradorSMPLX(graph);
				gerador.criaArquivo();
				gerador.leGabarito("/com/gabaritos/GABARITO_SMPLX.DAT");					
			}
			else if (lang.equals("C SIMPACK"))
			{
				gerador = new GeradorSIMPACK(graph);
				gerador.criaArquivo();
				gerador.leGabarito("/com/gabaritos/GABARITO_SIMPACK.DAT");				
			}
			else if ( lang.equals("C SIMPACK2"))
			{
				gerador = new GeradorSIMPACK2(graph);
				gerador.criaArquivo();
				gerador.leGabarito("/com/gabaritos/GABARITO_SIMPACK2.DAT");
			}	
			if(lang.equals("C SIMPACK2"))
			{
				String codigoGerado = "untitled.cpp";
				Path origem = Path.of(codigoGerado);
				String uuid = UUID.randomUUID().toString().replace("-", "");
				Path destino = Path.of("/tmp", uuid + ".cpp");
				Files.move(origem, destino, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Arquivo movido para: " + destino.toString());
			    ctx.contentType("application/octet-stream");
			    ctx.header("Content-Disposition", "attachment; filename=\"code.cpp\"");
			    ctx.result(new FileInputStream(destino.toFile()));
			}
			else
			{
				String codigoGerado = graph.getNomeModelo() + ".c"; 
				Path origem = Path.of(codigoGerado);
				String uuid = UUID.randomUUID().toString().replace("-", "");
				Path destino = Path.of("/tmp", uuid + ".c");
				Files.move(origem, destino, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Arquivo movido para: " + destino.toString());
				ctx.contentType("application/octet-stream");
				ctx.header("Content-Disposition", "attachment; filename=\"code.c\"");
				ctx.result(new FileInputStream(destino.toFile()));
			}	
		});
		app.post("/execute", ctx -> {
			String lang = ctx.formParam("lang");
			if (lang == null) {
				ctx.status(400).result("Lang not received");
				return;
			}
			UploadedFile arquivo = ctx.uploadedFile("file");
			if (arquivo == null) {
				ctx.status(400).result("File not received");
				return;
			}
			File tmpDir = new File("/app/tmp");
			if (!tmpDir.exists()) {
				tmpDir.mkdirs();
				System.out.println("Diretório /app/tmp criado.");
			}
			Files.deleteIfExists(Path.of("/app/tmp/untitled.c"));
			Files.deleteIfExists(Path.of("/app/tmp/untitled"));
			Files.deleteIfExists(Path.of("untitled.out")); 
			JanelaRedes.extrairParaTmp("exec/smpl/modelo.c", "untitled.c");
			File fsrc = new File("/app/tmp/untitled.c");
			String originalName = arquivo.filename(); 
			String extensao = originalName.contains(".") ? originalName.substring(originalName.lastIndexOf(".")) : "";
			try (InputStream in = arquivo.content(); FileOutputStream out = new FileOutputStream(fsrc)) {
				in.transferTo(out);
				System.out.println("Arquivo enviado salvo em: " + fsrc.getAbsolutePath());
			}
			if (!fsrc.exists()) {
				throw new FileNotFoundException("Arquivo " + fsrc.getAbsolutePath() + " não encontrado após upload.");
			}
			String[] comandoCompilar = new String[0];
			if (lang.equals("C SMPL")) {
				System.out.println("Modo de compilação: C SMPL");
				JanelaRedes.extrairParaTmp("exec/smpl/smpl.c", "smpl.c");
				JanelaRedes.extrairParaTmp("exec/smpl/smpl.h", "smpl.h");
				JanelaRedes.extrairParaTmp("exec/smpl/rand.c", "rand.c");
				JanelaRedes.extrairParaTmp("exec/smpl/bmeans.c", "bmeans.c");
				System.out.println(System.getenv("PATH"));
	            Path tempDir = Files.createTempDirectory("sessao"); 
				String sessaoDir = tempDir.toAbsolutePath().toString();
				String[] comandoDocker = {
				    "/usr/bin/docker", "run", "--rm",
				    "-v", sessaoDir + ":/app",
				    "--memory=256m",
				    "--cpus=1",
				    "--pids-limit=50",
				    "--network=none",
				    "gcc:12",
				    "bash", "-c",
				    "cc -I /app/tmp -o /app/tmp/untitled /app/tmp/*.c -lm && /app/tmp/untitled"
				};
	
				Process p = new ProcessBuilder(comandoDocker)
				        .redirectErrorStream(true)
				        .start();
	
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
				    String line;
				    while ((line = reader.readLine()) != null) {
				        System.out.println(line);
				    }
				}
	
	        } else if (lang.equals("C SMPLX")) {
				System.out.println("Modo de compilação: C SMPLX");
				JanelaRedes.extrairParaTmp("exec/smplx/smplx.c", "smplx.c");
				JanelaRedes.extrairParaTmp("exec/smplx/smplx.h", "smplx.h");
				JanelaRedes.extrairParaTmp("exec/smplx/randpar.c", "randpar.c");
				JanelaRedes.extrairParaTmp("exec/smplx/randpar.h", "randpar.h");
				JanelaRedes.extrairParaTmp("exec/smplx/declaracoes.h", "declaracoes.h");
				File fSmplx_c = new File("/app/tmp/smplx.c");
				File fRandpar_c = new File("/app/tmp/randpar.c");
				File fRandpar_h = new File("/app/tmp/randpar.h");
				File fDeclaracoes_h = new File("/app/tmp/declaracoes.h");
				System.out.println("Verificando arquivos SMPLX em /app/tmp:");
				System.out.println("- smplx.c existe? " + fSmplx_c.exists() + " | Caminho: " + fSmplx_c.getAbsolutePath());
				System.out.println("- randpar.c existe? " + fRandpar_c.exists() + " | Caminho: " + fRandpar_c.getAbsolutePath());
				System.out.println("- randpar.h existe? " + fRandpar_h.exists() + " | Caminho: " + fRandpar_h.getAbsolutePath());
				System.out.println("- declaracoes.h existe? " + fDeclaracoes_h.exists() + " | Caminho: " + fDeclaracoes_h.getAbsolutePath());
				comandoCompilar = new String[]{
					"cc", "-I", "/app/tmp",
					"-o", "/app/tmp/untitled",
					"/app/tmp/untitled.c",
					"/app/tmp/smplx.c",
					"/app/tmp/randpar.c",
					"-lm"
				};
	        } else {
				ctx.status(400).result("Linguagem '" + lang + "' não suportada.");
				return;
			}
			try {
				System.out.println("Comando de compilação: " + String.join(" ", comandoCompilar));
				Process p = new ProcessBuilder(comandoCompilar).redirectErrorStream(true).start();
				String compilationOutput = printSaida("gcc", p.getInputStream());
				int exitCode = p.waitFor();
				System.out.println("Compilação finalizada com código de saída: " + exitCode);
				File bin = new File("/app/tmp/untitled");
				if (!bin.exists()) {
					System.err.println("Erro: Binário não gerado após compilação.");
					ctx.status(500).result("Compilação falhou: binário não gerado.\nSaída do compilador:\n" + compilationOutput);
					return;
				}
				bin.setExecutable(true);
				System.out.println("Binário compilado e tornado executável: " + bin.getAbsolutePath());
				Files.deleteIfExists(Path.of("untitled.out"));
				System.out.println("Executando binário: /app/tmp/untitled");
				ProcessBuilder builder = new ProcessBuilder("/app/tmp/untitled");
				builder.redirectErrorStream(true);
				Process p2 = builder.start();
				String executionOutput = printSaida("exec", p2.getInputStream());
				int execExitCode = p2.waitFor();
				System.out.println("Execução finalizada com código de saída: " + execExitCode);
				File outputFile = new File("untitled.out");
				if (!outputFile.exists()) {
					System.err.println("Erro: Arquivo de saída 'untitled.out' não foi gerado pela execução.");
					ctx.status(500).result("Execução falhou: arquivo de saída 'untitled.out' não gerado.\nSaída da execução:\n" + executionOutput);
					return;
				}
				String uuid = UUID.randomUUID().toString().replace("-", "");
				Path finalOutputPath = Path.of("/tmp", uuid + ".out");
				Files.move(Path.of("untitled.out"), finalOutputPath, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Arquivo de saída movido para: " + finalOutputPath.toAbsolutePath());
				ctx.contentType("application/octet-stream");
				ctx.header("Content-Disposition", "attachment; filename=\"rel.out\"");
				ctx.result(new FileInputStream(finalOutputPath.toFile()));
	
			} catch (IOException eio) {
				System.err.println("Erro de I/O durante compilação/execução: " + eio.getMessage());
				eio.printStackTrace();
				ctx.status(500).result("Erro de I/O: " + eio.getMessage());
			} catch (InterruptedException e1) {
				System.err.println("Processo interrompido: " + e1.getMessage());
				e1.printStackTrace();
				ctx.status(500).result("Processo interrompido: " + e1.getMessage());
			} catch (RuntimeException re) {
				System.err.println("Erro em tempo de execução: " + re.getMessage());
				re.printStackTrace();
				ctx.status(500).result("Erro em tempo de execução: " + re.getMessage());
			}
		});
	}

	private static void copiarArquivos(File f1, File f2) {
		try {
			String temp;
			BufferedReader ori = new BufferedReader(new FileReader(f1));
			FileWriter dest = new FileWriter(f2, true);
			while ((temp = ori.readLine()) != null) {
				temp = temp + "\n";
				dest.write(temp);
			}
			ori.close();
			dest.close();
		} catch (FileNotFoundException e) {
			System.err.println("Erro: Arquivo não encontrado para cópia: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Erro de I/O durante cópia de arquivo: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static File extrairParaTmp(String caminhoInterno, String nomeArquivo) throws IOException {
		InputStream in = JanelaRedes.class.getClassLoader().getResourceAsStream(caminhoInterno);
		if (in == null) {
			throw new FileNotFoundException("Arquivo não encontrado no classpath: " + caminhoInterno);
		}
		File destino = new File("/app/tmp/" + nomeArquivo);
		destino.getParentFile().mkdirs();
		Files.copy(in, destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
		System.out.println("Extraído: " + caminhoInterno + " para " + destino.getAbsolutePath());
		return destino;
	}

	private static String printSaida(String prefixo, InputStream inputStream) throws IOException {
		StringBuilder output = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			String linha;
			while ((linha = reader.readLine()) != null) {
				output.append("[").append(prefixo).append("] ").append(linha).append("\n");
				System.out.println("[" + prefixo + "] " + linha);
			}
		}
		return output.toString();
	}
}

	
