package nl.anouk.bikerental.repositories;

import nl.anouk.bikerental.models.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File,Long> {
    File findByFilename(String fileName);
}
