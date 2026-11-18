package HaruIt.domain.auth.dto;

public class SignupRequestDto {
    private String email;
    private String authCode;
    private String password;
    private String passWordCheck;
    private String name;

    public String getEmail() {
        return email;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getPassword() {
        return password;
    }

    public String getPassWordCheck() {
        return passWordCheck;
    }

    public String getName() {
        return name;
    }
}
