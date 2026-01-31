package kr.co.mdi.device.dao;

import java.util.List;
import kr.co.mdi.device.dto.DeviceDTO;

public interface DeviceDao {
    List<DeviceDTO> selectAllDevices();
    DeviceDTO selectDeviceById(Long id);
    DeviceDTO insertDevice(DeviceDTO device);
    DeviceDTO updateDevice(DeviceDTO device);
    void deleteDevice(Long id);
}
