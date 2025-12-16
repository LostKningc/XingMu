package top.ashher.xingmu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(title="TicketCategoryDto", description ="节目票档")
public class TicketCategoryDto {

    @Schema(name ="id", type ="Long", description ="id",requiredMode= Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long id;

}
