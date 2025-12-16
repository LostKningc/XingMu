package top.ashher.xingmu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(title="TicketCategoryListByProgramDto", description ="节目票档集合")
public class TicketCategoryListByProgramDto {

    @Schema(name ="programId", type ="Long", description ="节目id",requiredMode= Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long programId;
}
