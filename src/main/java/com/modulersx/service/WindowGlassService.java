package com.modulersx.service;

import com.modulersx.domain.dto.WindowDesignSaveDTO;
import com.modulersx.domain.vo.WindowDesignVO;
import com.modulersx.domain.vo.WindowTemplateVO;
import java.util.List;

public interface WindowGlassService {

    List<WindowTemplateVO> listTemplates();

    List<WindowDesignVO> listDesigns();

    WindowDesignVO calculate(WindowDesignSaveDTO dto);

    WindowDesignVO createDesign(WindowDesignSaveDTO dto);
}
