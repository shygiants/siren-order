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
    public CreateMemberResponse createCustomer(@RequestBody CreateMemberRequest request) {
        try {
            Long createdCustomerId = memberService.createCustomer(request.emailAddress, request.password);

            return new CreateMemberResponse(createdCustomerId);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e);
        }
    }

    @PostMapping("/api/v1/owners")
    public CreateMemberResponse createOwner(@RequestBody CreateMemberRequest request) {
        try {
            Long createdOwnerId = memberService.createOwner(request.emailAddress, request.password);

            return new CreateMemberResponse(createdOwnerId);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e);
        }
    }

    public record CreateMemberResponse(Long id) {

    }

    public record CreateMemberRequest(String emailAddress, String password) {

    }
}
