package client.protector.hazard.hazardprotectorclient.model.Common;

/**
 * Created by Tautvilas on 26/02/2017.
 */

public class DbResponse
{

    public int status = 400;
    public String msg = "Default error";

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toString()
    {
        return "DbResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                '}';
    }

}
