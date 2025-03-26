package com.github.lpgflow.infrastructure.warehouse.adress;

import com.github.lpgflow.domain.warehouse.dto.request.CreateAddressRequestDto;
import com.github.lpgflow.domain.warehouse.dto.response.CreateAddressResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetAddressResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetAllAddressesResponseDto;
import com.github.lpgflow.domain.warehouse.WarehouseFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/addresses")
class AddressController {

    private final WarehouseFacade warehouseFacade;

    @GetMapping
    public ResponseEntity<GetAllAddressesResponseDto> getAllAddress(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        GetAllAddressesResponseDto response = warehouseFacade.getAllAddresses(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id")
    public ResponseEntity<GetAddressResponseDto> getAddressById(@RequestParam Long id) {
        GetAddressResponseDto response = warehouseFacade.findAddressById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CreateAddressResponseDto> postAddress(@RequestBody @Valid CreateAddressRequestDto request) {
        CreateAddressResponseDto body = warehouseFacade.addAddress(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        warehouseFacade.deleteAddressById(addressId);
        return ResponseEntity.noContent().build();
    }
}
