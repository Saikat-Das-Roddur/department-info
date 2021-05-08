package com.app.departmentinfos.Server;

import com.app.departmentinfos.Models.Assignment;
import com.app.departmentinfos.Models.Comments;
import com.app.departmentinfos.Models.Courses;
import com.app.departmentinfos.Models.Department;
import com.app.departmentinfos.Models.Faculty;
import com.app.departmentinfos.Models.Forum;
import com.app.departmentinfos.Models.Message;
import com.app.departmentinfos.Models.Notice;
import com.app.departmentinfos.Models.Student;
import com.app.departmentinfos.Models.Teacher;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("sign_up.php")
    Call<Student> studentSignUp(
            @Field("name") String name,
            @Field("email") String email,
            @Field("contact") String contact,
            @Field("password") String password,
            @Field("code") String code,
            @Field("section") String section,
            @Field("session") String session,
            @Field("department") String dept,
            @Field("status") String status,
            @Field("type") String type,
            @Field("token") String token
            //@Field("add_request") String addRequest,

    );

    @FormUrlEncoded
    @POST("sign_in.php")
    Call<Student> studentSignIn(
            @Field("code") String code,
            @Field("password") String password,
            @Field("type") String type,
            @Field("token") String token

    );

    @FormUrlEncoded
    @POST("sign_up.php")
    Call<Teacher> teacherSignUp(
            @Field("name") String name,
            @Field("email") String email,
            @Field("contact") String contact,
            @Field("password") String password,
            @Field("code") String code,
            @Field("department") String dept,
            @Field("status") String status,
            @Field("type") String type,
            @Field("token") String token
            //@Field("add_request") String addRequest,
            );

    @Multipart
    @POST("update_profile.php")
    Call<Student> updateStudent(
            @Part MultipartBody.Part file,
            @Part("code") RequestBody code,
            @Part("name") RequestBody name,
            @Part("password") RequestBody password,
            @Part("email") RequestBody email,
            @Part("contact") RequestBody contact

    );

    @FormUrlEncoded
    @POST("post_comment.php")
    Call<Comments> studentComment(
            @Field("forum_code") String forumCode,
            @Field("code") String code,
            @Field("user_name") String name,
            @Field("date") String date,
            @Field("comment") String comment,
            @Field("image") String image

    );


    @Multipart
    @POST("post_forum.php")
    Call<Student> studentPost(
            @Part MultipartBody.Part file,
            @Part("user_image") RequestBody userImage,
            @Part("user_name") RequestBody name,
            @Part("forum_code") RequestBody forumCode,
            @Part("description") RequestBody post,
            @Part("code") RequestBody code,
            @Part("date") RequestBody date,
            @Part("status") RequestBody status,
            @Part("section") RequestBody section,
            @Part("department") RequestBody department

    );
    @FormUrlEncoded
    @POST("submit_assignments_teacher.php")
    Call<Teacher> submitAssignmentsTeacher(
            @Field(" assignment_code") String assignmentCode,
            @Field("course_code") String courseCode,
            @Field("code") String code,
            @Field("title") String title,
            @Field("description") String description,
            @Field("section") String section,
            @Field("status") String status,
            @Field("posting_date") String postingDate,
            @Field("submission_date") String submissionDate

    );

    @Multipart
    @POST("submit_assignments_student.php")
    Call<Student> submitAssignmentsStudents(
            @Part MultipartBody.Part file,
            @Part("teacher_code") RequestBody teacherCode,
            @Part("course_code") RequestBody courseCode,
            @Part("code") RequestBody studentCode,
            @Part("assignment_code") RequestBody assignmentCode,
            @Part("title") RequestBody title,
            @Part("status") RequestBody status,
            @Part("description") RequestBody description,
            @Part("section") RequestBody section,
            @Part("posting_date") RequestBody postingDate,
            @Part("submission_date") RequestBody submissionDate

    );

    @FormUrlEncoded
    @POST("submit_assignment_teacher.php")
    Call<Teacher> submitTeacherAssignment(
            @Field("user_name") String name,
            @Field("forum_code") String forumCode,
            @Field("description") String post,
            @Field("code") String code,
            @Field("date") String date,
            @Field("file") String file,
            @Field("status") String status,
            @Field("department") String department

    );


    @FormUrlEncoded
    @POST("add_course_request.php")
    Call<Courses> addCourses(
            @Field("code") String student_code,
            @Field("teacher_code") String teacher_code,
            @Field("teacher_name") String teacher_name,
            @Field("course_code") String course_code,
            @Field("course_name") String course_name,
            @Field("semester") String semester,
            @Field("course_semester") String course_semester,
            @Field("section") String section,
            @Field("status") String status,
            @Field("request") String request
    );

    @FormUrlEncoded
    @POST("update_requested_courses.php")
    Call<Courses> updateRequestedCourses(
            @Field("student_code") String student_code,
            @Field("code") String code,
            @Field("course_code") String course_code,
            @Field("status") String status,
            @Field("request") String request
    );
    @FormUrlEncoded
    @POST("update_assigned_courses.php")
    Call<Courses> updateCourses(
            @Field("code") String code,
            @Field("course_code")String course_code,
            @Field("availability") String availability);

    @FormUrlEncoded
    @POST("post_notice.php")
    Call<Notice> addNotices(
            @Field("notice_code") String noticeCode,
            @Field("description") String description,
            @Field("user_name") String userName,
            @Field("code") String code,
            @Field("posting_date") String postingDate,
            @Field("section") String section,
            @Field("department") String department,
            @Field("image") String image
    );


    @GET("get_forum.php")
    Call<List<Forum>> getForums(
            @Query("department") String department,
            @Query("code") String code
    );
    @GET("get_comments.php")
    Call<List<Comments>> getComments( @Query("forum_code") String forum_code);
    @GET("get_teachers.php")
    Call<List<Teacher>> getTeachers( @Query("department") String code);
    @GET("get_department.php")
    Call<List<Department>> getDepartmentData();
    @GET("get_section.php")
    Call<List<Department>> getSectionData(
            @Query("semester") String semester,
            @Query("department_name") String department);
    @GET("get_session.php")
    Call<List<Department>> getSessionData(@Query("department_name") String department);
    @GET("get_semester.php")
    Call<List<Department>> getSemesterData();
    @GET("get_profile.php")
    Call<List<Student>> getStudentProfile(
            @Query("code") String code
    );
    @GET("get_profile.php")
    Call<List<Teacher>> getTeacherProfile(
            @Query("code") String code
    );
    @GET("get_assignments.php")
    Call<List<Assignment>> getAssignments(
            @Query("code") String code
    );
    @GET("get_assignments_student.php")
    Call<List<Assignment>> getAssignmentStudent(
            @Query("code") String code
    );
    @GET("get_submitted_assignments.php")
    Call<List<Assignment>> getSubmittedAssignments(
            @Query("code") String code
    );
    @GET ("get_courses.php")
    Call<List<Courses>> getCourses(
            @Query("teacher_code") String code,
            @Query("department") String department
    );
    @GET ("get_registered_courses.php")
    Call<List<Courses>> getRegisteredCourses(
            @Query("teacher_code") String code
    );
    @GET ("get_notices_admin.php")
    Call<List<Notice>> getNoticesAdmin(
            @Query("department") String code
    );
    @GET ("get_notice_not_admin.php")
    Call<List<Notice>> getNoticesNonAdmin(
            @Query("code") String code
    );
    @GET ("get_notices_section.php")
    Call<List<Notice>> getNoticesSection(
            @Query("section") String section,
            @Query("department") String code
    );
    @GET ("get_registered_courses.php")
    Call<List<Courses>> getRequestedCourse(
            @Query("teacher_code") String code
    );
    //Message Part
    @FormUrlEncoded
    @POST("add_message_user.php")
    Call<Message> addMessageUser(
            @Field("sender_code") String senderCode,
            @Field("sender_name") String senderName,
            @Field("sender_file") String senderFile,
            @Field("receiver_code") String receiverCode,
            @Field("receiver_name") String receiverName,
            @Field("receiver_file") String receiverImage,
            @Field("section") String section,
            @Field("designation") String designation,
            @Field("status") String status

    );
    @FormUrlEncoded
    @POST("update_message_status.php")
    Call<Message> updateMessageStatus(
            @Field("sender_code") String senderCode,
            @Field("receiver_code") String receiverCode,
            @Field("status") String status

    );
    @FormUrlEncoded
    @POST("update_comments.php")
    Call<Comments> updateComment(
            @Field("id") int Id,
            @Field("status") String status

    );
    @FormUrlEncoded
    @POST("add_messages.php")
    Call<Message> addMessage(
            @Field("sender_code") String senderCode,
            @Field("sender_message") String senderMessage,
            @Field("receiver_code") String receiverCode,
            @Field("date") String date,
            @Field("time") String time,
            @Field("status") String status
    );

    @GET ("get_message_user.php")
    Call<List<Message>> getAcceptedUser(
            @Query("sender_code") String code
    );
    @GET ("get_message_user.php")
    Call<List<Message>> getPendingUser(
            @Query("receiver_code") String code
    );
    @GET ("get_messages.php")
    Call<List<Message>> getMessages(
            @Query("sender_code") String senderCode,
            @Query("receiver_code") String receiverCode
    );

    @GET ("get_students.php")
    Call<List<Student>> getStudent(@Query("department")String department);
    @GET ("get_notices_course.php")
    Call<List<Notice>> getCourseNotice(
            @Query("code")String code,
            @Query("department")String department
    );
    @GET ("get_course_notice_teacher.php")
    Call<List<Notice>> getCourseNoticeTeacher(
            @Query("code")String code,
            @Query("department")String department
    );
    @GET ("get_faculty.php")
    Call<List<Faculty>> getFaculty(@Query("department")String department);

    @FormUrlEncoded
    @POST("delete_assignment.php")
    Call<Assignment> deleteAssignment(@Field("assignment_code") String assignmentCode);
    @FormUrlEncoded
    @POST("delete_forum.php")
    Call<Forum> deleteForum(@Field("forum_code")String forumCode);
}