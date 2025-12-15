package top.ashher.xingmu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(title="AreaGetDto", description ="AreaGetDto")
public class AreaGetDto {

    @Schema(name ="id", type ="Long",description="地区id",requiredMode= Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long id;
}
