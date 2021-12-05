package com.company.device.data;

import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="DEVICE")
public class Device {

    @Transient
    public static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "type");

    @Id
    @Column(name="MAC_ADDRESS", nullable = false, unique = true, updatable = false)
    private String macAddress;


    @Column(name="TYPE", nullable = false, updatable = false)
    @Enumerated(value = EnumType.STRING)
    private DeviceType type;

    @ManyToOne
    @JoinColumn(name = "UPLINK_DEVICE")
    private Device parent;

    @OneToMany(mappedBy = "parent")
    private List<Device> children;

    Device() {

    }

    public Device(
            String macAddress,
            DeviceType type
    ) {
        this(
                macAddress,
                type,
                null,
                null
        );
    }

    public Device(
            String macAddress,
            DeviceType type,
            Device parent,
            List<Device> children
    ) {
        this.macAddress = macAddress;
        this.type = type;
        this.parent = parent;
        this.children = children;
    }

    public DeviceType getType() {
        return type;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setParent(Device parent) {
        this.parent = parent;
    }

    public List<Device> getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(macAddress, device.macAddress) && type == device.type && Objects.equals(parent, device.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(macAddress, type, parent);
    }

    @Override
    public String toString() {
        return "Device{" +
                "macAddress='" + macAddress + '\'' +
                ", type=" + type +
                ", parent=" + (parent == null ? null : parent.getMacAddress()) +
                ", children=" + (children == null ? null : children.size()) +
                '}';
    }
}
