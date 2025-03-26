package com.github.lpgflow.domain.warehouse;

import com.github.lpgflow.domain.user.UserFacade;

class WarehouseFacadeConfiguration {

    public static WarehouseFacade createWarehouseFacade(final WarehouseRepository warehouseRepository,
                                                        final AddressRepository addressRepository,
                                                        final UserFacade userFacade) {
        AddressRetriever addressRetriever = new AddressRetriever(addressRepository);
        AddressAdder addressAdder = new AddressAdder(addressRepository);
        WarehouseRetriever warehouseRetriever = new WarehouseRetriever(warehouseRepository);
        AddressDeleter addressDeleter = new AddressDeleter(addressRepository, addressRetriever, warehouseRetriever);
        WarehouseParametersValidator warehouseValidator = new WarehouseParametersValidator(userFacade, warehouseRetriever);
        WarehouseAdder warehouseAdder = new WarehouseAdder(warehouseRepository, warehouseValidator);
        AddressAssigner addressAssigner = new AddressAssigner(addressRetriever, warehouseRetriever);
        WarehouseUpdater warehouseUpdater = new WarehouseUpdater(warehouseRepository, warehouseRetriever,
                warehouseValidator);
        return new WarehouseFacade(
                addressRetriever,
                addressAdder,
                addressDeleter,
                warehouseRetriever,
                warehouseAdder,
                addressAssigner,
                warehouseUpdater);
    }
}
