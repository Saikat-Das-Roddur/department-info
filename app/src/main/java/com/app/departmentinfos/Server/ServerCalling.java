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

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ServerCalling {
    static ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

    public static void studentSignUp(String name, String email, String contact, String password,
                                     String code, String section, String session,String department, String token,
                                     Callback<Student> callback){
        Call<Student> call = apiInterface.studentSignUp(name,email,contact,password,code,section,session,department,"Pending",
                "student",token);
        call.enqueue(callback);
    }

    public static void studentSignIn( String code, String password,String type,String token,
                                     Callback<Student> callback){
        Call<Student> call = apiInterface.studentSignIn(code,password,type,token);
        call.enqueue(callback);
    }

    public static void teacherSignUp(String name, String email, String contact, String password,
                                     String code, String department, String token,
                                     Callback<Teacher> callback){
        Call<Teacher> call = apiInterface.teacherSignUp(name,email,contact,password,code,department,"Pending",
                "teacher",token);
        call.enqueue(callback);
    }
//    public static void teacherSignIn( String code, String password, String token,
//                                      Callback<Teacher> callback){
//        Call<Teacher> call = apiInterface.teacherSignIn(code,password,token);
//        call.enqueue(callback);
//    }

    public static void getDepartments( Callback<List<Department>> callListener){
        Call<List<Department>> call = apiInterface.getDepartmentData();
        call.enqueue(callListener);
    }
    public static void getStudentProfile( String code, Callback<List<Student>> callListener){
        Call<List<Student>> call = apiInterface.getStudentProfile(code);
        call.enqueue(callListener);
    }
    public static void getTeacherProfile( String code, Callback<List<Teacher>> callListener){
        Call<List<Teacher>> call = apiInterface.getTeacherProfile(code);
        call.enqueue(callListener);
    }

    public static void updateStudent(MultipartBody.Part file,RequestBody code, RequestBody name, RequestBody email, RequestBody contact, RequestBody password,
                                     Callback<Student> callback){
        Call<Student> call = apiInterface.updateStudent(file,code,name,password,email,contact);
        call.enqueue(callback);

    }
//    public static void updateTeacher(String name, String email, String contact, String password,
//                                     Callback<Teacher> callback){
//        Call<Teacher> call = apiInterface.updateTeacher(name,password,email,contact);
//        call.enqueue(callback);
//    }

//    public static void getForumComment(String forumCode , Callback<List<Comments>> callback){
//        Call<List<Comments>> call = apiInterface.getComment(forumCode);
//        call.enqueue(callback);
//    }

    public static void postStudentComment(String forumCode, String code, String name, String date, String comment,String image,
                                     Callback<Comments> callback){
        Call<Comments> call = apiInterface.studentComment(forumCode, code, name,date,comment,image);
        call.enqueue(callback);
    }

    public static void submitStudentAssignments(RequestBody teacherCode, RequestBody courseCode, RequestBody studentCode, RequestBody assignmentCode, MultipartBody.Part file, RequestBody title, RequestBody status, RequestBody description,
                                                RequestBody section, RequestBody postingDate, RequestBody submissionDate,
                                                Callback<Student> callback){
        Call<Student> call = apiInterface.submitAssignmentsStudents(file,teacherCode,courseCode,studentCode,assignmentCode,title,status,description,section,postingDate,submissionDate);
        call.enqueue(callback);
    }

    public static void submitAssignmentsTeacher(String assignmentCode, String courseCode, String code,
                                                String title, String description, String section,
                                                String status, String postingDate, String submissionDate, Callback<Teacher> callback){
        Call<Teacher> call = apiInterface.submitAssignmentsTeacher(assignmentCode, courseCode, code,
                title, description, section, status, postingDate, submissionDate);
        call.enqueue(callback);
    }

    public static void postStudentBlog(MultipartBody.Part file,RequestBody userImage, RequestBody name, RequestBody forumCode, RequestBody description, RequestBody code, RequestBody date,
                                       RequestBody status,RequestBody section, RequestBody department, Callback<Student> callback){
        Call<Student> call = apiInterface.studentPost(file,userImage,name,forumCode,description,code,date,status,section,department);
        call.enqueue(callback);

    }

    public static void postNotices(String noticeCode, String description,
                                   String userName, String code, String postingDate,
                                   String section, String department,String image, Callback<Notice> callback ){
        Call<Notice> call = apiInterface.addNotices(noticeCode,description,userName,code,postingDate,section,department,image);
        call.enqueue(callback);
    }

    public static void updateRequestedCourses(String student_code,
                                              String code,
                                              String course_code,
                                              String status,
                                              String request, Callback<Courses> callback){
        Call<Courses> call = apiInterface.updateRequestedCourses(student_code,
                code,
                course_code,
                status,
                request);
        call.enqueue(callback);
    }
    public static void updateCourses(String code, String course_code, String availability,Callback<Courses> callback){
        Call<Courses> call = apiInterface.updateCourses(code, course_code, availability);
        call.enqueue(callback);
    }

    public static void getForumComments(String forumCode , Callback<List<Comments>> callback){
        Call<List<Comments>> call = apiInterface.getComments(forumCode);
        call.enqueue(callback);
    }

    public static void getForums(String department,String code, Callback<List<Forum>> callback){
        Call<List<Forum>> call = apiInterface.getForums(department,code);
        call.enqueue(callback);
    }
    public static void getTeachers(String department, Callback<List<Teacher>> callback){
        Call<List<Teacher>> call = apiInterface.getTeachers(department);
        call.enqueue(callback);
    }
    public static void getAssignment(String code, Callback<List<Assignment>> callback) {
        Call<List<Assignment>> call = apiInterface.getAssignments(code);
        call.enqueue(callback);
    }
    public static void getAssignmentStudent(String code, Callback<List<Assignment>> callback) {
        Call<List<Assignment>> call = apiInterface.getAssignmentStudent(code);
        call.enqueue(callback);
    }
    public static void getSubmittedAssignments(String code, Callback<List<Assignment>> callback){
        Call<List<Assignment>> call = apiInterface.getSubmittedAssignments(code);
        call.enqueue(callback);
    }
    public static void getCourses(String code, String department, Callback<List<Courses>> callback){
        Call<List<Courses>> call = apiInterface.getCourses(code, department);
        call.enqueue(callback);
    }
    public static void addCourses(String student_code, String teacher_code,String teacher_name,String course_code, String course_name,
                                  String semester,String course_semester,String section,
                                  String status, String request, Callback<Courses> callback){
        Call<Courses> call = apiInterface.addCourses(student_code,
                teacher_code,
                teacher_name,
                course_code,
                course_name,
                semester,
                course_semester,
                section,
                status,
                request);
        call.enqueue(callback);
    }
    public static void getRegisteredCourses(String code, Callback<List<Courses>> callback){
        Call<List<Courses>> call = apiInterface.getRegisteredCourses(code);
        call.enqueue(callback);
    }
    public static void getNoticesSection(String section, String department, Callback<List<Notice>> callback){
        Call<List<Notice>> call = apiInterface.getNoticesSection(section,department);
        call.enqueue(callback);
    }
    public static void getNoticesAdmin(String code, Callback<List<Notice>> callback){
        Call<List<Notice>> call = apiInterface.getNoticesAdmin(code);
        call.enqueue(callback);
    }
    public static void getNoticesNonAdmin(String code, Callback<List<Notice>> callback){
        Call<List<Notice>> call = apiInterface.getNoticesNonAdmin(code);
        call.enqueue(callback);
    }
    public static void getRequestedCourses(String code, Callback<List<Courses>> callback){
        Call<List<Courses>> call = apiInterface.getRequestedCourse(code);
        call.enqueue(callback);
    }
                                            // Message Part
    public static void addMessageUser(String senderCode,
                                      String senderName,
                                      String senderFile,
                                      String receiverCode,
                                      String receiverName,
                                      String receiverImage,
                                      String section,
                                      String designation,
                                      String status, Callback<Message> callback){
        Call<Message> call = apiInterface.addMessageUser(senderCode,senderName,senderFile,receiverCode,receiverName,receiverImage,section,designation,status);
        call.enqueue(callback);
    }

    public static void updateComment(int Id, String status,Callback<Comments> callback){
        Call<Comments> call = apiInterface.updateComment(Id,status);
        call.enqueue(callback);
    }

    public static void addMessages(String senderCode,
                                   String senderMessage,
                                   String receiverCode,
                                   String date, String time, String status, Callback<Message> callback){
        Call<Message> call = apiInterface.addMessage(senderCode,senderMessage,receiverCode,date,time,status);
        call.enqueue(callback);

    }
    public static void getAcceptedUser(String code, Callback<List<Message>> callback){
        Call<List<Message>> call = apiInterface.getAcceptedUser(code);
        call.enqueue(callback);
    }
    public static void getPendingUser(String code, Callback<List<Message>> callback){
        Call<List<Message>> call = apiInterface.getPendingUser(code);
        call.enqueue(callback);
    }
    public static void getMessages(String senderCode, String receiverCode, Callback<List<Message>> callback){
        Call<List<Message>> call = apiInterface.getMessages(senderCode,receiverCode);
        call.enqueue(callback);
    }

    public static void getStudents(String department, Callback<List<Student>> callback) {
        Call<List<Student>> call = apiInterface.getStudent(department);
        call.enqueue(callback);
    }
    public static void getSection(String semester,String department, Callback<List<Department>> callback) {
        Call<List<Department>> call = apiInterface.getSectionData(semester,department);
        call.enqueue(callback);
    }
    public static void getSession(String department, Callback<List<Department>> callback) {
        Call<List<Department>> call = apiInterface.getSessionData(department);
        call.enqueue(callback);
    }
    public static void getCourseNotice(String code, String department,Callback<List<Notice>> callback) {
        Call<List<Notice>> call = apiInterface.getCourseNotice(code,department);
        call.enqueue(callback);
    }
    public static void getCourseNoticeTeacher(String code, String department,Callback<List<Notice>> callback) {
        Call<List<Notice>> call = apiInterface.getCourseNoticeTeacher(code,department);
        call.enqueue(callback);
    }
    public static void getFaculty(String department,Callback<List<Faculty>> callback) {
        Call<List<Faculty>> call = apiInterface.getFaculty(department);
        call.enqueue(callback);
    }
    public static void deleteAssignment(String assignmentCode, Callback<Assignment> callback){
        Call<Assignment> call = apiInterface.deleteAssignment(assignmentCode);
        call.enqueue(callback);
    }
    public static void deleteForum(String forumCode, Callback<Forum> callback){
        Call<Forum> call = apiInterface.deleteForum(forumCode);
        call.enqueue(callback);
    }

    public static void updateMessageStatus(String senderCode, String receiverCode, String status, Callback<Message> callback){
        Call<Message> call = apiInterface.updateMessageStatus(senderCode,receiverCode,status);
        call.enqueue(callback);
    }
}
