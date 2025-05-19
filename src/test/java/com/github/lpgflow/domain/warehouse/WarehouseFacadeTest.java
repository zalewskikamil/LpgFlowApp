package com.github.lpgflow.domain.warehouse;

import com.github.lpgflow.domain.user.UserFacade;
import com.github.lpgflow.domain.util.enums.UserRole;
import com.github.lpgflow.domain.user.dto.response.GetUserWithDetailsResponseDto;
import com.github.lpgflow.domain.user.dto.response.RoleDto;
import com.github.lpgflow.domain.user.dto.response.UserWithDetailsDto;
import com.github.lpgflow.domain.warehouse.dto.request.CreateAddressRequestDto;
import com.github.lpgflow.domain.warehouse.dto.request.CreateWarehouseRequestDto;
import com.github.lpgflow.domain.warehouse.dto.request.UpdateWarehousePartiallyRequestDto;
import com.github.lpgflow.domain.warehouse.dto.response.AddressDto;
import com.github.lpgflow.domain.warehouse.dto.response.AssignAddressToWarehouseResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.CreateAddressResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.CreateWarehouseResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetAddressResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetAllAddressesResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetWarehouseResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetWarehousesByRegionalManagerEmailResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.UpdateWarehousePartiallyResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.WarehouseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WarehouseFacadeTest {

    UserFacade userFacade = mock(UserFacade.class);

    WarehouseFacade warehouseFacade = WarehouseFacadeConfiguration.createWarehouseFacade(
            new InMemoryWarehouseRepository(),
            new InMemoryAddressRepository(),
            userFacade);

    @Test
    @DisplayName("Should return  2 addresses")
    public void should_return_two_addressees() {
        // given
        assertThat(warehouseFacade.getAllAddresses(Pageable.unpaged()).addresses()).isEmpty();
        String street1 = "Street 1";
        String city1 = "City1";
        String postalCode1 = "00-001";
        CreateAddressRequestDto request1 = CreateAddressRequestDto.builder()
                .street(street1)
                .city(city1)
                .postalCode(postalCode1)
                .build();
        CreateAddressResponseDto createAddressResponseDto1 = warehouseFacade.addAddress(request1);
        Long address1Id = createAddressResponseDto1.address().id();
        String street2 = "Street 2";
        String city2 = "City2";
        String postalCode2 = "00-002";
        CreateAddressRequestDto request2 = CreateAddressRequestDto.builder()
                .street(street2)
                .city(city2)
                .postalCode(postalCode2)
                .build();
        CreateAddressResponseDto createAddressResponseDto2 = warehouseFacade.addAddress(request2);
        Long address2Id = createAddressResponseDto2.address().id();
        // when
        GetAllAddressesResponseDto result = warehouseFacade.getAllAddresses(Pageable.unpaged());
        // then
        assertThat(result.addresses()).hasSize(2);
        AddressDto addressDto = AddressDto.builder()
                .id(address1Id)
                .street(street1)
                .city(city1)
                .postalCode(postalCode1)
                .build();
        assertThat(result.addresses()).contains(addressDto);
    }

    @Test
    @DisplayName("Should return address by id When id was exist in db")
    public void should_return_address_by_id_when_id_was_exist_in_db() {
        // given
        assertThat(warehouseFacade.getAllAddresses(Pageable.unpaged()).addresses()).isEmpty();
        String street = "Street 1";
        String city = "City";
        String postalCode = "00-001";
        CreateAddressRequestDto request = CreateAddressRequestDto.builder()
                .street(street)
                .city(city)
                .postalCode(postalCode)
                .build();
        CreateAddressResponseDto createAddressResponseDto = warehouseFacade.addAddress(request);
        Long addressId = createAddressResponseDto.address().id();
        // when
        GetAddressResponseDto result = warehouseFacade.findAddressById(addressId);
        // then
        AddressDto addressDto = AddressDto.builder()
                .id(addressId)
                .street(street)
                .city(city)
                .postalCode(postalCode)
                .build();
        assertThat(result).isEqualTo(new GetAddressResponseDto(addressDto));
    }

    @Test
    @DisplayName("Should throw exception AddressNotFound When id was not exist in db")
    public void should_throw_exception_address_not_found_when_id_was_not_exist_in_db() {
        // given
        assertThat(warehouseFacade.getAllAddresses(Pageable.unpaged()).addresses()).isEmpty();
        Long addressId = 1L;
        // when
        Throwable throwable = catchThrowable(() -> warehouseFacade.findAddressById(addressId));
        // then
        assertThat(throwable).isInstanceOf(AddressNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Address with id: " + addressId + " not found");
    }

    @Test
    @DisplayName("Should add address with id: 1")
    public void should_add_address_with_id_one() {
        // given
        assertThat(warehouseFacade.getAllAddresses(Pageable.unpaged()).addresses()).isEmpty();
        String street = "Street 1";
        String city = "City";
        String postalCode = "00-001";
        CreateAddressRequestDto request = CreateAddressRequestDto.builder()
                .street(street)
                .city(city)
                .postalCode(postalCode)
                .build();
        // when
        CreateAddressResponseDto result = warehouseFacade.addAddress(request);
        // then
        AddressDto addressDto = AddressDto.builder()
                .id(1L)
                .street(street)
                .city(city)
                .postalCode(postalCode)
                .build();
        assertThat(result).isEqualTo(new CreateAddressResponseDto(addressDto));
        assertThat(warehouseFacade.getAllAddresses(Pageable.unpaged()).addresses()).hasSize(1);
    }

    @Test
    @DisplayName("Should delete address by id When id was exist in db and was not assigned to any warehouse")
    public void should_delete_address_by_id_when_id_was_exist_in_db_and_was_not_assigned_to_any_warehouse() {
        // given
        assertThat(warehouseFacade.getAllAddresses(Pageable.unpaged()).addresses()).isEmpty();
        String street = "Street 1";
        String city = "City";
        String postalCode = "00-001";
        CreateAddressRequestDto request = CreateAddressRequestDto.builder()
                .street(street)
                .city(city)
                .postalCode(postalCode)
                .build();
        CreateAddressResponseDto createAddressResponseDto = warehouseFacade.addAddress(request);
        Long addressId = createAddressResponseDto.address().id();
        assertThat(warehouseFacade.getAllAddresses(Pageable.unpaged()).addresses()).hasSize(1);
        // when
        warehouseFacade.deleteAddressById(addressId);
        // then
        assertThat(warehouseFacade.getAllAddresses(Pageable.unpaged()).addresses()).isEmpty();
    }

    @Test
    @DisplayName("Should throw exception AddressInUse When id was assigned to warehouse")
    public void should_throw_exception_address_in_use_when_id_was_assigned_to_warehouse() {
        // given
        assertThat(warehouseFacade.getAllAddresses(Pageable.unpaged()).addresses()).isEmpty();
        String street = "Street 1";
        String city = "City1";
        String postalCode = "00-001";
        CreateAddressRequestDto createAddressRequest = CreateAddressRequestDto.builder()
                .street(street)
                .city(city)
                .postalCode(postalCode)
                .build();
        CreateAddressResponseDto createAddressResponseDto = warehouseFacade.addAddress(createAddressRequest);
        Long addressId = createAddressResponseDto.address().id();
        assertThat(warehouseFacade.getAllWarehouses(Pageable.unpaged()).warehouses()).isEmpty();
        String name = "name";
        String regionalManagerEmail = "manager@test.pl";
        String warehousemanEmail = "warehouseman@test.pl";
        String bdfSize = "Large";
        int maxCylindersWithoutCollarPerBdf = 50;
        CreateWarehouseRequestDto createWarehouseRequest = CreateWarehouseRequestDto.builder()
                .name(name)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehousemanEmail)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .build();
        mockUserFacadeToReturnRegionalManagerRole(regionalManagerEmail);
        mockUserFacadeToReturnWarehousemanRole(warehousemanEmail);
        CreateWarehouseResponseDto createWarehouseResponseDto = warehouseFacade.addWarehouse(createWarehouseRequest);
        Long warehouseId = createWarehouseResponseDto.warehouse().id();
        assertThat(warehouseFacade.getAllWarehouses(Pageable.unpaged()).warehouses()).hasSize(1);
        warehouseFacade.assignAddressToWarehouse(warehouseId, addressId, true);
        // when
        Throwable throwable = catchThrowable(() -> warehouseFacade.deleteAddressById(addressId));
        // then
        assertThat(throwable).isInstanceOf(AddressInUseException.class);
        assertThat(throwable.getMessage()).isEqualTo("Address with id: " + warehouseId + " is still in use");
    }

    private void mockUserFacadeToReturnRegionalManagerRole(String regionalManagerEmail) {
        when(userFacade.findUserWithDetailsByEmail(regionalManagerEmail))
                .thenReturn(new GetUserWithDetailsResponseDto(
                        UserWithDetailsDto.builder()
                                .roles(Set.of(RoleDto.builder()
                                        .name(UserRole.REGIONAL_MANAGER.name())
                                        .build()))
                                .build()
                ));
    }

    private void mockUserFacadeToReturnWarehousemanRole(String warehousemanEmail) {
        when(userFacade.findUserWithDetailsByEmail(warehousemanEmail))
                .thenReturn(new GetUserWithDetailsResponseDto(
                        UserWithDetailsDto.builder()
                                .roles(Set.of(RoleDto.builder()
                                        .name(UserRole.WAREHOUSEMAN.name())
                                        .build()))
                                .build()
                ));
    }

    @Test
    @DisplayName("Should return warehouse by id When id was exist in db")
    public void should_return_warehouse_by_id_when_id_was_exist_in_db() {
        // given
        assertThat(warehouseFacade.getAllWarehouses(Pageable.unpaged()).warehouses()).isEmpty();
        String name = "name";
        String regionalManagerEmail = "manager@test.pl";
        String warehousemanEmail = "warehouseman@test.pl";
        String bdfSize = "LARGE";
        int maxCylindersWithoutCollarPerBdf = 50;
        CreateWarehouseRequestDto createWarehouseRequest = CreateWarehouseRequestDto.builder()
                .name(name)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehousemanEmail)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .build();
        mockUserFacadeToReturnRegionalManagerRole(regionalManagerEmail);
        mockUserFacadeToReturnWarehousemanRole(warehousemanEmail);
        CreateWarehouseResponseDto createWarehouseResponseDto = warehouseFacade.addWarehouse(createWarehouseRequest);
        Long warehouseId = createWarehouseResponseDto.warehouse().id();
        // when
        GetWarehouseResponseDto result = warehouseFacade.findWarehouseById(warehouseId);
        // then
        WarehouseDto warehouseDto = WarehouseDto.builder()
                .id(warehouseId)
                .name(name)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehousemanEmail)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .active(false)
                .build();
        assertThat(result).isEqualTo(new GetWarehouseResponseDto(warehouseDto));
    }

    @Test
    @DisplayName("Should return warehouse by name When name was exist in db")
    public void should_return_warehouse_by_name_when_name_was_exist_in_db() {
        // given
        assertThat(warehouseFacade.getAllWarehouses(Pageable.unpaged()).warehouses()).isEmpty();
        String name = "name";
        String regionalManagerEmail = "manager@test.pl";
        String warehousemanEmail = "warehouseman@test.pl";
        String bdfSize = "LARGE";
        int maxCylindersWithoutCollarPerBdf = 50;
        CreateWarehouseRequestDto createWarehouseRequest = CreateWarehouseRequestDto.builder()
                .name(name)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehousemanEmail)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .build();
        mockUserFacadeToReturnRegionalManagerRole(regionalManagerEmail);
        mockUserFacadeToReturnWarehousemanRole(warehousemanEmail);
        CreateWarehouseResponseDto createWarehouseResponseDto = warehouseFacade.addWarehouse(createWarehouseRequest);
        Long warehouseId = createWarehouseResponseDto.warehouse().id();
        // when
        GetWarehouseResponseDto result = warehouseFacade.findWarehouseByName(name);
        // then
        WarehouseDto warehouseDto = WarehouseDto.builder()
                .id(warehouseId)
                .name(name)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehousemanEmail)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .active(false)
                .build();
        assertThat(result).isEqualTo(new GetWarehouseResponseDto(warehouseDto));
    }

    @Test
    @DisplayName("Should return 2 warehouses by regional_manager_email")
    public void should_return_two_warehouses_by_regional_manager_email() {
        // given
        assertThat(warehouseFacade.getAllWarehouses(Pageable.unpaged()).warehouses()).isEmpty();
        String name1 = "name1";
        String regionalManagerEmail = "manager@test.pl";
        String warehouseman1Email = "warehouseman1@test.pl";
        String bdfSize = "MEDIUM";
        int maxCylindersWithoutCollarPerBdf = 50;
        CreateWarehouseRequestDto createWarehouse1Request = CreateWarehouseRequestDto.builder()
                .name(name1)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehouseman1Email)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .build();
        mockUserFacadeToReturnRegionalManagerRole(regionalManagerEmail);
        mockUserFacadeToReturnWarehousemanRole(warehouseman1Email);
        CreateWarehouseResponseDto createWarehouse1ResponseDto = warehouseFacade.addWarehouse(createWarehouse1Request);
        Long warehouse1Id = createWarehouse1ResponseDto.warehouse().id();
        String name2 = "name2";
        String warehouseman2Email = "warehouseman2@test.pl";
        CreateWarehouseRequestDto createWarehouse2Request = CreateWarehouseRequestDto.builder()
                .name(name2)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehouseman2Email)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .build();
        mockUserFacadeToReturnWarehousemanRole(warehouseman2Email);
        CreateWarehouseResponseDto createWarehouse2ResponseDto = warehouseFacade.addWarehouse(createWarehouse2Request);
        // when
        GetWarehousesByRegionalManagerEmailResponseDto result =
                warehouseFacade.getWarehousesByRegionalManagerEmail(regionalManagerEmail);
        // then
        assertThat(result.warehouses()).hasSize(2);
        WarehouseDto warehouseDto = WarehouseDto.builder()
                .id(warehouse1Id)
                .name(name1)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehouseman1Email)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .active(false)
                .build();
        assertThat(result.warehouses()).contains(warehouseDto);
    }

    @Test
    @DisplayName("Should return warehouse by warehouseman email When warehouse with warehouseman email was exist in db")
    public void should_return_warehouse_by_warehouseman_email_when_warehouse_with_warehouseman_email_was_exist_in_db() {
        // given
        assertThat(warehouseFacade.getAllWarehouses(Pageable.unpaged()).warehouses()).isEmpty();
        String name = "name";
        String regionalManagerEmail = "manager@test.pl";
        String warehousemanEmail = "warehouseman@test.pl";
        String bdfSize = "LARGE";
        int maxCylindersWithoutCollarPerBdf = 50;
        CreateWarehouseRequestDto createWarehouseRequest = CreateWarehouseRequestDto.builder()
                .name(name)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehousemanEmail)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .build();
        mockUserFacadeToReturnRegionalManagerRole(regionalManagerEmail);
        mockUserFacadeToReturnWarehousemanRole(warehousemanEmail);
        CreateWarehouseResponseDto createWarehouseResponseDto = warehouseFacade.addWarehouse(createWarehouseRequest);
        Long warehouseId = createWarehouseResponseDto.warehouse().id();
        // when
        GetWarehouseResponseDto result = warehouseFacade.findWarehouseByWarehousemanEmail(warehousemanEmail);
        // then
        WarehouseDto warehouseDto = WarehouseDto.builder()
                .id(warehouseId)
                .name(name)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehousemanEmail)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .active(false)
                .build();
        assertThat(result).isEqualTo(new GetWarehouseResponseDto(warehouseDto));
    }

    @Test
    @DisplayName("Should add warehouse with id: 1")
    public void should_add_warehouse_with_id_one() {
        // given
        assertThat(warehouseFacade.getAllWarehouses(Pageable.unpaged()).warehouses()).isEmpty();
        String name = "name";
        String regionalManagerEmail = "manager@test.pl";
        String warehousemanEmail = "warehouseman@test.pl";
        String bdfSize = "LARGE";
        int maxCylindersWithoutCollarPerBdf = 50;
        CreateWarehouseRequestDto createWarehouseRequest = CreateWarehouseRequestDto.builder()
                .name(name)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehousemanEmail)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .build();
        mockUserFacadeToReturnRegionalManagerRole(regionalManagerEmail);
        mockUserFacadeToReturnWarehousemanRole(warehousemanEmail);
        // when
        CreateWarehouseResponseDto result = warehouseFacade.addWarehouse(createWarehouseRequest);
        // then
        WarehouseDto warehouseDto = WarehouseDto.builder()
                .id(1L)
                .name(name)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehousemanEmail)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .active(false)
                .build();
        assertThat(result).isEqualTo(new CreateWarehouseResponseDto(warehouseDto));
        assertThat(warehouseFacade.getAllWarehouses(Pageable.unpaged()).warehouses()).hasSize(1);
    }

    @Test
    @DisplayName("Should throw exception WarehouseParameter When warehouseman was already assigned to warehouse")
    public void should_throw_exception_warehouse_parameter_when_warehouseman_email_was_already_assigned_to_warehouse() {
        // given
        assertThat(warehouseFacade.getAllWarehouses(Pageable.unpaged()).warehouses()).isEmpty();
        String name1 = "name1";
        String regionalManagerEmail = "manager@test.pl";
        String warehousemanEmail = "warehouseman@test.pl";
        String bdfSize = "LARGE";
        int maxCylindersWithoutCollarPerBdf = 50;
        CreateWarehouseRequestDto createWarehouse1Request = CreateWarehouseRequestDto.builder()
                .name(name1)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehousemanEmail)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .build();
        mockUserFacadeToReturnRegionalManagerRole(regionalManagerEmail);
        mockUserFacadeToReturnWarehousemanRole(warehousemanEmail);
        warehouseFacade.addWarehouse(createWarehouse1Request);
        assertThat(warehouseFacade.findWarehouseByWarehousemanEmail(warehousemanEmail).warehouse().name())
                .isEqualTo(name1);
        String name2 = "name2";
        CreateWarehouseRequestDto createWarehouse2Request = CreateWarehouseRequestDto.builder()
                .name(name2)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehousemanEmail)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .build();
        // when
        Throwable throwable = catchThrowable(() -> warehouseFacade.addWarehouse(createWarehouse2Request));
        // then
        assertThat(throwable).isInstanceOf(WarehouseParameterException.class);
        assertThat(throwable.getMessage()).isEqualTo("Warehouseman with email: " + warehousemanEmail +
                " already has a warehouse assigned");
    }

    @Test
    @DisplayName("Should assign address to warehouse")
    public void should_assign_address_to_warehouse() {
        // given
        assertThat(warehouseFacade.getAllAddresses(Pageable.unpaged()).addresses()).isEmpty();
        String street = "Street 1";
        String city = "City1";
        String postalCode = "00-001";
        CreateAddressRequestDto createAddressRequest = CreateAddressRequestDto.builder()
                .street(street)
                .city(city)
                .postalCode(postalCode)
                .build();
        CreateAddressResponseDto createAddressResponseDto = warehouseFacade.addAddress(createAddressRequest);
        Long addressId = createAddressResponseDto.address().id();
        assertThat(warehouseFacade.getAllWarehouses(Pageable.unpaged()).warehouses()).isEmpty();
        String name = "name";
        String regionalManagerEmail = "manager@test.pl";
        String warehousemanEmail = "warehouseman@test.pl";
        String bdfSize = "LARGE";
        int maxCylindersWithoutCollarPerBdf = 50;
        CreateWarehouseRequestDto createWarehouseRequest = CreateWarehouseRequestDto.builder()
                .name(name)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehousemanEmail)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .build();
        mockUserFacadeToReturnRegionalManagerRole(regionalManagerEmail);
        mockUserFacadeToReturnWarehousemanRole(warehousemanEmail);
        CreateWarehouseResponseDto createWarehouseResponseDto = warehouseFacade.addWarehouse(createWarehouseRequest);
        Long warehouseId = createWarehouseResponseDto.warehouse().id();
        assertThat(warehouseFacade.getAllWarehouses(Pageable.unpaged()).warehouses()).hasSize(1);
        // when
        AssignAddressToWarehouseResponseDto result =
                warehouseFacade.assignAddressToWarehouse(warehouseId, addressId, false);
        // then
        AddressDto addressDto = AddressDto.builder()
                .id(addressId)
                .street(street)
                .city(city)
                .postalCode(postalCode)
                .build();
        WarehouseDto warehouseDto = WarehouseDto.builder()
                .id(warehouseId)
                .name(name)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehousemanEmail)
                .address(addressDto)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .active(false)
                .build();
        assertThat(result).isEqualTo(new AssignAddressToWarehouseResponseDto(warehouseDto));
        assertThat(warehouseFacade.findWarehouseById(warehouseId).warehouse().address()).isEqualTo(addressDto);
    }

    @Test
    @DisplayName("Should partially update warehouse")
    public void should_partially_update_warehouse() {
        // given
        assertThat(warehouseFacade.getAllWarehouses(Pageable.unpaged()).warehouses()).isEmpty();
        String name = "name";
        String regionalManagerEmail = "manager@test.pl";
        String warehousemanEmail = "warehouseman@test.pl";
        String bdfSize = "LARGE";
        int maxCylindersWithoutCollarPerBdf = 50;
        CreateWarehouseRequestDto createWarehouseRequest = CreateWarehouseRequestDto.builder()
                .name(name)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehousemanEmail)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(maxCylindersWithoutCollarPerBdf)
                .build();
        mockUserFacadeToReturnRegionalManagerRole(regionalManagerEmail);
        mockUserFacadeToReturnWarehousemanRole(warehousemanEmail);
        CreateWarehouseResponseDto createWarehouseResponseDto = warehouseFacade.addWarehouse(createWarehouseRequest);
        Long warehouseId = createWarehouseResponseDto.warehouse().id();
        assertThat(warehouseFacade.getAllWarehouses(Pageable.unpaged()).warehouses()).hasSize(1);
        String updatedRegionalManagerEmail = "updatedmanager@test.pl";
        String updatedWarehousemanEmail = "updatedwarehouseman@test.pl";
        int updatedMaxCylindersWithoutCollarPerBdf = 100;
        boolean updatedActive = true;
        UpdateWarehousePartiallyRequestDto updateRequest = UpdateWarehousePartiallyRequestDto.builder()
                .regionalManagerEmail(updatedRegionalManagerEmail)
                .warehousemanEmail(updatedWarehousemanEmail)
                .maxCylindersWithoutCollarPerBdf(updatedMaxCylindersWithoutCollarPerBdf)
                .active(updatedActive)
                .build();
        mockUserFacadeToReturnRegionalManagerRole(updatedRegionalManagerEmail);
        mockUserFacadeToReturnWarehousemanRole(updatedWarehousemanEmail);
        // when
        UpdateWarehousePartiallyResponseDto result =
                warehouseFacade.updateWarehousePartiallyById(warehouseId, updateRequest);
        // then
        WarehouseDto updatedWarehouseDto = WarehouseDto.builder()
                .id(warehouseId)
                .name(name)
                .regionalManagerEmail(updatedRegionalManagerEmail)
                .warehousemanEmail(updatedWarehousemanEmail)
                .bdfSize(bdfSize)
                .maxCylindersWithoutCollarPerBdf(updatedMaxCylindersWithoutCollarPerBdf)
                .active(updatedActive)
                .build();
        assertThat(result).isEqualTo(new UpdateWarehousePartiallyResponseDto(updatedWarehouseDto));
    }
}