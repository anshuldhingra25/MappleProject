package com.findpackers.android.aap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.findpackers.android.aap.commanUtill.CommanMethod;
import com.findpackers.android.aap.commanUtill.HttpClient;
import com.findpackers.android.aap.commanUtill.MyPreferences;
import com.findpackers.android.aap.commanUtill.Utility;
import com.findpackers.android.aap.commanUtill.WebserviceUrlClass;
import com.findpackers.android.aap.fragment.ProfileFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileCategory extends AppCompatActivity {


    Toolbar toolbar;
    TextView toolbarTitle,txt_avlableCredit,txt_categoryTitle,upload_iba,upload_iso,txt_pancardimage,upload_gst;
    ImageView IvBack,pan_card_attach,upload_gst_attach,upload_iso_attach,upload_iba_attach;
    EditText et_panno;
    EditText et_gstno;
    RelativeLayout rl_pancard,rl_gstcerificate,rl_isocerficate,rl_ibacertificate,rl_pan_india_precence_compny;
    String profilecategory;
    private int REQUEST_CAMERA = 0,
            SELECT_FILE = 1;
    Bitmap updatedthumbnail;
    byte[] mData=null;
    byte[] Gstmdata=null;
    byte[] panmData=null;
    byte[] isomData=null;
    byte[] ibamData=null;
    File destination;
    private String userChoosenTask;
    String imageof;
    LinearLayout ln_creditbalance;
    double imageName;
    String userId;
    Button btn_save_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_category);


        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        txt_avlableCredit = (TextView) findViewById(R.id.txt_avlableCredit);
        txt_categoryTitle = (TextView) findViewById(R.id.txt_categoryTitle);
        upload_iba = (TextView) findViewById(R.id.upload_iba);
        upload_iso = (TextView) findViewById(R.id.upload_iso);
        txt_pancardimage = (TextView) findViewById(R.id.txt_pancardimage);
        upload_gst = (TextView) findViewById(R.id.upload_gst);
       // ln_creditbalance = (LinearLayout) findViewById(R.id.ln_creditbalance);

        IvBack = (ImageView) findViewById(R.id.back_icon);
        upload_gst_attach = (ImageView) findViewById(R.id.upload_gst_attach);
        upload_iso_attach = (ImageView) findViewById(R.id.upload_iso_attach);
        upload_iba_attach = (ImageView) findViewById(R.id.upload_iba_attach);
        pan_card_attach = (ImageView) findViewById(R.id.pan_card_attach);
        btn_save_update = (Button) findViewById(R.id.btn_save_update);
        setSupportActionBar(toolbar);



        profilecategory = getIntent().getExtras().getString("Profilecategory", "N/A");

        et_panno = (EditText) findViewById(R.id.et_panno);
        et_gstno = (EditText) findViewById(R.id.et_gstno);
        rl_pancard = (RelativeLayout) findViewById(R.id.rl_pancard);
        rl_gstcerificate = (RelativeLayout) findViewById(R.id.rl_gstcerificate);
        rl_isocerficate = (RelativeLayout) findViewById(R.id.rl_isocerficate);
        rl_ibacertificate = (RelativeLayout) findViewById(R.id.rl_ibacertificate);
        rl_pan_india_precence_compny = (RelativeLayout) findViewById(R.id.rl_pan_india_precence_compny);

        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txt_avlableCredit.setText(MyPreferences.getActiveInstance(ProfileCategory.this).getCreditbalance());
        userId = MyPreferences.getActiveInstance(ProfileCategory.this).getUserId();

