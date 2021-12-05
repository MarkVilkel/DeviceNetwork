package com.company.rest.device;

import com.company.device.DeviceRepository;
import com.company.device.data.Device;
import com.company.rest.device.error.DeviceAlreadyExistException;
import com.company.rest.device.error.DeviceNotFoundException;
import com.company.rest.device.interaction.CreateDeviceRequest;
import com.company.rest.device.interaction.LoadDeviceResponse;
import com.company.rest.device.interaction.LoadDeviceTypologyResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class DeviceController {

    private final DeviceRepository repository;

    DeviceController(DeviceRepository repository) {
        this.repository = repository;
    }

    @PutMapping("/device")
    EntityModel<LoadDeviceResponse> createDevice(@Valid @RequestBody CreateDeviceRequest newDevice) {
        repository.findById(newDevice.macAddress()).ifPresent(d -> {
            throw DeviceAlreadyExistException.ofId(newDevice.macAddress());
        });

        var device = new Device(
                newDevice.macAddress(),
                newDevice.type()
        );

        var uplinkMacAddress = newDevice.uplinkMacAddress();
        if (uplinkMacAddress != null) {
            var parent = repository
                    .findById(uplinkMacAddress)
                    .orElseThrow(() -> DeviceNotFoundException.of("Uplink device is not found for " + uplinkMacAddress));
            device.setParent(parent);
        }

        return EntityModel.of(
                LoadDeviceResponse.of(repository.save(device)),
                linkTo(methodOn(DeviceController.class).createDevice(newDevice)).withSelfRel(),
                linkTo(methodOn(DeviceController.class).loadById(device.getMacAddress())).withRel("loadById"),
                linkTo(methodOn(DeviceController.class).loadAllDevices()).withRel("loadAllDevices")
        );
    }

    @GetMapping("/device/{macAddress}")
    EntityModel<LoadDeviceResponse> loadById(@PathVariable String macAddress) {
        var result = repository
                .findById(macAddress)
                .map(LoadDeviceResponse::of)
                .orElseThrow(() -> DeviceNotFoundException.ofId(macAddress));

        return EntityModel.of(
                result,
                linkTo(methodOn(DeviceController.class).loadById(macAddress)).withSelfRel(),
                linkTo(methodOn(DeviceController.class).loadAllDevices()).withRel("loadAllDevices")
        );
    }

    @GetMapping("/device")
    CollectionModel<LoadDeviceResponse> loadAllDevices() {
        var result = repository
                .findAll(Device.DEFAULT_SORT)
                .stream()
                .map(LoadDeviceResponse::of)
                .toList();

        return CollectionModel.of(
                result,
                linkTo(methodOn(DeviceController.class).loadAllDevices()).withSelfRel(),
                linkTo(methodOn(DeviceController.class).loadById(null)).withRel("loadById")
        );
    }

    @GetMapping("/device/topology")
    CollectionModel<LoadDeviceTypologyResponse> loadWholeTopology() {
        var result = repository
                .findAllRoots()
                .stream()
                .map(LoadDeviceTypologyResponse::of)
                .toList();

        return CollectionModel.of(
                result,
                linkTo(methodOn(DeviceController.class).loadWholeTopology()).withSelfRel(),
                linkTo(methodOn(DeviceController.class).loadTopologyByMacAddress(null)).withRel("loadTopologyByMacAddress")
        );
    }

    @GetMapping("/device/topology/{macAddress}")
    EntityModel<LoadDeviceTypologyResponse> loadTopologyByMacAddress(@PathVariable String macAddress) {
        var result = repository
                .findById(macAddress)
                .map(LoadDeviceTypologyResponse::of)
                .orElseThrow(() -> DeviceNotFoundException.ofId(macAddress));

        return EntityModel.of(
                result,
                linkTo(methodOn(DeviceController.class).loadTopologyByMacAddress(macAddress)).withSelfRel(),
                linkTo(methodOn(DeviceController.class).loadWholeTopology()).withRel("loadWholeTopology")
        );
    }

}
