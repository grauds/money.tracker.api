package org.clematis.mt.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.clematis.mt.dto.MonthlyBalanceSummaryDto;
import org.clematis.mt.repository.MonthlyDeltaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
public class MonthlyDeltaSummaryController {

    private static final String DELIM = "-";
    private final MonthlyDeltaRepository repository;

    public MonthlyDeltaSummaryController(MonthlyDeltaRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/monthlyDeltas/search/summary")
    @ResponseBody
    public ResponseEntity<CollectionModel<MonthlyBalanceSummaryDto>> getSummaryInCurrency(
        @RequestParam("code") String code,
        @RequestParam("moisStart") int moisStart,
        @RequestParam("anStart") int anStart,
        @RequestParam("moisEnd") int moisEnd,
        @RequestParam("anEnd") int anEnd,
        HttpServletRequest request) {

        String requestUrl = request.getRequestURL().toString();
        String baseResourceUrl = requestUrl.substring(0, requestUrl.indexOf("/search/summary"));

        List<MonthlyBalanceSummaryDto> dtos = repository
            .getSummaryInCurrency(code, moisStart, anStart, moisEnd, anEnd)
            .stream()
            .map(p -> {
                MonthlyBalanceSummaryDto dto = new MonthlyBalanceSummaryDto(p);
                dto.add(Link.of(baseResourceUrl + "/" + p.getYear()
                        + DELIM + p.getMonth() + DELIM + p.getCode()
                    ).withSelfRel()
                );
                return dto;
            })
            .collect(Collectors.toList());

        String fullSelfUrl = requestUrl + "?" + request.getQueryString();
        return ResponseEntity.ok(CollectionModel.of(dtos, Link.of(fullSelfUrl).withSelfRel()));
    }
}
