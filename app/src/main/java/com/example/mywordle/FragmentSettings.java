package com.example.mywordle;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mywordle.data.model.PlayerModel;
import com.example.mywordle.data.model.PlayerSettingsModel;
import com.example.mywordle.data.repository.PlayerRepository;
import com.example.mywordle.data.repository.PlayerSettingsRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FragmentSettings extends Fragment {

    private TextView login;
    private ImageView accountExit;
    private ImageView bugRep;
    private ImageView picture_prof;
    private Switch mySwitchSound;
    private Switch mySwitchVibration;
    private Switch mySwitchNotification;
    private static final int PICK_IMAGE_REQUEST = 1;
    private PlayerRepository playerRepository;
    private PlayerSettingsRepository playerSettingsRepository;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        getAllId(view);



        playerRepository = PlayerRepository.getInstance(getContext());
        int userId = playerRepository.getCurrentUserId();
        PlayerModel player = playerRepository.getUserData(userId);

        login.setSelected(true);
        if (player != null) {
            login.setText(player.getLogin());
        }


        accountExit.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), RegistrationActivity.class);
            startActivity(intent);
        });





        playerSettingsRepository = PlayerSettingsRepository.getInstance(getContext());
        int user_Id = playerSettingsRepository.getCurrentUserId();
        PlayerSettingsModel user = playerSettingsRepository.getUserData(user_Id);


        mySwitchSound.setChecked( user.getSound() == 1);
        mySwitchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int newSoundValue = isChecked ? 1 : 0;

            ContentValues values = new ContentValues();
            values.put("sound", newSoundValue);

            playerSettingsRepository.updateUserData(user_Id, values);
        });

        mySwitchVibration.setChecked( user.getVibration() == 1);
        mySwitchVibration.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int newVibrationValue = isChecked ? 1 : 0;

            ContentValues values = new ContentValues();
            values.put("vibration", newVibrationValue);

            playerSettingsRepository.updateUserData(user_Id, values);
        });

        mySwitchNotification.setChecked( user.getVibration() == 1);
        mySwitchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int newNotificationValue = isChecked ? 1 : 0;

            ContentValues values = new ContentValues();
            values.put("notification", newNotificationValue);

            playerSettingsRepository.updateUserData(user_Id, values);
        });

        bugRep.setOnClickListener(v -> sendEmailToAgency());
        bugRep.setOnClickListener(v -> sendEmailToAgency());
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(50);
        drawable.setColor(Color.TRANSPARENT);
        loadProfileImage();
        picture_prof.setBackground(drawable);
        picture_prof.setClipToOutline(true);

        picture_prof.setOnClickListener(v -> openGallery());

        return view;
    }

    private void getAllId(View view) {
        picture_prof = view.findViewById(R.id.profCard);
        accountExit = view.findViewById(R.id.accountExit);
        bugRep = view.findViewById(R.id.bugReport);
        login = view.findViewById(R.id.yourLogin);
        mySwitchSound = view.findViewById(R.id.gameSwitchSound);
        mySwitchVibration = view.findViewById(R.id.gameSwitchVibration);
        mySwitchNotification = view.findViewById(R.id.gameSwitchNotification);

    }

    private static final String AGENCY_EMAIL = "matveicharniauski@gmail.com";

    private void sendEmailToAgency() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + AGENCY_EMAIL));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Сообщение об ошибке");
        intent.putExtra(Intent.EXTRA_TEXT, "Здравствуйте! Хотел бы сообщить об ошибке...");
        startActivity(Intent.createChooser(intent, "Выберите почтовое приложение"));
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void loadProfileImage() {
        int userId = playerSettingsRepository.getCurrentUserId();
        PlayerSettingsModel player = playerSettingsRepository.getUserData(userId);

        if (player != null && player.getProfileImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(player.getProfileImage(), 0, player.getProfileImage().length);
            picture_prof.setImageBitmap(bitmap);
        } else {
            picture_prof.setImageResource(R.drawable.default_profile);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                Toast.makeText(getContext(), width + "-" + height, Toast.LENGTH_SHORT).show();
                int img_min=Math.min(width,height);
                Bitmap resizedBitmap = cropAndResizeBitmap(bitmap, img_min, img_min);

                saveImageToDatabase(resizedBitmap,img_min);

                picture_prof.setImageBitmap(resizedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap cropAndResizeBitmap(Bitmap originalBitmap, int width, int height) {
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();

        int cropWidth = Math.min(originalWidth, width);
        int cropHeight = Math.min(originalHeight, height);

        int cropX = (originalWidth - cropWidth) / 2;
        int cropY = (originalHeight - cropHeight) / 2;

        Bitmap croppedBitmap = Bitmap.createBitmap(originalBitmap, cropX, cropY, cropWidth, cropHeight);

        return Bitmap.createScaledBitmap(croppedBitmap, width, height, true);
    }

    private void saveImageToDatabase(Bitmap bitmap, int size_img) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if(size_img>512){
            int compression= (int) (1000/size_img)*100;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API 30+
                bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, compression, byteArrayOutputStream);
            }
            else {
                bitmap.compress(Bitmap.CompressFormat.WEBP, compression, byteArrayOutputStream); // Поддерживается с API 17
            }
        }
        else{bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);}

        byte[] imageBytes = byteArrayOutputStream.toByteArray();

       int userId = playerSettingsRepository.getCurrentUserId();
        ContentValues values = new ContentValues();
        values.put("profileImage", imageBytes);

        playerSettingsRepository.updateUserData(userId, values);

        Toast.makeText(getContext(), "Изображение сохранено!", Toast.LENGTH_SHORT).show();
    }
}
