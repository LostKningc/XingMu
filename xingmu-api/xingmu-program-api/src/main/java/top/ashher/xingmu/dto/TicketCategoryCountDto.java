package top.ashher.xingmu.dto;

import lombok.Data;

@Data
public class TicketCategoryCountDto {

    /**
     * 票档id
     * */
    private Long ticketCategoryId;

    /**
     * 数量
     * */
    private Long count;
}
