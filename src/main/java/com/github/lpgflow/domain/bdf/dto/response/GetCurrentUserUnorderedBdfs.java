package com.github.lpgflow.domain.bdf.dto.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record GetCurrentUserUnorderedBdfs(Set<BdfDto> bdfs) {
}
