package com.fintech.web.controller;

import com.fintech.core.TransferService;
import com.fintech.core.exception.InsufficientFundsException;
import com.fintech.core.exception.NoPermissionException;
import com.fintech.model.Account;
import com.fintech.web.data.TransferData;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author d.mikheev 08.05.19
 */

@Path("api")
public class TransferController {

    @Inject
    private TransferService transferService;

    @GET
    @Path("/client/{id}")
    @Produces(APPLICATION_JSON)
    public Response getUserInfo(@PathParam("id") String id) {
        return Response.ok()
                .entity(null)

                .build();
    }

    @GET
    @Path(("/transfers"))
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public String createTransfer(@Valid TransferData transferRequest) throws NoPermissionException, InsufficientFundsException {
        transferService.processTransfer(1l,"40817","40818",100l);
        Account account = transferService.get("40817");
        System.out.println(account.getAmount());

        return "Shit works";
    }

    @GET
    @Path("/transfers/{id}")
    @Produces(APPLICATION_JSON)
    public Response getTransferStatus(@PathParam("id") String id) {
        return Response.ok()
                .entity(null)
                .build();
    }


}
