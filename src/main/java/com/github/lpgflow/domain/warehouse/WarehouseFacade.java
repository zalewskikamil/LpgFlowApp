package com.github.lpgflow.domain.warehouse;

import com.github.lpgflow.domain.warehouse.dto.request.CreateAddressRequestDto;
import com.github.lpgflow.domain.warehouse.dto.request.CreateWarehouseRequestDto;
import com.github.lpgflow.domain.warehouse.dto.request.UpdateWarehousePartiallyRequestDto;
import com.github.lpgflow.domain.warehouse.dto.response.AssignAddressToWarehouseResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.CreateAddressResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.CreateWarehouseResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetAddressResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetAllAddressesResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetAllWarehousesResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetWarehouseResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetWarehousesByRegionalManagerEmailResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.UpdateWarehousePartiallyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseFacade {

    private final AddressRetriever addressRetriever;
    private final AddressAdder addressAdder;
    private final AddressDeleter addressDeleter;
    private final WarehouseRetriever warehouseRetriever;
    private final WarehouseAdder warehouseAdder;
    private final AddressAssigner addressAssigner;
    private final WarehouseUpdater warehouseUpdater;

    public GetAllAddressesResponseDto getAllAddresses(Pageable pageable) {
        List<Address> addresses = addressRetriever.getAllAddresses(pageable);
        return AddressMapper.mapFromListAddressesToGetAllAddressesResponseDto(addresses);
    }

    public GetAddressResponseDto findAddressById(Long id) {
        Address addressById = addressRetriever.findAddressById(id);
        return AddressMapper.mapFromAddressToGetAddressResponseDto(addressById);
    }

    public CreateAddressResponseDto addAddress(CreateAddressRequestDto request) {
        Address address = AddressMapper.mapFromCreateAddressRequestDtoToAddress(request);
        Address savedAddress = addressAdder.addAddress(address);
        return AddressMapper.mapFromAddressToCreateAddressResponseDto(savedAddress);
    }

    public void deleteAddressById(Long id) {
        addressDeleter.deleteById(id);
    }

    public GetAllWarehousesResponseDto getAllWarehouses(Pageable pageable) {
        List<Warehouse> warehouses = warehouseRetriever.getAllWarehouses(pageable);
        return WarehouseMapper.mapFromListWarehousesToGetAllWarehousesResponseDto(warehouses);
    }

    public GetWarehouseResponseDto findWarehouseById(Long id) {
        Warehouse warehouseById = warehouseRetriever.findById(id);
        return WarehouseMapper.mapFromWarehouseToGetWarehouseResponseDto(warehouseById);
    }

    public GetWarehouseResponseDto findWarehouseByName(String name) {
        Warehouse warehouseByName = warehouseRetriever.findByName(name);
        return WarehouseMapper.mapFromWarehouseToGetWarehouseResponseDto(warehouseByName);
    }

    public GetWarehousesByRegionalManagerEmailResponseDto getWarehousesByRegionalManagerEmail(String email) {
        List<Warehouse> warehousesByRegionalManagerEmail = warehouseRetriever.findByRegionalManagerEmail(email);
        return WarehouseMapper.mapFromListWarehousesToGetWarehousesByRegionalManagerEmailResponseDto(
                warehousesByRegionalManagerEmail);
    }

    public GetWarehouseResponseDto findWarehouseByWarehousemanEmail(String email) {
        Warehouse warehouseByWarehousemanEmail = warehouseRetriever.findByWarehousemanEmail(email);
        return WarehouseMapper.mapFromWarehouseToGetWarehouseResponseDto(warehouseByWarehousemanEmail);
    }

    public CreateWarehouseResponseDto addWarehouse(CreateWarehouseRequestDto request) {
        Warehouse warehouse = WarehouseMapper.mapFromCreateWarehouseRequestDtoToWarehouse(request);
        Warehouse savedWarehouse = warehouseAdder.addWarehouse(warehouse);
        return WarehouseMapper.mapFromWarehouseToCreateWarehouseResponseDto(savedWarehouse);
    }

    public AssignAddressToWarehouseResponseDto assignAddressToWarehouse
            (Long warehouseId, Long addressId, boolean activate) {
        Warehouse warehouse = addressAssigner.assignAddressToWarehouse(warehouseId, addressId, activate);
        return WarehouseMapper.mapFromWarehouseToAssignAddressToWarehouseResponseDto(warehouse);
    }

    public UpdateWarehousePartiallyResponseDto updateWarehousePartiallyById(Long warehouseId,
                                                                            UpdateWarehousePartiallyRequestDto request) {
        Warehouse warehouseFromRequest = WarehouseMapper.mapFromUpdateWarehousePartiallyRequestDtoToWarehouse(request);
        warehouseUpdater.updatePartiallyById(warehouseId, warehouseFromRequest);
        Warehouse updatedWarehouse = warehouseRetriever.findById(warehouseId);
        return WarehouseMapper.mapFromWarehouseToUpdateWarehousePartiallyResponseDto(updatedWarehouse);
    }
}
