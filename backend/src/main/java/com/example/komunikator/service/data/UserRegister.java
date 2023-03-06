package com.example.komunikator.service.data;

import lombok.Data;
/** jedynie konieczne pola do rejestracji,
 zapobieganie udostępniania użytkownikowi obiektów domenowych**/

@Data
public class UserRegister {
    private String username;
    private String password;
}
