package com.modulersx.service;

import com.modulersx.domain.dto.ToolSaveDTO;
import com.modulersx.domain.vo.ToolVO;
import java.util.List;

public interface ToolService {

    List<ToolVO> listTools();

    ToolVO getTool(Long id);

    ToolVO createTool(ToolSaveDTO dto);

    ToolVO updateTool(Long id, ToolSaveDTO dto);

    void deleteTool(Long id);
}
