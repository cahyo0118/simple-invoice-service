package com.dicicip.starter.controller;

import com.dicicip.starter.model.Production;
import com.dicicip.starter.repository.ProductionRepository;
import com.dicicip.starter.util.APIResponse;
import com.dicicip.starter.util.query.DB;
import com.dicicip.starter.util.query.QueryHelpers;
import com.dicicip.starter.util.validator.Validator;
import com.dicicip.starter.util.validator.ValidatorItem;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/productions")
@PreAuthorize("isFullyAuthenticated()")
public class ProductionController {

    @Autowired
    private ProductionRepository repository;

    @Autowired
    DB db;

    @RequestMapping(method = RequestMethod.GET)
    public APIResponse<?> getAll(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return new APIResponse<>(QueryHelpers.getData(request.getParameterMap(), "productions", db));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/detail")
    public APIResponse<?> getOne(
            @PathVariable("id") Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Optional<Production> production = this.repository.findById(id);

        if (production.isPresent()) {
            return new APIResponse<>(production.get());
        } else {
            response.setStatus(400);
            return new APIResponse<>(
                    null,
                    false,
                    "Failed get data"
            );
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/store")
    @Transactional(rollbackFor = Exception.class)
    public APIResponse<?> store(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Production requestBody
    ) {

        Validator<Production> validator = new Validator<>(
                requestBody,
                new ValidatorItem("name", "required"),
                new ValidatorItem("code", "required"),
                new ValidatorItem("price", "required")
        );

        if (validator.valid()) {
            return new APIResponse<>(repository.save(requestBody));
        } else {
            response.setStatus(400);
            return new APIResponse<>(
                    validator.getErrorsList(),
                    false,
                    "Failed save data"
            );
        }

    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/update-status")
    @Transactional(rollbackFor = Exception.class)
    public APIResponse<?> updateStatus(
            @PathVariable("id") Long id,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody HashMap<String, Object> requestBody
    ) {

        Validator<HashMap<String, Object>> validator = new Validator<>(
                requestBody,
                new ValidatorItem("status", "required")
        );

        if (validator.valid()) {

            /*Validate Production Status Value*/
            if (!EnumUtils.isValidEnum(Production.ProductionStatus.class, String.valueOf(requestBody.get("status")))) {
                response.setStatus(400);
                return new APIResponse<>(
                        null,
                        false,
                        "Failed save data, production status unidentified"
                );
            }

            Optional<Production> production = this.repository.findById(id);

            if (production.isPresent()) {

                Production p = production.get();

                p.status = String.valueOf(requestBody.get("status"));

                if (p.status.equals(Production.ProductionStatus.cetak.name())
                        || p.status.equals(Production.ProductionStatus.siap.name())
                        || p.status.equals(Production.ProductionStatus.kirim.name())
                ) {
                    p.produced_at = new Timestamp(new Date().getTime());
                }

                if (p.status.equals(Production.ProductionStatus.sampai.name())) {
                    p.finish_at = new Timestamp(new Date().getTime());
                }

                return new APIResponse<>(this.repository.save(p));

            } else {
                response.setStatus(400);
                return new APIResponse<>(
                        null,
                        false,
                        "Failed get data"
                );
            }

        } else {
            response.setStatus(400);
            return new APIResponse<>(
                    validator.getErrorsList(),
                    false,
                    "Failed save data"
            );
        }

    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}/delete")
    @Transactional(rollbackFor = Exception.class)
    public APIResponse<?> delete(
            @PathVariable("id") Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Optional<Production> production = this.repository.findById(id);

        if (production.isPresent()) {

            this.repository.delete(production.get());

            return new APIResponse<>(null);

        } else {
            response.setStatus(400);
            return new APIResponse<>(
                    null,
                    false,
                    "Failed delete data"
            );
        }
    }

}
