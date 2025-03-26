package com.github.lpgflow.infrastructure.warehouse.warehouse;

import com.github.lpgflow.domain.warehouse.dto.request.CreateWarehouseRequestDto;
import com.github.lpgflow.domain.warehouse.dto.request.UpdateWarehousePartiallyRequestDto;
import com.github.lpgflow.domain.warehouse.dto.response.AssignAddressToWarehouseResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.CreateWarehouseResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetAllWarehousesResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetWarehouseResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetWarehousesByRegionalManagerEmailResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.UpdateWarehousePartiallyResponseDto;
import com.github.lpgflow.domain.warehouse.WarehouseFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/warehouses")
class WarehouseController {

    private final WarehouseFacade warehouseFacade;

    @GetMapping
    public ResponseEntity<GetAllWarehousesResponseDto> getAllWarehouses(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        GetAllWarehousesResponseDto body = warehouseFacade.getAllWarehouses(pageable);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<GetWarehouseResponseDto> getWarehouseById(@PathVariable Long id) {
        GetWarehouseResponseDto body = warehouseFacade.findWarehouseById(id);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<GetWarehouseResponseDto> getWarehouseByName(@PathVariable String name) {
        GetWarehouseResponseDto body = warehouseFacade.findWarehouseByName(name);
        return ResponseEntity.ok(body);
    }

    @GetMapping("regionalManager/{email}")
    public ResponseEntity<GetWarehousesByRegionalManagerEmailResponseDto> getWarehousesByRegionalManagerEmail
            (@PathVariable String email) {
        GetWarehousesByRegionalManagerEmailResponseDto body = warehouseFacade.getWarehousesByRegionalManagerEmail(email);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/warehouseman/{email}")
    public ResponseEntity<GetWarehouseResponseDto> getWarehouseByWarehousemanEmail(@PathVariable String email) {
        GetWarehouseResponseDto body = warehouseFacade.findWarehouseByWarehousemanEmail(email);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<CreateWarehouseResponseDto> postWarehouse(@RequestBody @Valid CreateWarehouseRequestDto request) {
        CreateWarehouseResponseDto body = warehouseFacade.addWarehouse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping("{warehouseId}/address/{addressId}")
    public ResponseEntity<AssignAddressToWarehouseResponseDto> assignAddressToWarehouse
            (@PathVariable Long warehouseId,
             @PathVariable Long addressId,
             @RequestParam(defaultValue = "false") boolean activate) {
        AssignAddressToWarehouseResponseDto body = warehouseFacade.assignAddressToWarehouse(warehouseId, addressId, activate);
        return ResponseEntity.ok(body);
    }

    @PatchMapping("/{warehouseId}")
    public ResponseEntity<UpdateWarehousePartiallyResponseDto> updateWarehousePartially
            (@PathVariable Long warehouseId, @RequestBody UpdateWarehousePartiallyRequestDto request) {
        UpdateWarehousePartiallyResponseDto body = warehouseFacade.updateWarehousePartiallyById(warehouseId, request);
        return ResponseEntity.ok(body);
    }
}
