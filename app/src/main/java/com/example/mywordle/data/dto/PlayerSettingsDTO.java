package com.example.mywordle.data.dto;

import lombok.Data;
@Data
public class PlayerSettingsDTO {
    private int userId;
    private int sound;
    private int vibration;
    private byte[] profileImage;
    //режим дальтоника
    //тема
    //уведомление

}
