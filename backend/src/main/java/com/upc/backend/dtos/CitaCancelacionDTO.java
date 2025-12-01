package com.upc.backend.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CitaCancelacionDTO {
    private Long citaId;
    private String motivoCancelacion;
}