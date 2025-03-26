package com.github.lpgflow.domain.bdf;

import com.github.lpgflow.domain.bdf.dto.request.AssignCylinderToBdfRequestDto;
import com.github.lpgflow.domain.bdf.dto.request.CreateBdfRequestDto;
import com.github.lpgflow.domain.bdf.dto.request.CreateCylinderRequestDto;
import com.github.lpgflow.domain.bdf.dto.request.UpdateCylindersAssignedToBdfRequestDto;
import com.github.lpgflow.domain.bdf.dto.response.CreateBdfResponseDto;
import com.github.lpgflow.domain.bdf.dto.response.CreateCylinderResponseDto;
import com.github.lpgflow.domain.bdf.dto.response.GetAllCylindersResponseDto;
import com.github.lpgflow.domain.bdf.dto.response.GetBdfByIdDto;
import com.github.lpgflow.domain.bdf.dto.response.GetCurrentUserUnorderedBdfs;
import com.github.lpgflow.domain.bdf.dto.response.GetCylinderDto;
import com.github.lpgflow.infrastructure.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BdfFacade {

    private final CylinderRetriever cylinderRetriever;
    private final CylinderAdder cylinderAdder;
    private final BdfRetriever bdfRetriever;
    private final BdfAdder bdfAdder;
    private final BdfDeleter bdfDeleter;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final CylinderAssigner cylinderAssigner;
    private final BdfUpdater bdfUpdater;

    public GetAllCylindersResponseDto getAllCylinders(Pageable pageable) {
        List<Cylinder> cylinders = cylinderRetriever.getAllCylinders(pageable);
        return CylinderMapper.mapFromListCylindersToGetAllCylindersResponseDto(cylinders);
    }

    public GetCylinderDto getCylinderById(Long id) {
        Cylinder cylinderById = cylinderRetriever.findById(id);
        return CylinderMapper.mapFromCylinderToGetCylinderDto(cylinderById);
    }

    public CreateCylinderResponseDto addCylinder(CreateCylinderRequestDto request) {
        Cylinder cylinder = CylinderMapper.mapFromCreateCylinderRequestDtoToCylinder(request);
        Cylinder savedCylinder = cylinderAdder.addCylinder(cylinder);
        return CylinderMapper.mapFromCylinderToCreateCylinderResponseDto(savedCylinder);
    }

    public GetBdfByIdDto getBdfById(Long id) {
        Bdf bdfById = bdfRetriever.findById(id);
        return BdfMapper.mapFromBdfToGetBdfByIdDto(bdfById);
    }

    public GetCurrentUserUnorderedBdfs getCurrentUserUnorderedBdfs() {
        String currentUserEmail = authenticatedUserProvider.getCurrentUserName();
        Set<Bdf> bdfs = bdfRetriever.findUnorderedBdfsByUserEmail(currentUserEmail);
        return BdfMapper.mapFromBdfsToGetCurrentUserUnorderedBdfsDto(bdfs);
    }

    public CreateBdfResponseDto addBdf(CreateBdfRequestDto request) {
        String currentUserEmail = authenticatedUserProvider.getCurrentUserName();
        Bdf bdf = BdfMapper.mapFromCreateBdfRequestDtoToBdf(request, currentUserEmail);
        Bdf savedBdf = bdfAdder.addBdf(bdf);
        return BdfMapper.mapFromBdfToCreateBdfResponseDto(savedBdf);
    }

    public void deleteUnorderedBdfById(Long id) {
        String currentUserEmail = authenticatedUserProvider.getCurrentUserName();
        bdfDeleter.deleteUnorderedBdfById(id, currentUserEmail);
    }

    public void assignCylindersToBdf(Long bdfId, Long cylinderId, AssignCylinderToBdfRequestDto request) {
        int cylindersQuantity = request.newQuantity();
        cylinderAssigner.assignCylindersToBdf(bdfId, cylinderId, cylindersQuantity);
    }

    public void updateCylindersAssignedToBdf(Long bdfId, UpdateCylindersAssignedToBdfRequestDto request) {
        long cylinderId = request.cylinderId();
        int cylindersNewQuantity = request.newQuantity();
        bdfUpdater.updateBdfPartiallyById(bdfId, cylinderId, cylindersNewQuantity);
    }

    public void setOrderedStatus(Long bdfId, boolean ordered) {
        bdfUpdater.setOrderedStatus(bdfId, ordered);
    }
}
