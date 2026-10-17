package fr.univ_amu.fr.formation.m1info.gla.service;
import com.google.inject.Inject;
import fr.univ_amu.fr.formation.m1info.gla.data.GradeConvertor;
import fr.univ_amu.fr.formation.m1info.gla.data.GradeDTO;
import fr.univ_amu.fr.formation.m1info.gla.data.StudentConvertor;
import fr.univ_amu.fr.formation.m1info.gla.data.StudentDTO;
import fr.univ_amu.fr.formation.m1info.gla.model.Course;
import fr.univ_amu.fr.formation.m1info.gla.model.Student;
import fr.univ_amu.fr.formation.m1info.gla.repository.StudentRepository;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class StudentService {
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final StudentRepository studentRepository;

    @Inject
    public StudentService(CourseService courseService,
                          TeacherService teacherService,
                          StudentRepository studentRepository
    ) {
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.studentRepository = studentRepository;
        this.studentRepository.setCourses(courseService.getCourses());
    }

    public void updateStudentMarks(Integer studentId, Integer courseId, Map<String, Double> newMarks) {
        StudentDTO originalStudent = studentRepository.getById(studentId).
                orElseThrow(()->new RuntimeException("Student not found"));

        Map<Integer, Map<String, Double>> allMarks = new HashMap<>(originalStudent.marks());
        allMarks.put(courseId, newMarks);

        StudentDTO updatedStudent = new StudentDTO(
                originalStudent.id(),
                originalStudent.name(),
                originalStudent.surname(),
                originalStudent.courses(),
                allMarks
        );

        studentRepository.update(updatedStudent,courseService.getCourses());
    }

    public GradeDTO getStudentGradeDTO(StudentDTO studentDto) {
        StudentConvertor studentConvertor = new StudentConvertor(courseService.getCourses());
        GradeConvertor gradeConvertor = new GradeConvertor(teacherService.getTeachers());
        Student student = studentConvertor.fromDTO(studentDto);
        var grade = student.computeGrades();
        return gradeConvertor.toDTO(grade);
    }

    public List<StudentDTO> findStudent(String name, String surname) {
        return studentRepository
                .getAll()
                .stream()
                .filter(e -> e.name().contains(name)
                        && e.surname().contains(surname))
                .toList();
    }

    public List<StudentDTO> findAllStudentsByCourse(Integer courseId) {
        return studentRepository.getAll()
                .stream()
                .filter(e -> e.courses()
                        .stream()
                        .anyMatch(c -> c.equals(courseId)))
                .toList();
    }


    public StudentDTO get(Integer id) {
        return studentRepository.getById(id).orElseThrow(()->new RuntimeException("Student not found"));
    }

    public List<StudentDTO> getAll() {
        return studentRepository.getAll();
    }

    public StudentDTO create(StudentDTO student) {
        return studentRepository.create(student);
    }

    public void delete(StudentDTO student) {
        studentRepository.delete(student);
    }

    public void update(StudentDTO student) {
        studentRepository.update(student,courseService.getCourses());
    }
}