/*
        ln_creditbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(ProfileCategory.this, AvlableBlance.class);
                startActivity(in);


            }
        });*/

        if(profilecategory.equalsIgnoreCase("1"))
        {
            toolbarTitle.setText("Standard Upgradation");


        }
        if(profilecategory.equalsIgnoreCase("premium"))
        {

            toolbarTitle.setText("Premium Upgradation");
            et_panno.setVisibility(View.VISIBLE);
            et_gstno.setVisibility(View.VISIBLE);
            rl_pancard.setVisibility(View.VISIBLE);
            rl_gstcerificate.setVisibility(View.VISIBLE);

            //toolbarTitle.setText("Premium Upgradation");
            //rl_gstcerificate.setVisibility(View.VISIBLE);
            txt_categoryTitle.setText("Please upload the required document for your premium account");

        }
        if(profilecategory.equalsIgnoreCase("luxry"))
        {
            toolbarTitle.setText("Luxury Upgradation");
           // rl_gstcerificate.setVisibility(View.VISIBLE);
            rl_isocerficate.setVisibility(View.VISIBLE);
            rl_ibacertificate.setVisibility(View.VISIBLE);
           // rl_pan_india_precence_compny.setVisibility(View.VISIBLE);
            txt_categoryTitle.setText("Please upload the required document for your Luxury account");

        } if(profilecategory.equalsIgnoreCase("standadrtoluxry"))
        {
            toolbarTitle.setText("Luxury Upgradation");


            et_panno.setVisibility(View.VISIBLE);
            et_gstno.setVisibility(View.VISIBLE);
            rl_pancard.setVisibility(View.VISIBLE);
            rl_gstcerificate.setVisibility(View.VISIBLE);

            rl_isocerficate.setVisibility(View.VISIBLE);
            rl_ibacertificate.setVisibility(View.VISIBLE);



          //  rl_pan_india_precence_compny.setVisibility(View.VISIBLE);
            txt_categoryTitle.setText("Please upload the required document for your Luxury account");

        }

        rl_pancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageof="pan";

                selectImage("Add Photo of PAN Card certificate !");

            }
        });
        rl_gstcerificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageof="gst";

                selectImage("Add Photo of GST certificate !");

            }
        }); rl_isocerficate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageof="iso";
                selectImage("Add Photo of ISO certificate !");

            }
        }); rl_ibacertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageof="iba";
                selectImage("Add Photo of IBA certificate !");

            }
        });

        btn_save_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(profilecategory.equalsIgnoreCase("premium"))
                {
                    if(validationPremium()) {
                        UpdatetoPremiumProfileCategory updatetoPremiumProfileCategory = new UpdatetoPremiumProfileCategory();
                        updatetoPremiumProfileCategory.execute();
                    }

                }else if(profilecategory.equalsIgnoreCase("luxry")){
                    if(validationLuxry()) {
                        UpdatetoLuxryProfileCategory updatetoLuxryProfileCategory = new UpdatetoLuxryProfileCategory();
                        updatetoLuxryProfileCategory.execute();
                    }


                }else if(profilecategory.equalsIgnoreCase("standadrtoluxry")){
                    if(validationStandardtoLuxry()) {
                        UpdatetoLuxryProfileCategory updatetoLuxryProfileCategory = new UpdatetoLuxryProfileCategory();
                        updatetoLuxryProfileCategory.execute();
                    }


                }else{

                }

            }
        });
    }

    private boolean validationPremium() {
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Matcher matcher = pattern.matcher(et_panno.getText().toString());
        boolean valid = true;
        if (et_panno.length() == 0) {
            et_panno.requestFocus();
            et_panno.setError(getResources().getString(R.string.txt_enterpanno));
            valid = false;
        } else if (!matcher.matches()) {
            et_panno.requestFocus();
            et_panno.setError(getResources().getString(R.string.txt_entervalidpanno));
            valid = false;
        } else if (et_gstno.length() == 0) {
            et_gstno.requestFocus();
            et_gstno.setError("Please enter GST no .");
            valid = false;
        } else if (panmData==null) {
            CommanMethod.showAlert("Please upload PAN card image ",this);
            valid = false;
        } else if (Gstmdata==null) {
            CommanMethod.showAlert("Please upload GST  image ",this);
            valid = false;

        }
        return valid;
    }private boolean validationStandardtoLuxry() {
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Matcher matcher = pattern.matcher(et_panno.getText().toString());
        boolean valid = true;
        if (et_panno.length() == 0) {
            et_panno.requestFocus();
            et_panno.setError(getResources().getString(R.string.txt_enterpanno));
            valid = false;
        } else if (!matcher.matches()) {
            et_panno.requestFocus();
            et_panno.setError(getResources().getString(R.string.txt_entervalidpanno));
            valid = false;
        } else if (et_gstno.length() == 0) {
            et_gstno.requestFocus();
            et_gstno.setError("Please enter GST no .");
            valid = false;
        } else if (panmData==null) {
            CommanMethod.showAlert("Please upload PAN card image ",this);
            valid = false;
        } else if (Gstmdata==null) {
            CommanMethod.showAlert("Please upload GST  image ",this);
            valid = false;

        }else if (isomData==null) {
            CommanMethod.showAlert("Please upload ISO Certificate image ",this);
            valid = false;
        } else if (ibamData==null) {
            CommanMethod.showAlert("Please upload IBA Certificate image  ",this);
            valid = false;

        }
        return valid;
    }
    private boolean validationLuxry() {
        boolean valid = true;
      if (isomData==null) {
            CommanMethod.showAlert("Please upload ISO Certificate image ",this);
            valid = false;
        } else if (ibamData==null) {
            CommanMethod.showAlert("Please upload IBA Certificate image  ",this);
            valid = false;

        }
        return valid;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        updatedthumbnail = thumbnail;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        mData = bytes.toByteArray();
        destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(imageof.equalsIgnoreCase("gst")){
            //upload_gst_attach.setImageBitmap(thumbnail);
            upload_gst_attach.setImageResource(R.mipmap.verifay);
           upload_gst.setText("GST_certificate.jpg");
            Log.e("imageeeeeeeee", imageName + ".jpg" + ", " + mData);
            Gstmdata=mData;

        }else if(imageof.equalsIgnoreCase("iso"))
        {
            upload_iso_attach.setImageResource(R.mipmap.verifay);
            upload_iso.setText("ISO_certificate.jpg");
            //upload_iso_attach.setImageBitmap(thumbnail);
            isomData=mData;
        }else if(imageof.equalsIgnoreCase("pan"))
        {
            pan_card_attach.setImageResource(R.mipmap.verifay);
            txt_pancardimage.setText("PAN_card.jpg");
            panmData=mData;
            //upload_iso_attach.setImageBitmap(thumbnail);
        }else{
           // upload_iba_attach.setImageBitmap(thumbnail);
            upload_iba_attach.setImageResource(R.mipmap.verifay);
            upload_iba.setText("IBA_certificate.jpg");
            ibamData=mData;
        }



    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(ProfileCategory.this.getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
       // bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        mData = stream.toByteArray();

        if(imageof.equalsIgnoreCase("gst")){
            upload_gst.setText("GST_certificate.jpg");
            upload_gst_attach.setImageResource(R.mipmap.verifay);
            Gstmdata=mData;
        }else if(imageof.equalsIgnoreCase("pan"))
        {
            pan_card_attach.setImageResource(R.mipmap.verifay);
            txt_pancardimage.setText("PAN_card.jpg");
            panmData=mData;

        }else if(imageof.equalsIgnoreCase("iso"))
        {
            upload_iso.setText("ISO_certificate.jpg");
            upload_iso_attach.setImageResource(R.mipmap.verifay);
            isomData=mData;

        }else{
            upload_iba_attach.setImageResource(R.mipmap.verifay);
            upload_iba.setText("IBA_certificate.jpg");
            ibamData=mData;
        }



    }

    private void selectImage( String Title ) {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileCategory.this);
        builder.setTitle(Title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ProfileCategory.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                    // cameraIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }



    ////////////////////////////////////text Multipart//////////////////////
    public class UpdatetoPremiumProfileCategory extends AsyncTask<String, Void, String> {
        private Dialog mDialog;
        private String response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            System.out.println("in asynctask");
            super.onPreExecute();
            mDialog = ProgressDialog.show(ProfileCategory.this, "", "Loading...", true);
            mDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            response = calltoupdatePremiumService();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);

            if (mDialog != null) {
                mDialog.dismiss();
            }
            Log.d("##########Response", "" + response);

            JSONObject object;

            if (response != null) {
                try {
                    object = new JSONObject(response);
                    String success = object.getString("responseCode");
                    String respMessage = object.getString("responseMessage");
                    Log.d("object", "" + object);
                    if (success.equals("200")) {





                        final Dialog dialog = new Dialog(ProfileCategory.this);
                        dialog.setContentView(R.layout.dialog_successfull);
                        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                        TextView txt_msg = (TextView) dialog.findViewById(R.id.txt_msg);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

                        txt_msg.setText(respMessage);

                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                finish();

                            }
                        });
                        dialog.show();
                        dialog.getWindow().setAttributes(lp);





                    } else {
                       // Toast.makeText(ProfileCategory.this, "Please check your login details", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        private String calltoupdatePremiumService() {
            String url = WebserviceUrlClass.userCategory;
            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);
            try {
                client.connectForMultipart();
                Log.e("after connection", "" + url);
                client.addFormPart("userId", userId);
                client.addFormPart("gst_number", et_gstno.getText().toString());
                client.addFormPart("pan_number", et_panno.getText().toString());
                client.addFilePart("pan_image", imageName + ".jpg", panmData);
                client.addFilePart("gst_image", imageName + ".jpg", Gstmdata);
                client.addFormPart("status", "2");

                Log.d("client", client.toString());
                client.finishMultipart();

                response = client.getResponse().toString();
                Log.d("response", response);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return response;
        }
    }
 ////////////////////////////////////text Multipart//////////////////////
    public class UpdatetoLuxryProfileCategory extends AsyncTask<String, Void, String> {
        private Dialog mDialog;
        private String response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            System.out.println("in asynctask");
            super.onPreExecute();
            mDialog = ProgressDialog.show(ProfileCategory.this, "", "Loading...", true);
            mDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            response = calltoupdateLuxryService();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);

            if (mDialog != null) {
                mDialog.dismiss();
            }
            Log.d("##########Response", "" + response);

            JSONObject object;

            if (response != null) {
                try {
                    object = new JSONObject(response);
                    String success = object.getString("responseCode");
                    String respMessage = object.getString("responseMessage");
                    Log.d("object", "" + object);
                    if (success.equals("200")) {





                        final Dialog dialog = new Dialog(ProfileCategory.this);
                        dialog.setContentView(R.layout.dialog_successfull);
                        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                        TextView txt_msg = (TextView) dialog.findViewById(R.id.txt_msg);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

                        txt_msg.setText(respMessage);

                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        dialog.show();
                        dialog.getWindow().setAttributes(lp);





                    } else {
                       // Toast.makeText(ProfileCategory.this, "Please check your login details", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        private String calltoupdateLuxryService() {
            String url = WebserviceUrlClass.userCategory;
            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);
            try {
                client.connectForMultipart();
                Log.e("after connection", "" + url);
                if(profilecategory.equalsIgnoreCase("standadrtoluxry")){
                    client.addFormPart("userId", userId);
                    client.addFormPart("gst_number", et_gstno.getText().toString());
                    client.addFormPart("pan_number", et_panno.getText().toString());
                    client.addFilePart("pan_image", imageName + ".jpg", panmData);
                    client.addFilePart("gst_image", imageName + ".jpg", Gstmdata);
                    client.addFilePart("ibac_image", imageName + ".jpg", ibamData);
                    client.addFilePart("iso_image", imageName + ".jpg", isomData);
                    client.addFormPart("status", "3");


                }else {
                    client.addFormPart("userId", userId);
                    client.addFilePart("ibac_image", imageName + ".jpg", ibamData);
                    client.addFilePart("iso_image", imageName + ".jpg", isomData);
                    client.addFormPart("status", "3");
                }
                Log.d("client", client.toString());
                client.finishMultipart();

                response = client.getResponse().toString();
                Log.d("response", response);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return response;
        }
    }




}
