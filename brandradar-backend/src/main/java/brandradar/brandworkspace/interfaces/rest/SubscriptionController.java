package brandradar.brandworkspace.interfaces.rest;

import brandradar.brandworkspace.application.commands.UpdateBrandWorkspaceCommand;
import brandradar.brandworkspace.application.commandservices.BrandWorkspaceCommandService;
import brandradar.brandworkspace.domain.model.aggregates.Subscription;
import brandradar.brandworkspace.domain.model.repositories.SubscriptionRepository;
import brandradar.brandworkspace.interfaces.rest.resources.PaySubscriptionResource;
import brandradar.brandworkspace.interfaces.rest.resources.SubscriptionPlanResource;
import brandradar.brandworkspace.interfaces.rest.resources.SubscriptionResource;
import brandradar.shared.infrastructure.security.OwnershipGuard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
@Tag(name = "Subscription", description = "Planes y suscripción SIMULADA — no procesa pagos reales, " +
        "nunca guarda número de tarjeta ni CVC completos")
public class SubscriptionController {

    private static final List<SubscriptionPlanResource> PLANS = List.of(
            new SubscriptionPlanResource("FREE", "Básico", 0, 0,
                    List.of("YOUTUBE", "TWITTER", "REDDIT", "TIKTOK"), 1,
                    List.of("4 canales de análisis", "1 workspace", "Alertas básicas")),
            new SubscriptionPlanResource("PRO", "Pro", 49.90, 479.00,
                    List.of("YOUTUBE", "FACEBOOK", "TWITTER", "TIKTOK", "INSTAGRAM", "GOOGLE_NEWS", "REDDIT", "BLOGS"), 2,
                    List.of("8 canales de análisis", "2 workspaces", "Reportes ilimitados", "Soporte prioritario")),
            new SubscriptionPlanResource("ENTERPRISE", "Enterprise", 149.90, 1439.00,
                    List.of("YOUTUBE", "FACEBOOK", "TWITTER", "TIKTOK", "INSTAGRAM", "GOOGLE_NEWS", "REDDIT", "BLOGS"), 10,
                    List.of("8 canales de análisis", "Workspaces ilimitados", "Reportes ilimitados", "Soporte dedicado"))
    );

    private final SubscriptionRepository subscriptionRepository;
    private final BrandWorkspaceCommandService workspaceCommandService;
    private final OwnershipGuard ownershipGuard;

    public SubscriptionController(SubscriptionRepository subscriptionRepository,
                                  BrandWorkspaceCommandService workspaceCommandService,
                                  OwnershipGuard ownershipGuard) {
        this.subscriptionRepository = subscriptionRepository;
        this.workspaceCommandService = workspaceCommandService;
        this.ownershipGuard = ownershipGuard;
    }

    @Operation(summary = "Get the static catalog of subscription plans")
    @GetMapping("/api/v1/subscription-plans")
    public ResponseEntity<List<SubscriptionPlanResource>> getPlans() {
        return ResponseEntity.ok(PLANS);
    }

    @Operation(summary = "Get the current subscription for a workspace")
    @GetMapping("/api/v1/workspaces/{workspaceId}/subscription")
    public ResponseEntity<SubscriptionResource> getSubscription(@PathVariable Long workspaceId) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);
        return subscriptionRepository.findByWorkspaceId(workspaceId)
                .map(this::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Pay for a subscription plan (SIMULADO — nunca se procesa dinero real, " +
            "el número de tarjeta y el CVC se validan y se descartan de inmediato, nunca se guardan)")
    @PostMapping("/api/v1/workspaces/{workspaceId}/subscription")
    public ResponseEntity<SubscriptionResource> paySubscription(
            @PathVariable Long workspaceId,
            @Valid @RequestBody PaySubscriptionResource resource) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);

        if (!List.of("PRO", "ENTERPRISE").contains(resource.plan().toUpperCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Plan must be PRO or ENTERPRISE");
        }

        // Validación básica de formato — el número completo y el CVC se descartan
        // inmediatamente después de esta validación, nunca se persisten ni se loguean.
        String digitsOnly = resource.cardNumber().replaceAll("\\s", "");
        if (digitsOnly.length() < 13 || digitsOnly.length() > 19 || !digitsOnly.matches("\\d+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid card number format");
        }
        if (!resource.cvc().matches("\\d{3,4}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CVC format");
        }

        String last4 = digitsOnly.substring(digitsOnly.length() - 4);
        String brand = detectCardBrand(digitsOnly);

        // Simulación: siempre exitoso. Aquí es donde en el futuro se conectaría una
        // pasarela real (Stripe/Culqi/MercadoPago) para procesar el cobro de verdad.
        var existing = subscriptionRepository.findByWorkspaceId(workspaceId);
        var subscription = Subscription.create(workspaceId, resource.plan().toUpperCase(),
                resource.billingPeriod().toUpperCase(), last4, brand);
        var saved = subscriptionRepository.save(subscription);

        workspaceCommandService.handle(new UpdateBrandWorkspaceCommand(workspaceId, null, resource.plan().toUpperCase()));

        return ResponseEntity.status(HttpStatus.CREATED).body(toResource(saved));
    }

    @Operation(summary = "Cancel the subscription and downgrade the workspace to FREE")
    @PostMapping("/api/v1/workspaces/{workspaceId}/subscription/cancel")
    public ResponseEntity<SubscriptionResource> cancelSubscription(@PathVariable Long workspaceId) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);

        var subscription = subscriptionRepository.findByWorkspaceId(workspaceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No active subscription found"));

        var canceled = subscriptionRepository.save(subscription.cancel());
        workspaceCommandService.handle(new UpdateBrandWorkspaceCommand(workspaceId, null, "FREE"));

        return ResponseEntity.ok(toResource(canceled));
    }

    private String detectCardBrand(String digitsOnly) {
        if (digitsOnly.startsWith("4")) return "VISA";
        if (digitsOnly.startsWith("5")) return "MASTERCARD";
        if (digitsOnly.startsWith("3")) return "AMEX";
        return "UNKNOWN";
    }

    private SubscriptionResource toResource(Subscription s) {
        return new SubscriptionResource(s.getId(), s.getWorkspaceId(), s.getPlan(), s.getBillingPeriod(),
                s.getStatus(), s.getFakeCardLast4(), s.getFakeCardBrand(), s.getStartedAt(), s.getRenewsAt());
    }
}