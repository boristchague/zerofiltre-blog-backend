package tech.zerofiltre.blog.infra.providers.database.course;

import lombok.*;
import org.mapstruct.factory.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import tech.zerofiltre.blog.domain.course.*;
import tech.zerofiltre.blog.domain.course.model.*;
import tech.zerofiltre.blog.infra.providers.database.course.mapper.*;
import tech.zerofiltre.blog.infra.providers.database.course.model.*;

import java.util.*;

@Component
@Transactional
@RequiredArgsConstructor
public class DBLessonProvider implements LessonProvider {

    private final LessonJPARepository lessonJPARepository;
    private final EnrollmentJPARepository enrollmentJPARepository;
    LessonJPAMapper lessonJPAMapper = Mappers.getMapper(LessonJPAMapper.class);

    @Override
    public Optional<Lesson> lessonOfId(long id) {
        return lessonJPARepository.findById(id).map(lessonJPAMapper::fromJPA);
    }

    @Override
    public Lesson save(Lesson lesson) {
        LessonJPA lessonJPA = lessonJPAMapper.toJPA(lesson);
        LessonJPA saved = lessonJPARepository.save(lessonJPA);
        return lessonJPAMapper.fromJPA(saved);
    }

    @Override
    public void delete(Lesson lesson) {
        enrollmentJPARepository.getAllByCompletedLessonsLesson(lessonJPAMapper.toJPA(lesson)).forEach(enrollmentJPA -> {
            enrollmentJPA.getCompletedLessons().removeIf(completedLessonJPA -> completedLessonJPA.getLesson().getId() == lesson.getId());
            enrollmentJPARepository.save(enrollmentJPA);
        });
        lessonJPARepository.delete(lessonJPAMapper.toJPA(lesson));
    }

    @Override
    public List<Long> listNotCompletedLessons(long enrollmentId) {
        return lessonJPARepository.findAllLessonIdNotCompletedByCourseIdAndEnrollmentId(enrollmentId);
    }

    @Override
    public List<Lesson> saveAll(List<Lesson> lessons) {
        List<LessonJPA> lessonsJPA = lessonJPAMapper.toJPAs(lessons);
        List<LessonJPA> savedLessonsJPA = lessonJPARepository.saveAll(lessonsJPA);
        return lessonJPAMapper.fromJPAs(savedLessonsJPA);

    }

    @Override
    public List<String> getCourseChapterLessonTitle(String lessonId){
        return lessonJPARepository.findLessonCourseChapterTitle(lessonId);
    }

}
