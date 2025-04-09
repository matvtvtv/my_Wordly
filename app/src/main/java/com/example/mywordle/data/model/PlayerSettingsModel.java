package com.example.mywordle.data.model;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerSettingsModel {
    private int userId;
    private int sound;
    private int vibration;
    private byte[] profileImage;
}
