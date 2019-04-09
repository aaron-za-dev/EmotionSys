package com.aaronzadev.model.dao;



import com.aaronzadev.model.pojo.Device;

import java.util.List;

public interface DeviceDAO extends CRUD<Device> {

    List<Device> getAllByBrandAndType(short type, short brand);

}
