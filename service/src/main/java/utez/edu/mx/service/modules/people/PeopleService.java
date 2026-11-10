package utez.edu.mx.service.modules.people;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.service.kernel.ApiResponse;

import java.sql.SQLException;

@Service
@AllArgsConstructor
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll() {
        ApiResponse response = new ApiResponse(
                "Operación exitosa",
                peopleRepository.findAll(),
                false,
                HttpStatus.OK
        );

        return new ResponseEntity<>(response, response.getStatus());
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseEntity<ApiResponse> savePerson(Person payload) {
        ApiResponse response = null;

        if (payload == null || payload.getName() == null || payload.getName().isBlank()) {
            response = new ApiResponse("El nombre es obligatorio", null, true, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, response.getStatus());
        }

        if (payload.getName().trim().length() > 255) {
            response = new ApiResponse("El nombre no puede tener más de 255 caracteres", null, true, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, response.getStatus());
        }

        try {
            // El POST siempre crea un registro nuevo, nunca sobrescribe uno existente
            payload.setId(null);
            payload.setName(payload.getName().trim());
            peopleRepository.save(payload);
            response = new ApiResponse("Operación exitosa", null, false, HttpStatus.CREATED);
        } catch (Exception ex) {
            response = new ApiResponse("Error al guardar", null, true, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, response.getStatus());
    }
}
