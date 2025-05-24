package com.github.lpgflow.infrastructure.bdf.bdf;

import com.github.lpgflow.domain.bdf.BdfFacade;
import com.github.lpgflow.domain.bdf.dto.request.AssignCylinderToBdfRequestDto;
import com.github.lpgflow.domain.bdf.dto.request.CreateBdfRequestDto;
import com.github.lpgflow.domain.bdf.dto.request.UpdateCylindersAssignedToBdfRequestDto;
import com.github.lpgflow.domain.bdf.dto.response.CreateBdfResponseDto;
import com.github.lpgflow.domain.bdf.dto.response.GetBdfByIdDto;
import com.github.lpgflow.domain.bdf.dto.response.GetCurrentUserUnorderedBdfs;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bdfs")
class BdfController {

    private final BdfFacade bdfFacade;

    @GetMapping
    public ResponseEntity<GetCurrentUserUnorderedBdfs> getCurrentUserUnorderedBdfs() {
        GetCurrentUserUnorderedBdfs body = bdfFacade.getCurrentUserUnorderedBdfs();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetBdfByIdDto> getBdfById(@PathVariable Long id) {
        GetBdfByIdDto body = bdfFacade.getBdfById(id);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<CreateBdfResponseDto> createBdf(@RequestBody CreateBdfRequestDto request) {
        CreateBdfResponseDto body = bdfFacade.addBdf(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/{bdfId}/cylinders/{cylinderId}")
    public ResponseEntity<Void> assignCylinderToBdf(@PathVariable Long bdfId,
                                                    @PathVariable Long cylinderId,
                                                    @Valid @RequestBody AssignCylinderToBdfRequestDto request) {
        bdfFacade.assignCylindersToBdf(bdfId, cylinderId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{id}/cylinders")
    public ResponseEntity<Void> updateCylindersAssignedToBdf(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCylindersAssignedToBdfRequestDto dto) {
        bdfFacade.updateCylindersAssignedToBdf(id, dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBdfById(@PathVariable Long id) {
        bdfFacade.deleteUnorderedBdfById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
