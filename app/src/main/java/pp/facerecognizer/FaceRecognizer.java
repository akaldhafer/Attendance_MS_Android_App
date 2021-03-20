package pp.facerecognizer;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.squareup.picasso.Picasso;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class FaceRecognizer extends Activity {

    protected Interpreter tflite;
    private  int imageSizeX;
    private  int imageSizeY;

    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;

    public Bitmap oribitmap,testbitmap, bitmap;
    public static Bitmap cropped;
    Uri imageuri, imageuri2, myImage;

    ImageView oriImage,testImage;
    Button buverify;
    TextView result_text;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference ref;
    private String sid, sname, scourse, simage;
    float[][] ori_embedding = new float[1][128];
    float[][] test_embedding = new float[1][128];
    private static final String TAG = "FaceRecognizer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_recognizer);
        sid = getIntent().getStringExtra("id");
        sname = getIntent().getStringExtra("name");
        scourse = getIntent().getStringExtra("course");
        simage = getIntent().getStringExtra("image");
        //imageuri2 = Uri.parse(simage);
        Log.d(TAG, "onCreate: imageuri"+imageuri2);
        try {
            initComponents();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "onCreate: error"+e.getMessage());
            e.printStackTrace();
        }

    }

    private void initComponents() throws FileNotFoundException {
        oriImage=(ImageView)findViewById(R.id.image1);
        testImage=(ImageView)findViewById(R.id.image2);
        buverify=(Button)findViewById(R.id.verify);
        result_text=(TextView)findViewById(R.id.result);
        //

        try{
            tflite=new Interpreter(loadmodelfile(this));

        }catch (Exception e) {
            e.printStackTrace();
        }
        //https://www.geeksforgeeks.org/how-to-display-dynamic-alertdialog-in-android-using-firebase-firestore/
        //Picasso.get().load(value.getData().get("NotificationImage").toString()).into(oriImage);
        Picasso.with(this).load(simage).into(oriImage);
        oriImage.setImageURI(myImage);

       // new GetImageFromURL(oriImage).execute(simage);

        testImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),13);
            }
        });

        buverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double distance=calculate_distance(ori_embedding,test_embedding);

                if(distance<6.0) {
                    result_text.setText("Result : Same Faces");
                }
                else{
                    result_text.setText("Result : Different Faces");
                }
            }

        });
    }
    public void download(){
        storageReference = firebaseStorage.getInstance().getReference();
        ref = storageReference.child(sid+".png");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                 String ur = uri.toString();
                downloadFile(FaceRecognizer.this, sid, "png", DIRECTORY_DOWNLOADS,ur);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    public void downloadFile(Context context, String filename, String fileExtension, String destinationDirectory, String url){
        DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri =Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, filename+fileExtension);
        downloadmanager.enqueue(request);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==13 && resultCode==RESULT_OK && data!=null) {
            imageuri = data.getData();
            Log.d(TAG, "onCreate: imageuri test "+imageuri);
            try {
                testbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                testImage.setImageBitmap(testbitmap);
                face_detector(testbitmap,"test");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Log.d(TAG, "onCreate: imageuri orginl "+imageuri);
            oribitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
            oriImage.setImageBitmap(oribitmap);
            face_detector(oribitmap,"original");
            //saveToGallery();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {
        ImageView imgV;
        public GetImageFromURL(ImageView imgV){
            this.imgV = imgV;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String uridisplay = url[0];
            bitmap = null;
            try{
                InputStream srt = new java.net.URL(uridisplay).openStream();
                bitmap = BitmapFactory.decodeStream(srt);

            }catch (Exception e){
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgV.setImageBitmap(bitmap);
        }
    }
     */
    /*
    private void saveToGallery(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) oriImage.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/StudentPcs");
        dir.mkdirs();

        String filename = String.format(sid+".jpg");
        File outFile = new File(dir,filename);
        try{
            outputStream = new FileOutputStream(outFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        try{
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            outputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

     */
    private double calculate_distance(float[][] ori_embedding, float[][] test_embedding) {
        double sum =0.0;
        for(int i=0;i<128;i++){
            sum=sum+Math.pow((ori_embedding[0][i]-test_embedding[0][i]),2.0);
        }
        return Math.sqrt(sum);
    }

    private TensorImage loadImage(final Bitmap bitmap, TensorImage inputImageBuffer ) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }

    private MappedByteBuffer loadmodelfile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor=activity.getAssets().openFd("Qfacenet.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startoffset = fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startoffset,declaredLength);
    }

    private TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }


    public void face_detector(final Bitmap bitmap, final String imagetype){

        final InputImage image = InputImage.fromBitmap(bitmap,0);
        FaceDetector detector = FaceDetection.getClient();
        detector.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<List<Face>>() {
                            @Override
                            public void onSuccess(List<Face> faces) {
                                // Task completed successfully
                                for (Face face : faces) {
                                    Rect bounds = face.getBoundingBox();
                                    cropped = Bitmap.createBitmap(bitmap, bounds.left, bounds.top, bounds.width(), bounds.height());
                                    get_embaddings(cropped,imagetype);
                                }
                            }

                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
    }

    public void get_embaddings(Bitmap bitmap,String imagetype){
        TensorImage inputImageBuffer;
        float[][] embedding = new float[1][128];
        int imageTensorIndex = 0;
        int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
        imageSizeY = imageShape[1];
        imageSizeX = imageShape[2];
        DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();
        inputImageBuffer = new TensorImage(imageDataType);
        inputImageBuffer = loadImage(bitmap,inputImageBuffer);
        tflite.run(inputImageBuffer.getBuffer(),embedding);
        if(imagetype.equals("original"))
            ori_embedding=embedding;
        else if (imagetype.equals("test"))
            test_embedding=embedding;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FaceRecognizer.this, StudentActivity.class);
        intent.putExtra("name", sname);
        intent.putExtra("id", sid);
        intent.putExtra("course", scourse);
        intent.putExtra("image", simage);
        startActivity(intent);
        finish();
    }
}
