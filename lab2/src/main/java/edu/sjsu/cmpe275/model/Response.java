package edu.sjsu.cmpe275.model;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * The type Response.
 */
@XmlRootElement
public class Response {

    private int code;

    private String msg;

    /**
     * Instantiates a new Response.
     */
    public Response(){}

    /**
     * Instantiates a new Response.
     *
     * @param code the code
     * @param msg  the msg
     */
    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets msg.
     *
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Sets msg.
     *
     * @param msg the msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

}
