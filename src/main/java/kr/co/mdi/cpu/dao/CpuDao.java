package kr.co.mdi.cpu.dao;

import java.util.List;
import kr.co.mdi.cpu.dto.CpuDTO;

public interface CpuDao {
    List<CpuDTO> selectAllCpus();
    CpuDTO selectCpuById(Long id);
    CpuDTO insertCpu(CpuDTO cpu);
    CpuDTO updateCpu(CpuDTO cpu);
    void deleteCpu(Long id);
}
