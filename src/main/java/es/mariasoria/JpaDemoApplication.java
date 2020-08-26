package es.mariasoria;

import es.mariasoria.model.Categoria;
import es.mariasoria.model.Perfil;
import es.mariasoria.model.Usuario;
import es.mariasoria.model.Vacante;
import es.mariasoria.repository.CategoriasRepository;
import es.mariasoria.repository.PerfilesRepository;
import es.mariasoria.repository.UsuariosRepository;
import es.mariasoria.repository.VacantesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class JpaDemoApplication implements CommandLineRunner {

	@Autowired
	private CategoriasRepository repoCategorias;

	@Autowired
	private VacantesRepository repoVacantes;

	@Autowired
	private UsuariosRepository repoUsuarios;

	@Autowired
	private PerfilesRepository repoPerfiles;

	public static void main(String[] args) {
		SpringApplication.run(JpaDemoApplication.class, args);
	}

	// este metodo nos permite ver mensajes y ejecutar métodos "extra" en la command line
	@Override
	public void run(String... args) throws Exception {
		buscarUsuario();
	}

	/*
	* Método para buscar un usuario y desplegar sus perfiles asociados
	* */
	public void buscarUsuario(){
		Optional <Usuario> opcional = repoUsuarios.findById(1);
		if (opcional.isPresent()) {
			Usuario u = opcional.get();
			System.out.println("Usuario: " + u.getNombre());
			System.out.println("Perfiles asignados: ");
			for (Perfil perfil : u.getPerfiles()){
				System.out.println("--> " + perfil.getPerfil());
			}
		} else {
			System.out.println("Usuario no encontrado");
		}
	}


	/*
	 * Crear un usuario con dos perfiles "ADMINISTRADOR" y "USUARIO"
	 * */
	private void crearUsuarioConDosPerfiles(){
		Usuario usuario = new Usuario();
		usuario.setNombre("Maria Soria");
		usuario.setEmail("maria.it3@gmail.com");
		usuario.setUsername("marieta");
		usuario.setPassword("p4ssw0rd");
		usuario.setFechaRegistro(new Date());
		usuario.setEstatus(1);
		// Le asignamos 2 perfiles a este usuario
		Perfil perfil1 = new Perfil();
		perfil1.setId(2); // en BD este id es del ADMINISTRADOR

		Perfil perfil2 = new Perfil();
		perfil2.setId(3); // en BD este id es del USUARIO

		// uso el método creado en el modelo Usuario
		usuario.agregar(perfil1);
		usuario.agregar(perfil2);

		repoUsuarios.save(usuario);
	}

	/*
	* Método que regresa una lista de objetos de tipo Perfil que representa los diferentes
	* PERFILES o ROLES que tendremos en nuestra aplicación de Empleos
	* */
	private List<Perfil> getPerfilesAplicacion(){
		List<Perfil> lista = new LinkedList<Perfil>();
		Perfil perfil1 = new Perfil();
		perfil1.setPerfil("SUPERVISOR");

		Perfil perfil2 = new Perfil();
		perfil2.setPerfil("ADMINISTRADOR");

		Perfil perfil3 = new Perfil();
		perfil3.setPerfil("USUARIO");

		lista.add(perfil1);
		lista.add(perfil2);
		lista.add(perfil3);

		return lista;
	}


	// METODOS CON REPOPERFILES

	// Este metodo inserta en la BD los perfiles establecidos
	private void crearPerfilesAplicacion(){
		repoPerfiles.saveAll(getPerfilesAplicacion());
	}



	// METODOS CON REPOCATEGORIAS

	/*
	 * METODOS QUE USAN LA INTERFAZ CRUDREPOSITORY
	 * */
	// Método save - Interfaz CrudRepository
	// Permite guardar un elemento en la BD
	private void guardar(){
		Categoria cat1 = new Categoria();
		cat1.setNombre("Finanzas");
		cat1.setDescripcion("Trabajos relacionados con finanzas y contabilidad");

		Categoria cat2 = new Categoria();
		cat2.setNombre("Idiomas");
		cat2.setDescripcion("Trabajos relacionados con la traduccion y la interpretacion");
		// no asignamos el id xq se genera y autoincrementara solo
		repoCategorias.save(cat1);
		repoCategorias.save(cat2);
		System.out.println("cat:" + cat1);
		System.out.println("cat:" + cat2);
	}

	// Método findById - Interfaz CrudRepository
	// Busca un registro segun su ID
	private void buscarPorId(){
		Optional<Categoria> opcional = repoCategorias.findById(2);
		if(opcional.isPresent()){
			System.out.println("Categoria con id = 2: " + opcional.get());
		} else {
			System.out.println("Categoria no encontrada");
		}
	}

	// Método findById+save - Interfaz CrudRepository
	// Actualiza un registro
	private void modificar(){
		Optional<Categoria> opcional = repoCategorias.findById(2);
		if(opcional.isPresent()){
			Categoria catTmp = opcional.get();
			catTmp.setNombre("Ingenieria de software");
			catTmp.setDescripcion("Desarrollo de sistemas");
			repoCategorias.save(catTmp);
			System.out.println("Categoria con id = 2: " + opcional.get());
		} else {
			System.out.println("Categoria no encontrada");
		}
	}

	// Método deleteById - Interfaz CrudRepository
	// Permite eliminar un registro de la BD
	private void eliminar() {
		int idCategoria = 1;
		repoCategorias.deleteById(idCategoria);
	}

	// Método count - Interfaz CrudRepository
	// Devuelve el número de registros en la BD
	private void contar(){
		long count = repoCategorias.count();
		System.out.println("Hay un total de: " + count + " registros en la BD");
	}

	// Método deleteAll - Interfaz CrudRepository
	// Elimina todos los registros de la BD
	private void eliminarTodos(){
		repoCategorias.deleteAll();
		System.out.println("Todos los registros han sido eliminados");
		contar();
	}

	// Método findAllById - Interfaz CrudRepository
	// Busca todos los registros segun su ID
	private void encontrarTodosPorIds(){
		List<Integer> lista = new LinkedList<Integer>();
		lista.add(1);
		lista.add(2);
		Iterable<Categoria> iterable = repoCategorias.findAllById(lista);
		for (Categoria cat : iterable){
			System.out.println(cat);
		}
	}

	// Método findAll - Interfaz CrudRepository
	// Busca todos los registros
	private void encontrarTodas(){
		Iterable<Categoria> iterable = repoCategorias.findAll();
		for (Categoria cat : iterable){
			System.out.println(cat);
		}
	}

	// Método existsById - Interfaz CrudRepository
	// Comprueba si el registro con ese id existe en la BD
	private void existeId(){
		if(repoCategorias.existsById(2)){
			System.out.println("Encontrado el registro con id: 2");
		} else {
			System.out.println("No se ha encontrado el registro con id: 2");
		}
	}

	// Método saveAll - Interfaz CrudRepository
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

		repoCategorias.saveAll(lista);
	}



	/*
	* METODOS QUE USAN LA INTERFAZ JPAREPOSITORY
	* */
	// Método findAll - Interfaz JpaRepository
	// Recupera todos los registros y los devuelve en una lista
	private void buscarTodosJpa(){
		List<Categoria> categorias = repoCategorias.findAll();
		for(Categoria cat : categorias){
			System.out.println(cat.getId() + " - " + cat.getNombre());
		}
	}

	// Método deleteAllInBatch [usar con precaucion] - Interfaz JpaRepository
	// Borra todos los registros de la BD en bloque
	private void borrarTodoEnBloque(){
		repoCategorias.deleteAllInBatch();
	}

	// Método findAll [ordenados por un campo] - Interfaz PagingAndSortingRepository
	// Recupera todos los registros de la BD ordenados segun el criterio especificado
	// El criterio debe ser el nombre de la propiedad de la clase del modelo
	// si especifico "descending" lo ordenara en orden descendente.
	// Si no especifico nada, en ascendente
	private void buscarTodosOrdenados(){
		List<Categoria> categorias = repoCategorias.findAll(Sort.by("nombre").descending());
		for(Categoria cat : categorias){
			System.out.println(cat.getId() + " - " + cat.getNombre());
		}
	}

	// Método findAll [con paginacion] - Interfaz PagingAndSortingRepository
	// Recupera todos los registros de la BD por paginas
	// especificamos la pagina que queremos que nos saque y el numero de registros de cada pagina
	private void buscarTodosPaginacion(){
		Page<Categoria> page = repoCategorias.findAll(PageRequest.of(0, 3));
		System.out.println("Total Registros: " + page.getTotalElements());
		System.out.println("Total Paginas: " + page.getTotalPages());
		for(Categoria categoria : page.getContent()) {
			System.out.println(categoria.getId() + " - " + categoria.getNombre());
		}
	}

	// Método findAll [con paginacion y ordenacion] - Interfaz PagingAndSortingRepository
	// Recupera todos los registros de la BD ordenados por paginas
	// especificamos la pagina que queremos que saque, el numero de registros de cada pagina y que ordene por nombre
	// si especifico "descending" lo ordenara en orden descendente.
	// Si no especifico nada, en ascendente
	private void buscarTodosPaginacionOrdenados(){
		Page<Categoria> page = repoCategorias.findAll(PageRequest.of(0, 3, Sort.by("nombre").descending()));
		System.out.println("Total Registros: " + page.getTotalElements());
		System.out.println("Total Paginas: " + page.getTotalPages());
		for(Categoria categoria : page.getContent()) {
			System.out.println(categoria.getId() + " - " + categoria.getNombre());
		}
	}

	// METODOS CON REPOVACANTES
	// Método findAll
	private void buscarVacantes(){
		List<Vacante> lista = repoVacantes.findAll();
		for (Vacante vacante : lista) {
			System.out.println(vacante.getId() + " - " + vacante.getNombre() + " - " + vacante.getCategoria().getNombre());
		}
	}

	// Método save
	private void guardarVacante(){
		Vacante vacante = new Vacante();
		vacante.setNombre("Profesor de Matemáticas");
		vacante.setDescripcion("Escuela primaria solicita profesor para curso de Matematicas.");
		vacante.setFecha(new Date());
		vacante.setSalario(8500.0);
		vacante.setEstatus("Aprobada");
		vacante.setDestacado(0);
		vacante.setImagen("escuela.png");
		vacante.setDetalles("Los requisitos para un profesor de matemáticas");
		Categoria categoria = new Categoria();
		categoria.setId(15);
		vacante.setCategoria(categoria);
		repoVacantes.save(vacante);
		System.out.println("Vacante guardada");
	}



}
