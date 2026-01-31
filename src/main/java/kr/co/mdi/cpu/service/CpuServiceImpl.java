package kr.co.mdi.cpu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import kr.co.mdi.cpu.dao.CpuDao;
import kr.co.mdi.cpu.dto.CpuDTO;

@Profile("dev-user-mysql")
@Service
public class CpuServiceImpl implements CpuService {

    private final CpuDao cpuDao;

    public CpuServiceImpl(CpuDao cpuDao) {
        this.cpuDao = cpuDao;
    }

    @Override
    public List<CpuDTO> findAll() {
        return cpuDao.selectAllCpus();
    }

    @Override
    public Optional<CpuDTO> findById(Long id) {
        return Optional.ofNullable(cpuDao.selectCpuById(id));
    }

    @Override
    public CpuDTO save(CpuDTO cpu) {
        if (cpu.getIdCpu() == null) {
            // 신규 등록
            return cpuDao.insertCpu(cpu);
        } else {
            // 업데이트
            return cpuDao.updateCpu(cpu);
        }
    }

    @Override
    public void delete(Long id) {
        cpuDao.deleteCpu(id);
    }
}
