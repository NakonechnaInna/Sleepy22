package com.example.inna.sleepy2;

public class UserModel {
  public  String Email;
  public String Password;
  public String Login;

    public UserModel(String login, String email, String password){
        Email = email;
        Password = password;
        Login = login;
    }
    public UserModel(){
        Email = "";
        Password = "";
        Login = "";
    }
}
