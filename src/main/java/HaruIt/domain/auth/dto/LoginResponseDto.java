package HaruIt.domain.auth.dto;


public class LoginResponseDto {
    private String email;
    private String name;
    private String message;

    public LoginResponseDto(String email,String name,String message){
        this.email=email;
        this.name=name;
        this.message=message;
    }

    public String getEmail(){return email;}
    public String getName(){return name;}
    public String getMessage(){return message;

    }
}
