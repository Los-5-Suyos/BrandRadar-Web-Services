package brandradar.brandworkspace.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record PaySubscriptionResource(
        @NotBlank String plan,          // PRO, ENTERPRISE
        @NotBlank String billingPeriod, // MENSUAL, ANUAL
        @NotBlank String cardName,
        @NotBlank String cardNumber,    // se valida y se descarta, NUNCA se guarda completo
        @NotBlank String expiry,
        @NotBlank String cvc            // se valida y se descarta, NUNCA se guarda ni se loguea
) {}