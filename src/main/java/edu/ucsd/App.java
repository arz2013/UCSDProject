package edu.ucsd;

import org.springframework.context.ApplicationContext;

import edu.ucsd.dao.Neo4JMovieDaoImpl;
import edu.ucsd.model.Actor;
import edu.ucsd.model.Movie;
import edu.ucsd.model.Role;
import edu.ucsd.system.SystemApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ApplicationContext appContext = SystemApplicationContext.getApplicationContext();
        Neo4JMovieDaoImpl dao = Neo4JMovieDaoImpl.class.cast(appContext.getBean("movieDao"));
        Movie newMovie = Movie.newMovie("The Matrix", "Action", 1993); 
        newMovie.addLabel("Favorite");
        Actor keanu = new Actor("Keanu Reeves");
        Role keanuRole = keanu.playedIn(newMovie, "neo");
        dao.save(newMovie, keanu, keanuRole);
        Actor carrie = new Actor("Carrie Moss");
        Role carrieRole = carrie.playedIn(newMovie, "Trinity");
        dao.save(newMovie, carrie, carrieRole);
        newMovie.setYear(1994);
        dao.update(newMovie.getId(), newMovie);
        Movie movie = dao.findByTitleGenreAndYear(newMovie.getTitle(), newMovie.getGenre(), newMovie.getYear());
        if(movie == null) {
        	System.out.println("Movie's year has been successfully updated to a new value hence the null result");
        }
    }
}
