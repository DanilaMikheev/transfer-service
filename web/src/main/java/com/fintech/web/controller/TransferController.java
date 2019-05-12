package com.fintech.web.controller;

import com.fintech.core.TransferService;
import com.fintech.core.exception.InsufficientFundsException;
import com.fintech.core.exception.NoPermissionException;
import com.fintech.enums.TransferStatus;
import com.fintech.web.data.TransferData;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author d.mikheev 08.05.19
 */

@Path("api")
public class TransferController {

    @Inject
    private TransferService transferService;


    @POST
    @Path(("/transfers"))
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public String createTransfer(@Valid TransferData request) throws NoPermissionException, InsufficientFundsException {
        String uid = transferService.processTransfer(request.getClientId(), request.getFrom(), request.getTo(), request.getAmount());
        return uid;
    }

    @GET
    @Path("/transfers/{id}")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public String getTransferStatus(@PathParam("id") String id) {
        TransferStatus status = transferService.get(id);
        return String.valueOf(status.getVal());
    }


}
