package top.ashher.xingmu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(title="TicketCategoryAddDto", description ="节目票档添加")
public class TicketCategoryAddDto {

    @Schema(name ="programId", type ="Long", description ="节目表id",requiredMode= Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long programId;

    @Schema(name ="introduce", type ="String", description ="介绍",requiredMode= Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String introduce;

    @Schema(name ="price", type ="BigDecimal", description ="价格",requiredMode= Schema.RequiredMode.REQUIRED)
    @NotNull
    private BigDecimal price;

    @Schema(name ="totalNumber", type ="Long", description ="总数量",requiredMode= Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long totalNumber;

    @Schema(name ="remainNumber", type ="Long", description ="剩余数量",requiredMode= Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long remainNumber;


}
