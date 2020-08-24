package es.mariasoria;

import es.mariasoria.model.Categoria;
import es.mariasoria.repository.CategoriasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class JpaDemoApplication implements CommandLineRunner {

	@Autowired
	private CategoriasRepository repo;

	public static void main(String[] args) {
		SpringApplication.run(JpaDemoApplication.class, args);
	}

	// este metodo nos permite ver mensajes y ejecutar métodos "extra" en la command line
	@Override
	public void run(String... args) throws Exception {
		//guardar();
		//buscarPorId();
		//modificar();
		//eliminar();
		contar();
		//encontrarTodosPorIds();
		//encontrarTodas();
		//existeId();
		//guardarTodas();
	}

	// Permite guardar un elemento en la BD
	private void guardar(){
		Categoria cat1 = new Categoria();
		cat1.setNombre("Finanzas");
		cat1.setDescripcion("Trabajos relacionados con finanzas y contabilidad");

		Categoria cat2 = new Categoria();
		cat2.setNombre("Idiomas");
		cat2.setDescripcion("Trabajos relacionados con la traduccion y la interpretacion");
		// no asignamos el id xq se genera y autoincrementara solo
		repo.save(cat1);
		repo.save(cat2);
		System.out.println("cat:" + cat1);
		System.out.println("cat:" + cat2);
	}

	// Busca un registro segun su ID
	private void buscarPorId(){
		Optional<Categoria> opcional = repo.findById(2);
		if(opcional.isPresent()){
			System.out.println("Categoria con id = 2: " + opcional.get());
		} else {
			System.out.println("Categoria no encontrada");
		}
	}

	// Actualiza un registro
	private void modificar(){
		Optional<Categoria> opcional = repo.findById(2);
		if(opcional.isPresent()){
			Categoria catTmp = opcional.get();
			catTmp.setNombre("Ingenieria de software");
			catTmp.setDescripcion("Desarrollo de sistemas");
			repo.save(catTmp);
			System.out.println("Categoria con id = 2: " + opcional.get());
		} else {
			System.out.println("Categoria no encontrada");
		}
	}

	// Permite eliminar un registro de la BD
	private void eliminar() {
		int idCategoria = 3;
		repo.deleteById(idCategoria);
	}

	// Devuelve el número de registros en la BD
	private void contar(){
		long count = repo.count();
		System.out.println("Hay un total de: " + count + " registros en la BD");
	}

	// Elimina todos los registros de la BD
	private void eliminarTodos(){
		repo.deleteAll();
		System.out.println("Todos los registros han sido eliminados");
		contar();
	}

	// Busca todos los registros segun su ID
	private void encontrarTodosPorIds(){
		List<Integer> lista = new LinkedList<Integer>();
		lista.add(1);
		lista.add(2);
		Iterable<Categoria> iterable = repo.findAllById(lista);
		for (Categoria cat : iterable){
			System.out.println(cat);
		}
	}

	// Busca todos los registros
	private void encontrarTodas(){
		Iterable<Categoria> iterable = repo.findAll();
		for (Categoria cat : iterable){
			System.out.println(cat);
		}
	}

	// Comprueba si el registro con ese id existe en la BD
	private void existeId(){
		if(repo.existsById(2)){
			System.out.println("Encontrado el registro con id: 2");
		} else {
			System.out.println("No se ha encontrado el registro con id: 2");
		}
	}

	// Permite guardar muchos elementos en la BD a la vez
	private void guardarTodas(){

		Categoria cat1 = new Categoria();
		cat1.setNombre("Finanzas");
		cat1.setDescripcion("Trabajos relacionados con finanzas y contabilidad");

		Categoria cat2 = new Categoria();
		cat2.setNombre("Idiomas");
		cat2.setDescripcion("Trabajos relacionados con la traduccion y la interpretacion");

		List<Categoria> lista = new LinkedList<Categoria>();
		lista.add(cat1);
		lista.add(cat2);

		repo.saveAll(lista);
	}

}
