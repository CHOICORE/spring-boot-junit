package me.choicore.junittest.web.handler;


import me.choicore.junittest.web.dto.GlobalCommonResponseCode;
import me.choicore.junittest.web.dto.GlobalCommonResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException e) {
        GlobalCommonResponseDTO<Object> responseDTO = GlobalCommonResponseDTO.builder()
                .code(GlobalCommonResponseCode.FAIL.getCode())
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(responseDTO);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        GlobalCommonResponseDTO<Object> responseDTO = GlobalCommonResponseDTO.builder()
                .code(GlobalCommonResponseCode.FAIL.getCode())
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(responseDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        GlobalCommonResponseDTO responseDTO = GlobalCommonResponseDTO.builder()
                .code(GlobalCommonResponseCode.FAIL.getCode())
                .message("validation error")
                .data(errors)
                .build();
        return ResponseEntity.badRequest().body(responseDTO);
    }

}
