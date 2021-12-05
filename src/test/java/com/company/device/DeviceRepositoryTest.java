package com.company.device;

import com.company.device.data.Device;
import com.company.device.data.DeviceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.transaction.TestTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
public class DeviceRepositoryTest {

    @Autowired
    DeviceRepository repo;

    @Test
    void createAndLoadDevice() throws Exception {
        var macAddress = "AA-BB-10-20-30-40";

        assertTrue(repo.findById(macAddress).isEmpty());

        var device = new Device(macAddress, DeviceType.GATEWAY);
        var saved = repo.save(device);
        assertEquals(device, saved);

        var loaded = repo.getById(device.getMacAddress());
        assertEquals(device, loaded);
    }

    @Test
    void createAndLoadTopology() throws Exception {
        /*
            No roots, due to no devices yet saved
         */
        assertTrue(repo.findAllRoots().isEmpty());

        /*
            Create and test one root
        */
        var root1 = new Device("0", DeviceType.GATEWAY, null, new ArrayList<>());
        repo.save(root1);

        var roots = repo.findAllRoots();
        assertEquals(1, roots.size());
        var loadedRoot1 = roots.get(0);
        assertEquals(root1, loadedRoot1);
        assertEquals(0, loadedRoot1.getChildren().size());

        /*
            Create and test another root
        */
        var root2 = new Device("00", DeviceType.GATEWAY, null, new ArrayList<>());
        repo.save(root2);
        roots = repo.findAllRoots();

        assertEquals(2, roots.size());
        loadedRoot1 = roots.get(0);
        var loadedRoot2 = roots.get(1);
        assertEquals(root1, loadedRoot1);
        assertEquals(root2, loadedRoot2);
        assertEquals(0, loadedRoot1.getChildren().size());
        assertEquals(0, loadedRoot2.getChildren().size());

        /*
            Test root with children
         */
        Map<String, Device> children = createChildren(root1,10);
        for (var e : children.entrySet()) {
            assertFalse(repo.findById(e.getKey()).isPresent());
        }

        repo.saveAll(children.values());

        for (var e : children.entrySet()) {
            var loadedChild = repo.getById(e.getKey());
            assertEquals(e.getValue(), loadedChild);
        }

        /*
            Need to refresh entities
         */
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        roots = repo.findAllRoots();

        assertEquals(2, roots.size());
        loadedRoot1 = roots.get(0);
        loadedRoot2 = roots.get(1);
        assertEquals(root1, loadedRoot1);
        assertEquals(root2, loadedRoot2);
        assertEquals(10, loadedRoot1.getChildren().size());
        assertEquals(0, loadedRoot2.getChildren().size());

        loadedRoot1.getChildren().forEach(c -> assertEquals(c, children.get(c.getMacAddress())));
    }

    private Map<String, Device> createChildren(Device root, int n) {
        var random = new Random();
        Map<String, Device> children = new HashMap<>();
        for (int i = 0; i < n; i++) {
            var macAddress = "Child_" + String.valueOf(i);
            var child = new Device(
                    macAddress,
                    DeviceType.values()[random.nextInt(DeviceType.values().length)],
                    root,
                    new ArrayList<>()
            );
            children.put(macAddress, child);
        }
        return children;
    }

}
