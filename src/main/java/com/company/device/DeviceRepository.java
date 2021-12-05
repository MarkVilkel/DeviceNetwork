package com.company.device;

import com.company.device.data.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, String> {
    @Query("SELECT d FROM Device d WHERE d.parent is null order by d.macAddress")
    List<Device> findAllRoots();
}
