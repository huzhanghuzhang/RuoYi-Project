package com.ruoyi.common.infrastructure;

import com.ruoyi.common.model.GenTableDto;

import java.util.List;
import java.util.Map;

/**
 * @author Hu.Zhang
 * @version 1.0
 * @package com.ruoyi.common.infrastructure
 * @description GenTableInfra    代码生成表内部调用服务
 * @date 2026/1/13 16:04
 */
public interface GenTableInfra {

    GenTableDto addTable(GenTableDto genTableDto);

    GenTableDto updateTable(GenTableDto genTableDto);

    void deleteTable(Long tableId);

    List<GenTableDto> selectTableListByIds(List<Long> tableIds);

    /**
     * 根据ids查询表
     * @param tableIds 表ids
     * @return 表集合
     */
    Map<Long, GenTableDto> getTableMap(List<Long> tableIds);
}
