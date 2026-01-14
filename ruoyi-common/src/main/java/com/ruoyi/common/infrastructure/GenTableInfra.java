package com.ruoyi.common.infrastructure;

import com.ruoyi.common.model.GenTableDto;

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
}
