package top.ashher.xingmu.service.lua;

import lombok.Data;
import top.ashher.xingmu.vo.SeatVo;

import java.util.List;

@Data
public class ProgramCacheCreateOrderData {

    private Integer code;

    private List<SeatVo> purchaseSeatList;
}