package nord_codes.aqa.task.apitests.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {
    private String result;
    private String message;
    private Integer statusCode;

    public ApiResponse() {}

    public ApiResponse(String result, String message) {
        this.result = result;
        this.message = message;
    }

    public ApiResponse(String result, String message, Integer statusCode) {
        this.result = result;
        this.message = message;
        this.statusCode = statusCode;
    }

    @JsonProperty("result")
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("statusCode")
    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}