package fr.univ_amu.fr.formation.m1info.gla.storage;

import fr.univ_amu.fr.formation.m1info.gla.data.StudentDTO;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryDAOTest {

    StudentDTO createStudentDTO() {
        Map<Integer, Map<String, Double>> marksDto = new HashMap<>();
        marksDto.put(1, new HashMap<>());
        marksDto.get(1).put("A", 10.0);
        return new StudentDTO(1, "T", "A", List.of(1), marksDto);
    }

    @Test
    void get() {
        InMemoryDAO<Integer, IntegerFactory, StudentDTO> dao = new InMemoryDAO<>(new IntegerFactory());
        StudentDTO sdto = createStudentDTO();
        assertTrue(dao.get(1).isEmpty());
        dao.create(sdto);
        Optional<StudentDTO> dto_req = dao.get(1);
        assertTrue(dto_req.isPresent());
        assertEquals("T", dto_req.get().name());
    }

    @Test
    void getAll() {
        InMemoryDAO<Integer, IntegerFactory, StudentDTO> dao = new InMemoryDAO<>(new IntegerFactory());
        StudentDTO sdto = createStudentDTO();
        dao.create(sdto);
        List<StudentDTO> dto_list = dao.getAll();
        assertEquals(1, dto_list.size());
    }

    @Test
    void create() {
        InMemoryDAO<Integer, IntegerFactory, StudentDTO> dao = new InMemoryDAO<>(new IntegerFactory());
        StudentDTO sdto = createStudentDTO();
        int new_id = dao.create(sdto);
        assertEquals(1, new_id);
        assertTrue(dao.get(new_id).isPresent());
    }

    @Test
    void update() {
        InMemoryDAO<Integer, IntegerFactory, StudentDTO> dao = new InMemoryDAO<>(new IntegerFactory());
        StudentDTO sdto = createStudentDTO();
        int new_id = dao.create(sdto);
        StudentDTO new_dto = new StudentDTO(new_id, "A", sdto.surname(), sdto.courses(), sdto.marks());
        dao.update(new_dto);
        assertTrue(dao.get(new_id).isPresent());
        assertEquals("A", new_dto.surname());
        assertEquals(dao.get(new_id).get().surname(), sdto.surname());
    }

    @Test
    void delete() {
        InMemoryDAO<Integer, IntegerFactory, StudentDTO> dao = new InMemoryDAO<>(new IntegerFactory());
        StudentDTO sdto = createStudentDTO();
        int new_id = dao.create(sdto);
        assertTrue(dao.get(new_id).isPresent());
        StudentDTO new_dto = new StudentDTO(new_id, sdto.name(), sdto.surname(), sdto.courses(), sdto.marks());
        dao.delete(new_dto);
        assertFalse(dao.get(new_id).isPresent());
    }
}