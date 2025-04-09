package com.example.mywordle.data.dto;

import lombok.Data;
@Data
public class PlayerSettingsDTO {
    private int userId;
    private int sound;
    private int vibration;
    private byte[] profileImage;
    //разменр шрифта
    //тема
    //уведомление

}
