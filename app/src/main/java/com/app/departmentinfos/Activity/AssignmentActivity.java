package com.app.departmentinfos.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.net.Uri;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.departmentinfos.Activity.Student.StudentActivity;
import com.app.departmentinfos.Activity.Teacher.TeacherActivity;
import com.app.departmentinfos.Adapter.AllAssignmentAdapter;
import com.app.departmentinfos.Adapter.AssignmentTabsAdapter;
import com.app.departmentinfos.Config;
import com.app.departmentinfos.Fragments.AllAssignmentsFragment;
import com.app.departmentinfos.Models.Assignment;
import com.app.departmentinfos.Models.Student;
import com.app.departmentinfos.Others.ConnectionDetector;
import com.app.departmentinfos.Others.FilePath;
import com.app.departmentinfos.Others.FilePathListener;
import com.app.departmentinfos.R;
import com.app.departmentinfos.Server.ServerCalling;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.internal.$Gson$Preconditions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignmentActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    Dialog dialog;
    TextView textViewTitle, textViewCourseCode, textViewDate,textViewDescription,textViewSubmissionDate,textViewFileName;
    FilePathListener filePathListener;
    String TAG = getClass().getSimpleName();
    ConnectionDetector connectionDetector;
    String type = "", code="",name="",department="";
    SharedPreferences sharedPreferences;
    Uri filepath;
    private static final int BUFFER_SIZE = 1024 * 2;
    private ArrayList<Uri> docPaths = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPagerTab);
        connectionDetector = new ConnectionDetector(this);
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        type = sharedPreferences.getString(Config.TYPE,"");
        code = sharedPreferences.getString(Config.CODE,"");
        department = sharedPreferences.getString(Config.DEPARTMENT,"");
        tabLayout.addTab(tabLayout.newTab().setText("All Assignments"));
        if (type.equalsIgnoreCase("student")){
            tabLayout.addTab(tabLayout.newTab().setText("My Submissions"));
        }else {
            tabLayout.addTab(tabLayout.newTab().setText("Submitted Assignments"));
        }
        requestStoragePermission();
        //tabLayout.setTabTextColors(Color.WHITE,Color.WHITE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        AssignmentTabsAdapter tabsAdapter = new AssignmentTabsAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
//            name = FilePath.getPath(this,uri);
            name = getFilePathFromURI(AssignmentActivity.this,uri);
            Log.d("ioooo",name);
            textViewFileName.setText(name);
            //uploadPDFfile(path);

        }

//        if (data!=null){
//            filepath = data.getData();
//            Cursor returnCursor =
//                    getContentResolver().query(filepath, new String[]{"_data"}, null, null, null);
//            assert returnCursor != null;
//            int nameIndex = returnCursor.getColumnIndexOrThrow("_data");
//            returnCursor.moveToFirst();
//            name = returnCursor.getString(nameIndex);
//            Log.d(TAG, "onActivityResult: "+name);
//            textViewFileName.setText(name);
//            File file = new File(filepath.getScheme());
//            Log.d(TAG, "addAssignment: "+file);
//            returnCursor.close();
//        }else {
//            //Toasty.warning(this,"Null data").show();
//
//        }
//        Log.d("TAG", "onActivityResult: ");
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getFilePathFromURI(Context context, Uri uri) {
        String fileName = getFileName(uri);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory()+"");
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(wallpaperDirectory + File.separator + fileName);
            // create folder if not exists

            copy(context, uri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copystream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int copystream(InputStream input, OutputStream output) throws Exception, IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
        }
        return count;
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Displaying a toast
            Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
        } else {
            //Displaying another toast if permission is not granted
            Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
        }

    }

    public void showStudentAssignmentDialog(Assignment assignments) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_assignment_student);
        Button buttonChooseFile, buttonCancel, buttonSubmit;

        textViewTitle = dialog.findViewById(R.id.titleTv);
        textViewCourseCode = dialog.findViewById(R.id.courseCodeTv);
        textViewDate = dialog.findViewById(R.id.postingDateTv);
        textViewDescription = dialog.findViewById(R.id.descTv);
        textViewSubmissionDate = dialog.findViewById(R.id.submissionDateTv);
        textViewFileName = dialog.findViewById(R.id.fileNameTv);
        //textViewFileName.setText(filepath.getLastPathSegment());
        buttonCancel = dialog.findViewById(R.id.cancelBtn);
        buttonChooseFile = dialog.findViewById(R.id.chooseBtn);
        buttonSubmit = dialog.findViewById(R.id.submitBtn);

        textViewTitle.setText(assignments.getTitle());
        textViewCourseCode.setText(assignments.getCourseCode());
        textViewDate.setText("Submission Date: "+assignments.getSubmissionDate());
        textViewDescription.setText(assignments.getDesc());
        textViewSubmissionDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        if (type.equalsIgnoreCase("teacher")){
            buttonChooseFile.setVisibility(View.GONE);
            textViewFileName.setVisibility(View.GONE);
            buttonSubmit.setVisibility(View.GONE);
        }

        buttonChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
               startActivityForResult(Intent.createChooser(intent,"Select pdf"),1);
