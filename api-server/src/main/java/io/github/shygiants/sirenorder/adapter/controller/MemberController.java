package io.github.shygiants.sirenorder.adapter.controller;

import io.github.shygiants.sirenorder.adapter.controller.exceptions.BadRequestException;
import io.github.shygiants.sirenorder.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/v1/customers")
    public CreateCustomerResponse createCustomer(@RequestBody CreateCustomerRequest request) {
        try {
            Long createdCustomerId = memberService.createCustomer(request.emailAddress, request.password);

            return new CreateCustomerResponse(createdCustomerId);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e);
        }
    }

    public record CreateCustomerResponse(Long id) {

    }

    public record CreateCustomerRequest(String emailAddress, String password) {

    }
}
