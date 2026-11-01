package pt.isec.pd.httpClient;

public class TokenManager {
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public boolean isAuthenticated() {
        return token != null && !token.isEmpty();
    }
}