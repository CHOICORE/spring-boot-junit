package me.choicore.junittest.web.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalCommonResponseDTO<T> {

    private Integer code;
    private String message;
    private T data;

    @Builder
    public GlobalCommonResponseDTO(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


}
