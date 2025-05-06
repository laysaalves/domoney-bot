package dev.layseiras.domoney.dto;

import dev.layseiras.domoney.dto.enums.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExternalApiDTO
        (UUID id,
         String title,
         BigDecimal price,
         PaymentType paymentType,
         PaymentMethod method,
         PaymentStatus status,
         LocalDateTime createdAt,
         LocalDateTime updatedAt,
         LocalDateTime deletedAt) {}

