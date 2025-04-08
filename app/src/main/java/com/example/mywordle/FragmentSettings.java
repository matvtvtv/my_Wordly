package com.example.mywordle;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
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
import com.example.mywordle.data.repository.PlayerRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FragmentSettings extends Fragment {

    private TextView login;
    private ImageView accountExit;
    private ImageView bugRep;
    private ImageView picture_prof;
    private Switch mySwitch;
    private static final int PICK_IMAGE_REQUEST = 1;
    private PlayerRepository playerRepository;

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
        PlayerModel user = playerRepository.getUserData(userId);

        int sound = (Integer) user.getSound();
        mySwitch.setChecked(sound == 1);
        mySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int newSoundValue = isChecked ? 1 : 0;

            ContentValues values = new ContentValues();
            values.put("sound", newSoundValue);

            playerRepository.updateUserData(userId, values);
        });

        login.setSelected(true);
        if (user != null) {
            login.setText(user.getLogin());
        }

        accountExit.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), RegistrationActivity.class);
            startActivity(intent);
        });

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
        mySwitch = view.findViewById(R.id.gameSoundSwitch);
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
        int userId = playerRepository.getCurrentUserId();
        PlayerModel player = playerRepository.getUserData(userId);

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

                Bitmap resizedBitmap = cropAndResizeBitmap(bitmap, 3000, 3000);

                saveImageToDatabase(resizedBitmap);

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

    private void saveImageToDatabase(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        int userId = playerRepository.getCurrentUserId();
        ContentValues values = new ContentValues();
        values.put("profileImage", imageBytes);

        playerRepository.updateUserData(userId, values);

        Toast.makeText(getContext(), "Изображение сохранено!", Toast.LENGTH_SHORT).show();
    }
}
