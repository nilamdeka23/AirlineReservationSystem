package edu.sjsu.cmpe275.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The type Bad request.
 */
@XmlRootElement
public class BadRequest {

    private Response response;

    /**
     * Instantiates a new Bad request.
     */
    public BadRequest() {
    }

    /**
     * Instantiates a new Bad request.
     *
     * @param response the response
     */
    public BadRequest(Response response) {
        this.response = response;
    }

    /**
     * Gets response.
     *
     * @return the response
     */
    public Response getResponse() {
        return response;
    }

    /**
     * Sets response.
     *
     * @param response the response
     */
    public void setResponse(Response response) {
        this.response = response;
    }

}
