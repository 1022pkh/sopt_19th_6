package com.pkh.sopt_19th_6.register;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pkh.sopt_19th_6.R;
import com.pkh.sopt_19th_6.application.ApplicationController;
import com.pkh.sopt_19th_6.network.NetworkService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    Button addImgBtn;
    TextView imgNameTextView;
    LinearLayout inputContentArea;
    EditText inputTitleEdit;
    EditText inputContentEdit;
    Button completeBtn;

    final int REQ_CODE_SELECT_IMAGE=100;
    String imgUrl = "";
    Uri data;
    NetworkService service;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        addImgBtn = (Button)findViewById(R.id.getImageBtn);
        imgNameTextView = (TextView)findViewById(R.id.addImageName);
        inputContentArea = (LinearLayout)findViewById(R.id.inputContentArea);
        inputTitleEdit = (EditText)findViewById(R.id.inputTitleEdit);
        inputContentEdit = (EditText)findViewById(R.id.inputContentEdit);
        completeBtn = (Button)findViewById(R.id.completeBtn);

        mProgressDialog = new ProgressDialog(RegisterActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("등록 중...");
        mProgressDialog.setIndeterminate(true);

        service = ApplicationController.getInstance().getNetworkService();


        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사진 갤러리 호출
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });


        inputContentArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputContentEdit.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputTitleEdit.length() == 0 || inputContentEdit.length() == 0){
                    Toast.makeText(getApplicationContext(),"제목 및 내용을 확인해주세요.",Toast.LENGTH_SHORT).show();
                }
                else{

                    mProgressDialog.show();
                    // TODO: 2016. 11. 21. 등록하기.

                    /**
                     * 서버로 보낼 파일의 전체 url을 이용해 작업
                     */

                    RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), inputTitleEdit.getText().toString());
                    RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), inputContentEdit.getText().toString());

                    MultipartBody.Part body;

                    if(imgUrl==""){
                        body = null;
                    }
                    else{

                        /**
                         * 비트맵 관련한 자료는 아래의 링크에서 참고
                         * http://mainia.tistory.com/468
                         */
                        /**
                         * 아래의 부분은 이미지 리사이징하는 부분입니다
                         * 왜?? 이미지를 리사이징해서 보낼까요?
                         * 안드로이드는 기본적으로 JVM Heap Memory가 얼마되지 않습니다.
                         * 구글에서는 최소 16MByte로 정하고 있으나, 제조사 별로 디바이스별로 Heap영역의 크기는 다르게 정하여 사용하고 있습니다.
                         * 또한, 이미지를 서버에 업로드할 때 이미지크기가 크면 그만큼 시간이 걸리고 데이터 소모가 되겠죠!
                         */
                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inSampleSize = 4; //얼마나 줄일지 설정하는 옵션 4--> 1/4로 줄이겠다

                        InputStream in = null; // here, you need to get your context.
                        try {
                            in = getContentResolver().openInputStream(data);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        Bitmap bitmap = BitmapFactory.decodeStream(in, null, options); // InputStream 으로부터 Bitmap 을 만들어 준다.
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos); // 압축 옵션( JPEG, PNG ) , 품질 설정 ( 0 - 100까지의 int형 ),


                        RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());

                        File photo = new File(imgUrl); // 그저 블러온 파일의 이름을 알아내려고 사용..

                        // MultipartBody.Part is used to send also the actual file name
                        body = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);

                    }


                    Call<RegisterResult> requestImgNotice = service.registerImgNotice(body,title,content);

                    requestImgNotice.enqueue(new Callback<RegisterResult>() {
                        @Override
                        public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                            if (response.isSuccessful()){

                                if(response.body().result.equals("create")){
                                    finish();
                                    mProgressDialog.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RegisterResult> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                            Log.i("myTag",t.toString());
                            mProgressDialog.dismiss();
                        }
                    });

                }

            }
        });

    }

    // 선택된 이미지 가져오기
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    String name_Str = getImageNameToUri(data.getData());
                    imgNameTextView.setText(name_Str);
                    this.data = data.getData();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else{
                imgUrl = "";
                imgNameTextView.setText("");
            }
        }
    }

    // 선택된 이미지 파일명 가져오기
    public String getImageNameToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        imgUrl = imgPath;

        return imgName;
    }

}
