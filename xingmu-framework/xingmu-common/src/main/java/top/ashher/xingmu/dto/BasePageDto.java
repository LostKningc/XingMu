package top.ashher.xingmu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BasePageDto {

    @Schema(name ="pageNumber", type ="Integer", description ="页码", requiredMode= Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer pageNumber;

    @Schema(name ="pageSize", type ="Integer", description ="每页条数", requiredMode= Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer pageSize;
}