//                textViewFileName.setText(filepath.getPath());
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()){

                        if (textViewFileName.getText().toString().isEmpty()){
                            textViewFileName.setError("Must choose a pdf file");
                        }else {
                            addAssignment(assignments);
                        }


                }else {
                    new AlertDialog.Builder(AssignmentActivity.this)
                            .setTitle("Internet Connection Err!!")
                            .setMessage("Internet not available, Check your internet connectivity and try again")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create().show();
                }

            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }



    private void addAssignment(Assignment assignment) {
        File file = new File(name);
        Log.d(TAG, "addAssignment: "+file);
        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/pdf"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
       // RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        ServerCalling.submitStudentAssignments(
                RequestBody.create(MediaType.parse("text/plain"), assignment.getCode()),
                RequestBody.create(MediaType.parse("text/plain"), assignment.getCourseCode()),
                RequestBody.create(MediaType.parse("text/plain"), code),
                RequestBody.create(MediaType.parse("text/plain"), assignment.getAssignmentCode()),
                fileToUpload,
                RequestBody.create(MediaType.parse("text/plain"), assignment.getTitle()),
                RequestBody.create(MediaType.parse("text/plain"), assignment.getStatus()),
                RequestBody.create(MediaType.parse("text/plain"), assignment.getDesc()),
                RequestBody.create(MediaType.parse("text/plain"), assignment.getSection()),
                RequestBody.create(MediaType.parse("text/plain"), textViewSubmissionDate.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), assignment.getSubmissionDate()),
                new Callback<Student>() {
                    @Override
                    public void onResponse(Call<Student> call, Response<Student> response) {
                        if (response.body().getValue().equals("success")){
                            dialog.dismiss();
                            Log.d(TAG, "onResponse: "+response.body());
                            Toasty.success(AssignmentActivity.this,"Assignment Uploaded Successfully").show();
                        }else {
                            dialog.dismiss();
                            Toasty.warning(AssignmentActivity.this,"Assignment already submitted").show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Student> call, Throwable t) {
                        dialog.dismiss();
                        Log.d(TAG, "onFailure: "+t.getMessage());
                        Toasty.warning(AssignmentActivity.this,"Data not found").show();
                    }
                }

        );
    }

    public void goBack(View view) {
        if (type.equalsIgnoreCase("student")){
            startActivity(new Intent(AssignmentActivity.this, StudentActivity.class));

        }else{
            startActivity(new Intent(AssignmentActivity.this, TeacherActivity.class));

        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (type.equalsIgnoreCase("student")){
            startActivity(new Intent(AssignmentActivity.this, StudentActivity.class));

        }else{
            startActivity(new Intent(AssignmentActivity.this, TeacherActivity.class));

        }
        finish();
    }
}