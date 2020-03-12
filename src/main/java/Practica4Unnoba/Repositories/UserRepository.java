package Practica4Unnoba.Repositories;
	
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Practica4Unnoba.Entities.Usuario;

	public interface UserRepository extends JpaRepository<Usuario, Long> {
		
		@Query("select u from Usuario u where u.username = :username")
		public Usuario findUserByUsername(@Param("username") String username);

		@Query("select u from Usuario u where u.email = :email")
		public Usuario findUserbyEmail(@Param("email")String email); 
		
	}
