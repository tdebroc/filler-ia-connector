package com.tdebroc.myapp.repository;

import com.tdebroc.myapp.filler.game.Game;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by thibautdebroca on 13/04/16.
 */
public interface GameRepository extends CrudRepository<Game, Long> {

}

