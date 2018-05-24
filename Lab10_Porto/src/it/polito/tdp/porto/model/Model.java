package it.polito.tdp.porto.model;



import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;

import java.util.Map;



import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import org.jgrapht.graph.SimpleGraph;


import it.polito.tdp.porto.db.PortoDAO;



public class Model {



	private PortoDAO pdao = new PortoDAO();

	private Graph<Author, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

	private Map<Integer, Author> idMapAutore = new HashMap<>(pdao.getAutori());

	private Map<Integer, Paper> idMapPaper = new HashMap<>(pdao.getArticoli());

	private List<AutArt> autArt = new ArrayList<>(pdao.getArticoliEAutori());



	public void createGraph() {

		for (Author a : idMapAutore.values()) {

			graph.addVertex(a);

			// System.out.println("aggiunto vertice");

		}



		// aggiungo autori a pubblicazioni e pubblicazioni ad autori

		for (AutArt aa : this.autArt) {

			int idAut = aa.getAutore();

			int idArt = aa.getPubblicazione();

			idMapAutore.get(idAut).addPubblicazione(idMapPaper.get(idArt));

			idMapPaper.get(idArt).addAutore(idMapAutore.get(idAut));

		}



		for (AutArt aa : this.autArt) {

			for (AutArt aa2 : this.autArt) {

				if ((aa.getPubblicazione() == aa2.getPubblicazione()) && (aa.getAutore() != aa2.getAutore())) {

					Author a1 = idMapAutore.get(aa.getAutore());

					Author a2 = idMapAutore.get(aa2.getAutore());

					graph.addEdge(a1, a2);

				}

			}

		}

	}



	public Graph<Author, DefaultEdge> getGraph() {

		return graph;

	}



	public Map<Integer, Author> getIdMapAutore() {
		return idMapAutore;
	}



	public void setIdMapAutore(Map<Integer, Author> idMapAutore) {
		this.idMapAutore = idMapAutore;
	}



	public Map<Integer, Paper> getIdMapPaper() {
		return idMapPaper;
	}



	public void setIdMapPaper(Map<Integer, Paper> idMapPaper) {
		this.idMapPaper = idMapPaper;
	}

	public List<Author> displayNeighbours(Author a) {
		List<Author> vicini=new ArrayList<Author>();
		vicini=Graphs.neighborListOf(graph, a);
		
		return vicini;
	}
	public Map<Integer, Author> getIdMapAutore2(Author a) {
		Map<Integer, Author> autori2=new HashMap<Integer, Author>(idMapAutore);
		autori2.remove(displayNeighbours(a));
		return autori2;
	}
	
	public List<Paper> getShortestPath(Author source, Author destination) {
		


//		DijkstraShortestPath<Author,DefaultEdge> spa = new DijkstraShortestPath<Author,DefaultEdge>(graph, destinatario, destinatario);
//
//
//		GraphPath<Author,DefaultEdge> gp = spa.getPath();
//		
//
//		return gp.getEdgeList();
		if (source == null || destination == null) {

			throw new RuntimeException("Gli areoporti selezionati non sono presenti in memoria");

		}

		

		ShortestPathAlgorithm<Author,DefaultEdge> spa = new DijkstraShortestPath<Author,DefaultEdge>(graph);

		double weight = spa.getPathWeight(source, destination);

		System.out.println(weight);

		

		GraphPath<Author,DefaultEdge> gp = spa.getPath(source, destination);
		List<Author> autori=new ArrayList<Author>(gp.getVertexList());
		List<Paper> pubblicazioni=new ArrayList<Paper>();

		for(int i=0; i<(autori.size()-1);i++) {
			int flag=0;
			for(Paper p1:autori.get(i).getPubblicazioni()) {
				for(Paper p2:autori.get(i+1).getPubblicazioni()) {
					if(p1==p2) {
						pubblicazioni.add(p1);
						flag=1;
						break;
					}
				}
				if(flag!=0)
					break;
			}
		}
		
		return pubblicazioni;

	}
}