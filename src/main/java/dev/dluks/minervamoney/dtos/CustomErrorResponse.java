package dev.dluks.minervamoney.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CustomErrorResponse {
    private Instant timestamp;
    private String error;
    private String path;
    private Integer status;

}