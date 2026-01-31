package kr.co.mdi.device.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import kr.co.mdi.device.dao.DeviceDao;
import kr.co.mdi.device.dto.DeviceDTO;

@Profile("dev-user-mysql")
@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceDao deviceDao;

    public DeviceServiceImpl(DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
    }

    @Override
    public List<DeviceDTO> findAll() {
        return deviceDao.selectAllDevices();
    }

    @Override
    public Optional<DeviceDTO> findById(Long id) {
        return Optional.ofNullable(deviceDao.selectDeviceById(id));
    }

    @Override
    public DeviceDTO save(DeviceDTO device) {
        if (device.getIdDevice() == null) {
            return deviceDao.insertDevice(device);
        } else {
            return deviceDao.updateDevice(device);
        }
    }

    @Override
    public void delete(Long id) {
        deviceDao.deleteDevice(id);
    }
}
