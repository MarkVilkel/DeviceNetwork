package com.company.rest.device;

import com.company.device.data.Device;
import com.company.device.data.DeviceType;
import com.company.rest.device.interaction.CreateDeviceRequest;
import com.company.rest.device.interaction.LoadDeviceResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeviceController.class)
public class DeviceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceController service;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void createDevice() throws Exception {
        var macAddress = "AA:AA:BB:AA:10:12";
        var deviceType = DeviceType.ACCESS_POINT;
        var request = new CreateDeviceRequest(macAddress, deviceType, null);
        var model = EntityModel.of(LoadDeviceResponse.of(new Device(macAddress, deviceType)));

        when(service.createDevice(request)).thenReturn(model);
        mockMvc
                .perform(put("/device")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{\"type\":\"" + deviceType.getTitle() + "\",\"macAddress\":\"" + macAddress + "\"}\"")
                );
    }

    @Test
    public void createDeviceWrongMacAddress() throws Exception {
        var macAddress = "AA:AA:BB:AA:10";
        var deviceType = DeviceType.ACCESS_POINT;
        var request = new CreateDeviceRequest(macAddress, deviceType, null);
        var model = EntityModel.of(LoadDeviceResponse.of(new Device(macAddress, deviceType)));

        when(service.createDevice(request)).thenReturn(model);
        mockMvc
                .perform(put("/device")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(
                        "{\"message\":\"Validation Failed\",\"details\":[\"Invalid mac address format\"]}")
                );
    }

    @Test
    public void loadById() throws Exception {
        var macAddress = "AA:BB:10:12:12:12";
        var model = EntityModel.of(
                LoadDeviceResponse.of(new Device(macAddress, DeviceType.GATEWAY)));

        when(service.loadById(macAddress)).thenReturn(model);
        mockMvc
                .perform(get("/device/" + macAddress))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{\"type\":\"Gateway\",\"macAddress\":\"" + macAddress + "\"}\"")
                );
    }

    @Test
    public void loadAllDevices() throws Exception {
        var cm = CollectionModel.of(
                List.of(
                        LoadDeviceResponse.of(new Device("1", DeviceType.GATEWAY)),
                        LoadDeviceResponse.of(new Device("2", DeviceType.SWITCH))
                ));

        when(service.loadAllDevices()).thenReturn(cm);
        mockMvc
                .perform(get("/device"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{\"_embedded\":{\"loadDeviceResponseList\":[{\"type\":\"Gateway\",\"macAddress\":\"1\"},{\"type\":\"Switch\",\"macAddress\":\"2\"}]}}\"")
                );
    }

    /*
        Test other rest API methods
     */
}
