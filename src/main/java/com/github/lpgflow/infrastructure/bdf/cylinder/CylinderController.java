package com.github.lpgflow.infrastructure.bdf.cylinder;

import com.github.lpgflow.domain.bdf.BdfFacade;
import com.github.lpgflow.domain.bdf.dto.request.CreateCylinderRequestDto;
import com.github.lpgflow.domain.bdf.dto.response.CreateCylinderResponseDto;
import com.github.lpgflow.domain.bdf.dto.response.GetAllCylindersResponseDto;
import com.github.lpgflow.domain.bdf.dto.response.GetCylinderDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cylinders")
class CylinderController {

    private final BdfFacade bdfFacade;

    @GetMapping
    public ResponseEntity<GetAllCylindersResponseDto> getAllCylinders(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        GetAllCylindersResponseDto body = bdfFacade.getAllCylinders(pageable);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetCylinderDto> getCylinderById(@PathVariable Long id) {
        GetCylinderDto body = bdfFacade.getCylinderById(id);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<CreateCylinderResponseDto> postCylinder(@RequestBody @Valid CreateCylinderRequestDto request) {
        CreateCylinderResponseDto body = bdfFacade.addCylinder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
