package com.skillforge.backend.utils;

import com.skillforge.backend.dto.*;
import com.skillforge.backend.entity.*;
import com.skillforge.backend.entity.Module;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ObjectMappers {

    public static Course courseDtoToCourseMapper(CourseDTO courseDTO) {
        Course course = new Course();
        course.setCourseName(courseDTO.getCourseName());
        course.setCourseTags(courseDTO.getCourseTags());
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        course.setCourseDirectory(courseDTO.getCourseName());
        course.setDays(courseDTO.getDaysToFinish());
        course.setCourseDescription(courseDTO.getCourseDescription());
        return course;
    }

    public static CourseDTO courseToCourseDTOMapper(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseId(course.getCourseid());
        courseDTO.setCourseName(course.getCourseName());
        courseDTO.setCourseTags(course.getCourseTags());
        courseDTO.setCreatedAt(course.getCreatedAt().toString());
        courseDTO.setUpdatedAt(course.getUpdatedAt().toString());
        courseDTO.setCourseDirectory(course.getCourseName());
        courseDTO.setCourseDescription(course.getCourseDescription());
        courseDTO.setDaysToFinish(course.getDays());
        return courseDTO;
    }

    public static EmployeeCourseDTO employeecourseToEmployeecourseDTOMapper(EmployeeCourses course) {
        EmployeeCourseDTO employeeCourseDTO = new EmployeeCourseDTO();
        employeeCourseDTO.setId(course.getId());
        employeeCourseDTO.setCourse(courseToCourseDTOMapper(course.getCourse()));
        employeeCourseDTO.setStatus(course.getStatus());
        employeeCourseDTO.setDueDate(course.getDueDate().toString());
        employeeCourseDTO.setAssignedAt(course.getAssignedAt().toString());
        employeeCourseDTO.setQuizcompleted(course.getQuizcompleted());
        return employeeCourseDTO;
    }

    public static UserDTO userToUserDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .userName(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }

    public static ReplyDTO replyToReplyDTO(ConcernReply concernReply) {
        return ReplyDTO.builder()
                .id(concernReply.getId())
                .reply(concernReply.getReply())
                .repliedat(concernReply.getRepliedat().toString())
                .repliedBy(concernReply.getRepliedBy())
                .build();
    }

    public static ConcernDTO concernsToConcernDTO(Concerns concerns) {
        List<ReplyDTO> replyDTOs = new ArrayList<>();
        List<ConcernReply> concernReplies = concerns.getConcernReplies();
        for(ConcernReply concernReply: concernReplies) {
            replyDTOs.add(replyToReplyDTO(concernReply));
        }
        return ConcernDTO.builder()
                .description(concerns.getDescription())
                .subject(concerns.getSubject())
                .status(concerns.getStatus())
                .createdat(concerns.getCreatedat().toString())
                .id(concerns.getId())
                .concernReplies(replyDTOs)
                .createdBy(concerns.getUser().getUsername())
                .build();
    }

    public static AnnouncementDTO announcementToDTO(Announcement announcement) {
        return AnnouncementDTO.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .createdby(announcement.getCreatedby())
                .description(announcement.getDescription())
                .createdat(announcement.getCreatedat().toString())
                .updatedat(announcement.getUpdatedat().toString())
                .build();
    }

    public static Announcement announcementDTOToAnnouncement(AnnouncementDTO announcementDTO) {
        return Announcement.builder()
                .title(announcementDTO.getTitle())
                .description(announcementDTO.getDescription())
                .createdat(LocalDateTime.now())
                .updatedat(LocalDateTime.now())
                .build();
    }

    public static ModuleDTO moduleToModuleDTO(Module module) {
        return ModuleDTO.builder()
                .moduleId(module.getModuleid())
                .moduleName(module.getModuleName())
                .moduleNumber(module.getModulenumber())
                .build();
    }

    public static Quiz quizDTOtoQuiz(QuizDTO quizDTO) {
        return Quiz.builder()
                .question(quizDTO.getQuestion())
                .option1(quizDTO.getOption1())
                .option2(quizDTO.getOption2())
                .option3(quizDTO.getOption3())
                .option4(quizDTO.getOption4())
                .correctans(quizDTO.getCorrectans())
                .build();
    }

    public static QuizDTO quiztoQuizDTO(Quiz quiz) {
        return QuizDTO.builder()
                .id(quiz.getId())
                .question(quiz.getQuestion())
                .option1(quiz.getOption1())
                .option2(quiz.getOption2())
                .option3(quiz.getOption3())
                .option4(quiz.getOption4())
                .correctans(quiz.getCorrectans())
                .build();
    }

    public static Discussions discussionDTOtodiscussion(DiscussionDTO discussionDTO) {
        return Discussions.builder()
                .title(discussionDTO.getTitle())
                .description(discussionDTO.getDescription())
                .build();
    }

    public static DiscussionDTO discussiontoDiscussionDTO(Discussions discussions) {
        List<DiscussionReply> discussionReplyList = discussions.getDiscussionReplyList();
        List<DiscussionReplyDTO> discussionReplyDTOS = new ArrayList<>();
        for(DiscussionReply discussionReply: discussionReplyList) {
            discussionReplyDTOS.add(ObjectMappers.discussionReplytoDiscussionReplyDTO(discussionReply));
        }
        return DiscussionDTO.builder()
                .title(discussions.getTitle())
                .description(discussions.getDescription())
                .createdby(discussions.getCreatedby())
                .createdat(discussions.getCreatedat().toString())
                .discussionReplyList(discussionReplyDTOS)
                .id(discussions.getId())
                .build();
    }

    public static DiscussionReply discussionReplyDTOtoDiscussionReply(DiscussionReplyDTO discussionReplyDTO) {
        return DiscussionReply.builder()
                .reply(discussionReplyDTO.getReply())
                .build();
    }

    public static DiscussionReplyDTO discussionReplytoDiscussionReplyDTO(DiscussionReply discussionReply) {
        return DiscussionReplyDTO.builder()
                .reply(discussionReply.getReply())
                .repliedBy(discussionReply.getRepliedBy())
                .repliedat(discussionReply.getRepliedat().toString())
                .id(discussionReply.getId())
                .build();
    }
}
