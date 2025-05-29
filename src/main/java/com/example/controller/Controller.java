package com.example.controller;

import com.example.dto.AddContactRequest;
import com.example.dto.ContactCard;
import com.example.dto.DeletedResponse;
import com.example.dto.FetchRequest;
import com.example.service.AddressBookAdapter;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Controller {
    @Inject
    AddressBookAdapter addressBookAdapter;

    @POST
    @Path("/create")
    public Response addContacts(List<AddContactRequest> requests) {
        try {
            List<ContactCard> added = addressBookAdapter.insert(requests);
            return Response.status(Response.Status.CREATED).entity(added).build();
        }
        catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid input: " + e.getMessage())
                    .build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while adding contacts: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/search")
    public Response searchContacts(FetchRequest query) {
        if (query == null || query.getQuery() == null || query.getQuery().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Query parameter 'query' is required")
                    .build();
        }
        List<ContactCard> results = addressBookAdapter.find(new FetchRequest(query.getQuery()));
        return Response.ok(results).build();
    }

    @PUT
    @Path("/update")
    public Response updateContacts(List<ContactCard> requests) {
        List<ContactCard> updated = addressBookAdapter.update(requests);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/delete")
    public Response deleteContacts(List<String> ids) {
        DeletedResponse response = addressBookAdapter.delete(ids);
        return Response.ok(response).build();
    }
}
