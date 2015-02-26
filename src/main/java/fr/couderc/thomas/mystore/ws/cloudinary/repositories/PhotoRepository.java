package fr.couderc.thomas.mystore.ws.cloudinary.repositories;

import fr.couderc.thomas.mystore.ws.cloudinary.models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}