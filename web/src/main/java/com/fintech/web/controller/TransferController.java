package com.fintech.web.controller;

import com.fintech.core.TransferService;
import com.fintech.core.exception.InsufficientFundsException;
import com.fintech.core.exception.NoPermissionException;
import com.fintech.enums.TransferStatus;
import com.fintech.web.data.ErrorData;
import com.fintech.web.data.TransferData;
import com.fintech.web.data.TransferStatusData;
import com.fintech.web.data.UidData;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.NoSuchElementException;

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
    public Response createTransfer(@Valid TransferData request) {
        try{
            String uid = transferService.processTransfer(request.getClientId(), request.getFromAcc(), request.getToAcc(), request.getAmount());
            return Response.ok().entity(new UidData(uid)).build();
        } catch (NoPermissionException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(new ErrorData(e.getMessage())).build();
        } catch (InsufficientFundsException e) {
            return Response.status(Response.Status.CONFLICT).entity(new ErrorData(e.getMessage())).build();
        }
    }

    @GET
    @Path("/transfers/{id}")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response getTransferStatus(@PathParam("id") String id) {
        try {
            TransferStatus status = transferService.get(id);
            return Response.ok().entity(new TransferStatusData(status.getVal())).build();
        } catch (NoSuchElementException e){
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorData(e.getMessage())).build();
        }
    }
}
